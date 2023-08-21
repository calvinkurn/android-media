package com.tokopedia.search.result.presentation.model

import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_FULFILLMENT
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup

data class LabelGroupDataView(
    val position: String,
    val type: String,
    val title: String,
    val imageUrl: String = ""
) {

    fun isLabelIntegrity() = position == LABEL_INTEGRITY

    fun isLabelFulfillment() = position == LABEL_FULFILLMENT

    fun getPositionTitle(): String = "$position.$title"

    companion object {
        fun create(productLabelGroup: ProductLabelGroup): LabelGroupDataView =
            LabelGroupDataView(
                productLabelGroup.position,
                productLabelGroup.type,
                productLabelGroup.title,
                productLabelGroup.url,
            )

        fun hasFulfillment(labelGroupList: List<LabelGroupDataView>?): Boolean =
            labelGroupList?.any(LabelGroupDataView::isLabelFulfillment) == true
    }
}
