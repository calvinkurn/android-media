package com.tokopedia.tokopedianow.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.common.domain.model.AddToWishListResponse
import com.tokopedia.tokopedianow.common.domain.model.RemoveFromWishListResponse
import com.tokopedia.tokopedianow.common.domain.usecase.AddToWishlistUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.RemoveFromWishlistUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowWishlistViewModel @Inject constructor(
    private val addToWishlistUseCase: AddToWishlistUseCase,
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers,
): BaseViewModel(dispatchers.io) {

    val addToWishlistLiveData: LiveData<Result<AddToWishListResponse>>
        get() = _addToWishlistLiveData
    private val _addToWishlistLiveData = MutableLiveData<Result<AddToWishListResponse>>()

    val removeFromWishlistLiveData: LiveData<Result<RemoveFromWishListResponse>>
        get() = _removeFromWishlistLiveData
    private val _removeFromWishlistLiveData = MutableLiveData<Result<RemoveFromWishListResponse>>()

    fun addToWishlist(productId: String){
        launchCatchError(coroutineContext, block = {
            _addToWishlistLiveData.postValue(Success(
                addToWishlistUseCase.execute(productId, userSession.userId))
            )
        }, onError = {
            _addToWishlistLiveData.postValue(Fail(it))
        })
    }

    fun removeFromWishlist(productId: String){
        launchCatchError(coroutineContext, block = {
            _removeFromWishlistLiveData.postValue(Success(
                removeFromWishlistUseCase.execute(productId, userSession.userId))
            )
        }, onError = {
            _removeFromWishlistLiveData.postValue(Fail(it))
        })
    }

}
