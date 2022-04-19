package com.tokopedia.play.broadcaster.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.play.broadcaster.R

/**
 * @author by jessica on 03/06/20
 */

enum class TrafficMetricType(
    @StringRes val label: Int
) {
    GameParticipants(R.string.play_summary_total_game_participant),
    TotalViews(R.string.play_summary_total_view),
    VideoLikes(R.string.play_summary_total_likes),
    ShopVisit(R.string.play_summary_shop_visit),
    ProductVisit(R.string.play_summary_product_visit),
    NumberOfAtc(R.string.play_summary_atc),
    NumberOfPaidOrders(R.string.play_summary_purchased_product),
    NewFollowers(R.string.play_summary_new_followers);
}

val TrafficMetricType.isGameParticipants
    get() = this == TrafficMetricType.GameParticipants