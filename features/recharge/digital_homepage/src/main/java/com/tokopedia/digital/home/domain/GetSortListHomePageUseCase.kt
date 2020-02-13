package com.tokopedia.digital.home.domain

import com.tokopedia.digital.home.model.*

class GetSortListHomePageUseCase {

    fun getSortEmptyList(loadFromCloud: Boolean): List<DigitalHomePageItemModel>{

        val homeBanner = DigitalHomePageBannerModel()
        homeBanner.isLoadFromCloud = loadFromCloud

        val favorites = DigitalHomePageFavoritesModel()
        favorites.isLoadFromCloud = loadFromCloud

        val trustMark = DigitalHomePageTrustMarkModel()
        trustMark.isLoadFromCloud = loadFromCloud

        val recommendation = DigitalHomePageRecommendationModel()
        recommendation.isLoadFromCloud = loadFromCloud

        val newUserZone = DigitalHomePageNewUserZoneModel()
        newUserZone.isLoadFromCloud = loadFromCloud

        val spotLight = DigitalHomePageSpotlightModel()
        spotLight.isLoadFromCloud = loadFromCloud

        val subscription = DigitalHomePageSubscriptionModel()
        subscription.isLoadFromCloud = loadFromCloud

        val category = DigitalHomePageCategoryModel()
        category.isLoadFromCloud = loadFromCloud

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
}