package com.tokopedia.feedcomponent.view.viewmodel.posttag

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactory

data class ProductPostTagModelNew(
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
    var product: FeedXProduct = FeedXProduct(),
    var isFreeShipping: Boolean = false,
    var freeShipping: String = "",
    var freeShippingURL: String = "",
    var originalPrice: Int = 0,
    var originalPriceFmt: String = "",
    var priceDiscountFmt: String = "",
    var totalSold: Int = 0,
    val rating: Int = 0,
    var mods: List<String> = emptyList(),
    var shopId: String = "0",
    var shopName: String = "",
    override var feedType: String = "",
    override var positionInFeed: Int = 0,
    override var postId: String = "0",
    var postType: String = "",
    var mediaType : String = "",
    var isFollowed:Boolean = false,
    var description:String = "",
    var isTopads:Boolean = false,
    var adClickUrl:String = "",
    var playChannelId:String = "",
    val saleType: String = "",
    val saleStatus: String = "",
    var isWishlisted: Boolean = false
) : BasePostTagModel {
    override fun type(typeFactory: PostTagTypeFactory): Int {
        return typeFactory.type(this)
    }

    val isRilisanSpl: Boolean
        get() = saleType == ASGC_RILISAN_SPECIAL
    val isFlashSaleToko: Boolean
        get() = type == ASGC_FLASH_SALE_TOKO
    val isUpcoming: Boolean
        get() = saleStatus == Upcoming
    val isOngoing: Boolean
        get() = saleStatus == Ongoing

    companion object {
        private const val ASGC_RILISAN_SPECIAL = "Rilisan Spesial"
        private const val ASGC_FLASH_SALE_TOKO = "asgc_flash_sale_toko"
        private const val Upcoming = "upcoming"
        private const val Ongoing = "ongoing"
    }
}

