package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.CodDataPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrivalPromo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PriceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.oneclickcheckout.order.view.mapper.PrescriptionMapper
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfilePayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderData
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentFee
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.ImageUploadDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult

class OrderSummaryPageViewModelTestHelper {

    val firstCourierFirstDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "kirimin"
            shipperId = 1
            shipperProductId = 1
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 1000
            }
        }
        ratesId = "0"
    }

    val secondCourierFirstDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "pakirim"
            shipperId = 2
            shipperProductId = 2
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 1500
            }
        }
        ratesId = "0"
    }

    val firstDuration = ShippingDurationUiModel().apply {
        serviceData = ServiceData().apply {
            serviceId = 1
            serviceName = "durasi (1 hari)"
            error = ErrorServiceData()
        }
        shippingCourierViewModelList = listOf(firstCourierFirstDuration, secondCourierFirstDuration)
    }

    val firstCourierSecondDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "pakirim"
            shipperId = 2
            shipperProductId = 3
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 2000
            }
        }
        ratesId = "0"
    }

    val secondDuration = ShippingDurationUiModel().apply {
        serviceData = ServiceData().apply {
            serviceId = 2
            serviceName = "durasi (2 hari)"
            error = ErrorServiceData()
        }
        shippingCourierViewModelList = listOf(firstCourierSecondDuration)
    }

    val logisticPromo = LogisticPromoUiModel("bbo", "bbo", "bbo", firstCourierSecondDuration.productData.shipperName,
            secondDuration.serviceData.serviceId, firstCourierSecondDuration.productData.shipperId, firstCourierSecondDuration.productData.shipperProductId,
            "", "", "", false, "",
            500, 2000, 1500, false, false, CodDataPromo(), EstimatedTimeArrivalPromo(), "Bebas Ongkir (Rp 0)", "Bebas Ongkir", "Tersedia bbo", false,
            freeShippingMetadata = """{"sent_shipper_partner":true}""")

    val logisticPromoEko = LogisticPromoUiModel("boeko", "boeko", "boeko", firstCourierSecondDuration.productData.shipperName,
            secondDuration.serviceData.serviceId, firstCourierSecondDuration.productData.shipperId, firstCourierSecondDuration.productData.shipperProductId,
            "", "", "", false, "",
            500, 2000, 1500, false, false, CodDataPromo(), EstimatedTimeArrivalPromo(), "Bebas Ongkir (Rp 0)", "Bebas Ongkir", "Tersedia bbo", false,
            freeShippingMetadata = """{"sent_shipper_partner":false}""")

    val shippingRecommendationData = ShippingRecommendationData().apply {
        shippingDurationUiModels = listOf(firstDuration, secondDuration)
        logisticPromo = this@OrderSummaryPageViewModelTestHelper.logisticPromo
        listLogisticPromo = listOf(this@OrderSummaryPageViewModelTestHelper.logisticPromo, this@OrderSummaryPageViewModelTestHelper.logisticPromoEko)
    }

    val address = OrderProfileAddress(addressId = 1, latitude = "0", longitude = "0")

    val shipment = OrderProfileShipment(serviceId = 1)

    val payment = OrderProfilePayment(gatewayCode = "payment")

    val preference = OrderProfile(address = address, shipment = shipment, payment = payment)

    val orderShipment = OrderShipment(serviceId = firstDuration.serviceData.serviceId,
            serviceName = firstDuration.serviceData.serviceName,
            serviceDuration = firstDuration.serviceData.serviceName,
            shipperId = firstCourierFirstDuration.productData.shipperId,
            shipperProductId = firstCourierFirstDuration.productData.shipperProductId,
            shippingRecommendationData = shippingRecommendationData,
            logisticPromoViewModel = logisticPromo,
            logisticPromoTickerMessage = "Tersedia bbo")

    val product = OrderProduct(productId = Long.MAX_VALUE, orderQuantity = 1)

    val orderData = OrderData(cart = OrderCart(shop = OrderShop(shopId = Long.MAX_VALUE), products = mutableListOf(product)), preference = preference)

    val saveAddOnStateShopLevelResult = SaveAddOnStateResult(
            addOns = listOf(AddOnResult(
                    addOnKey = "123-0",
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_ORDER,
                    addOnData = listOf(AddOnData(
                            addOnPrice = 2000
                    ))
            ))
    )

    val saveAddOnStateShopLevelResultNegativeTest = SaveAddOnStateResult(
            addOns = listOf(AddOnResult(
                    addOnKey = "123-0",
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                    addOnData = listOf(AddOnData(
                            addOnPrice = 2000
                    ))
            ))
    )

    val saveAddOnStateProductLevelResult = SaveAddOnStateResult(
            addOns = listOf(AddOnResult(
                    addOnKey = "123-456",
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                    addOnData = listOf(AddOnData(
                            addOnPrice = 1000
                    ))
            ))
    )

    val saveAddOnStateProductLevelResultNegativeTest = SaveAddOnStateResult(
            addOns = listOf(AddOnResult(
                    addOnKey = "123-456",
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_ORDER,
                    addOnData = listOf(AddOnData(
                            addOnPrice = 1000
                    ))
            ))
    )

    val saveAddOnStateEmptyResult = SaveAddOnStateResult(
            addOns = emptyList()
    )

    val paymentFeeDetails = listOf(
        OrderPaymentFee(fee = 500.0),
        OrderPaymentFee(fee = 1000.0)
    )

    val imageUploadDataModel = ImageUploadDataModel(
        showImageUpload = true,
        text = "",
        leftIconUrl = "",
        checkoutId = "1234",
        frontEndValidation = true
    )

    val prescriptionIdsResponse = GetPrescriptionIdsResponse(
        detailData = GetPrescriptionIdsResponse.EPharmacyCheckoutData(
            prescriptionData = GetPrescriptionIdsResponse.EPharmacyCheckoutData.EPharmacyPrescriptionDetailData(
                checkoutId = "1234567890",
                prescriptions = listOf(
                    GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("123"),
                    GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("234"),
                    GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("345"),
                )
            )
        )
    )

    val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = imageUploadDataModel.showImageUpload,
            uploadImageText = imageUploadDataModel.text,
            leftIconUrl = imageUploadDataModel.leftIconUrl,
            checkoutId = imageUploadDataModel.checkoutId,
            prescriptionIds = PrescriptionMapper.mapPrescriptionResponse(prescriptionIdsResponse).prescriptionIds,
            uploadedImageCount = PrescriptionMapper.mapPrescriptionResponse(prescriptionIdsResponse).prescriptionIds.size,
            isError = false,
            frontEndValidation = imageUploadDataModel.frontEndValidation,
            isOcc = true,
    )
}
