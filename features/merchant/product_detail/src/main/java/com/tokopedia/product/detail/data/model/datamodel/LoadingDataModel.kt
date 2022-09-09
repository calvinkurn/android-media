package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class LoadingDataModel(
    override val impressHolder: ImpressHolder = ImpressHolder()
) : DynamicPdpDataModel {
    override fun type(): String = ""

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = ""

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean = true

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}