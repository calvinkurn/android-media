package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataQuery
import com.tokopedia.shop.common.graphql.domain.mapper.GraphQLResultMapper
import com.tokopedia.shop.common.graphql.domain.usecase.base.SingleGraphQLUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject


class GetShopBasicDataUseCase @Inject
constructor(@ApplicationContext context: Context) : UseCase<ShopBasicDataModel>() {
    private val graphQLUseCase: SingleGraphQLUseCase<ShopBasicDataQuery>

    init {
        graphQLUseCase = object : SingleGraphQLUseCase<ShopBasicDataQuery>(context, ShopBasicDataQuery::class.java) {

            override fun getRawString(): String {
                return GraphqlHelper.loadRawString(context.resources, R.raw.gql_query_shop_basic_data)
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ShopBasicDataModel> {
        return graphQLUseCase.createObservable(requestParams)
                .flatMap(GraphQLResultMapper())

    }

    override fun unsubscribe() {
        super.unsubscribe()
        graphQLUseCase.unsubscribe()
    }
}
