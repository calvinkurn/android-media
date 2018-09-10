package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
        }
    }

    fun setForceNetwork(forceNetwork: Boolean) {
        graphQLUseCase.forceNetwork = forceNetwork
    }

    override fun createObservable(requestParams: RequestParams): Observable<ArrayList<ShopEtalaseModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())
                //TODO remove below, just for test.
                .onErrorResumeNext {
                    val jsonString = """{"shopShowcasesByID":{"result":[{"id":"etalase","name":"Semua Etalase","count":0,"type":-1,"highlighted":false,"alias":"etalase","useAce":true},{"id":"sold","name":"Produk Terjual","count":0,"type":-1,"highlighted":true,"alias":"sold","useAce":true},{"id":"discount","name":"Discount","count":1,"type":1,"highlighted":false,"alias":"Powerbank","useAce":true},{"id":"7598623","name":"Powerbank","count":1,"type":1,"highlighted":true,"alias":"Powerbank","useAce":true},{"id":"7583097","name":"Kabel Data","count":1,"type":1,"highlighted":false,"alias":"Kabel Data","useAce":true},{"id":"7598627","name":"Charger & Car Charger","count":1,"type":1,"highlighted":false,"alias":"Charger & Car Charger","useAce":true},{"id":"7583082","name":"Tas","count":1,"type":1,"highlighted":false,"alias":"tas","useAce":true},{"id":"7598633","name":"Audio","count":1,"type":1,"highlighted":false,"alias":"audio","useAce":true},{"id":"7600154","name":"Screen Protector","count":1,"type":1,"highlighted":false,"alias":"Screen Protector","useAce":true},{"id":"11131981","name":"Konektor","count":1,"type":1,"highlighted":false,"alias":"Konektor","useAce":true}],"error":{"message":""}}}"""
                    val response = Gson().fromJson(jsonString, ShopEtalaseByShopQuery::class.java)
                    Observable.just(response).flatMap(GraphQLResultMapper())
                }
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
