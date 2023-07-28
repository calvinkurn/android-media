package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class DynamicOneLinerDataModel(
    val name: String,
    val type: String,
    var data: Data,
    override val impressHolder: ImpressHolder = ImpressHolder()
) : DynamicPdpDataModel {
    override fun type(): String = type
    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)
    override fun name(): String = name
    override fun newInstance(): DynamicPdpDataModel = this.copy()
    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean =
        newData is DynamicOneLinerDataModel &&
            newData.data.hashCode() == data.hashCode()

    data class Data(
        val text: String = "",
        val applink: String = "",
        val separator: String = "",
        val icon: String = "",
        val status: String = "",
        val chevronPos: String = ""
    ) {
        val shouldShowSeparatorTop
            get() = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH ||
                separator == ProductCustomInfoDataModel.SEPARATOR_TOP

        val shouldShowSeparatorBottom
            get() = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH ||
                separator == ProductCustomInfoDataModel.SEPARATOR_BOTTOM
    }
}
