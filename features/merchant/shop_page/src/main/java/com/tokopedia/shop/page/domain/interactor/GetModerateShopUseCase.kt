package com.tokopedia.shop.page.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetModerateShopUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                                 private val graphqlUseCase: GraphqlUseCase): UseCase<ShopModerateRequestData>() {


    override fun createObservable(requestParams: RequestParams?): Observable<ShopModerateRequestData> {
        val query: String = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.shop_moderate_request_status
        )

        val graphqlRequest = GraphqlRequest(query, ShopModerateRequestData::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data: ShopModerateRequestData? = it.getData(ShopModerateRequestData::class.java)
            val error: MutableList<GraphqlError>? = it.getError(GraphqlError::class.java)

            if (data == null) {
                throw RuntimeException()
            } else if (!data.shopModerateRequestStatus.error.message.isEmpty()) {
                throw MessageErrorException(data.shopModerateRequestStatus.error.message)
            } else if (error != null && !error[0].message.isEmpty()) {
                throw MessageErrorException(error[0].message)
            }
            data
        }

    }


}