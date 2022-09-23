package com.tokopedia.common.topupbills.favoritepage.view.util

import com.tokopedia.common.topupbills.R

enum class FavoriteNumberPageConfig(
    val headerTextRes: Int,
    val clueTextRes: Int,
    val searchBarHintRes: Int,
) {
    CREDIT_CARD (
        R.string.common_topup_fav_number_title_cc,
        R.string.common_topup_fav_number_clue_cc,
        R.string.common_topup_saved_number_searchbar_placeholder_cc
    ),
    TELCO (
        R.string.common_topup_fav_number_title,
        R.string.common_topup_fav_number_clue,
        R.string.common_topup_saved_number_searchbar_placeholder
    )
}
