package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.ReorderShopEtalaseMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.ArrayList
import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class ReorderShopEtalaseUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<ReorderShopEtalaseMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ReorderShopEtalaseMutation>(context, ReorderShopEtalaseMutation::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_mutation_reorder_shop_etalase

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val ids = requestParams.getObject(IDS) as ArrayList<*>
                variables[IDS] = ids
                return variables
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLSuccessMapper())
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }

    companion object {
        val IDS = "ids"

        @JvmStatic
        fun createRequestParams(etalaseIdList: ArrayList<String>): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(IDS, etalaseIdList)
            return requestParams
        }
    }
}
