package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class TopadsHeadlineUiModel(
    var cpmModel: CpmModel? = null,
    var topadsHeadLinePage: Int = 0,
    var productId: String = ""
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = "topads_headline"

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = "topads_headline"

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
//        return if (newData is PageErrorDataModel) {
//            errorCode == newData.errorMessage
//        } else false
        return false
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}