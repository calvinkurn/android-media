package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.productbundlewidget.model.WidgetType

data class GlobalBundlingDataModel(
    private val name: String = "",
    private val type: String = "",
    override val impressHolder: ImpressHolder = ImpressHolder(),
    val data: GlobalBundling = GlobalBundling(),
    var shouldRefresh: Boolean = true
) : DynamicPdpDataModel {

    companion object {
        val DEFAULT_WIDGET_TYPE = WidgetType.TYPE_2.typeCode
    }

    override fun type() = type
    override fun name() = name
    override fun newInstance() = this.copy()
    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
    override fun type(typeFactory: DynamicProductDetailAdapterFactory) = typeFactory.type(this)

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is GlobalBundlingDataModel &&
            newData.data == this.data
    }
}

data class GlobalBundling(
    val title: String = "",
    val widgetType: Int = GlobalBundlingDataModel.DEFAULT_WIDGET_TYPE,
    val productId: String = "",
    val whId: String = ""
)
