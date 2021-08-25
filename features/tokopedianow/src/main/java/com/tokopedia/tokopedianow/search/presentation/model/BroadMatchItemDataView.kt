package com.tokopedia.tokopedianow.search.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.NonVariantATCDataView

data class BroadMatchItemDataView(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val applink: String = "",
    val priceString: String = "",
    val position: Int = 0,
    val alternativeKeyword: String = "",
    val ratingAverage: String = "",
    val labelGroupDataList: List<LabelGroupDataView> = listOf(),
    val dimension90: String = "",
    val shop: Shop = Shop(),
    val nonVariantATC: NonVariantATCDataView? = null,
): ImpressHolder() {

    data class Shop(val id: String = "")
}