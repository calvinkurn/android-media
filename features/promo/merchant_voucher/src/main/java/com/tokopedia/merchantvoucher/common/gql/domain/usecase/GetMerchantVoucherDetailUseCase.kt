
package com.tokopedia.merchantvoucher.common.gql.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherDetailQuery
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.GraphQLResultMapper
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class GetMerchantVoucherDetailUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<MerchantVoucherModel>() {
    private val graphQLUseCase: SingleGraphQLUseCase<MerchantVoucherDetailQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<MerchantVoucherDetailQuery>(context, MerchantVoucherDetailQuery::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_query_merchant_voucher_detail

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
//                val withDefault = requestParams.getBoolean(WITH_DEFAULT, true)
//                variables[WITH_DEFAULT] = withDefault
                return variables
            }
        }
        setForceNetwork(false)
        graphQLUseCase.useCacheAfterNetworkSuccess = true
    }

    fun setForceNetwork(forceNetwork: Boolean) {
        graphQLUseCase.forceNetwork = forceNetwork
    }

    override fun createObservable(requestParams: RequestParams): Observable<MerchantVoucherModel> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())
                //TODO remove below, just for test.
                .onErrorResumeNext {
                    val jsonString = """{"shopShowcases":{"result":{"id":"etalase","name":"Semua Etalase","count":0,"type":-1,"highlighted":false,"alias":"etalase","useAce":true},"error":{"message":""}}}"""
                    val response = Gson().fromJson(jsonString, MerchantVoucherDetailQuery::class.java)
                    Observable.just(response).flatMap(GraphQLResultMapper())
                }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {

//        val WITH_DEFAULT = "withDefault"

        @JvmStatic
        fun createRequestParams(): RequestParams {
            val requestParams = RequestParams.create()
//            requestParams.putBoolean(WITH_DEFAULT, withDefault)
            return requestParams
        }
    }
}
