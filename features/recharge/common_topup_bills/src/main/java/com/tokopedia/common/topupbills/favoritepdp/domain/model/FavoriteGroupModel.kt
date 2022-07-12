package com.tokopedia.common.topupbills.favoritepdp.domain.model

data class FavoriteGroupModel(
    val autoCompletes: List<AutoCompleteModel>,
    val favoriteChips: List<FavoriteChipModel>,
    val prefill: PrefillModel
)