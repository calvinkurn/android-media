package com.tokopedia.deals.checkout.ui.mapper

import com.google.gson.Gson
import com.tokopedia.common_entertainment.data.DealsMetaDataCheckout
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapCheckout
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.common_entertainment.data.MetaDataResponse
import com.tokopedia.kotlin.extensions.view.toIntSafely

object DealsCheckoutMapper {

    fun getMetaDataString(verify: EventVerifyResponse): String {
        return Gson().toJson(mapToIntMetaData(verify.metadata))
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
                itemIds = convertStringListtoIntList(itemIds),
                productNames = productNames,
                productIds = convertStringListtoIntList(productIds),
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

    private fun convertStringListtoIntList(listString: List<String>): List<Int> {
        return listString.map {
            it.toIntSafely()
        }
    }
}
