package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMerchantVoucherSummaryDataModel(
        val type: String = "",
        val name: String = "",
        var animatedInfos: List<AnimatedInfos> = listOf(),
        var isShown: Boolean = false,
        var shopId: String = "",
        var productIdMVC: String = ""
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMerchantVoucherSummaryDataModel) {
            newData.animatedInfos.hashCode() == animatedInfos.hashCode()
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