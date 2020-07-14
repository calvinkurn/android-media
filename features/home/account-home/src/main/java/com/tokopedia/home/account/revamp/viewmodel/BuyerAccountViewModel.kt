package com.tokopedia.home.account.revamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase
import com.tokopedia.home.account.data.model.AccountModel
import com.tokopedia.home.account.domain.GetBuyerWalletBalanceUseCase
import com.tokopedia.home.account.presentation.util.dispatchers.DispatcherProvider
import com.tokopedia.home.account.revamp.domain.GetBuyerAccountDataUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuyerAccountViewModel @Inject constructor (
        private val getBuyerAccountDataUseCase: GetBuyerAccountDataUseCase,
        private val checkAffiliateUseCase: CheckAffiliateUseCase,
        private val getBuyerWalletBalanceUseCase: GetBuyerWalletBalanceUseCase,
        private val userSession: UserSessionInterface,
        private val walletPref: WalletPref,
        private val dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()) {

    private val _buyerAccountData = MutableLiveData<Result<AccountModel>>()
    val buyerAccountData: LiveData<Result<AccountModel>>
        get() = _buyerAccountData

    fun getBuyerData() {
        launchCatchError(block = {
            val accountModel = getBuyerAccountDataUseCase.executeOnBackground()
            val walletModel = getBuyerWalletBalance()
            val isAffiliate = checkIsAffiliate()
            withContext(dispatcher.main()) {
                accountModel.wallet = walletModel
                accountModel.isAffiliate = isAffiliate
                _buyerAccountData.value = Success(accountModel)
            }
        }, onError = {
            _buyerAccountData.value = Fail(it)
        })
    }

    fun saveLocallyAttributes(accountModel: AccountModel) {
        saveLocallyWallet(accountModel)
        saveLocallyVccUserStatus(accountModel)
        savePhoneVerified(accountModel)
        saveIsAffiliateStatus(accountModel)
        saveDebitInstantData(accountModel)
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

    private fun saveLocallyWallet(accountModel: AccountModel) {
        walletPref.saveWallet(accountModel.wallet)
        if (accountModel.vccUserStatus != null) {
            walletPref.tokoSwipeUrl = accountModel.vccUserStatus.redirectionUrl
        }
    }

    private fun saveLocallyVccUserStatus(accountModel: AccountModel) {
        if (accountModel.vccUserStatus != null) {
            walletPref.saveVccUserStatus(accountModel.vccUserStatus)
        }
    }

    private fun savePhoneVerified(accountModel: AccountModel) {
        if (accountModel.profile != null) {
            userSession.setIsMSISDNVerified(accountModel.profile.isPhoneVerified)
        }
    }

    private fun saveIsAffiliateStatus(accountModel: AccountModel) {
        userSession.setIsAffiliateStatus(accountModel.isAffiliate)
    }

    private fun saveDebitInstantData(accountModel: AccountModel) {
        if (accountModel.debitInstant != null) {
            walletPref.saveDebitInstantUrl(accountModel.debitInstant.data.redirectUrl)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getBuyerAccountDataUseCase.cancelJobs()
    }
}