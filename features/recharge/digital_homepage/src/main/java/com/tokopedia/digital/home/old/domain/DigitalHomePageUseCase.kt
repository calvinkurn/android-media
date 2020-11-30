package com.tokopedia.digital.home.old.domain

import com.tokopedia.digital.home.old.model.*
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.UseCase

class DigitalHomePageUseCase (private val useCase: MultiRequestGraphqlUseCase,
                              var isFromCloud: Boolean = false): UseCase<List<DigitalHomePageItemModel>>() {

    var queryList: Map<String, String> = mapOf()
        set(value) {
            if (value.isNotEmpty()
                    && value.containsKey(QUERY_BANNER)
                    && value.containsKey(QUERY_CATEGORY)
                    && value.containsKey(QUERY_RECOMMENDATION)
                    && value.containsKey(QUERY_SECTIONS)) {
                field = value
            }
        }
    var sectionOrdering: Map<String, Int> = mapOf()
        set(value) {
            if (value.isNotEmpty()
                    && value.containsKey(BANNER_ORDER)
                    && value.containsKey(CATEGORY_ORDER)
                    && value.containsKey(RECOMMENDATION_ORDER)
                    && value.containsKey(FAVORITES_ORDER)
                    && value.containsKey(TRUST_MARK_ORDER)
                    && value.containsKey(NEW_USER_ZONE_ORDER)
                    && value.containsKey(SPOTLIGHT_ORDER)
                    && value.containsKey(SUBSCRIPTION_ORDER)) {
                field = value
            }
        }

    fun getEmptyList(): List<DigitalHomePageItemModel>{
        val homeBanner = DigitalHomePageBannerModel()
        val favorites = DigitalHomePageFavoritesModel()
        val trustMark = DigitalHomePageTrustMarkModel()
        val recommendation = DigitalHomePageRecommendationModel()
        val newUserZone = DigitalHomePageNewUserZoneModel()
        val spotLight = DigitalHomePageSpotlightModel()
        val subscription = DigitalHomePageSubscriptionModel()
        val category = DigitalHomePageCategoryModel()

        return listOf(homeBanner,
                favorites,
                trustMark,
                recommendation,
                newUserZone,
                spotLight,
                subscription,
                category
        )
    }

    override suspend fun executeOnBackground(): List<DigitalHomePageItemModel> {
        if (queryList.isNotEmpty() && sectionOrdering.isNotEmpty()) {
            useCase.clearRequest()
            useCase.setCacheStrategy(GraphqlCacheStrategy
                    .Builder(if (isFromCloud) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build())

            val bannerRequest = GraphqlRequest(queryList[QUERY_BANNER],
                    DigitalHomePageBannerModel::class.java)
            val categoryRequest = GraphqlRequest(queryList[QUERY_CATEGORY],
                    DigitalHomePageCategoryModel::class.java)
            val recommendationRequest = GraphqlRequest(queryList[QUERY_RECOMMENDATION],
                    DigitalHomePageRecommendationModel::class.java, createParams(QUERY_RECOMMENDATION))
            val favoritesRequest = GraphqlRequest(queryList[QUERY_SECTIONS],
                    DigitalHomePageFavoritesModel::class.java, createParams(QUERY_FAVORITES))
            val trustMarkRequest = GraphqlRequest(queryList[QUERY_SECTIONS],
                    DigitalHomePageTrustMarkModel::class.java, createParams(QUERY_TRUST_MARK))
            val newUserZoneRequest = GraphqlRequest(queryList[QUERY_SECTIONS],
                    DigitalHomePageNewUserZoneModel::class.java, createParams(QUERY_NEW_USER_ZONE))
            val spotlightRequest = GraphqlRequest(queryList[QUERY_SECTIONS],
                    DigitalHomePageSpotlightModel::class.java, createParams(QUERY_SPOTLIGHT))
            val subscriptionRequest = GraphqlRequest(queryList[QUERY_SECTIONS],
                    DigitalHomePageSubscriptionModel::class.java, createParams(QUERY_SUBSCRIPTION))

            useCase.addRequests(listOf(
                    bannerRequest,
                    categoryRequest,
                    recommendationRequest,
                    favoritesRequest,
                    trustMarkRequest,
                    newUserZoneRequest,
                    spotlightRequest,
                    subscriptionRequest))

            val gqlResponse: GraphqlResponse
            try {
                gqlResponse = useCase.executeOnBackground()
            } catch (error: Throwable) {
                return listOf()
            }
            val sectionList = getEmptyList().toMutableList()

            val bannerOrder = sectionOrdering[BANNER_ORDER] ?: 0
            val categoryOrder = sectionOrdering[CATEGORY_ORDER] ?: 0
            val recommendationOrder = sectionOrdering[RECOMMENDATION_ORDER] ?: 0
            val favoritesOrder = sectionOrdering[FAVORITES_ORDER] ?: 0
            val trustMarkOrder = sectionOrdering[TRUST_MARK_ORDER] ?: 0
            val newUserZoneOrder = sectionOrdering[NEW_USER_ZONE_ORDER] ?: 0
            val spotlightOrder = sectionOrdering[SPOTLIGHT_ORDER] ?: 0
            val subscriptionOrder = sectionOrdering[SUBSCRIPTION_ORDER] ?: 0

            // Banner
            var bannerData = sectionList[bannerOrder]
            try {
                val responseBannerData = gqlResponse.getSuccessData<DigitalHomePageBannerModel>()
                responseBannerData.isLoaded = true
                responseBannerData.isSuccess = true
                bannerData = responseBannerData
            } catch (t: Throwable) {
                bannerData.isLoaded = true
                bannerData.isSuccess = false
            }
            sectionList[bannerOrder] = bannerData

            // Category
            var categoryData = sectionList[categoryOrder]
            try {
                val responseCategoryData = gqlResponse.getSuccessData<DigitalHomePageCategoryModel>()
                responseCategoryData.isLoaded = true
                responseCategoryData.isSuccess = true
                categoryData = responseCategoryData
            } catch (t: Throwable) {
                categoryData.isLoaded = true
                categoryData.isSuccess = false
            }
            sectionList[categoryOrder] = categoryData

            // Recommendation
            var recommendationData = sectionList[recommendationOrder]
            try {
                val responseRecommendationData = gqlResponse.getSuccessData<DigitalHomePageRecommendationModel>()
                responseRecommendationData.isLoaded = true
                responseRecommendationData.isSuccess = true
                recommendationData = responseRecommendationData
            } catch (t: Throwable) {
                recommendationData.isLoaded = true
                recommendationData.isSuccess = false
            }
            sectionList[recommendationOrder] = recommendationData

            // Favorites
            var favoritesData = sectionList[favoritesOrder]
            try {
                val responseFavoritesData = gqlResponse.getSuccessData<DigitalHomePageFavoritesModel>()
                responseFavoritesData.isLoaded = true
                responseFavoritesData.isSuccess = true
                responseFavoritesData.isEmpty = responseFavoritesData.data == null
                favoritesData = responseFavoritesData
            } catch (t: Throwable) {
                favoritesData.isLoaded = true
                favoritesData.isSuccess = false
            }
            sectionList[favoritesOrder] = favoritesData

            // Trust Mark
            var trustMarkData = sectionList[trustMarkOrder]
            try {
                val responseTrustMarkData = gqlResponse.getSuccessData<DigitalHomePageTrustMarkModel>()
                responseTrustMarkData.isLoaded = true
                responseTrustMarkData.isSuccess = true
                responseTrustMarkData.isEmpty = responseTrustMarkData.data == null
                trustMarkData = responseTrustMarkData
            } catch (t: Throwable) {
                trustMarkData.isLoaded = true
                trustMarkData.isSuccess = false
            }
            sectionList[trustMarkOrder] = trustMarkData

            // New User Zone
            var newUserZoneData = sectionList[newUserZoneOrder]
            try {
                val responseNewUserZoneData = gqlResponse.getSuccessData<DigitalHomePageNewUserZoneModel>()
                responseNewUserZoneData.isLoaded = true
                responseNewUserZoneData.isSuccess = true
                responseNewUserZoneData.isEmpty = responseNewUserZoneData.data == null
                newUserZoneData = responseNewUserZoneData
            } catch (t: Throwable) {
                newUserZoneData.isLoaded = true
                newUserZoneData.isSuccess = false
            }
            sectionList[newUserZoneOrder] = newUserZoneData

            // Spotlight
            var spotlightData = sectionList[spotlightOrder]
            try {
                val responseSpotlightData = gqlResponse.getSuccessData<DigitalHomePageSpotlightModel>()
                responseSpotlightData.isLoaded = true
                responseSpotlightData.isSuccess = true
                responseSpotlightData.isEmpty = responseSpotlightData.data == null
                spotlightData = responseSpotlightData
            } catch (t: Throwable) {
                spotlightData.isLoaded = true
                spotlightData.isSuccess = false
            }
            sectionList[spotlightOrder] = spotlightData

            // Subscription
            var subscriptionData = sectionList[subscriptionOrder]
            try {
                val responseSubscriptionData = gqlResponse.getSuccessData<DigitalHomePageSubscriptionModel>()
                responseSubscriptionData.isLoaded = true
                responseSubscriptionData.isSuccess = true
                responseSubscriptionData.isEmpty = responseSubscriptionData.data == null
                subscriptionData = responseSubscriptionData
            } catch (t: Throwable) {
                subscriptionData.isLoaded = true
                subscriptionData.isSuccess = false
            }
            sectionList[subscriptionOrder] = subscriptionData

            return sectionList
        }
        return listOf()
    }

    private fun createParams(queryType: String): Map<String, Any> {
        return when (queryType) {
            QUERY_RECOMMENDATION -> mapOf(PARAM_DEVICE_ID to DEFAULT_DEVICE_ID)
            QUERY_FAVORITES -> mapOf(SECTION_TYPE to PARAM_FAVORITES)
            QUERY_TRUST_MARK -> mapOf(SECTION_TYPE to PARAM_TRUST_MARK)
            QUERY_NEW_USER_ZONE -> mapOf(SECTION_TYPE to PARAM_NEW_USER_ZONE)
            QUERY_SPOTLIGHT -> mapOf(SECTION_TYPE to PARAM_SPOTLIGHT)
            QUERY_SUBSCRIPTION -> mapOf(SECTION_TYPE to PARAM_SUBSCRIPTION)
            else -> mapOf()
        }
    }

    companion object {
        const val QUERY_BANNER = "QUERY_BANNER"
        const val QUERY_CATEGORY = "QUERY_CATEGORY"
        const val QUERY_RECOMMENDATION = "QUERY_RECOMMENDATION"
        const val QUERY_SECTIONS = "QUERY_SECTIONS"
        const val QUERY_FAVORITES = "QUERY_FAVORITES"
        const val QUERY_TRUST_MARK = "QUERY_TRUST_MARK"
        const val QUERY_NEW_USER_ZONE = "QUERY_NEW_USER_ZONE"
        const val QUERY_SPOTLIGHT = "QUERY_SPOTLIGHT"
        const val QUERY_SUBSCRIPTION = "QUERY_SUBSCRIPTION"

        const val SECTION_TYPE = "sectionType"
        const val PARAM_FAVORITES = "BEHAVIOURAL_ICON"
        const val PARAM_TRUST_MARK = "TRUSTMARK"
        const val PARAM_NEW_USER_ZONE = "NEW_USER_ZONE"
        const val PARAM_SPOTLIGHT = "SPOTLIGHT"
        const val PARAM_SUBSCRIPTION = "LANGGANAN_SUGGESTION"
        const val PARAM_DEVICE_ID = "device_id"
        const val DEFAULT_DEVICE_ID = 5

        const val BANNER_ORDER = "BANNER_ORDER"
        const val FAVORITES_ORDER = "FAVORITES_ORDER"
        const val TRUST_MARK_ORDER = "TRUST_MARK_ORDER"
        const val RECOMMENDATION_ORDER = "RECOMMENDATION_ORDER"
        const val NEW_USER_ZONE_ORDER = "NEW_USER_ZONE_ORDER"
        const val SPOTLIGHT_ORDER = "SPOTLIGHT_ORDER"
        const val SUBSCRIPTION_ORDER = "SUBSCRIPTION_ORDER"
        const val CATEGORY_ORDER = "CATEGORY_ORDER"
        const val PROMO_ORDER = "PROMO_ORDER"
    }
}