package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductMerchantVoucherSummaryDataModel(
    val type: String = "",
    val name: String = "",
    var uiModel: UiModel = UiModel()
) : DynamicPdpDataModel {

    data class UiModel(
        val animatedInfo: List<AnimatedInfos> = listOf(),
        val isShown: Boolean = false,
        val shopId: String = "",
        val productIdMVC: String = "",
        val additionalData: String = ""
    ) {
        override fun equals(other: Any?): Boolean {
            val uiModel = (other as? UiModel) ?: return false
            return uiModel.animatedInfo.hashCode() == animatedInfo.hashCode()
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMerchantVoucherSummaryDataModel) {
            newData.uiModel == uiModel
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
