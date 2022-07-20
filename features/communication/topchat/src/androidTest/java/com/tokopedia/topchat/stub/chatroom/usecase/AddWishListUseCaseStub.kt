package com.tokopedia.topchat.stub.chatroom.usecase

import android.content.Context
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import javax.inject.Inject

//TODO: Migrate Wishlist UseCase to Coroutine
class AddWishListUseCaseStub @Inject constructor(context: Context) : AddWishListUseCase(context) {

    var isFail = false

    override fun createObservable(
        productId: String?,
        userId: String?,
        wishlistActionListener: WishListActionListener?
    ) {
        if (isFail) {
            wishlistActionListener?.onErrorAddWishList("Oops!", "123")
        } else {
            wishlistActionListener?.onSuccessAddWishlist("123")
        }
    }
}