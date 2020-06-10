package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener

class DigitalHomePageTypeFactory(val onItemBindListener: OnItemBindListener,
                                 val transactionListener: DigitalHomePageTransactionViewHolder.TransactionListener?)
    : BaseAdapterTypeFactory() {

    fun type(digitalHomePageBannerModel: DigitalHomePageBannerModel): Int {
        return DigitalHomePageBannerViewHolder.LAYOUT
    }

    fun type(digitalHomePageCategoryModel: DigitalHomePageCategoryModel): Int {
        return DigitalHomePageCategoryViewHolder.LAYOUT
    }

    fun type(digitalHomePageTransactionModel: DigitalHomePageTransactionModel): Int {
        return DigitalHomePageTransactionViewHolder.LAYOUT
    }

    fun type(digitalHomePageFavoritesModel: DigitalHomePageFavoritesModel): Int {
        return DigitalHomePageFavoriteViewHolder.LAYOUT
    }

    fun type(digitalHomePageTrustMarkModel: DigitalHomePageTrustMarkModel): Int {
        return DigitalHomePageTrustMarkViewHolder.LAYOUT
    }

    fun type(digitalHomePageNewUserZoneModel: DigitalHomePageNewUserZoneModel): Int {
        return DigitalHomePageNewUserZoneViewHolder.LAYOUT
    }

    fun type(digitalHomePageSpotlightModel: DigitalHomePageSpotlightModel): Int {
        return DigitalHomePageSpotlightViewHolder.LAYOUT
    }

    fun type(digitalHomePageSubscriptionModel: DigitalHomePageSubscriptionModel): Int {
        return DigitalHomePageSubscriptionViewHolder.LAYOUT
    }

    fun type(digitalHomePageRecommendationModel: DigitalHomePageRecommendationModel): Int {
        return DigitalHomePageRecommendationViewHolder.LAYOUT
    }

    fun type(section: RechargeHomepageSections.Section): Int {
        // Map section based on template
        // TODO: Finish the rest of the sections and add them to the list
        with (RechargeHomepageSections.Companion) {
            return when (section.template) {
//                SECTION_TOP_BANNER ->
//                SECTION_TOP_BANNER_EMPTY ->
                SECTION_TOP_ICONS -> RechargeHomepageFavoriteViewHolder.LAYOUT
                SECTION_DYNAMIC_ICONS -> RechargeHomepageCategoryViewHolder.LAYOUT
                SECTION_DUAL_ICONS -> RechargeHomepageTrustMarkViewHolder.LAYOUT
//                SECTION_URGENCY_WIDGET ->
//                SECTION_VIDEO_HIGHLIGHT ->
//                SECTION_VIDEO_HIGHLIGHTS ->
//                SECTION_SINGLE_BANNER ->
//                SECTION_COUNTDOWN_SINGLE_BANNER ->
//                SECTION_DUAL_BANNERS ->
//                SECTION_LEGO_BANNERS ->
//                SECTION_PRODUCT_CARD_ROW ->
//                SECTION_COUNTDOWN_PRODUCT_BANNER ->
                else -> 0
            }
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            // TODO: Finish the rest of viewholders and add them to the list
            RechargeHomepageFavoriteViewHolder.LAYOUT -> RechargeHomepageFavoriteViewHolder(parent, onItemBindListener)
            RechargeHomepageCategoryViewHolder.LAYOUT -> RechargeHomepageCategoryViewHolder(parent, onItemBindListener)
            RechargeHomepageTrustMarkViewHolder.LAYOUT -> RechargeHomepageTrustMarkViewHolder(parent, onItemBindListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
