package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeIconViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeLabelViewHolder
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLabelUiModel

class TransparencyFeeAttributesAdapterFactoryImpl(
    private val actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : BaseAdapterTypeFactory(),
    TransparencyFeeAdapterAttributesFactory {
    override fun type(uiModel: TransparencyFeeLabelUiModel): Int {
        return TransparencyFeeLabelViewHolder.LAYOUT
    }

    override fun type(uiModel: TransparencyFeeIconUiModel): Int {
        return TransparencyFeeIconViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TransparencyFeeLabelViewHolder.LAYOUT -> TransparencyFeeLabelViewHolder(parent)
            TransparencyFeeIconViewHolder.LAYOUT -> TransparencyFeeIconViewHolder(parent, actionListener)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface TransparencyFeeAttributesListener {
        fun onTransparencyInfoIconClicked(title: String, desc: String)
    }
}
