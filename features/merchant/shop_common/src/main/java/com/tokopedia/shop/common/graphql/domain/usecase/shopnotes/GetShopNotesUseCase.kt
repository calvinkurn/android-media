package com.tokopedia.shop.common.graphql.domain.usecase.shopnotes

import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.shop.common.graphql.data.shopnote.gql.ShopNoteQuery
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.util.ArrayList

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class GetShopNotesUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<ArrayList<ShopNoteModel>>() {
    private val graphQLUseCase: SingleGraphQLUseCase<ShopNoteQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopNoteQuery>(context, ShopNoteQuery::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_query_shop_notes
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ArrayList<ShopNoteModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())
    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }
}
