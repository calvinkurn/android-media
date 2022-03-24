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
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
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
        private val addWishListUseCase: AddToWishlistV2UseCase,
        private val removeWishListUseCase: DeleteWishlistV2UseCase,
        private val recentViewUseCase: RecentViewUseCase
): BaseViewModel(baseDispatcher.io) {


    val recentViewDetailProductDataResp : LiveData<Result<ArrayList<RecentViewDetailProductDataModel>>>
        get() = _recentViewDetailProductDataResp
    private val _recentViewDetailProductDataResp : MutableLiveData<Result<ArrayList<RecentViewDetailProductDataModel>>> = MutableLiveData()

    val addWishlistResponse: LiveData<Result<String>> get() = _addWishlistResponse
    private val _addWishlistResponse = MutableLiveData<Result<String>>()

    val removeWishlistResponse: LiveData<Result<String>> get() = _removeWishlistResponse
    private val _removeWishlistResponse = MutableLiveData<Result<String>>()

    fun getRecentView() {
        recentViewUseCase.apply {
            getParam(userSession.userId)
        }.execute({
            _recentViewDetailProductDataResp.postValue(Success(it))
        }, {
            _recentViewDetailProductDataResp.postValue(Fail(it))
        })
    }

    fun addToWishlist(adapterPosition: Int, productId: String) {
        addWishListUseCase.setParams(productId, userSession.userId)
        addWishListUseCase.execute(
                onSuccess = {
                    _addWishlistResponse.postValue(Success(productId))},
                onError = {
                    _addWishlistResponse.postValue(Fail(it))
                })
    }

    fun removeFromWishlist(adapterPosition: Int, productId: String) {
        removeWishListUseCase.setParams(productId, userSession.userId)
        removeWishListUseCase.execute(
                onSuccess = { _removeWishlistResponse.postValue(Success(productId)) },
                onError = { _removeWishlistResponse.postValue(Fail(it)) })
    }
}