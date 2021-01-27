package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.Content
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductGeneralInfoDataModel(
        val name: String = "",
        val type: String = "",
        val applink: String = "",
        var title: String = "",
        var isApplink: Boolean = false,
        val parentIcon: String = "",
        var data: List<Content> = listOf(Content())
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductGeneralInfoDataModel) {
            applink == newData.applink
                    && title == newData.title
                    && isApplink == newData.isApplink
                    && parentIcon == newData.parentIcon
                    && areContentTheSame(newData.data.firstOrNull())
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy(data = data.map { it.copy() })
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }

    private fun areContentTheSame(content: Content?): Boolean {
        val currentContent = data.firstOrNull()
        return currentContent?.applink == content?.applink
                && currentContent?.icon == content?.icon
                && currentContent?.subtitle == content?.subtitle
                && currentContent?.title == content?.title
    }
}