package com.tokopedia.search.result.mps.loading

import androidx.annotation.StringRes
import com.tokopedia.search.R

enum class MPSLoadingCopy(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
) {
    PAIR_1(
        title = R.string.search_mps_loading_title_1,
        subtitle = R.string.search_mps_loading_subtitle_1,
    ),
    PAIR_2(
        title = R.string.search_mps_loading_title_1,
        subtitle = R.string.search_mps_loading_subtitle_2,
    ),
    PAIR_3(
        title = R.string.search_mps_loading_title_2,
        subtitle = R.string.search_mps_loading_subtitle_3,
    ),
    PAIR_4(
        title = R.string.search_mps_loading_title_2,
        subtitle = R.string.search_mps_loading_subtitle_4,
    ),
    PAIR_5(
        title = R.string.search_mps_loading_title_3,
        subtitle = R.string.search_mps_loading_subtitle_5,
    ),
    PAIR_6(
        title = R.string.search_mps_loading_title_3,
        subtitle = R.string.search_mps_loading_subtitle_6,
    );

    operator fun component1() = title

    operator fun component2() = subtitle

    companion object {
        private const val PAIR_NAME = "PAIR_"

        fun get(): MPSLoadingCopy {
            val randomCopy = "$PAIR_NAME${(1..6).random()}"
            return MPSLoadingCopy.valueOf(randomCopy)
        }
    }
}
