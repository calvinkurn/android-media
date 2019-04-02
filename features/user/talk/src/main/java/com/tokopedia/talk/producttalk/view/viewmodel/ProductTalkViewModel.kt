package com.tokopedia.talk.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable


/**
 * @author by Steven.
 */

data class ProductTalkViewModel(
        var screen: String = "",
        var listThread: ArrayList<Visitable<*>> = ArrayList(),
        var hasNextPage: Boolean = false,
        var page_id: Int = 0,
        var productId: Int = 0,
        var productName: String = "",
        var productImage: String = "",
        var productUrl: String = "",
        var productPrice: String = "",
        var shopId: Int = 0,
        var shopName : String = "",
        var shopAvatar : String = ""
) {

}
