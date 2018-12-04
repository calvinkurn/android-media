package com.tokopedia.shop.common.graphql.domain.usecase.shoplocation

import android.content.Context
import android.text.TextUtils

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.GraphQLDataError
import com.tokopedia.shop.common.graphql.data.GraphQLResult
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.shop.common.graphql.data.shoplocation.gql.ShopLocationQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import rx.functions.Func1

class GetShopLocationUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<List<ShopLocationModel>>() {
    private val graphQLUseCase: SingleGraphQLUseCase<ShopLocationQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopLocationQuery>(context, ShopLocationQuery::class.java) {
            override val graphQLRawResId: Int
                get() = R.raw.gql_query_shop_location
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<ShopLocationModel>> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())

    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }
}
