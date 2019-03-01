package com.tokopedia.affiliate.feature.createpost.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.data.pojo.CheckQuotaQuery
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.ContentFormData
import com.tokopedia.affiliate.feature.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 9/26/18.
 */
class GetContentFormUseCase @Inject internal constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase): UseCase<GetContentFormDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<GetContentFormDomain> {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_af_content_form
        )
        val request = GraphqlRequest(query, ContentFormData::class.java, requestParams.parameters)

        val queryQouta = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_af_quota
        )
        val requestQouta = GraphqlRequest(queryQouta, CheckQuotaQuery::class.java)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(requestQouta)
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            GetContentFormDomain(
                    it.getData(ContentFormData::class.java),
                    it.getData(CheckQuotaQuery::class.java)
            )
        }
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_AD_ID = "adID"
        private const val TYPE_AFFILIATE = "affiliate"

        fun createRequestParams(productId: String, adId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_TYPE, TYPE_AFFILIATE)
            requestParams.putString(PARAM_PRODUCT_ID, productId)
            requestParams.putString(PARAM_AD_ID, adId)
            return requestParams
        }
    }

}
