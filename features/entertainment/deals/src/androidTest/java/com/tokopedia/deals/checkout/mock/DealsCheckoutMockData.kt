package com.tokopedia.deals.checkout.mock

import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.common_entertainment.data.MetaDataResponse
import com.tokopedia.common_entertainment.data.PassengerForm
import com.tokopedia.common_entertainment.data.PassengerInformation
import com.tokopedia.deals.pdp.data.Brand
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.pdp.data.ProductDetailData

object DealsCheckoutMockData {
    fun createPDPData(): ProductDetailData {
        return ProductDetailData(
            id = "297498",
            displayName = "Voucher Haagen Dazs Rp 100.000 (Penawaran Spesial)",
            imageApp = "https://images.tokopedia.net/img/banner/2020/3/19/51998951/51998951_8b4dd7a9-d39e-403b-8edf-1a7dc53f9e22.jpg",
            brand = Brand(
                title = "Haagen-Dazs",
                featuredImage = "https://images.tokopedia.net/img/cache/300-square/qfBCeS/2020/10/19/dc67c938-6fbe-4eb8-ae39-a2b6c2d05e16.jpg",
                seoUrl = "haagen-dazs-1522"
            ),
            saleEndDate = "1672801200",
            outlets = listOf(
                Outlet(
                    id = "28752",
                    name = "Plaza Indonesia",
                    district = "Jl. Asia Afrika No.8, Gelora, Tanah Abang",
                    coordinates = "-6.2260231,106.7990043"
                )
            ),
            mrp = "100000",
            salesPrice = "60000",
            categoryId = "15",
        )
    }

    fun createVerifyData(): EventVerifyResponse {
        return EventVerifyResponse(
            error = "",
            errorDescription = "",
            status = "0",
            metadata = MetaDataResponse(
                productIds = listOf(
                    "28603"
                ),
                productNames = listOf(
                    "Voucher Haagen Dazs Rp 100.000 (Penawaran Spesial)"
                ),
                providerIds = listOf(
                    "11"
                ),
                itemIds = listOf(
                    "28603"
                ),
                categoryName = "deal",
                quantity = 1,
                totalPrice = 60000,
                error = "",
                itemMap = listOf(
                    ItemMapResponse(
                        id = "28603",
                        name = "Voucher Haagen Dazs Rp 100.000 (Penawaran Spesial)",
                        productId = "28603",
                        categoryId = "15",
                        childCategoryIds = "25",
                        providerId = "11",
                        productName = "Haagen-Dazs",
                        packageName = "Haagen-Dazs",
                        endTime = "04 Jan 2023 10:00",
                        startTime = "26 Jan 2020 10:00",
                        price = 60000,
                        quantity = 1,
                        totalPrice = 60000,
                        locationDesc = "",
                        locationName = "",
                        productAppUrl = "tokopedia://deals/voucher-haagen-dazs-rp-100000-penawaran-spesial-28603",
                        webAppUrl = "https://www.tokopedia.com/deals/i/voucher-haagen-dazs-rp-100000-penawaran-spesial-28603/",
                        productWebUrl = "https://www.tokopedia.com/deals/i/voucher-haagen-dazs-rp-100000-penawaran-spesial-28603/",
                        productImage = "https://images.tokopedia.net/img/cache/300-square/qfBCeS/2020/10/19/dc67c938-6fbe-4eb8-ae39-a2b6c2d05e16.jpg",
                        flagId = "1025",
                        packageId = "0",
                        orderTraceId = "b705bd82-9e7c-414d-5dfe-98aed52a0db6",
                        error = "",
                        email = "android.automation.seller.h5+frontendtest@tokopedia.com",
                        mobile = "081908878591",
                        scheduleTimestamp = "1672801200",
                        description = "",
                        providerTicketId = "",
                        providerScheduleId = "",
                        basePrice = "0",
                        commission = 0,
                        commissionType = "",
                        currencyPrice = 0,
                        passengerForms = listOf(
                            PassengerForm(
                                listOf(
                                    PassengerInformation(
                                        name = "Firman",
                                        value = "1",
                                        title = "yoi"
                                    )
                                )
                            )
                        ),
                        invoiceItemId = "0",
                        providerOrderId = "",
                        providerInvoiceCode = "",
                        providerPackageId = "",
                        invoiceId = "12588370",
                        invoiceStatus = "BOOKED",
                        paymentType = "-"
                    )
                ),
                orderTitle = "Haagen-Dazs",
                orderSubTitle = "Voucher Haagen Dazs Rp 100.000 (Penawaran Spesial)",
            ),
            gatewayCode = ""
        )
    }
}
