package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class PageErrorDataModel(
        val type: String = "",
        val name: String = "",
        val errorCode: String = "",
        val errorMessage: String = "",
        val shouldShowTobacoError: Boolean = false,
        val tobacoErrorData: TobacoErrorData? = null
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is PageErrorDataModel) {
            errorCode == newData.errorMessage
        } else false
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}

data class TobacoErrorData(
        val messages: String = "",
        val title: String = "",
        val button: String = "",
        val url: String = ""
)