package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeComponentLabelViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeHeaderLabelViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeIconViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeLabelViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeSubComponentLabelViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.TransparencyFeeSummaryLabelViewHolder
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryLabelUiModel

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

    override fun type(transparencyFeeHeaderLabelUiModel: TransparencyFeeHeaderLabelUiModel): Int {
        return TransparencyFeeHeaderLabelViewHolder.LAYOUT
    }

    override fun type(transparencyFeeComponentLabelUiModel: TransparencyFeeComponentLabelUiModel): Int {
        return TransparencyFeeComponentLabelViewHolder.LAYOUT
    }

    override fun type(transparencyFeeSubComponentLabelUiModel: TransparencyFeeSubComponentLabelUiModel): Int {
        return TransparencyFeeSubComponentLabelViewHolder.LAYOUT
    }

    override fun type(transparencyFeeSummaryLabelUiModel: TransparencyFeeSummaryLabelUiModel): Int {
        return TransparencyFeeSummaryLabelViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TransparencyFeeHeaderLabelViewHolder.LAYOUT -> TransparencyFeeHeaderLabelViewHolder(parent)
            TransparencyFeeComponentLabelViewHolder.LAYOUT -> TransparencyFeeComponentLabelViewHolder(parent)
            TransparencyFeeSubComponentLabelViewHolder.LAYOUT -> TransparencyFeeSubComponentLabelViewHolder(parent)
            TransparencyFeeSummaryLabelViewHolder.LAYOUT -> TransparencyFeeSummaryLabelViewHolder(parent)
            TransparencyFeeLabelViewHolder.LAYOUT -> TransparencyFeeLabelViewHolder(parent)
            TransparencyFeeIconViewHolder.LAYOUT -> TransparencyFeeIconViewHolder(parent, actionListener)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface TransparencyFeeAttributesListener {
        fun onTransparencyInfoIconClicked(title: String, desc: String)
    }
}
