package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.widget.global.RecomVisitable

/**
 * Created by frenzel on 27/03/23
 */
data class PdpRecomWidgetDataModel(
    var recomWidgetModel: RecomVisitable
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = recomWidgetModel.recomWidgetMetadata.pageType

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = recomWidgetModel.recomWidgetMetadata.pageName

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is PdpRecomWidgetDataModel) {
            recomWidgetModel.recomWidgetMetadata == newData.recomWidgetModel.recomWidgetMetadata &&
                recomWidgetModel.recomWidgetMetadata.isInitialized == newData.recomWidgetModel.recomWidgetMetadata.isInitialized &&
                recomWidgetModel.recomWidgetMetadata.pageName == newData.recomWidgetModel.recomWidgetMetadata.pageName &&
                recomWidgetModel.recomWidgetMetadata.verticalPosition == newData.recomWidgetModel.recomWidgetMetadata.verticalPosition
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
