package com.tokopedia.feedplus.view.adapter.viewholder.feeddetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory

data class ProductFeedDetailViewModelNew (
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
        var isFreeShipping: Boolean = false,
        var freeShipping: String = "",
        var freeShippingURL: String = "",
        var originalPrice: Int = 0,
        var originalPriceFmt: String = "",
        var priceDiscountFmt: String = "",
        var totalSold: Int = 0,
        val rating: Int = 0,
        var mods: List<String>,
        var shopId: String = "0",
        var shopName: String = "",
        var feedType: String = "",
        var positionInFeed: Int = 0,
        var postId: Int = 0,
        var postType: String = "",
        var isFollowed:Boolean = false,
        var description:String = "",
        var isTopads:Boolean = false,
        var adClickUrl:String = "",
        var playChannelId:String = ""
) : Visitable<FeedPlusDetailTypeFactory> {
    override fun type(typeFactory: FeedPlusDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}

