package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofErrorStateUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofFullyFulfilledProductListHeaderUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofLoadingUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownTotalUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductListHeaderUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductStaticUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofSummaryDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThickDividerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThinDividerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofTickerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent

interface PofAdapterTypeFactory: AdapterTypeFactory {
    fun type(pofLoadingUiModel: PofLoadingUiModel): Int
    fun type(pofProductEditableUiModel: PofProductEditableUiModel): Int
    fun type(pofThinDividerUiModel: PofThinDividerUiModel): Int
    fun type(pofThickDividerUiModel: PofThickDividerUiModel): Int
    fun type(pofTickerUiModel: PofTickerUiModel): Int
    fun type(pofDescriptionUiModel: PofDescriptionUiModel): Int
    fun type(pofProductListHeaderUiModel: PofProductListHeaderUiModel): Int
    fun type(pofErrorStateUiModel: PofErrorStateUiModel): Int
    fun type(pofPriceBreakdownUiModel: PofPriceBreakdownUiModel): Int
    fun type(pofPriceBreakdownTotalUiModel: PofPriceBreakdownTotalUiModel): Int
    fun type(pofSummaryDescriptionUiModel: PofSummaryDescriptionUiModel): Int
    fun type(pofProductStaticUiModel: PofProductStaticUiModel): Int
    fun type(pofFullyFulfilledProductListHeaderUiModel: PofFullyFulfilledProductListHeaderUiModel): Int

    interface Listener {
        fun onEvent(event: UiEvent)
    }
}
