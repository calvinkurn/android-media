package com.tokopedia.play.broadcaster.ui.model

import androidx.annotation.StringRes
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R

/**
 * @author by jessica on 03/06/20
 */

enum class TrafficMetricType(
    @StringRes val label: Int,
    val iconRes: Int,
) {
    TotalViews(R.string.play_broadcaster_live_stats_total_viewer_label, IconUnify.VISIBILITY),
    MaximumConcurrentViews(R.string.play_broadcaster_live_stats_max_concurrent_view_label, IconUnify.USER),
    VideoLikes(R.string.play_summary_total_likes, IconUnify.THUMB),
    NewFollowers(R.string.play_summary_new_followers, IconUnify.USER_SUCCESS),
    ProductVisit(R.string.play_summary_product_visit, IconUnify.PRODUCT_NEXT),
    ShopVisit(R.string.play_summary_shop_visit, IconUnify.SHOP),
    ProfileVisit(R.string.play_summary_profile_visit, 0),
    NumberOfAtc(R.string.play_summary_atc, IconUnify.CART),
    NumberOfPaidOrders(R.string.play_summary_purchased_product, IconUnify.SHOPPING_BAG);
}
