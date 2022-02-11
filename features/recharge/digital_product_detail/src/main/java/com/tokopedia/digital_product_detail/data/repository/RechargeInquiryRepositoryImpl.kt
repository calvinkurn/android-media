package com.tokopedia.digital_product_detail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.digital_product_detail.domain.repository.RechargeInquiryRepository
import com.tokopedia.digital_product_detail.domain.usecase.GetRechargeInquiryUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeInquiryRepositoryImpl @Inject constructor(
    private val getRechargeInquiryUseCase: GetRechargeInquiryUseCase,
    private val dispatchers: CoroutineDispatchers
): RechargeInquiryRepository {

    override suspend fun inquiryProduct(
        productId: String,
        clientNumber: String,
        inputData: Map<String, String>
    ): TopupBillsEnquiryData = withContext(dispatchers.io) {
        return@withContext getRechargeInquiryUseCase.apply {
            createInquiryParams(productId, clientNumber, inputData)
        }.executeOnBackground()
    }
}