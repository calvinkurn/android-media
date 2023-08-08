package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMUiState
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

data class ProductBMGMDataModel(
    val type: String = "",
    val name: String = "",
    var state: BMGMUiState = BMGMUiState.Loading
): DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun name(): String = name

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is ProductBMGMDataModel && state == newData.state
    }

    override fun newInstance(): DynamicPdpDataModel = copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}
