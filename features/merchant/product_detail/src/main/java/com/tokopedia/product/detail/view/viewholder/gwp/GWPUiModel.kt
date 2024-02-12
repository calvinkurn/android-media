package com.tokopedia.product.detail.view.viewholder.gwp

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.viewholder.bmgm.BMGMUiModel
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiState

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

data class GWPUiModel(
    val type: String = "",
    val name: String = ""
) : DynamicPdpDataModel {

    var state: GWPWidgetUiState = GWPWidgetUiState.Hide
        private set

    fun setState(state: GWPWidgetUiState) {
        this.state = state
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = type

    override fun name(): String = name

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is GWPUiModel && state == newData.state
    }

    override fun newInstance(): DynamicPdpDataModel = copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return if (newData is GWPUiModel) {
            if (state !== newData.state)
                return null

            if (state != newData.state) {
                Bundle().apply { putString(ProductDetailConstant.DIFFUTIL_PAYLOAD, "") }
            } else {
                null
            }
        } else {
            null
        }
    }
}
