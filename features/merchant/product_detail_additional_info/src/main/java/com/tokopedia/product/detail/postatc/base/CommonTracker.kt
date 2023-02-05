package com.tokopedia.product.detail.postatc.base

import com.tokopedia.product.detail.postatc.model.PostAtcInfo
import com.tokopedia.user.session.UserSessionInterface

class CommonTracker(
    private val userSession: UserSessionInterface,
    private val postAtcInfo: PostAtcInfo
) {

    var userId: String = ""
        private set

    var productId: String = ""
        private set

    var cartId: String = ""
        private set

    var layoutId: String = ""
        private set

    var pageSource: String = ""
        private set

    var layoutName: String = ""
        private set

    var categoryId: String = ""
        private set

    var categoryName: String = ""
        private set

    var shopId: String = ""
        private set

    fun get(): CommonTracker {
        userId = userSession.userId
        productId = postAtcInfo.productId
        cartId = postAtcInfo.cartId
        layoutId = postAtcInfo.layoutId
        pageSource = postAtcInfo.pageSource
        layoutName = postAtcInfo.layoutName
        categoryId = postAtcInfo.categoryId
        categoryName = postAtcInfo.categoryName
        shopId = postAtcInfo.shopId

        return this
    }
}
