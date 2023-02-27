package com.tokopedia.minicart.common.widget.shoppingsummary.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryTotalTransactionUiModel

interface ShoppingSummaryTypeFactory {

    fun type(uiModel: ShoppingSummaryHeaderUiModel): Int

    fun type(uiModel: ShoppingSummaryProductUiModel): Int

    fun type(uiModel: ShoppingSummarySeparatorUiModel): Int

    fun type(uiModel: ShoppingSummaryTotalTransactionUiModel): Int

    fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<*>
}
