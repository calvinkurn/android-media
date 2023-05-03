package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationVisitable

/**
 * Created by frenzel on 27/03/23
 */
data class PdpRecommendationWidgetDataModel(
    var recommendationWidgetModel: RecommendationVisitable
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = recommendationWidgetModel.metadata.pageType

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = recommendationWidgetModel.metadata.pageName

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is PdpRecommendationWidgetDataModel) {
            recommendationWidgetModel.metadata == newData.recommendationWidgetModel.metadata &&
                recommendationWidgetModel.metadata.isInitialized == newData.recommendationWidgetModel.metadata.isInitialized &&
                recommendationWidgetModel.metadata.pageName == newData.recommendationWidgetModel.metadata.pageName &&
                recommendationWidgetModel.metadata.verticalPosition == newData.recommendationWidgetModel.metadata.verticalPosition
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }
}
