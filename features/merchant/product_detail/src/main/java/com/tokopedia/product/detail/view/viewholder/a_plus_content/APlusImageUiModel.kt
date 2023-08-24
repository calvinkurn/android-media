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
    var expanded: Boolean = true,
    var showTopDivider: Boolean = false,
    var haveBottomPadding: Boolean = false,
    var trackerData: TrackerData = TrackerData()
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean = this == newData

    override fun newInstance(): DynamicPdpDataModel = copy(
        trackerData = trackerData.copy(
            componentTrackData = trackerData.componentTrackData.copy()
        )
    )

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder = ImpressHolder()

    data class TrackerData(
        var componentTrackData: ComponentTrackDataModel = ComponentTrackDataModel(),
        var mediaCount: Int = 0,
        var mediaPosition: Int = 0,
        var mediaUrl: String = "",
        var expanded: Boolean = false,
        var userID: String = "",
        var shopID: String = "",
        var productID: String = "",
        var layoutName: String = "",
        var categoryId: String = "",
        var categoryName: String = ""
    )
}
