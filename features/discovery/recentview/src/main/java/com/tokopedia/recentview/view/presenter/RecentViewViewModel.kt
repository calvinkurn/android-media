package com.tokopedia.recentview.view.presenter

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.recentview.domain.usecase.RecentViewUseCase
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author by yoasfs on 13/08/20
 */

@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class RecentViewViewModel @Inject constructor(
        baseDispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
        private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
        private val recentViewUseCase: RecentViewUseCase
): BaseViewModel(baseDispatcher.io) {


    val recentViewDetailProductDataResp : LiveData<Result<ArrayList<RecentViewDetailProductDataModel>>>
        get() = _recentViewDetailProductDataResp
    private val _recentViewDetailProductDataResp : MutableLiveData<Result<ArrayList<RecentViewDetailProductDataModel>>> = MutableLiveData()

    val addWishlistResponse: LiveData<Result<String>> get() = _addWishlistResponse
    private val _addWishlistResponse = MutableLiveData<Result<String>>()

    val addWishlistV2Response: LiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>> get() = _addWishlistV2Response
    private val _addWishlistV2Response = MutableLiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>>()

    val removeWishlistResponse: LiveData<Result<String>> get() = _removeWishlistResponse
    private val _removeWishlistResponse = MutableLiveData<Result<String>>()

    val removeWishlistV2Response: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>> get() = _removeWishlistV2Response
    private val _removeWishlistV2Response = MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()

    fun getRecentView() {
        recentViewUseCase.apply {
            getParam(userSession.userId)
        }.execute({
            _recentViewDetailProductDataResp.postValue(Success(it))
        }, {
            _recentViewDetailProductDataResp.postValue(Fail(it))
        })
    }

    fun addToWishlist(productId: String) {
        addWishListUseCase.createObservable(productId,
            userSession.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String, productId: String) {
                    _addWishlistResponse.postValue(Fail(Throwable(errorMessage)))
                }

                override fun onSuccessAddWishlist(productId: String) {
                    _addWishlistResponse.postValue(Success(productId))
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

                override fun onSuccessRemoveWishlist(productId: String?) {}
            })
    }

    fun addToWishlistV2(productId: String, wishlistV2ActionListener: WishlistV2ActionListener) {
        addToWishlistV2UseCase.setParams(productId, userSession.userId)
        addToWishlistV2UseCase.execute(
            onSuccess = { result ->
                if (result is Success) {
                    _addWishlistV2Response.postValue(Success(result.data))
                    wishlistV2ActionListener.onSuccessAddWishlist(result.data, productId)
                } else if (result is Fail) {
                    _addWishlistV2Response.postValue(Fail(result.throwable))
                    wishlistV2ActionListener.onErrorAddWishList(result.throwable, productId)
                } },
            onError = {
                _addWishlistV2Response.postValue(Fail(it))
                wishlistV2ActionListener.onErrorAddWishList(it, productId)
            })
    }

    fun removeFromWishlist(productId: String) {
        removeWishListUseCase.createObservable(productId,
            userSession.userId,  object : WishListActionListener{
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}

                override fun onSuccessAddWishlist(productId: String?) {}

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    _removeWishlistResponse.postValue(Fail(Throwable(errorMessage)))
                }

                override fun onSuccessRemoveWishlist(productId: String) {
                    _removeWishlistResponse.postValue(Success(productId))
                }
            })
    }

    fun removeFromWishlistV2(productId: String) {
        deleteWishlistV2UseCase.setParams(productId, userSession.userId)
        deleteWishlistV2UseCase.execute(
                onSuccess = { result ->
                    if (result is Success) {
                        _removeWishlistV2Response.postValue(Success(result.data))
                    } else if (result is Fail) {
                        _removeWishlistV2Response.postValue(Fail(result.throwable))
                    } },
                onError = {
                    _removeWishlistV2Response.postValue(Fail(it)) })
    }

    override fun onCleared() {
        addToWishlistV2UseCase.cancelJobs()
        deleteWishlistV2UseCase.cancelJobs()
        super.onCleared()
    }
}