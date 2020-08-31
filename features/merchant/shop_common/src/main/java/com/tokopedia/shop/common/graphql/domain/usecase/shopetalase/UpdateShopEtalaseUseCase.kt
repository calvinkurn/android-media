package com.tokopedia.shop.common.graphql.domain.usecase.shopetalase

import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.UpdateShopEtalaseMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class UpdateShopEtalaseUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<UpdateShopEtalaseMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<UpdateShopEtalaseMutation>(context, UpdateShopEtalaseMutation::class.java) {

            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_update_shop_etalase)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                variables[ID] = requestParams.getString(ID, "")
                variables[NAME] = requestParams.getString(NAME, "")
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
        val ID = "id"
        val NAME = "name"

        @JvmStatic
        fun createRequestParams(etalaseId: String, etalaseName: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(ID, etalaseId)
            requestParams.putString(NAME, etalaseName)
            return requestParams
        }
    }
}
