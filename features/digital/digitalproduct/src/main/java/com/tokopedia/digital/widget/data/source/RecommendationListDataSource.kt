package com.tokopedia.digital.widget.data.source

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.digital.R
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService
import com.tokopedia.digital.widget.view.model.Recommendation
import rx.Observable

/**
 * Created by Rizky on 22/11/18.
 */
class RecommendationListDataSource constructor(private val digitalGqlApiService: DigitalGqlApiService,
                                               private var context: Context?) {

    fun getRecommendationList(deviceId: Int): Observable<List<Recommendation>> {
        return digitalGqlApiService.api.getRecommendationList(getCategoryAndFavRequestPayload(deviceId))
                .map { listResponse ->
                    return@map listResponse.body()[0]
                            .data.rechargeFavoriteRecommentaionList.recommendationItemEntityList
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

    private fun getCategoryAndFavRequestPayload(deviceId: Int): String {
        val query = GraphqlHelper.loadRawString(context?.resources, R.raw.digital_recommendation_list)

        return String.format(query, deviceId)
    }

}