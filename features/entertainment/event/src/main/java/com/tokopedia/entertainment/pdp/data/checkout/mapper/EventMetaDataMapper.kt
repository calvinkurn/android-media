package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.google.gson.Gson
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.checkout.*
import com.tokopedia.entertainment.pdp.data.pdp.ItemMapResponse
import com.tokopedia.entertainment.pdp.data.pdp.MetaDataResponse
import com.tokopedia.entertainment.pdp.data.pdp.PassengerForm
import com.tokopedia.entertainment.pdp.data.pdp.PassengerInformation

object EventMetaDataMapper {

    fun getPassengerMetaData(metaDataResponse: MetaDataResponse, forms: List<Form>,
                             listAdditionalDataItems: List<EventCheckoutAdditionalData>,
                             additionalDataPackage: EventCheckoutAdditionalData,
                             missString: String
    ): MetaDataResponse {

        val passengerInformation = forms.map {
            val value = getValueForm(it, missString)
            PassengerInformation(it.name, value, it.title)
        }.toMutableList()

        if(additionalDataPackage.listForm.isNotEmpty() &&
                additionalDataPackage.additionalType.equals(AdditionalType.PACKAGE_FILLED)){
            additionalDataPackage.listForm.map {
                val value = getValueForm(it, missString)
                passengerInformation.add(PassengerInformation(it.name, value, it.title))
            }
        }

        val passengerForm = PassengerForm(passengerInformation)

        for (itemMap in metaDataResponse.itemMap) {
                itemMap.passengerForms.clear()
                itemMap.passengerForms.add(passengerForm)
                if (!listAdditionalDataItems.isNullOrEmpty()) {
                    for (additionalItem in listAdditionalDataItems) {
                        if (itemMap.id.equals(additionalItem.idItemMap) && additionalItem.additionalType.equals(AdditionalType.ITEM_FILLED)) {
                            val passengerInformationItem = additionalItem.listForm.map {
                                val value = getValueForm(it, missString)
                                PassengerInformation(it.name, value, it.title)
                            }.toMutableList()
                            val passengerFormItem = PassengerForm(passengerInformationItem)
                            itemMap.passengerForms.add(passengerFormItem)
                        }
                    }
                }
        }

        return metaDataResponse
    }

    private fun getValueForm(form: Form, missString: String):String{
        return if(form.value.equals(missString, false)) ""
        else if(form.elementType.equals(ELEMENT_LIST)) form.valuePosition
        else form.value

    }

    fun getCheckoutParam(metaDataResponse: MetaDataResponse, productDetailData: ProductDetailData, packageV3: PackageV3): CheckoutGeneralV2Params {
        val gson = Gson()
        val checkoutGeneralV2Params = CheckoutGeneralV2Params()
        val cartInfo = CartInfo(gson.toJson(mapToIntMetaData(metaDataResponse,productDetailData,packageV3)),productDetailData.checkoutDataType)
        checkoutGeneralV2Params.carts.businessType = productDetailData.checkoutBusinessType
        checkoutGeneralV2Params.carts.cartInfo.add(0, cartInfo)
        return checkoutGeneralV2Params
    }

    fun getCheckoutParamInstant(gatewayCode:String, metaDataResponse: MetaDataResponse, productDetailData: ProductDetailData, packageV3: PackageV3): CheckoutGeneralV2InstantParams {
        val gson = Gson()
        val checkoutGeneralV2InstantParams = CheckoutGeneralV2InstantParams(gatewayCode = gatewayCode)
        val cartInfo = CartInfo(gson.toJson(mapToIntMetaData(metaDataResponse,productDetailData,packageV3)),productDetailData.checkoutDataType)
        checkoutGeneralV2InstantParams.carts.businessType = productDetailData.checkoutBusinessType
        checkoutGeneralV2InstantParams.carts.cartInfo.add(0, cartInfo)
        return checkoutGeneralV2InstantParams
    }

    private fun mapToIntMetaData(metaDataResponse: MetaDataResponse, productDetailData: ProductDetailData, packageV3: PackageV3): EventMetaDataCheckout {
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
                    itemMap = mapToItemMapCheckout(itemMap,productDetailData,packageV3)
            )
        }
    }

    private fun mapToItemMapCheckout(itemMapResponses: List<ItemMapResponse>, productDetailData: ProductDetailData, packageV3: PackageV3): List<ItemMapCheckout> {
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
                    locationName = productDetailData.outlets.firstOrNull().let { it?.district ?: "" },
                    locationDesc = productDetailData.outlets.firstOrNull().let { it?.name ?: "" },
                    mobile = it.mobile,
                    name = it.name,
                    orderTraceId = it.orderTraceId,
                    packageId = packageV3.id.toInt(),
                    packageName = packageV3.name,
                    paymentType = it.paymentType,
                    price = it.price,
                    productAppUrl = it.productAppUrl,
                    productId = it.productId.toInt(),
                    productImage = productDetailData.thumbnailApp,
                    productName = it.productName,
                    providerInvoiceCode = it.providerInvoiceCode,
                    providerPackageId = it.providerPackageId,
                    providerScheduleId = it.providerScheduleId,
                    providerTicketId = it.providerTicketId,
                    quantity = it.quantity,
                    scheduleTimestamp = it.scheduleTimestamp.toInt(),
                    startTime = it.startTime,
                    totalPrice = it.totalPrice,
                    productWebUrl = it.productWebUrl,
                    providerId = it.providerId.toInt(),
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