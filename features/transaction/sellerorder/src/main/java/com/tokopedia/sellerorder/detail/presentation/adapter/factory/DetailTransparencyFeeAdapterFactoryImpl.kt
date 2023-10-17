package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeComponentViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeErrorStateViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeHeaderViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeLoadingViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeSubComponentViewHolder
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeErrorStateUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeHeaderUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeLoadingUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSubComponentUiModel

class DetailTransparencyFeeAdapterFactoryImpl(
    private val actionListener: ActionListener
) : BaseAdapterTypeFactory(), DetailTransparencyFeeAdapterFactory {

    override fun type(uiModel: TransparencyFeeHeaderUiModel): Int {
        return TransparencyFeeHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: TransparencyFeeComponentUiModel): Int {
        return TransparencyFeeComponentViewHolder.LAYOUT
    }

    override fun type(uiModel: TransparencyFeeSubComponentUiModel): Int {
        return TransparencyFeeSubComponentViewHolder.LAYOUT
    }

    override fun type(uiModel: TransparencyFeeLoadingUiModel): Int {
        return TransparencyFeeLoadingViewHolder.LAYOUT
    }

    override fun type(uiModel: TransparencyFeeErrorStateUiModel): Int {
        return TransparencyFeeErrorStateViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TransparencyFeeHeaderViewHolder.LAYOUT -> TransparencyFeeHeaderViewHolder(
                parent,
                actionListener
            )
            TransparencyFeeComponentViewHolder.LAYOUT -> TransparencyFeeComponentViewHolder(
                parent,
                actionListener
            )
            TransparencyFeeSubComponentViewHolder.LAYOUT -> TransparencyFeeSubComponentViewHolder(
                parent,
                actionListener
            )
            TransparencyFeeLoadingViewHolder.LAYOUT -> TransparencyFeeLoadingViewHolder(parent)
            TransparencyFeeErrorStateViewHolder.LAYOUT -> TransparencyFeeErrorStateViewHolder(
                parent,
                actionListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }

    interface ActionListener :
        TransparencyFeeErrorStateViewHolder.Listener,
        TransparencyFeeAttributesAdapterFactoryImpl.TransparencyFeeAttributesListener
}
