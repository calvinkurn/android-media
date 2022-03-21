package com.tokopedia.product.detail.imagepreview.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import javax.inject.Inject

class ImagePreviewPdpViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddToWishlistV2UseCase,
        private val removeWishlistUseCase: DeleteWishlistV2UseCase,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    fun isShopOwner(shopId: String): Boolean = userSessionInterface.isLoggedIn && userSessionInterface.shopId == shopId

    private fun isProductIdValid(productId: String): Boolean {
        return productId.isNotEmpty() && productId.matches(Regex(PATTERN_REGEX))
    }

    fun addWishList(productId: String, onErrorAddWishList: ((throwable: Throwable?) -> Unit)?, onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        if (isProductIdValid(productId)) {
            addWishListUseCase.setParams(productId, userSessionInterface.userId)
            addWishListUseCase.execute(onSuccess = { onSuccessAddWishlist?.invoke(productId) },
                    onError = {onErrorAddWishList?.invoke(it)})
        } else {
            onErrorAddWishList?.invoke(Throwable(""))
        }
    }

    fun removeWishList(productId: String, onSuccessRemoveWishlist: ((productId: String?) -> Unit)?, onErrorRemoveWishList: ((throwable: Throwable?) -> Unit)?) {
        if (isProductIdValid(productId)) {
            removeWishlistUseCase.setParams(productId, userSessionInterface.userId)
            removeWishlistUseCase.execute(onSuccess = { onSuccessRemoveWishlist?.invoke(productId) },
                    onError = { onErrorRemoveWishList?.invoke(it) })
        } else {
            onErrorRemoveWishList?.invoke(Throwable(""))
        }
    }

    companion object {
        /** Regex pattern is only accepted when product id is numeric, example : "12331213" */
        const val PATTERN_REGEX = "^(?![a-zA-Z])([0-9])*\$"
    }
}