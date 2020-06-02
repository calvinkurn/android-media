package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.google.gson.Gson
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.checkout.*
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import com.tokopedia.entertainment.pdp.data.pdp.PassengerForm
import com.tokopedia.entertainment.pdp.data.pdp.PassengerInformation

object EventMetaDataMapper {

    fun getPassengerMetaData(metaDataResponse: MetaDataResponse, forms: List<Form>): MetaDataResponse {
        val passengerInformation = forms.map {
            PassengerInformation(it.name, it.value)
        }
        val passengerForm = PassengerForm(passengerInformation)

        for (itemMap in metaDataResponse.itemMap) {
            itemMap.passengerForms.add(0, passengerForm)
        }
        return metaDataResponse
    }


    fun getCheckoutParam(metaDataResponse: MetaDataResponse): CheckoutGeneralV2Params {
        val gson = Gson()
        val checkoutGeneralV2Params = CheckoutGeneralV2Params()
        val cartInfo = CartInfo(gson.toJson(mapToIntMetaData(metaDataResponse)))
        checkoutGeneralV2Params.carts.cartInfo.add(0, cartInfo)
        return checkoutGeneralV2Params
    }

    private fun mapToIntMetaData(metaDataResponse: MetaDataResponse): EventMetaDataCheckout {
        metaDataResponse.apply {
            return EventMetaDataCheckout(
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
                    basePrice = it.basePrice.toInt(),
                    categoryId = it.categoryId.toInt(),
                    childCategoryIds = it.childCategoryIds,
                    commission = it.commission,
                    commissionType = it.commissionType,
                    currencyPrice = it.currencyPrice,
                    description = it.description,
                    email = it.email,
                    endTime = it.endTime,
                    error = it.error,
                    flagId = it.flagId.toInt(),
                    id = it.id.toInt(),
                    invoiceId = it.invoiceId.toInt(),
                    invoiceItemId = it.invoiceItemId.toInt(),
                    invoiceStatus = it.invoiceStatus,
                    locationName = it.locationName,
                    locationDesc = it.locationDesc,
                    mobile = it.mobile,
                    name = it.name,
                    orderTraceId = it.orderTraceId,
                    packageId = it.packageId.toInt(),
                    packageName = it.packageName,
                    paymentType = it.paymentType,
                    price = it.price,
                    productAppUrl = it.productAppUrl,
                    productId = it.productId.toInt(),
                    productImage = it.productImage,
                    productName = it.productName,
                    providerInvoiceCode = it.providerInvoiceCode,
                    providerPackageId = it.providerPackageId,
                    providerScheduleId = it.providerScheduleId,
                    providerTicketId = it.providerTicketId,
                    quantity = it.quantity,
                    scheduleTimestamp = it.scheduleTimestamp.toInt(),
                    startTime = it.startTime,
                    totalPrice = it.totalPrice,
                    webAppUrl = it.webAppUrl,
                    passengerForms = it.passengerForms
            )
        }
    }

    private fun convertStringListtoIntList(listString: List<String>): List<Int> {
        return listString.map {
            it.toInt()
        }
    }
}