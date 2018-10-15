package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.ShopEtalaseByShopQuery
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

class GetShopEtalaseByShopUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<ArrayList<ShopEtalaseModel>>() {
    private val graphQLUseCase: SingleGraphQLUseCase<ShopEtalaseByShopQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopEtalaseByShopQuery>(context, ShopEtalaseByShopQuery::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_query_shop_etalase_by_shop

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                variables[SHOP_ID] = requestParams.getString(SHOP_ID, "")
                variables[HIDE_NO_COUNT] = requestParams.getBoolean(HIDE_NO_COUNT, true)
                variables[HIDE_SHOWCASE_GROUP] = requestParams.getBoolean(HIDE_SHOWCASE_GROUP, true)
                variables[IS_OWNER] = requestParams.getBoolean(IS_OWNER, false)
                return variables
            }

            override fun createGraphQLCacheStrategy(): GraphqlCacheStrategy? {
                return GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setSessionIncluded(true)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                        .build();
            }
        }
    }

    fun clearCache() {
        graphQLUseCase.clearCache()
    }

    override fun createObservable(requestParams: RequestParams): Observable<ArrayList<ShopEtalaseModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())
                .doOnError { graphQLUseCase.clearCache() }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {

        val SHOP_ID = "shopId"
        val HIDE_NO_COUNT = "hideNoCount"
        val HIDE_SHOWCASE_GROUP = "hideShowcaseGroup"
        val IS_OWNER = "isOwner"

        @JvmStatic
        fun createRequestParams(shopId: String, hideNoCount: Boolean, hideShowCaseGroup: Boolean, isOwner: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_ID, shopId)
            requestParams.putBoolean(HIDE_NO_COUNT, hideNoCount)
            requestParams.putBoolean(HIDE_SHOWCASE_GROUP, hideShowCaseGroup)
            requestParams.putBoolean(IS_OWNER, isOwner)
            return requestParams
        }
    }
}
