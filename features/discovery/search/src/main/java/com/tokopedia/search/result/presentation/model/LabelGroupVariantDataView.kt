package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductV5

data class LabelGroupVariantDataView(
        val title: String = "",
        val type: String = "",
        val typeVariant: String = "",
        val hexColor: String = ""
) {

    companion object {
        fun create(labelGroupVariant: SearchProductV5.Data.Product.LabelGroupVariant) =
            LabelGroupVariantDataView(
                title = labelGroupVariant.title,
                type = labelGroupVariant.type,
                typeVariant = labelGroupVariant.typeVariant,
                hexColor = labelGroupVariant.hexColor,
            )
    }
}
