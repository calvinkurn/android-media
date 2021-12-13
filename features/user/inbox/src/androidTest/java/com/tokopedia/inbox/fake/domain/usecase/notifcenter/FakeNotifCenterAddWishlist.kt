package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import android.content.Context
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import javax.inject.Inject

class FakeNotifCenterAddWishlist @Inject constructor(context: Context): AddWishListUseCase(context) {

    var isError = false

    override fun createObservable(
        productId: String?,
        userId: String?,
        wishlistActionListener: WishListActionListener?
    ) {
        if(isError) {
            wishlistActionListener?.onErrorAddWishList("Oops!", "123")
        } else {
            wishlistActionListener?.onSuccessAddWishlist("123")
        }
    }
}