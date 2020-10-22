package com.tokopedia.home.account.revamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase
import com.tokopedia.home.account.domain.GetBuyerWalletBalanceUseCase
import com.tokopedia.home.account.presentation.util.dispatchers.DispatcherProvider
import com.tokopedia.home.account.revamp.domain.usecase.GetBuyerAccountDataUseCase
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.home.account.revamp.domain.usecase.GetShortcutDataUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuyerAccountViewModel @Inject constructor (
        private val getBuyerAccountDataUseCase: GetBuyerAccountDataUseCase,
        private val checkAffiliateUseCase: CheckAffiliateUseCase,
        private val getBuyerWalletBalanceUseCase: GetBuyerWalletBalanceUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        private val shortcutDataUseCase: GetShortcutDataUseCase,
        private val userSession: UserSessionInterface,
        private val walletPref: WalletPref,
        private val dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()) {

    private val _buyerAccountData = MutableLiveData<Result<AccountDataModel>>()
    val buyerAccountDataData: LiveData<Result<AccountDataModel>>
        get() = _buyerAccountData

    private val _addWishList = MutableLiveData<Result<String>>()
    val addWishList : LiveData<Result<String>>
        get() = _addWishList

    private val _removeWishList = MutableLiveData<Result<String>>()
    val removeWishList : LiveData<Result<String>>
        get() = _removeWishList

    private val _recommendation = MutableLiveData<Result<RecommendationWidget>>()
    val recommendation : LiveData<Result<RecommendationWidget>>
        get() = _recommendation

    private val _firstRecommendation = MutableLiveData<Result<RecommendationWidget>>()
    val firstRecommendation : LiveData<Result<RecommendationWidget>>
        get() = _firstRecommendation

    fun getBuyerData() {
        launchCatchError(block = {
            val accountModel = getBuyerAccountDataUseCase.executeOnBackground()
            val walletModel = getBuyerWalletBalance()
            val isAffiliate = checkIsAffiliate()
            val shortcutResponse = shortcutDataUseCase.executeOnBackground()
            withContext(dispatcher.main()) {
                accountModel.wallet = walletModel
                accountModel.isAffiliate = isAffiliate
                accountModel.shortcutResponse = shortcutResponse
                saveLocallyAttributes(accountModel)
                _buyerAccountData.postValue(Success(accountModel))
            }
        }, onError = {
            _buyerAccountData.postValue(Fail(it))
        })
    }

    private fun saveLocallyAttributes(accountDataModel: AccountDataModel) {
        saveLocallyWallet(accountDataModel)
        saveLocallyVccUserStatus(accountDataModel)
        savePhoneVerified(accountDataModel)
        saveIsAffiliateStatus(accountDataModel)
        saveDebitInstantData(accountDataModel)
    }

    fun addWishList(item: RecommendationItem) {
        if (item.isTopAds) {
            launchCatchError(block = {
                val params = RequestParams.create()
                params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, item.wishlistUrl)
                val result = topAdsWishlishedUseCase.createObservable(params).toBlocking().single()
                if (result.data.isSuccess) {
                    _addWishList.postValue(Success(MSG_SUCCESS_ADD_WISHLIST))
                } else {
                    _addWishList.postValue(Fail(Throwable(MSG_FAILED_ADD_WISHLIST)))
                }
            }, onError = {
                _addWishList.postValue(Fail(Throwable(it)))
            })
        } else {
            addWishListUseCase.createObservable("${item.productId}", userSession.userId, wishListListener())
        }
    }

    fun removeWishList(item: RecommendationItem) {
        removeWishListUseCase.createObservable("${item.productId}", userSession.userId, wishListListener())
    }

    fun getFirstRecommendationData() {
        getRecommendationData(0, true)
    }

    fun getRecommendationData(page: Int, isFirstData: Boolean = false) {
        launchCatchError(block = {
            val params = getRecommendationUseCase.getRecomParams(
                    page,
                    GetRecommendationUseCase.DEFAULT_VALUE_X_SOURCE,
                    AKUN_PAGE,
                    emptyList()
            )

            val data = getRecommendationUseCase.createObservable(params).toBlocking().single()
            if (isFirstData) {
                _firstRecommendation.postValue(Success(data[0]))
            } else {
                _recommendation.postValue(Success(data[0]))
            }
        }, onError = {
            if (isFirstData) {
                _firstRecommendation.postValue(Fail(it))
            } else {
                _recommendation.postValue(Fail(it))
            }
        })
    }


    private fun checkIsAffiliate(): Boolean {
        return if (userSession.isAffiliate) {
            userSession.isAffiliate
        } else {
            checkAffiliateUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
        }
    }

    private fun getBuyerWalletBalance(): WalletModel {
        return getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
    }

    private fun saveLocallyWallet(accountDataModel: AccountDataModel) {
        walletPref.saveWallet(accountDataModel.wallet)
        if (accountDataModel.vccUserStatus != null) {
            walletPref.tokoSwipeUrl = accountDataModel.vccUserStatus.redirectionUrl
        }
    }

    private fun saveLocallyVccUserStatus(accountDataModel: AccountDataModel) {
        if (accountDataModel.vccUserStatus != null) {
            walletPref.saveVccUserStatus(accountDataModel.vccUserStatus)
        }
    }

    private fun savePhoneVerified(accountDataModel: AccountDataModel) {
        if (accountDataModel.profile != null) {
            userSession.setIsMSISDNVerified(accountDataModel.profile.isPhoneVerified)
        }
    }

    private fun saveIsAffiliateStatus(accountDataModel: AccountDataModel) {
        userSession.setIsAffiliateStatus(accountDataModel.isAffiliate)
    }

    private fun saveDebitInstantData(accountDataModel: AccountDataModel) {
        if (accountDataModel.debitInstant != null && accountDataModel.debitInstant.data != null) {
            walletPref.saveDebitInstantUrl(accountDataModel.debitInstant.data.redirectUrl)
        }
    }

    private fun wishListListener(): WishListActionListener {
        return object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                _addWishList.postValue(Fail(Throwable(errorMessage)))
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                _removeWishList.postValue(Fail(Throwable(errorMessage)))
            }

            override fun onSuccessAddWishlist(productId: String?) {
                _addWishList.postValue(Success(MSG_SUCCESS_ADD_WISHLIST))
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                _removeWishList.postValue(Success(MSG_SUCCESS_REMOVE_WISHLIST))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        getBuyerAccountDataUseCase.cancelJobs()
    }

    companion object {
        private const val MSG_SUCCESS_ADD_WISHLIST = "Berhasil menambahkan ke Wishlist"
        private const val MSG_FAILED_ADD_WISHLIST = "Gagal menambahkan ke Wishlist"
        private const val MSG_SUCCESS_REMOVE_WISHLIST = "Berhasil menghapus dari Wishlist"
        private const val AKUN_PAGE = "account"
    }
}