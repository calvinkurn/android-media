package com.tokopedia.home_explore_category.analytic

import android.os.Bundle
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.ALL_CATEGORY_PAGE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.CLICK_ACCORDION
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.CLICK_BACK_BUTTON
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.CLICK_HOMEPAGE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.CREATIVE_NAME
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.HOME_AND_BROWSE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.KEY_BUSINESS_UNIT
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.KEY_CURRENT_SITE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.KEY_TRACKER_ID
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.KEY_USER_ID
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.SELECT_CONTENT
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TOKOPEDIA_MARKETPLACE
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TRACKER_ID_47052
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TRACKER_ID_47053
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TRACKER_ID_47054
import com.tokopedia.home_explore_category.analytic.ExploreCategoryConstants.Companion.TRACKER_ID_47058
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Ecommerce.PROMO_VIEW
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ExploreCategoryAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) : BaseTrackerConst() {

    private fun createEventMap(
        event: String,
        category: String,
        action: String,
        label: String
    ): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[EVENT] = event
        eventMap[EVENT_CATEGORY] = category
        eventMap[EVENT_ACTION] = action
        eventMap[EVENT_LABEL] = label
        return eventMap
    }

    fun sendCategoryItemClicked(categoryName: String) {
        val event = createEventMap(
            event = CLICK_HOMEPAGE,
            category = ALL_CATEGORY_PAGE,
            action = CLICK_ACCORDION,
            label = categoryName
        ).apply {
            put(KEY_TRACKER_ID, TRACKER_ID_47052)
            put(KEY_BUSINESS_UNIT, HOME_AND_BROWSE)
            put(KEY_CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            put(KEY_USER_ID, userSession.userId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendBackButtonClicked() {
        val event = createEventMap(
            event = CLICK_HOMEPAGE,
            category = ALL_CATEGORY_PAGE,
            action = CLICK_BACK_BUTTON,
            label = ""
        ).apply {
            put(KEY_TRACKER_ID, TRACKER_ID_47058)
            put(KEY_BUSINESS_UNIT, HOME_AND_BROWSE)
            put(KEY_CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            put(KEY_USER_ID, userSession.userId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun sendSubCategoryItemClicked(
        categoryName: String,
        subExploreCategory: ExploreCategoryUiModel.SubExploreCategoryUiModel,
        position: Int
    ) {
        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, "click - $categoryName")
            putString(EVENT_CATEGORY, ALL_CATEGORY_PAGE)
            putString(
                EVENT_LABEL,
                subExploreCategory.id
            )
            putString(
                KEY_TRACKER_ID,
                TRACKER_ID_47054
            )
            putString(
                KEY_BUSINESS_UNIT,
                HOME_AND_BROWSE
            )
            putString(
                KEY_CURRENT_SITE,
                TOKOPEDIA_MARKETPLACE
            )
            val creativeSlot = (position + Int.ONE).toString()

            putParcelableArrayList(
                Promotion.KEY,
                arrayListOf(
                    Bundle().also {
                        it.putString(
                            CREATIVE_NAME,
                            subExploreCategory.name
                        )
                        it.putString(Promotion.CREATIVE_SLOT, creativeSlot)
                        it.putString(Promotion.ITEM_ID, subExploreCategory.id)
                        it.putString(
                            Promotion.ITEM_NAME,
                            categoryName
                        )
                    }
                )
            )
            putString(KEY_USER_ID, userSession.userId)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            SELECT_CONTENT,
            bundle
        )
    }

    fun getSubCategoryItemImpressed(
        categoryName: String,
        subExploreCategory: ExploreCategoryUiModel.SubExploreCategoryUiModel,
        position: Int
    ): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeSlot = (position + Int.ONE).toString()

        val promotionList = listOf(
            Promotion(
                creative = subExploreCategory.name,
                position = creativeSlot,
                id = subExploreCategory.id,
                name = categoryName,
                promoIds = Label.NONE,
                promoCodes = Label.NONE
            )
        )

        return trackingBuilder.constructBasicPromotionView(
            event = PROMO_VIEW,
            eventAction = "impression - $categoryName",
            eventCategory = ALL_CATEGORY_PAGE,
            eventLabel = subExploreCategory.id,
            promotions = promotionList
        ).appendBusinessUnit(
            HOME_AND_BROWSE
        ).appendCurrentSite(
            TOKOPEDIA_MARKETPLACE
        ).appendUserId(userSession.userId).appendCustomKeyValue(
            KEY_TRACKER_ID,
            TRACKER_ID_47053
        ).build() as HashMap<String, Any>
    }
}
