package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 12/11/20
 */
data class ProductReportDataModel(
        val type: String = "",
        val name: String = ""
) : DynamicPdpDataModel {
    override fun type(): String = type
    override fun name(): String = name
    override val impressHolder: ImpressHolder = ImpressHolder()
    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is ProductReportDataModel
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}