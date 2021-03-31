package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel

data class PdpComparisonWidgetDataModel(
        val type: String = "",
        val name: String = "",
        var recommendationWidget: RecommendationWidget
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    val isRecomenDataEmpty: Boolean
        get() = recommendationWidget.recommendationItemList.isEmpty()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is PdpComparisonWidgetDataModel) {
            newData.recommendationWidget == recommendationWidget
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return Bundle()
    }
}