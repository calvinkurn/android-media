package com.tokopedia.shop.pageheader.util

import androidx.annotation.StringDef
import com.tokopedia.shop.pageheader.util.ShopPageTabName.Companion.CAMPAIGN
import com.tokopedia.shop.pageheader.util.ShopPageTabName.Companion.FEED
import com.tokopedia.shop.pageheader.util.ShopPageTabName.Companion.HOME
import com.tokopedia.shop.pageheader.util.ShopPageTabName.Companion.PRODUCT
import com.tokopedia.shop.pageheader.util.ShopPageTabName.Companion.REVIEW
import com.tokopedia.shop.pageheader.util.ShopPageTabName.Companion.SHOWCASE

@Retention(AnnotationRetention.SOURCE)
@StringDef(HOME, PRODUCT, SHOWCASE, FEED, REVIEW, CAMPAIGN)
annotation class ShopPageTabName {
    companion object {
        const val HOME = "HomeTab"
        const val PRODUCT = "ProductTab"
        const val SHOWCASE = "EtalaseTab"
        const val FEED = "FeedTab"
        const val REVIEW = "ReviewTab"
        const val CAMPAIGN = "CampaignTab"
    }
}
