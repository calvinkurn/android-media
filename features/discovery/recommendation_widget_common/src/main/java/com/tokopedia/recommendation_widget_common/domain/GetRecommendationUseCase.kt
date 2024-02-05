package com.tokopedia.recommendation_widget_common.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.recommendation_widget_common.extension.PAGENAME_IDENTIFIER_RECOM_ATC
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

/**
 * Created by devara fikry on 16/04/19.
 * Credit errysuprayogi
 */

@Deprecated(message = "please use GetRecommendationUseCase from coroutine folder instead")
open class GetRecommendationUseCase @Inject
constructor(
    private val context: Context,
    private val recomRawString: String,
    private val graphqlUseCase: GraphqlUseCase,
    private val userSession: UserSessionInterface
) : UseCase<List<RecommendationWidget>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<RecommendationWidget>> {
        val graphqlRequest = GraphqlRequest(recomRawString, RecommendationEntity::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
            .map { graphqlResponse ->
                val entity = graphqlResponse.getData<RecommendationEntity>(RecommendationEntity::class.java)
                entity?.productRecommendationWidget?.data?.mappingToRecommendationModel()
            }
    }

    fun getRecomParams(
        pageNumber: Int,
        xSource: String,
        pageName: String,
        productIds: List<String>
    ): RequestParams {
        return getRecomParams(pageNumber, xSource, pageName, productIds, "")
    }

    fun getRecomParams(
        pageNumber: Int,
        xSource: String = DEFAULT_VALUE_X_SOURCE,
        pageName: String,
        productIds: List<String>,
        queryParam: String = "",
        hasNewProductCardEnabled: Boolean = false,
    ): RequestParams {
        val params = RequestParams.create()
        val productIdsString = TextUtils.join(",", productIds)
        val reimagineCardVersion = getProductCardReimagineVersion(pageName, hasNewProductCardEnabled)
        val newQueryParam = try {
            ChooseAddressUtils
                .getLocalizingAddressData(context)
                .toQueryParam(queryParam)
        } catch (e: Exception) {
            queryParam
        }

        if (userSession.isLoggedIn) {
            params.putInt(USER_ID, userSession.userId.toInt())
        } else {
            params.putInt(USER_ID, 0)
        }
        if (xSource.isEmpty()) {
            params.putString(X_SOURCE, DEFAULT_VALUE_X_SOURCE)
        } else {
            params.putString(X_SOURCE, xSource)
        }
        params.putInt(PAGE_NUMBER, pageNumber)
        params.putString(PAGE_NAME, pageName)
        params.putString(PRODUCT_IDS, productIdsString)
        params.putString(QUERY_PARAM, newQueryParam)
        params.putInt(PARAM_CARD_REIMAGINE, reimagineCardVersion)
        params.putString(X_DEVICE, DEFAULT_VALUE_X_DEVICE)
        return params
    }

    fun getRecomTokonowParams(
        pageNumber: Int,
        xSource: String = DEFAULT_VALUE_X_SOURCE,
        pageName: String,
        productIds: List<String>,
        queryParam: String = "",
        isTokonow: Boolean = false
    ): RequestParams {
        val params = RequestParams.create()
        val productIdsString = TextUtils.join(",", productIds)
        val newQueryParam = ChooseAddressUtils.getLocalizingAddressData(context)?.toQueryParam(queryParam) ?: queryParam

        if (userSession.isLoggedIn) {
            params.putInt(USER_ID, userSession.userId.toInt())
        } else {
            params.putInt(USER_ID, 0)
        }
        if (xSource.isEmpty()) {
            params.putString(X_SOURCE, DEFAULT_VALUE_X_SOURCE)
        } else {
            params.putString(X_SOURCE, xSource)
        }
        if (isTokonow) params.putBoolean(PARAM_TOKONOW, isTokonow)
        params.putInt(PAGE_NUMBER, pageNumber)
        params.putString(PAGE_NAME, pageName)
        params.putString(PRODUCT_IDS, productIdsString)
        params.putString(QUERY_PARAM, newQueryParam)
        params.putString(X_DEVICE, DEFAULT_VALUE_X_DEVICE)
        return params
    }

    fun getOfficialStoreRecomParams(
        pageNumber: Int,
        pageName: String,
        categoryIds: String
    ): RequestParams {
        val params = getRecomParams(pageNumber, OFFICIAL_STORE, pageName, listOf(), "")
        params.putString(CATEGORY_IDS, categoryIds)
        params.putBoolean(OS, true)
        return params
    }

    private fun getProductCardReimagineVersion(pageName: String, hasNewProductCardEnabled: Boolean): Int {
        val isQuantityEditor = pageName.contains(PAGENAME_IDENTIFIER_RECOM_ATC)
        return if (ProductCardExperiment.isReimagine() && hasNewProductCardEnabled && isQuantityEditor.not()) {
            CARD_REIMAGINE_VERSION
        } else {
            CARD_REVERT_VERSION
        }
    }

    companion object {
        const val USER_ID = "userID"
        const val X_SOURCE = "xSource"
        const val PAGE_NUMBER = "pageNumber"
        const val X_DEVICE = "xDevice"
        const val PAGE_NAME = "pageName"
        const val QUERY_PARAM = "queryParam"
        const val DEFAULT_VALUE_X_SOURCE = "recom_widget"
        const val DEFAULT_VALUE_X_DEVICE = "android"
        const val DEFAULT_PAGE_NAME = ""
        const val PRODUCT_IDS = "productIDs"
        const val OFFICIAL_STORE = "official-store"
        const val OS = "os"
        const val CATEGORY_IDS = "categoryIDs"
        private const val PARAM_TOKONOW = "tokoNow"
        private const val PARAM_CARD_REIMAGINE = "productCardVersion"

        private const val CARD_REIMAGINE_VERSION = 5
        private const val CARD_REVERT_VERSION = 0
    }
}
