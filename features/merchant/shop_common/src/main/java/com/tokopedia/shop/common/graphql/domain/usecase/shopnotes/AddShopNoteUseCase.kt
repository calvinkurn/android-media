package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes

import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopnote.gql.AddShopNoteMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class AddShopNoteUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<AddShopNoteMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<AddShopNoteMutation>(context, AddShopNoteMutation::class.java) {
            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_add_shop_note)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                variables[TITLE] = requestParams.getString(TITLE, "")
                variables[CONTENT] = requestParams.getString(CONTENT, "")
                variables[IS_TERMS] = requestParams.getBoolean(IS_TERMS, false)
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
        val TITLE = "title"
        val CONTENT = "content"
        val IS_TERMS = "isTerms"

        @JvmStatic
        fun createRequestParams(title: String, content: String,
                                isTerms: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(TITLE, title)
            requestParams.putString(CONTENT, content)
            requestParams.putBoolean(IS_TERMS, isTerms)
            return requestParams
        }
    }
}
