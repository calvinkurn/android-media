package com.tokopedia.recommendation_widget_common.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.recommendation_widget_common.byteio.RecommendationByteIoUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.data.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

/**
 * Created by Lukas on 05/09/19
 */

@Deprecated(message = "please use GetSingleRecommendationUseCase from coroutine folder instead")
open class GetSingleRecommendationUseCase @Inject
constructor(
    private val context: Context,
    private val recomRawString: String,
    private val graphqlUseCase: GraphqlUseCase,
    private val userSession: UserSessionInterface
) : UseCase<RecommendationEntity.RecommendationData>() {

    private val byteIoUseCase = RecommendationByteIoUseCase()

    override fun createObservable(requestParams: RequestParams): Observable<RecommendationEntity.RecommendationData> {
        val graphqlRequest = GraphqlRequest(recomRawString, SingleProductRecommendationEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
            .map {
                val entity = it.getData<SingleProductRecommendationEntity>(SingleProductRecommendationEntity::class.java)
                entity.productRecommendationWidget.data.also { data ->
                    // if the request does not have pageName, use productId as identifier
                    // example use case: similar recommendation landing page
                    val requestIdentifier = requestParams.parameters[PRODUCT_IDS].toString().ifEmpty {
                        requestParams.parameters[PRODUCT_IDS].toString()
                    }
                    byteIoUseCase.updateMap(
                        requestIdentifier,
                        sessionId = data.appLog.sessionId,
                        totalData = data.recommendation.size
                    )
                }
            }
    }

    fun getRecomParams(
        pageNumber: Int,
        productIds: List<String>,
        queryParam: String = "",
        hasNewProductCardEnabled: Boolean = false,
    ): RequestParams {
        val params = RequestParams.create()
        val productIdsString = TextUtils.join(",", productIds)
        val reimagineCardParam = getProductCardReimagineVersion(hasNewProductCardEnabled)
        val newQueryParam = ChooseAddressUtils
            .getLocalizingAddressData(context)
            .toQueryParam(queryParam)

        val byteIoParam = byteIoUseCase.getParameter(GetRecommendationRequestParam(pageNumber = pageNumber, pageName = productIds.joinToString()))

        if (userSession.isLoggedIn) {
            params.putInt(USER_ID, userSession.userId.toIntOrZero())
        } else {
            params.putInt(USER_ID, 0)
        }

        params.putInt(PAGE_NUMBER, pageNumber)
        params.putString(PRODUCT_IDS, productIdsString)
        params.putString(QUERY_PARAM, newQueryParam)
        params.putString(X_DEVICE, DEFAULT_VALUE_X_DEVICE)
        params.putInt(PARAM_CARD_REIMAGINE, reimagineCardParam)
        params.putString(REFRESH_TYPE, byteIoParam.refreshType.value.toString())
        params.putString(CURRENT_SESSION_ID, byteIoParam.bytedanceSessionId)
        params.putString(ENTER_FROM, AppLogAnalytics.getLastData(AppLogParam.ENTER_FROM)?.toString().orEmpty())
        params.putString(SOURCE_PAGE_TYPE, AppLogAnalytics.getLastData(AppLogParam.SOURCE_PAGE_TYPE)?.toString().orEmpty())
        return params
    }

    private fun getProductCardReimagineVersion(hasNewProductCardEnabled: Boolean): Int {
        return if (ProductCardExperiment.isReimagine() && hasNewProductCardEnabled) {
            CARD_REIMAGINE_VERSION
        } else {
            CARD_REVERT_VERSION
        }
    }

    companion object {
        const val USER_ID = "userID"
        const val PAGE_NUMBER = "pageNumber"
        const val X_DEVICE = "xDevice"
        const val QUERY_PARAM = "queryParam"
        const val DEFAULT_VALUE_X_DEVICE = "android"
        const val PRODUCT_IDS = "productIDs"
        private const val PARAM_CARD_REIMAGINE = "productCardVersion"
        private const val REFRESH_TYPE = "refreshType"
        private const val CURRENT_SESSION_ID = "currentSessionID"
        private const val ENTER_FROM = "enterFrom"
        private const val SOURCE_PAGE_TYPE = "sourcePageType"

        private const val CARD_REIMAGINE_VERSION = 5
        private const val CARD_REVERT_VERSION = 0
    }
}
