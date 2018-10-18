package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.ShopEtalaseQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class GetShopEtalaseUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<ArrayList<ShopEtalaseModel>>() {
    private val graphQLUseCase: SingleGraphQLUseCase<ShopEtalaseQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopEtalaseQuery>(context, ShopEtalaseQuery::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_query_shop_etalase

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val withDefault = requestParams.getBoolean(WITH_DEFAULT, true)
                variables[WITH_DEFAULT] = withDefault
                return variables
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ArrayList<ShopEtalaseModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {

        val WITH_DEFAULT = "withDefault"

        @JvmStatic
        fun createRequestParams(withDefault: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putBoolean(WITH_DEFAULT, withDefault)
            return requestParams
        }
    }
}
