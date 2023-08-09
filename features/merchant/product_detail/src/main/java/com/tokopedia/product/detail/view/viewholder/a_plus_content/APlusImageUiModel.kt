package com.tokopedia.product.detail.view.viewholder.a_plus_content

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class APlusImageUiModel(
    var name: String = "",
    var type: String = "",
    var url: String = "",
    var ratio: String = "1:1",
    var title: String = "",
    var description: String = "",
    var showOnCollapsed: Boolean = true,
    var ctaText: String = "",
    var collapsed: Boolean = true,
    var showTopDivider: Boolean = false,
    var trackerData: ComponentTrackDataModel = ComponentTrackDataModel()
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean = this == newData

    override fun newInstance(): DynamicPdpDataModel = copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder = ImpressHolder()
}
