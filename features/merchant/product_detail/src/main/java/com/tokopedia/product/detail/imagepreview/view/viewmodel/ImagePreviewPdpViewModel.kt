package com.tokopedia.product.detail.imagepreview.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class ImagePreviewPdpViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        @Named("Main")
        val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    fun addWishList(productId: String, onErrorAddWishList: ((errorMessage: String?) -> Unit)?, onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        if (productId.isNotEmpty() && productId.matches(Regex("^(?![a-zA-Z])([0-9])*\$"))) {
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

    fun removeWishList(productId: String, onSuccessRemoveWishlist: ((productId: String?) -> Unit)?, onErrorRemoveWishList: ((errorMessage: String?) -> Unit)?) {
        if (productId.isNotEmpty() && productId.matches(Regex("^(?![a-zA-Z])([0-9])*\$"))) {
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
}