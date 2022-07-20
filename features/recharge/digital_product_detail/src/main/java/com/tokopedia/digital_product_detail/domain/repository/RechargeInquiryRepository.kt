package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData

interface RechargeInquiryRepository {
    suspend fun inquiryProduct(productId: String, clientNumber: String, inputData: Map<String, String>): TopupBillsEnquiryData
}