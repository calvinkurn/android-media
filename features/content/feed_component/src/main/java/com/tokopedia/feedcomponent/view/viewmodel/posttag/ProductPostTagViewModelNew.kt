package com.tokopedia.feedcomponent.view.viewmodel.posttag

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactory

data class ProductPostTagViewModelNew(
        var id: String = "",
        var text: String = "",
        var imgUrl:String = "",
        var price: String = "",
        var priceFmt: String = "",
        var isDiscount:Boolean = false,
        var discountFmt: String = "",
        var type: String = "",
        var applink: String = "",
        var weblink: String = "",
        var product: FeedXProduct,
        var isFreeShipping:Boolean = false,
        var freeShipping:String = "",
        var freeShippingURL:String = "",
        var originalPrice:Int = 0,
        var originalPriceFmt:String ="",
        var priceDiscountFmt:String = "",
        var totalSold:Int = 0,
        val rating: Int = 0,
        var mods: List<Any>,
        override var feedType: String = "",
        override var positionInFeed: Int = 0,
        override var postId: Int = 0

) : BasePostTagViewModel {
    override fun type(typeFactory: PostTagTypeFactory): Int {
        return typeFactory.type(this)
    }
}