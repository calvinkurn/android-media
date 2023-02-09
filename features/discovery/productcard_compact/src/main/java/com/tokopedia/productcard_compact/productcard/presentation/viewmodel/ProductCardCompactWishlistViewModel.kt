package com.tokopedia.productcard_compact.productcard.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductCardCompactWishlistViewModel @Inject constructor(
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
            _addToWishlistLiveData.postValue(result)
        }
    }

    fun removeFromWishlist(productId: String){
        launch(dispatchers.io){
            removeFromWishlistUseCase.setParams(productId, userSession.userId)
            val result = removeFromWishlistUseCase.executeOnBackground()
            _removeFromWishlistLiveData.postValue(result)
        }
    }

}
