package com.tokopedia.digital_product_detail.domain.model

data class FavoriteGroupModel(
    val autoCompletes: List<AutoCompleteModel>,
    val favoriteChips: List<FavoriteChipModel>,
    val prefill: PrefillModel
)