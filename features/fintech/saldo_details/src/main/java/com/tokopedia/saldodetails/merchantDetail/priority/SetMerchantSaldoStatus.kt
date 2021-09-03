package com.tokopedia.saldodetails.merchantDetail.priority

import com.tokopedia.saldodetails.commom.di.module.GqlQueryModule
import com.tokopedia.saldodetails.commom.utils.GqlUseCaseWrapper
import com.tokopedia.saldodetails.merchantDetail.priority.data.GqlSetMerchantSaldoStatus
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
