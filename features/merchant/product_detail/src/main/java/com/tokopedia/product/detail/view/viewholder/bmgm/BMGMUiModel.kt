package com.tokopedia.product.detail.view.viewholder.bmgm

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiState

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

data class BMGMUiModel(
    val type: String = "",
    val name: String = "",
    var state: BMGMWidgetUiState = BMGMWidgetUiState.Loading
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun name(): String = name

    override fun type(typeFactory: ProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is BMGMUiModel && state == newData.state
    }

    override fun newInstance(): DynamicPdpDataModel = copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}
