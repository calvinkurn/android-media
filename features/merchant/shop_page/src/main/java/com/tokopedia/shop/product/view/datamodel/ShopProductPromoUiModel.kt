package com.tokopedia.shop.product.view.datamodel

/**
 * Created by zulfikarrahman on 1/16/18.
 */
class ShopProductPromoUiModel {
    var url: String? = null
        private set
    var isLogin = false
        private set
    var userId: String? = null
        private set
    var accessToken: String? = null
        private set

    constructor() {}
    constructor(url: String?, userId: String?, accessToken: String?, isLogin: Boolean) {
        this.url = url
        this.userId = userId
        this.accessToken = accessToken
        this.isLogin = isLogin
    }
}