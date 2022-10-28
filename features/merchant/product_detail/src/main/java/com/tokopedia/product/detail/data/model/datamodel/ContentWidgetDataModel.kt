package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ContentWidgetDataModel(
    val name: String = "",
    val type: String = "",
    var playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true),
) : DynamicPdpDataModel {
    override fun type() = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory) = typeFactory.type(this)

    override fun name() = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is ContentWidgetDataModel &&
                newData.playWidgetState == playWidgetState
    }

    override fun newInstance() = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder = ImpressHolder()
}
