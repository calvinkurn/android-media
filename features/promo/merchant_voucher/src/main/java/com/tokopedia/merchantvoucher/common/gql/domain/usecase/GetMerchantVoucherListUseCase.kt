package com.tokopedia.merchantvoucher.common.gql.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQuery
import com.tokopedia.merchantvoucher.common.gql.data.request.CartItemDataVoucher
import com.tokopedia.merchantvoucher.common.gql.domain.mapper.GraphQLMerchantListMapper
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
                val cartItemData = requestParams.getObject(CART_ITEM_DATA)
                variables[CART_ITEM_DATA] = cartItemData
                return variables
            }

            override fun createGraphQLCacheStrategy(): GraphqlCacheStrategy? {
                return GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setSessionIncluded(true)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`())
                        .build();
            }
        }
    }

    fun clearCache() {
        graphQLUseCase.clearCache()
    }

    override fun createObservable(requestParams: RequestParams): Observable<ArrayList<MerchantVoucherModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLMerchantListMapper())
                .doOnError { graphQLUseCase.clearCache() }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {

        val SHOP_ID = "shop_id"
        val NUM_VOUCHER = "num_voucher"
        val CART_ITEM_DATA = "cart_item_data"

        @JvmStatic
        fun createRequestParams(shopId: String, numVoucher: Int, cartItemDatumVouchers: List<CartItemDataVoucher>? = null): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(SHOP_ID, shopId.toInt())
            requestParams.putInt(NUM_VOUCHER, numVoucher)
            if (cartItemDatumVouchers != null) {
                requestParams.putObject(CART_ITEM_DATA, cartItemDatumVouchers)
            }
            return requestParams
        }
    }
}
