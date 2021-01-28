package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 13/08/20
 */
data class ProductCustomInfoDataModel(
        var name: String = "",
        var type: String = "",

        var title: String = "",
        var icon: String = "",
        var description: String = "",
        var separator: String = "",
        var applink: String = ""
) : DynamicPdpDataModel {

    companion object {
        const val SEPARATOR_TOP = "top"
        const val SEPARATOR_BOTTOM = "bottom"
        const val SEPARATOR_BOTH = "both"
    }

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductCustomInfoDataModel) {
            title == newData.title
                    && icon == newData.icon
                    && description == newData.description
                    && separator == newData.separator
                    && applink == newData.applink
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