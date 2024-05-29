package com.tokopedia.product.detail.view.viewholder.dynamic_oneliner

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoDataModel
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.unifycomponents.toPx

data class DynamicOneLinerUiModel(
    val name: String,
    val type: String,
    var data: Data,
    override val impressHolder: ImpressHolder = ImpressHolder()
) : DynamicPdpDataModel {
    override fun type(): String = type
    override fun type(typeFactory: ProductDetailAdapterFactory): Int = typeFactory.type(this)
    override fun name(): String = name
    override fun newInstance(): DynamicPdpDataModel = this.copy()
    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean =
        newData is DynamicOneLinerUiModel &&
            newData.data.hashCode() == data.hashCode()

    data class Data(
        val text: String = "",
        val applink: String = "",
        val separator: String = "",
        val icon: String = "",
        val status: String = "",
        val chevronPos: String = "",
        private val paddingTop: Int = 0,
        private val paddingBottom: Int = 0,
        private val imageWidth: Int = 0,
        private val imageHeight: Int = 0
    ) {
        val paddingTopPx = paddingTop.toPx()
        val paddingBottomPx = paddingBottom.toPx()
        val imageWidthPx = imageWidth.toPx()
        val imageHeightPx = imageHeight.toPx()

        val shouldShowSeparatorTop
            get() = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH ||
                separator == ProductCustomInfoDataModel.SEPARATOR_TOP

        val shouldShowSeparatorBottom
            get() = separator == ProductCustomInfoDataModel.SEPARATOR_BOTH ||
                separator == ProductCustomInfoDataModel.SEPARATOR_BOTTOM
    }
}
