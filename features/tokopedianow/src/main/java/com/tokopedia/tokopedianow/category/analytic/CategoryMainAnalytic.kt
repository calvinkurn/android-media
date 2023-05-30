package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Category Revamp L1 Tracker
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3980
 **/

class CategoryMainAnalytic @Inject constructor(
    private val userSession: UserSessionInterface
) {
    internal object ACTION {
        const val EVENT_ACTION_CLICK_OTHER_CATEGORIES = "click kategori lain"
        const val EVENT_ACTION_IMPRESS_CATEGORY_L2 = "impression category widget - l2"
        const val EVENT_ACTION_CLICK_CATEGORY_L2 = "click category widget - l2"
        const val EVENT_ACTION_IMPRESS_PRODUCT_CAROUSEL = "impression product carousel"
        const val EVENT_ACTION_CLICK_PRODUCT_CAROUSEL = "click product carousel"
        const val EVENT_ACTION_IMPRESS_PRODUCT_SHOWCASE = "impression product in showcase - l2"
        const val EVENT_ACTION_CLICK_ARROW_BUTTON_SHOWCASE = "click arrow button showcase - l2"
        const val EVENT_ACTION_CLICK_PRODUCT_SHOWCASE = "click product showcase - l2"
        const val EVENT_ACTION_IMPRESS_CATEGORY_RECOM_WIDGET = "impression category recom widget"
        const val EVENT_ACTION_CLICK_CATEGORY_RECOM_WIDGET = "click category recom widget"
        const val EVENT_ACTION_CLICK_ATC_ON_SHOWCASE = "click atc on showcase - l2"
        const val EVENT_ACTION_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET = "click atc carousel widget"
    }

    internal object CATEGORY {
        const val EVENT_CATEGORY_PAGE_L1 = "tokonow - category page - l1"
    }

    internal object VALUE {
        const val ITEM_LIST_CATEGORY_L1 = "category l1"
        const val ITEM_LIST_SHOWCASE = "showcase"
        const val ITEM_LIST_RECOMPRODUCT = "recomproduct"
    }

    internal object TRACKER_ID {
        const val ID_CLICK_OTHER_CATEGORIES  = "43848"
        const val ID_IMPRESS_CATEGORY_L2 = "43849"
        const val ID_CLICK_CATEGORY_L2 = "43850"
        const val ID_IMPRESS_PRODUCT_CAROUSEL = "43851"
        const val ID_CLICK_PRODUCT_CAROUSEL = "43852"
        const val ID_IMPRESS_PRODUCT_SHOWCASE = "43853"
        const val ID_CLICK_ARROW_BUTTON_SHOWCASE = "43854"
        const val ID_CLICK_PRODUCT_SHOWCASE = "43855"
        const val ID_IMPRESS_CATEGORY_RECOM_WIDGET = "43858"
        const val ID_CLICK_CATEGORY_RECOM_WIDGET = "43859"
        const val ID_CLICK_ATC_ON_SHOWCASE= "43860"
        const val ID_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET = "43862"
    }

    val categoryTitleAnalytics: CategoryTitleAnalytic
        get() = CategoryTitleAnalytic()

    val categoryNavigationAnalytic: CategoryNavigationAnalytic
        get() = CategoryNavigationAnalytic()

    val categoryProductRecommendationAnalytic: CategoryProductRecommendationAnalytic
        get() = CategoryProductRecommendationAnalytic(userSession.userId)

    val categoryMenuAnalytic: CategoryMenuAnalytic
        get() = CategoryMenuAnalytic()

    val categoryShowcaseAnalytic: CategoryShowcaseAnalytic
        get() = CategoryShowcaseAnalytic(userSession.userId)
}
