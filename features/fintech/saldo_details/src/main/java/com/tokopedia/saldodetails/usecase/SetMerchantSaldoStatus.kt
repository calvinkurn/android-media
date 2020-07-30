package com.tokopedia.saldodetails.usecase

import com.tokopedia.saldodetails.data.GqlUseCaseWrapper
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.response.model.GqlSetMerchantSaldoStatus
import javax.inject.Inject
import javax.inject.Named

class SetMerchantSaldoStatus @Inject constructor(
        @Named(GqlQueryModule.UPDATE_MERCHANT_SALDO_STATUS)
        val updateSaldoStatusQuery: String,
        val gqlUseCaseWrapper: GqlUseCaseWrapper
) {

    suspend fun updateStatus(isEnabled: Boolean): GqlSetMerchantSaldoStatus {
        val variables = HashMap<String, Any>()
        variables["enable"] = isEnabled
        return gqlUseCaseWrapper.getResponse(GqlSetMerchantSaldoStatus::class.java, updateSaldoStatusQuery, variables)
    }

}
