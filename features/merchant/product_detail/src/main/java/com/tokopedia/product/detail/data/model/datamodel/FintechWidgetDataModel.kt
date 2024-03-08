package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.pdp.fintech.view.FintechPriceURLDataModel
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory

data class FintechWidgetDataModel(
    val name: String = "",
    val type: String = "",
    var productId: String = "",
    var categoryId: String? = null,
    var idToPriceUrlMap: HashMap<String, FintechPriceURLDataModel> = HashMap(),
    var isLoggedIn: Boolean = false,
    var shopId: String = "",
    var parentId: String = "",
    val widgetSession: Long = 0L
) : DynamicPdpDataModel,
    LoadableComponent by BlocksLoadableComponent(
        { false },
        "FintechWidgetDataModel"
    ) {
    override fun type() = type

    override fun type(typeFactory: ProductDetailAdapterFactory) = typeFactory.type(this)

    override fun name() = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is FintechWidgetDataModel &&
            (
                newData.productId == this.productId &&
                    newData.idToPriceUrlMap == this.idToPriceUrlMap &&
                    newData.isLoggedIn == this.isLoggedIn
                )
    }

    override fun newInstance() = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder = ImpressHolder()

    override fun isLoading(): Boolean {
        return productId.isEmpty()
    }
}
