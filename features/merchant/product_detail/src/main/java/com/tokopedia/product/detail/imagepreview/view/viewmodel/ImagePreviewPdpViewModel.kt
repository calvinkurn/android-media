package com.tokopedia.product.detail.imagepreview.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImagePreviewPdpViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
        private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    fun isShopOwner(shopId: String): Boolean = userSessionInterface.isLoggedIn && userSessionInterface.shopId == shopId

    fun addWishListV2(productId: String, wishlistV2ActionListener: WishlistV2ActionListener) {
        launch(dispatcher.main) {
            addToWishlistV2UseCase.setParams(productId, userSessionInterface.userId)
            val result = withContext(dispatcher.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistV2ActionListener.onSuccessAddWishlist(result.data, productId)
            } else if (result is Fail) {
                wishlistV2ActionListener.onErrorAddWishList(result.throwable, productId)
            }
        }
    }

    fun removeWishListV2(productId: String, listener: WishlistV2ActionListener) {
        launch(dispatcher.main) {
            deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId)
            val result = withContext(dispatcher.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                listener.onSuccessRemoveWishlist(result.data, productId)
            } else if (result is Fail) {
                listener.onErrorRemoveWishlist(result.throwable, productId)
            }
        }
    }

    companion object {
        /** Regex pattern is only accepted when product id is numeric, example : "12331213" */
        const val PATTERN_REGEX = "^(?![a-zA-Z])([0-9])*\$"
    }
}