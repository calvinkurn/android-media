package com.tokopedia.sellerorder.detail.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.detail.data.model.GetResolutionTicketStatusResponse
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDynamicPriceResponse
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory
import com.tokopedia.sellerorder.detail.presentation.model.DividerUiModel
import com.tokopedia.unifycomponents.toPx

object SomDetailMapper {

    private const val THICK_DIVIDER_HEIGHT = 8
    private const val THICK_DIVIDER_VERTICAL_MARGIN = 16

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includeHeader(
        somGetOrderDetailResponse: SomDetailOrder.Data.GetSomDetail?
    ) {
        SomGetOrderDetailResponseMapper.mapResponseToHeaderUiModel(somGetOrderDetailResponse)?.let {
            add(it)
        }
    }

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includeReso(
        getResolutionTicketStatusResponse: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData?
    ) {
        if (getResolutionTicketStatusResponse?.shouldShow() == true) {
            SomResolutionResponseMapper.mapResponseToResolutionUIModel(
                getResolutionTicketStatusResponse
            )?.let {
                add(it)
                includeDivider()
            }
        }
    }

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includeProducts(
        somGetOrderDetailResponse: SomDetailOrder.Data.GetSomDetail?
    ) {
        SomGetOrderDetailResponseMapper.mapResponseToProductsHeaderUiModel(somGetOrderDetailResponse)
            ?.let {
                add(it)
            }
        addAll(
            SomGetOrderDetailResponseMapper.mapResponseToProductsUiModels(
                somGetOrderDetailResponse
            )
        )
    }

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includeShipment(
        somGetOrderDetailResponse: SomDetailOrder.Data.GetSomDetail?
    ) {
        SomGetOrderDetailResponseMapper.mapResponseToShipmentUiModel(somGetOrderDetailResponse)?.let {
            add(it)
        }
    }

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includePayment(
        somGetSomDynamicPrice: SomDynamicPriceResponse.GetSomDynamicPrice?
    ) {
        add(SomDynamicPaymentResponseMapper.mapResponseToPaymentsUiModel(somGetSomDynamicPrice))
    }

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includeMvc(
        somGetSomDynamicPrice: SomDynamicPriceResponse.GetSomDynamicPrice?
    ) {
        SomDynamicPaymentResponseMapper.mapResponseToMerchantVoucherUsageUiModel(
            somGetSomDynamicPrice
        )?.let {
            add(it)
        }
    }

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includePofData(
        pofData: SomDynamicPriceResponse.GetSomDynamicPrice.PofData?
    ) {
        SomDynamicPaymentResponseMapper.mapResponseToPofDataUiModel(
            pofData
        )?.let {
            add(it)
        }
    }

    private fun ArrayList<Visitable<SomDetailAdapterFactory>>.includeDivider() {
        add(
            DividerUiModel(
                height = THICK_DIVIDER_HEIGHT.toPx(),
                marginTop = THICK_DIVIDER_VERTICAL_MARGIN.toPx(),
                marginBottom = THICK_DIVIDER_VERTICAL_MARGIN.toPx()
            )
        )
    }

    fun mapSomGetOrderDetailResponseToVisitableList(
        somGetOrderDetailResponse: SomDetailOrder.Data.GetSomDetail?,
        somGetSomDynamicPrice: SomDynamicPriceResponse.GetSomDynamicPrice?,
        resolutionTicketStatusResponse: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData?
    ): List<Visitable<SomDetailAdapterFactory>> {
        return arrayListOf<Visitable<SomDetailAdapterFactory>>().apply {
            includeHeader(somGetOrderDetailResponse)
            includeDivider()
            includeReso(resolutionTicketStatusResponse)
            includeProducts(somGetOrderDetailResponse)
            includeDivider()
            includeShipment(somGetOrderDetailResponse)
            includeDivider()
            includePayment(somGetSomDynamicPrice)
            includePofData(somGetSomDynamicPrice?.pofData)
            includeMvc(somGetSomDynamicPrice)
        }
    }
}
