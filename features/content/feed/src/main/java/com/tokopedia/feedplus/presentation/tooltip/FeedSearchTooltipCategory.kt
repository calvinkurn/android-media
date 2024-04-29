package com.tokopedia.feedplus.presentation.tooltip

import androidx.annotation.StringRes
import com.tokopedia.feedplus.R

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
enum class FeedSearchTooltipCategory {

    UserAffinity,
    Creator,
    Story,
    Trending,
    Promo,
    Unknown;

    val isUnknown: Boolean
        get() = this == Unknown

    companion object {
        fun getByDay(day: Int): FeedSearchTooltipCategory {
            return when(day) {
                in 1..6 -> UserAffinity
                in 7..12 -> Creator
                in 13..18 -> Story
                in 19..24 -> Trending
                in 25..31 -> Promo
                else -> Unknown
            }
        }
    }
}
