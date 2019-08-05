package com.tokopedia.logisticaddaddress.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.district_boundary.DistrictBoundaryResponse
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-06-10.
 */
class DistrictBoundaryUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {
    var queryString: String = ""

    companion object {
        val PARAM_DISTRICT_ID = "#districtId"
        val PARAM_KERO_TOKEN = "#keroToken"
        val PARAM_KERO_UT = "#keroUt"
    }

    fun setParams(districtId: Int, keroToken: String, keroUt: Int) {
        queryString = GraphqlHelper.loadRawString(context.resources, R.raw.district_boundary)
        queryString = queryString.replace(PARAM_DISTRICT_ID, districtId.toString(), false)
        queryString = queryString.replace(PARAM_KERO_TOKEN, keroToken, false)
        queryString = queryString.replace(PARAM_KERO_UT, keroUt.toString(), false)
    }

    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {
        val graphqlRequest = GraphqlRequest(queryString, DistrictBoundaryResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        return super.createObservable(params)
    }
}