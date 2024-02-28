package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory

data class LoadingDataModel(
    override val impressHolder: ImpressHolder = ImpressHolder()
) : DynamicPdpDataModel {

    override val tabletSectionPosition: TabletPosition
        get() = TabletPosition.BOTTOM

    override fun type(): String = ProductDetailConstant.PDP_VERTICAL_LOADING

    override fun type(typeFactory: ProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = ProductDetailConstant.PDP_VERTICAL_LOADING

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean = true

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}
