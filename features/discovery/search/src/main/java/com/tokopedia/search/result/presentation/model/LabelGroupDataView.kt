package com.tokopedia.search.result.presentation.model

import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_FULFILLMENT
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY
import com.tokopedia.search.result.domain.model.SearchProductModel.ProductLabelGroup
import com.tokopedia.search.result.domain.model.SearchProductV5

data class LabelGroupDataView(
    val position: String,
    val type: String,
    val title: String,
    val imageUrl: String = "",
    val styleList: List<StyleDataView> = listOf(),
) {

    fun isLabelIntegrity() = position == LABEL_INTEGRITY

    fun isLabelFulfillment() = position == LABEL_FULFILLMENT

    fun hasStyle() = styleList.isNotEmpty()

    fun getPositionTitle(): String = "$position.$title"

    companion object {
        fun create(productLabelGroup: ProductLabelGroup): LabelGroupDataView =
            LabelGroupDataView(
                productLabelGroup.position,
                productLabelGroup.type,
                productLabelGroup.title,
                productLabelGroup.url,
                productLabelGroup.styleList.map(StyleDataView::create),
            )

        fun hasFulfillment(labelGroupList: List<LabelGroupDataView>?): Boolean =
            labelGroupList?.any(LabelGroupDataView::isLabelFulfillment) == true

        fun create(labelGroup: SearchProductV5.Data.LabelGroup): LabelGroupDataView =
            LabelGroupDataView(
                labelGroup.position,
                labelGroup.type,
                labelGroup.title,
                labelGroup.url,
                labelGroup.styleList.map(StyleDataView::create),
            )
    }
}
