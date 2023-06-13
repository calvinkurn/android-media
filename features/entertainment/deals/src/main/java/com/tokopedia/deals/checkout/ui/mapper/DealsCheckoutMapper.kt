package com.tokopedia.deals.checkout.ui.mapper

import com.google.gson.Gson
import com.tokopedia.common_entertainment.data.CartInfo
import com.tokopedia.common_entertainment.data.DealCheckoutGeneral
import com.tokopedia.common_entertainment.data.DealCheckoutGeneralInstant
import com.tokopedia.common_entertainment.data.DealCheckoutGeneralInstantNoPromo
import com.tokopedia.common_entertainment.data.DealCheckoutGeneralNoPromo
import com.tokopedia.common_entertainment.data.DealsMetaDataCheckout
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapCheckout
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.common_entertainment.data.MetaDataResponse
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import javax.inject.Inject

class DealsCheckoutMapper @Inject constructor(val gson: Gson) {

    companion object {
        private const val DEFAULT_CHECKOUT_DATA_TYPE = "foodvchr"
    }

    fun mapCheckoutDeals(dealsDetail: ProductDetailData, verify: EventVerifyResponse, promoCodes: List<String>):
        DealCheckoutGeneral {
        val checkoutGeneral = DealCheckoutGeneral()
        val cartInfo = CartInfo(gson.toJson(mapToIntMetaData(verify.metadata)),
            dealsDetail.checkoutDataType.ifEmpty { DEFAULT_CHECKOUT_DATA_TYPE }
        )
        checkoutGeneral.carts.businessType = dealsDetail.checkoutBusinessType
        checkoutGeneral.carts.cartInfo.add(Int.ZERO, cartInfo)
        checkoutGeneral.carts.promoCodes = promoCodes
        return checkoutGeneral
    }

    fun mapCheckoutDeals(dealsDetail: ProductDetailData, verify: EventVerifyResponse): DealCheckoutGeneralNoPromo {
        val checkoutGeneral = DealCheckoutGeneralNoPromo()
        val cartInfo = CartInfo(gson.toJson(mapToIntMetaData(verify.metadata)),
            dealsDetail.checkoutDataType.ifEmpty { DEFAULT_CHECKOUT_DATA_TYPE }
        )
        checkoutGeneral.carts.businessType = dealsDetail.checkoutBusinessType
        checkoutGeneral.carts.cartInfo.add(Int.ZERO, cartInfo)
        return checkoutGeneral
    }

    fun mapCheckoutDealsInstant(dealsDetail: ProductDetailData, verify: EventVerifyResponse, promoCodes: List<String>):
        DealCheckoutGeneralInstant {
        val checkoutGeneral = DealCheckoutGeneralInstant()
        val cartInfo = CartInfo(gson.toJson(mapToIntMetaData(verify.metadata)),
            dealsDetail.checkoutDataType.ifEmpty { DEFAULT_CHECKOUT_DATA_TYPE }
        )
        checkoutGeneral.carts.businessType = dealsDetail.checkoutBusinessType
        checkoutGeneral.carts.cartInfo.add(Int.ZERO, cartInfo)
        checkoutGeneral.carts.promoCodes = promoCodes
        checkoutGeneral.gatewayCode = verify.gatewayCode
        return checkoutGeneral
    }

    fun mapCheckoutDealsInstant(dealsDetail: ProductDetailData, verify: EventVerifyResponse): DealCheckoutGeneralInstantNoPromo {
        val checkoutGeneral = DealCheckoutGeneralInstantNoPromo()
        val cartInfo = CartInfo(gson.toJson(mapToIntMetaData(verify.metadata)),
            dealsDetail.checkoutDataType.ifEmpty { DEFAULT_CHECKOUT_DATA_TYPE }
        )
        checkoutGeneral.carts.businessType = dealsDetail.checkoutBusinessType
        checkoutGeneral.carts.cartInfo.add(Int.ZERO, cartInfo)
        checkoutGeneral.gatewayCode = verify.gatewayCode
        return checkoutGeneral
    }

    fun getMetaDataString(verify: EventVerifyResponse): String {
        return gson.toJson(mapToIntMetaData(verify.metadata))
    }

    private fun mapToIntMetaData(metaDataResponse: MetaDataResponse): DealsMetaDataCheckout {
        metaDataResponse.apply {
            return DealsMetaDataCheckout(
                categoryName = categoryName,
                error = error,
                orderTitle = orderTitle,
                orderSubTitle = orderSubTitle,
                quantity = quantity,
                totalPrice = totalPrice,
                itemIds = convertStringListToIntList(itemIds),
                productNames = productNames,
                productIds = convertStringListToIntList(productIds),
                itemMap = mapToItemMapCheckout(itemMap)
            )
        }
    }

    private fun mapToItemMapCheckout(itemMapResponses: List<ItemMapResponse>): List<ItemMapCheckout> {
        return itemMapResponses.map {
            ItemMapCheckout(
                basePrice = it.basePrice.toIntSafely().toLong(),
                categoryId = it.categoryId.toIntSafely().toLong(),
                childCategoryIds = it.childCategoryIds,
                commission = it.commission,
                commissionType = it.commissionType,
                currencyPrice = it.currencyPrice,
                description = it.description,
                email = it.email,
                endTime = it.endTime,
                error = it.error,
                flagId = it.flagId.toIntSafely().toLong(),
                id = it.id.toIntSafely().toLong(),
                invoiceId = it.invoiceId.toIntSafely().toLong(),
                invoiceItemId = it.invoiceItemId.toIntSafely().toLong(),
                invoiceStatus = it.invoiceStatus,
                locationName = it.locationName,
                locationDesc = it.locationDesc,
                mobile = it.mobile,
                name = it.name,
                orderTraceId = it.orderTraceId,
                packageId = it.packageId.toIntSafely().toLong(),
                packageName = it.packageName,
                paymentType = it.paymentType,
                price = it.price,
                productAppUrl = it.productAppUrl,
                productId = it.productId.toIntSafely().toLong(),
                productImage = it.productImage,
                productName = it.productName,
                providerInvoiceCode = it.providerInvoiceCode,
                providerPackageId = it.providerPackageId,
                providerScheduleId = it.providerScheduleId,
                providerTicketId = it.providerTicketId,
                quantity = it.quantity,
                scheduleTimestamp = it.scheduleTimestamp.toIntSafely(),
                startTime = it.startTime,
                totalPrice = it.totalPrice,
                productWebUrl = it.productWebUrl,
                providerId = it.providerId.toIntSafely().toLong(),
                passengerForms = it.passengerForms
            )
        }
    }

    private fun convertStringListToIntList(listString: List<String>): List<Int> {
        return listString.map {
            it.toIntSafely()
        }
    }
}
