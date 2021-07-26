package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
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
    var isFromCacheFirst: Boolean = true

    fun getQueryString():String{
        return """
        query shopShowcasesByShopID(${'$'}shopId:String!,${'$'}hideNoCount:Boolean,${'$'}hideShowcaseGroup:Boolean,${'$'}isOwner:Boolean) {
          shopShowcasesByShopID(shopId:${'$'}shopId, hideNoCount:${'$'}hideNoCount, hideShowcaseGroup:${'$'}hideShowcaseGroup, isOwner:${'$'}isOwner) {
            result {
              id
              name
              count
              type
              highlighted
              alias
              uri
              useAce
              badge
              aceDefaultSort
              rules {
                name
              }
              imageURL
            }
            error {
              message
            }
          }
        }
        """.trimIndent()
    }
    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopEtalaseByShopQuery>(context, ShopEtalaseByShopQuery::class.java) {

            override fun getRawString(): String {
                return getQueryString()
            }

            var cacheType: CacheType = CacheType.CACHE_FIRST

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                cacheType =  if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD

                val variables = HashMap<String, Any>()
                variables[SHOP_ID] = requestParams.getString(SHOP_ID, "")
                variables[HIDE_NO_COUNT] = requestParams.getBoolean(HIDE_NO_COUNT, true)
                variables[HIDE_SHOWCASE_GROUP] = requestParams.getBoolean(HIDE_SHOWCASE_GROUP, true)
                variables[IS_OWNER] = requestParams.getBoolean(IS_OWNER, false)
                return variables
            }

            override fun createGraphQLCacheStrategy(): GraphqlCacheStrategy? {
                return GraphqlCacheStrategy.Builder(cacheType)
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

        object BuyerQueryParam {
            const val HIDE_NO_COUNT_VALUE = true
            const val HIDE_SHOWCASE_GROUP_VALUE = false
            const val IS_OWNER_VALUE = false
        }

        object SellerQueryParam {
            const val HIDE_NO_COUNT_VALUE = false
            const val HIDE_SHOWCASE_GROUP_VALUE = false     // Can be true
            const val IS_OWNER_VALUE = true
        }

        @JvmStatic
        fun createRequestParams(
                shopId: String,
                hideNoCount: Boolean,
                hideShowCaseGroup: Boolean,
                isOwner: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_ID, shopId)
            requestParams.putBoolean(HIDE_NO_COUNT, hideNoCount)
            requestParams.putBoolean(HIDE_SHOWCASE_GROUP, hideShowCaseGroup)
            requestParams.putBoolean(IS_OWNER, isOwner)
            return requestParams
        }
    }
}
