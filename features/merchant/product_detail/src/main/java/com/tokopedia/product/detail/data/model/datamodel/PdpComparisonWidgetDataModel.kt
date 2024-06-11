package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class PdpComparisonWidgetDataModel(
    val type: String = "",
    val name: String = "",
    val recommendationWidget: RecommendationWidget,
    val appLogAdditionalParam: AppLogAdditionalParam = AppLogAdditionalParam.PDP()
) : DynamicPdpDataModel {

    override val tabletSectionPosition: TabletPosition
        get() = TabletPosition.BOTTOM

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun type(typeFactory: ProductDetailAdapterFactory): Int {
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
