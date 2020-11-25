package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes

import android.content.Context
import android.text.TextUtils

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopnote.gql.AddShopNoteMutation
import com.tokopedia.shop.common.graphql.data.shopnote.gql.UpdateShopNoteMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class UpdateShopNoteUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<AddShopNoteMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<AddShopNoteMutation>(context, AddShopNoteMutation::class.java) {

            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_mutation_update_shop_note)
            }

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                variables[ID] = requestParams.getString(ID, "")
                variables[TITLE] = requestParams.getString(TITLE, "")
                variables[CONTENT] = requestParams.getString(CONTENT, "")
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
        val TITLE = "title"
        val CONTENT = "content"

        @JvmStatic
        fun createRequestParams(id: String,
                                title: String?,
                                content: String?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(ID, id)
            requestParams.putString(TITLE, title)
            requestParams.putString(CONTENT, content)
            return requestParams
        }
    }
}
