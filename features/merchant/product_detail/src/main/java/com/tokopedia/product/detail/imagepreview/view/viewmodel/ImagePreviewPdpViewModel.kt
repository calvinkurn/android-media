package com.tokopedia.product.detail.imagepreview.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import javax.inject.Inject

class ImagePreviewPdpViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
        private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    fun isShopOwner(shopId: String): Boolean = userSessionInterface.isLoggedIn && userSessionInterface.shopId == shopId

    private fun isProductIdValid(productId: String): Boolean {
        return productId.isNotEmpty() && productId.matches(Regex(PATTERN_REGEX))
    }

    fun addWishList(productId: String, onErrorAddWishList: ((errorMessage: String?) -> Unit)?, onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        if (isProductIdValid(productId)) {
            addWishListUseCase.createObservable(productId, userSessionInterface.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    onErrorAddWishList?.invoke(errorMessage)
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    // no op
                }

                override fun onSuccessRemoveWishlist(productId: String?) {
                    // no op
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    onSuccessAddWishlist?.invoke(productId)
                }
            })
        } else {
            onErrorAddWishList?.invoke("")
        }
    }

    fun addWishListV2(productId: String, onErrorAddWishList: ((throwable: Throwable?) -> Unit)?, onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        if (isProductIdValid(productId)) {
            addToWishlistV2UseCase.setParams(productId, userSessionInterface.userId)
            addToWishlistV2UseCase.execute(onSuccess = { onSuccessAddWishlist?.invoke(productId) },
                    onError = {onErrorAddWishList?.invoke(it)})
        } else {
            onErrorAddWishList?.invoke(Throwable(""))
        }
    }

    fun removeWishList(productId: String, onSuccessRemoveWishlist: ((productId: String?) -> Unit)?, onErrorRemoveWishList: ((errorMessage: String?) -> Unit)?) {
        if (isProductIdValid(productId)) {
            removeWishlistUseCase.createObservable(productId, userSessionInterface.userId, object : WishListActionListener {
                override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                    // no op
                }

                override fun onSuccessAddWishlist(productId: String?) {
                    // no op
                }

                override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                    onErrorRemoveWishList?.invoke(errorMessage)
                }

                override fun onSuccessRemoveWishlist(productId: String?) {
                    onSuccessRemoveWishlist?.invoke(productId)
                }
            })
        } else {
            onErrorRemoveWishList?.invoke("")
        }
    }

    fun removeWishListV2(productId: String, onSuccessRemoveWishlist: ((productId: String?) -> Unit)?, onErrorRemoveWishList: ((throwable: Throwable?) -> Unit)?) {
        if (isProductIdValid(productId)) {
            deleteWishlistV2UseCase.setParams(productId, userSessionInterface.userId)
            deleteWishlistV2UseCase.execute(onSuccess = { onSuccessRemoveWishlist?.invoke(productId) },
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