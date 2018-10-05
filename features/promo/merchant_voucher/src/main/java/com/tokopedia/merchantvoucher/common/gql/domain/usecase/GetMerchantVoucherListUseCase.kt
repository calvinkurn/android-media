package com.tokopedia.merchantvoucher.common.gql.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.GraphQLMerchantListMapper
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
                val shopId = requestParams.getInt(SHOP_ID, 0)
                variables[SHOP_ID] = shopId
                val numVoucher = requestParams.getInt(NUM_VOUCHER, 0)
                variables[NUM_VOUCHER] = numVoucher
                return variables
            }

            override fun createGraphQLCacheStrategy(): GraphqlCacheStrategy? {
                //TODO use cache?
                return null
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ArrayList<MerchantVoucherModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLMerchantListMapper())
                .doOnError { graphQLUseCase.clearCache() }
                //TODO remove below, just for test.
//                .onErrorResumeNext {
//                    val jsonString = """{"getPublicMerchantVoucherList":{"vouchers":[{"voucher_id":2,"voucher_name":"World","voucher_code":"WORLD1","type":{"type":2,"identifier":"free-shipping"},"amount":{"type":1,"amount":17.5},"minimum_spend":500000,"owner":{"owner_id":15635,"identifier":"seller"},"valid_thru":1538971024,"tnc":"Term and Condition","banner":{"desktop_url":"https://www.tokopedia.com/","mobile_url":"https://m.tokopedia.com/"},"status":{"status":2,"identifier":"in-use"},"in_use_expiry":1538627824},{"voucher_id":1,"voucher_name":"Hello","voucher_code":"HELLO1","type":{"type":1,"identifier":"discount"},"amount":{"type":1,"amount":13.5},"minimum_spend":500000,"owner":{"owner_id":15635,"identifier":"seller"},"valid_thru":1538798224,"tnc":"Term and Condition","banner":{"desktop_url":"https://www.tokopedia.com/","mobile_url":"https://m.tokopedia.com/"},"status":{"status":1,"identifier":"available"},"in_use_expiry":0}],"error_code":0,"error_message_title":"","error_message":""}}"""
//                    val response = Gson().fromJson(jsonString, MerchantVoucherQuery::class.java)
//                    Observable.just(response).flatMap(GraphQLMerchantListMapper())
//                }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {

        val SHOP_ID = "shop_id"
        val NUM_VOUCHER = "num_voucher"

        @JvmStatic
        fun createRequestParams(shopId:String, numVoucher: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(SHOP_ID, shopId.toInt())
            requestParams.putInt(NUM_VOUCHER, numVoucher)
            return requestParams
        }
    }
}
