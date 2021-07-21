package com.tokopedia.play.broadcaster.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.play.broadcaster.R

/**
 * @author by jessica on 03/06/20
 */

enum class TrafficMetricType(
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    GameParticipants(com.tokopedia.play_common.R.drawable.ic_play_interactive_crown, R.string.play_summary_total_game_participant),
    TotalViews(R.drawable.ic_play_view_white, R.string.play_summary_total_view),
    VideoLikes(R.drawable.ic_play_like_white, R.string.play_summary_total_likes),
    ShopVisit(R.drawable.ic_play_visit_store_white, R.string.play_summary_shop_visit),
    ProductVisit(R.drawable.ic_play_visit_product_white, R.string.play_summary_product_visit),
    NumberOfAtc(R.drawable.ic_play_cart_white, R.string.play_summary_atc),
    NumberOfPaidOrders(R.drawable.ic_play_shop_white, R.string.play_summary_purchased_product),
    NewFollowers(R.drawable.ic_play_new_follower_white, R.string.play_summary_new_followers);
}

val TrafficMetricType.isGameParticipants
    get() = this == TrafficMetricType.GameParticipants