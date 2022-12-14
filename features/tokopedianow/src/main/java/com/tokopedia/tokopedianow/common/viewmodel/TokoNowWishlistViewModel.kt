package com.tokopedia.tokopedianow.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowWishlistViewModel @Inject constructor(
    private val addToWishlistUseCase: AddToWishlistV2UseCase,
    private val removeFromWishlistUseCase: DeleteWishlistV2UseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
): BaseViewModel(dispatchers.io) {

    val addToWishlistLiveData: LiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>>
        get() = _addToWishlistLiveData
    private val _addToWishlistLiveData = MutableLiveData<Result<AddToWishlistV2Response.Data.WishlistAddV2>>()

    val removeFromWishlistLiveData: LiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>
        get() = _removeFromWishlistLiveData
    private val _removeFromWishlistLiveData = MutableLiveData<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>()

    fun addToWishlist(productId: String){
        launch(dispatchers.io){
            addToWishlistUseCase.setParams(productId, userSession.userId)
            val result = addToWishlistUseCase.executeOnBackground()
            if (result is Success) {
                _addToWishlistLiveData.postValue(Success(result.data))
            } else if (result is Fail) {
                _addToWishlistLiveData.postValue(Fail(result.throwable))
            }
        }
    }

    fun removeFromWishlist(productId: String){
        launch(dispatchers.io){
            removeFromWishlistUseCase.setParams(productId, userSession.userId)
            val result = removeFromWishlistUseCase.executeOnBackground()
            if (result is Success) {
                _removeFromWishlistLiveData.postValue(Success(result.data))
            } else if (result is Fail) {
                _removeFromWishlistLiveData.postValue(Fail(result.throwable))
            }
        }
    }

}
