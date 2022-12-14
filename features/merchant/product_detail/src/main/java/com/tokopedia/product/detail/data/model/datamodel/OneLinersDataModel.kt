package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.OneLinersContent
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class OneLinersDataModel(
        var name: String = "",
        var type: String = "",
        var oneLinersContent: OneLinersContent? = null,
        val impressIconRight: ImpressHolder = ImpressHolder()
) : DynamicPdpDataModel {

    companion object {
        const val SEPARATOR_TOP = "top"
        const val SEPARATOR_BOTTOM = "bottom"
        const val SEPARATOR_BOTH = "both"
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is OneLinersDataModel) {
            this == newData
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

    val shouldRenderContent
        get() = oneLinersContent?.isVisible == true

}
