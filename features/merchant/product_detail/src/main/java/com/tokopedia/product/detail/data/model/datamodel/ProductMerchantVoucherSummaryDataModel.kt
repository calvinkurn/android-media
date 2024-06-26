package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory

data class ProductMerchantVoucherSummaryDataModel(
    val type: String = "",
    val name: String = "",
    var uiModel: UiModel = UiModel()
) : DynamicPdpDataModel,
    LoadableComponent by BlocksLoadableComponent(
        isFinishedLoading = { false },
        customBlocksName = "ProductMerchantVoucherSummaryDataModel"
    ) {

    override val tabletSectionPosition: TabletPosition
        get() = TabletPosition.LEFT

    data class UiModel(
        val animatedInfo: List<AnimatedInfos> = listOf(),
        val isShown: Boolean = false,
        val shopId: String = "",
        val productIdMVC: String = "",
        val additionalData: String = ""
    )

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun type(typeFactory: ProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMerchantVoucherSummaryDataModel) {
            newData.uiModel.animatedInfo.hashCode() == uiModel.animatedInfo.hashCode()
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

    override fun isLoading(): Boolean {
        return uiModel.animatedInfo.isEmpty()
    }
}
