package com.tokopedia.attachvoucher.stub.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachvoucher.common.GraphqlRepositoryStub
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.mapper.VoucherMapper
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import javax.inject.Inject

class GetVoucherUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers,
    mapper: VoucherMapper
): GetVoucherUseCase(repository, dispatcher, mapper) {

    var response: GetMerchantPromotionGetMVListResponse = GetMerchantPromotionGetMVListResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var errorMessage = ""
        set(value) {
            if (value.isNotEmpty()) {
                repository.createErrorMapResult(response::class.java, value)
            }
            field = value
        }
}