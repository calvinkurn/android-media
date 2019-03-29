package com.tokopedia.merchantvoucher.common.gql.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.GraphQLUseMerchantVoucherMapper
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class UseMerchantVoucherUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<UseMerchantVoucherQueryResult>() {
    private val graphQLUseCase: SingleGraphQLUseCase<UseMerchantVoucherQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<UseMerchantVoucherQuery>(context, UseMerchantVoucherQuery::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_mutation_use_merchant_voucher

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val voucherId = requestParams.getInt(VOUCHER_ID, 0)
                if (voucherId > 0) {
                    variables[VOUCHER_ID] = voucherId
                }
                val voucherCode = requestParams.getString(VOUCHER_CODE, "")
                variables[VOUCHER_CODE] = voucherCode

                return variables
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<UseMerchantVoucherQueryResult> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLUseMerchantVoucherMapper())
                .doOnError { graphQLUseCase.clearCache() }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {

        val VOUCHER_ID = "voucher_id"
        val VOUCHER_CODE = "voucher_code"

        @JvmStatic
        fun createRequestParams(voucherCode: String, voucherId: Int = 0): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(VOUCHER_ID, voucherId)
            requestParams.putString(VOUCHER_CODE, voucherCode)
            return requestParams
        }
    }
}
