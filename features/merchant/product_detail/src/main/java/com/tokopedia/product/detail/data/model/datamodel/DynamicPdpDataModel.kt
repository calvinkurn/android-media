package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

interface DynamicPdpDataModel : Visitable<DynamicProductDetailAdapterFactory> {
    fun type(): String
    fun name(): String
    fun equalsWith(newData: DynamicPdpDataModel): Boolean
    fun newInstance() : DynamicPdpDataModel
    fun getChangePayload(newData: DynamicPdpDataModel) : Bundle?
    val tabletSectionPosition: TabletPosition
        get() = TabletPosition.RIGHT

    val impressHolder: ImpressHolder
    fun impressionKey() = type() + name()
}

enum class TabletPosition(
    val type: String
) {
    LEFT("left"),
    RIGHT("right"),
    BOTTOM("bottom")
}
