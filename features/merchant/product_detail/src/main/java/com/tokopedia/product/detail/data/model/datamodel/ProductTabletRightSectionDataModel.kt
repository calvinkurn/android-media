package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant.TABLET_PAYLOAD
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory

data class ProductTabletRightSectionDataModel(
    val name: String = "",
    val type: String = "",
    val listVisitable: List<DynamicPdpDataModel> = listOf()
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: ProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductTabletRightSectionDataModel) {
            listVisitable.hashCode() == newData.listVisitable.hashCode()
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        bundle.putInt(TABLET_PAYLOAD, Int.ONE)
        return bundle
    }

    override val impressHolder: ImpressHolder
        get() = ImpressHolder()

}
