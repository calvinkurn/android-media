package com.tokopedia.feedplus.presentation.tooltip

import androidx.annotation.StringRes
import com.tokopedia.feedplus.R

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
enum class FeedSearchTooltipCategory(
    @StringRes val text: Int,
) {

    UserAffinity(R.string.feed_search_tooltip_user_affinity),
    Creator(R.string.feed_search_tooltip_creator),
    Story(R.string.feed_search_tooltip_story),
    Trending(R.string.feed_search_tooltip_trending_and_viral_video),
    Promo(R.string.feed_search_tooltip_promo);

    companion object {
        fun getByDay(day: Int): FeedSearchTooltipCategory {
            return when(day) {
                in 1..6 -> UserAffinity
                in 7..12 -> Creator
                in 13..18 -> Story
                in 19..24 -> Trending
                else -> Promo
            }
        }
    }
}
