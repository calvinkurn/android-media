package com.tokopedia.play.broadcaster.view.uimodel

import com.tokopedia.play.broadcaster.R

/**
 * @author by jessica on 03/06/20
 */

enum class LiveTrafficMetricsEnum(val thumbnailRes: Int, val descriptionRes: Int) {
    TOTAL_VIEWS(R.drawable.ic_play_view_white, R.string.play_summary_total_view),
    VIDEO_LIKES(R.drawable.ic_play_like_white, R.string.play_summary_total_likes),
    SHOP_VISIT(R.drawable.ic_play_visit_store_white, R.string.play_summary_shop_visit),
    PRODUCT_VISIT(R.drawable.ic_play_visit_product_white, R.string.play_summary_product_visit),
    NUMBER_OF_ATC(R.drawable.ic_play_cart_white, R.string.play_summary_atc),
    NUMBER_OF_PAID_ORDER(R.drawable.ic_play_shop_white, R.string.play_summary_purchased_product),
    NEW_BUYER(R.drawable.ic_play_new_follower_white, R.string.play_summary_new_buyer);
}