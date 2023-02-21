package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewItemData

data class ViewToViewWidgetDataModel(
    val type: String = "",
    val name: String = "",
    var recomWidgetData: RecommendationWidget? = null,

    //UI Data
    var cardModel: List<ViewToViewItemData>? = null,
    var position: Int = -1,
    var state: Int = RecommendationCarouselData.STATE_LOADING,
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    val isLoading: Boolean
        get() = state == RecommendationCarouselData.STATE_LOADING

    val isFailed: Boolean
        get() = state == RecommendationCarouselData.STATE_FAILED

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ViewToViewWidgetDataModel) {
            recomWidgetData?.title == newData.recomWidgetData?.title
                && areRecomItemTheSame(newData.recomWidgetData)
                && state == newData.state
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

    private fun areRecomItemTheSame(newRecomWidgetData: RecommendationWidget?): Boolean {
        if (recomWidgetData?.recommendationItemList == null && newRecomWidgetData?.recommendationItemList == null) {
            return true
        } else if (recomWidgetData?.recommendationItemList?.size != newRecomWidgetData?.recommendationItemList?.size) {
            return false
        }

        return recomWidgetData?.recommendationItemList?.hashCode() == newRecomWidgetData?.recommendationItemList?.hashCode()
    }
}
