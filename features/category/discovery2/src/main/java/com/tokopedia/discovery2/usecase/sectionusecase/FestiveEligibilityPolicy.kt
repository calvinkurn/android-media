package com.tokopedia.discovery2.usecase.sectionusecase

import com.tokopedia.discovery2.ComponentNames

data class FestiveEligibilityPolicy(
    val isFeatureEnable: Boolean,
    val isBackgroundAvailable: Boolean
) {

    fun isAllowed(itemNames: List<String?>): Boolean {
        if (!isFeatureEnable || !isBackgroundAvailable) return false

        return validateEachComponent(itemNames)
    }

    private fun validateEachComponent(itemNames: List<String?>): Boolean {
        var areComponentsSupportBG = true

        loop@ for (name in itemNames) {
            areComponentsSupportBG = componentsSupportBG.find { name == it } != null

            if (!areComponentsSupportBG) break@loop
        }
        return areComponentsSupportBG
    }

    companion object {
        val componentsSupportBG = arrayOf(
            ComponentNames.LihatSemua.componentName,
            ComponentNames.ProductCardSingle.componentName,
            ComponentNames.ProductCardSingleReimagine.componentName,
            ComponentNames.SingleBanner.componentName,
            ComponentNames.DoubleBanner.componentName,
            ComponentNames.TripleBanner.componentName,
            ComponentNames.QuadrupleBanner.componentName,
            ComponentNames.CalendarWidgetCarousel.componentName,
            ComponentNames.ProductHighlight.componentName,
            ComponentNames.ProductCardCarousel.componentName,
            ComponentNames.ShopOfferHeroBrand.componentName,
            ComponentNames.Margin.componentName
        )
    }
}
