package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.util.PdpUiUpdater
import com.tokopedia.product.detail.view.viewholder.PdpRecommendationWidgetViewHolder
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel

/**
 * Created by frenzel on 27/03/23
 */
data class PdpRecommendationWidgetDataModel(
    val recommendationWidgetModel: RecommendationWidgetModel,
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = recommendationWidgetModel.metadata.pageType

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = recommendationWidgetModel.metadata.pageName

    /**
     * Here we perform reference equality check instead of content check because the [PdpRecommendationWidgetDataModel] will only get created from the [PdpUiUpdater.updateDataP1] and everytime it gets recreated, we want to refresh the recom data.
     */
    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return this === newData
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    /**
     * Here we return empty [Bundle] just to trigger the bind with payload functionality so the adapter can reuse the already attached [PdpRecommendationWidgetViewHolder] instead of recreating a new one whenever we want the recom data to be reloaded.
     */
    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return Bundle.EMPTY
    }
}
