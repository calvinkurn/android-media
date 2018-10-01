package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes

import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopnote.gql.DeleteShopNoteMutation
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLSuccessMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.HashMap

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class DeleteShopNoteUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<String>() {

    private val graphQLUseCase: SingleGraphQLUseCase<DeleteShopNoteMutation>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<DeleteShopNoteMutation>(context, DeleteShopNoteMutation::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_mutation_delete_shop_note

            override fun createGraphQLVariable(requestParams: RequestParams): HashMap<String, Any> {
                val variables = HashMap<String, Any>()
                val name = requestParams.getString(ID, "")
                variables[ID] = name
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

        @JvmStatic
        fun createRequestParams(noteId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(ID, noteId)
            return requestParams
        }
    }
}
