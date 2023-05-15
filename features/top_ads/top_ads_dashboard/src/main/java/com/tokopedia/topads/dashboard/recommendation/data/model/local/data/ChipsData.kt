package com.tokopedia.topads.dashboard.recommendation.data.model.local.data

import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsItemUiModel

object ChipsData {
    val chipsList = listOf(
        GroupDetailChipsItemUiModel("semua", true),
        GroupDetailChipsItemUiModel("Kata Kunci"),
        GroupDetailChipsItemUiModel("Biaya Kata Kunci"),
        GroupDetailChipsItemUiModel("Biaya Iklan"),
        GroupDetailChipsItemUiModel("Anggaran Harian"),
        GroupDetailChipsItemUiModel("Kata Kunci Negatif")
    )
}
