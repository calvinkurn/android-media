package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class TopAdsImageDataModel(
        val name: String = "",
        val type: String = "",
        var data: ArrayList<TopAdsImageViewModel>?= null
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is TopAdsImageDataModel) {
            val oldBannerId = data?.getOrNull(0)?.bannerId ?: ""
            val newBannerId = newData.data?.getOrNull(0) ?: ""

            oldBannerId == newBannerId
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}