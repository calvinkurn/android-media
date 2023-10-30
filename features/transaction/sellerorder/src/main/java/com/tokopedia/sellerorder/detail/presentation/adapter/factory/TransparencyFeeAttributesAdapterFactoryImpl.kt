package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeHeaderLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSubComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeSummaryLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.BaseWidgetTransparencyFeeAttribute
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeComponentLabel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeHeaderLabel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeIcon
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeLabel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeSubComponentLabel
import com.tokopedia.sellerorder.detail.presentation.widget.transparency_fee.WidgetTransparencyFeeSummaryLabel

class TransparencyFeeAttributesAdapterFactoryImpl(
    private val actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : TransparencyFeeAdapterAttributesFactory {
    override fun type(uiModel: TransparencyFeeLabelUiModel): Int {
        return WidgetTransparencyFeeLabel.LAYOUT
    }

    override fun type(uiModel: TransparencyFeeIconUiModel): Int {
        return WidgetTransparencyFeeIcon.LAYOUT
    }

    override fun type(transparencyFeeHeaderLabelUiModel: TransparencyFeeHeaderLabelUiModel): Int {
        return WidgetTransparencyFeeHeaderLabel.LAYOUT
    }

    override fun type(transparencyFeeComponentLabelUiModel: TransparencyFeeComponentLabelUiModel): Int {
        return WidgetTransparencyFeeComponentLabel.LAYOUT
    }

    override fun type(transparencyFeeSubComponentLabelUiModel: TransparencyFeeSubComponentLabelUiModel): Int {
        return WidgetTransparencyFeeSubComponentLabel.LAYOUT
    }

    override fun type(transparencyFeeSummaryLabelUiModel: TransparencyFeeSummaryLabelUiModel): Int {
        return WidgetTransparencyFeeSummaryLabel.LAYOUT
    }

    fun createWidget(parent: View, type: Int): BaseWidgetTransparencyFeeAttribute<out BaseTransparencyFeeAttributes> {
        return when (type) {
            WidgetTransparencyFeeHeaderLabel.LAYOUT -> WidgetTransparencyFeeHeaderLabel(parent)
            WidgetTransparencyFeeComponentLabel.LAYOUT -> WidgetTransparencyFeeComponentLabel(parent)
            WidgetTransparencyFeeSubComponentLabel.LAYOUT -> WidgetTransparencyFeeSubComponentLabel(parent)
            WidgetTransparencyFeeSummaryLabel.LAYOUT -> WidgetTransparencyFeeSummaryLabel(parent)
            WidgetTransparencyFeeLabel.LAYOUT -> WidgetTransparencyFeeLabel(parent)
            WidgetTransparencyFeeIcon.LAYOUT -> WidgetTransparencyFeeIcon(parent, actionListener)
            else -> throw TypeNotSupportedException.create("Layout not supported");
        }
    }

    interface TransparencyFeeAttributesListener {
        fun onTransparencyInfoIconClicked(title: String, desc: String)
    }
}
