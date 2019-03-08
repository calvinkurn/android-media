package com.tokopedia.digital.widget.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.digital.R
import com.tokopedia.digital.widget.data.entity.RecommendationEntity
import com.tokopedia.digital.widget.view.model.Recommendation
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

/**
 * Created by Rizky on 14/11/18.
 */
class DigitalRecommendationUseCase(private val graphqlUseCase: GraphqlUseCase, @param:ApplicationContext private val context: Context) : UseCase<List<Recommendation>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<Recommendation>> {
        val variables = HashMap<String, Any>()
        variables["device_id"] = requestParams.getInt(PARAM_REQUEST_ID, 0)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.digital_recommendation_list), RecommendationEntity::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams)
                .map { graphqlResponse ->
                    val entity = graphqlResponse.getData<RecommendationEntity>(RecommendationEntity::class.java)
                    entity.rechargeFavoriteRecommentaionList.recommendationItemEntityList
                            .map {
                                with(it) {
                                    Recommendation(
                                            iconUrl,
                                            title,
                                            clientNumber,
                                            applink,
                                            webLink,
                                            categoryId,
                                            categoryName,
                                            productId,
                                            productName,
                                            type,
                                            position
                                    )
                                }
                            }
                }
    }

    fun createRequestParams(deviceId: Int?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(PARAM_REQUEST_ID, deviceId!!)
        return requestParams
    }

    companion object {

        private val PARAM_REQUEST_ID = "PARAM_REQUEST_ID"
    }
}

