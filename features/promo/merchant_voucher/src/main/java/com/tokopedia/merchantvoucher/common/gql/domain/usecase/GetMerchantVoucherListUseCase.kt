package com.tokopedia.merchantvoucher.common.gql.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.GraphQLResultMapper
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class GetMerchantVoucherListUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<ArrayList<MerchantVoucherModel>>() {
    private val graphQLUseCase: SingleGraphQLUseCase<MerchantVoucherQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<MerchantVoucherQuery>(context, MerchantVoucherQuery::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_query_merchant_voucher

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
//                val withDefault = requestParams.getBoolean(WITH_DEFAULT, true)
//                variables[WITH_DEFAULT] = withDefault
                return variables
            }
        }
        setForceNetwork(false)
    }

    fun setForceNetwork(forceNetwork: Boolean) {
        graphQLUseCase.forceNetwork = forceNetwork
    }

    override fun createObservable(requestParams: RequestParams): Observable<ArrayList<MerchantVoucherModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())
                //TODO remove below, just for test.
                .onErrorResumeNext {
                    val jsonString = """{"shopShowcases":{"result":[{"id":"etalase","name":"Semua Etalase","count":0,"type":-1,"highlighted":false,"alias":"etalase","useAce":true},{"id":"sold","name":"Produk Terjual","count":0,"type":-1,"highlighted":true,"alias":"sold","useAce":true},{"id":"discount","name":"Discount","count":1,"type":1,"highlighted":false,"alias":"Powerbank","useAce":true},{"id":"7598623","name":"Powerbank","count":1,"type":1,"highlighted":true,"alias":"Powerbank","useAce":true},{"id":"7583097","name":"Kabel Data","count":1,"type":1,"highlighted":false,"alias":"Kabel Data","useAce":true},{"id":"7598627","name":"Charger & Car Charger","count":1,"type":1,"highlighted":false,"alias":"Charger & Car Charger","useAce":true},{"id":"7583082","name":"Tas","count":1,"type":1,"highlighted":false,"alias":"tas","useAce":true},{"id":"7598633","name":"Audio","count":1,"type":1,"highlighted":false,"alias":"audio","useAce":true},{"id":"7600154","name":"Screen Protector","count":1,"type":1,"highlighted":false,"alias":"Screen Protector","useAce":true},{"id":"11131981","name":"Konektor","count":1,"type":1,"highlighted":false,"alias":"Konektor","useAce":true}],"error":{"message":""}}}"""
                    val response = Gson().fromJson(jsonString, MerchantVoucherQuery::class.java)
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
