package com.tokopedia.shop.pageheader.util

import androidx.annotation.StringDef
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName.Companion.CAMPAIGN
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName.Companion.FEED
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName.Companion.HOME
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName.Companion.PRE_FETCH_DATA
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName.Companion.PRODUCT
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName.Companion.REVIEW
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName.Companion.SHOWCASE

@Retention(AnnotationRetention.SOURCE)
@StringDef(HOME, PRODUCT, SHOWCASE, FEED, REVIEW, CAMPAIGN, PRE_FETCH_DATA)
annotation class ShopPageHeaderTabName {
    companion object {
        const val HOME = "HomeTab"
        const val PRODUCT = "ProductTab"
        const val SHOWCASE = "EtalaseTab"
        const val FEED = "FeedTab"
        const val REVIEW = "ReviewTab"
        const val CAMPAIGN = "CampaignTab"
        const val PRE_FETCH_DATA = "PreFetchDataTab"
    }

}
