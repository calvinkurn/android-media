package com.tokopedia.feedplus.oldFeed.view.viewmodel.feeddetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedplus.oldFeed.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory

data class FeedDetailProductModel (
        val id: String ,
        val text: String ,
        val imgUrl:String ,
        val price: String ,
        val priceDiscount: String ,
        val priceFmt: String ,
        var isDiscount:Boolean = false,
        val discountFmt: String ,
        val type: String ,
        val applink: String,
        val weblink: String ,
        val product: FeedXProduct,
        var isFreeShipping: Boolean = false,
        val freeShipping: String ,
        val freeShippingURL: String ,
        val originalPriceFmt: String ,
        val priceDiscountFmt: String ,
        val totalSold: Int = 0,
        val rating: Int = 0,
        val mods: List<String>,
        var shopId: String = "0",
        val shopName: String ,
        var feedType: String = "",
        var positionInFeed: Int = 0 ,
        var postId: String = "",
        var postType: String = "",
        var isFollowed:Boolean = false,
        var description:String = "",
        var isTopads:Boolean = false,
        var adClickUrl:String = "",
        var playChannelId: String = "",
        val saleType: String = "",
        val saleStatus: String = "",
        var isWishlisted: Boolean = false
) : Visitable<FeedPlusDetailTypeFactory> {
        override fun type(typeFactory: FeedPlusDetailTypeFactory): Int {
                return typeFactory.type(this)
        }

        val isUpcoming: Boolean
                get() = saleStatus == UPCOMING
        val isOngoing: Boolean
                get() = saleStatus == ONGOING
        val isRilisanSpl: Boolean
                get() = saleType == ASGC_RILISAN_SPECIAL

        companion object {
                const val UPCOMING = "upcoming"
                private const val ONGOING = "ongoing"
                private const val ASGC_RILISAN_SPECIAL = "Rilisan Spesial"

        }
}

