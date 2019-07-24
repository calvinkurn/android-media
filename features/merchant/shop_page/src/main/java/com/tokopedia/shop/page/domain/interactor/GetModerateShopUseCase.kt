package com.tokopedia.shop.page.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetModerateShopUseCase @Inject constructor(@Named(ShopPageConstant.MODERATE_STATUS_QUERY) private val moderateQuery:String,
                                                 private val graphqlUseCase: GraphqlUseCase): UseCase<ShopModerateRequestData>() {


    override fun createObservable(requestParams: RequestParams?): Observable<ShopModerateRequestData> {

        val graphqlRequest = GraphqlRequest(moderateQuery, ShopModerateRequestData::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data: ShopModerateRequestData? = it.getData(ShopModerateRequestData::class.java)
            val error: MutableList<GraphqlError> = it.getError(GraphqlError::class.java) ?: mutableListOf()

            if (data == null) {
                throw RuntimeException()
            } else if (data.shopModerateRequestStatus.error.message.isNotEmpty()) {
                throw MessageErrorException(data.shopModerateRequestStatus.error.message)
            } else if (error.isNotEmpty() && error[0].message.isNotEmpty()) {
                throw MessageErrorException(error[0].message)
            }
            data
        }

    }


}