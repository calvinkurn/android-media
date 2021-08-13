package com.tokopedia.homenav.category.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by Lukas on 23/10/20.
 */
object CategoryTracking : BaseTrackerConst(){
    private const val CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer"
    private const val GLOBAL_MENU_CATEGORY_PAGE = "global menu - kategori page"
    private const val GLOBAL_MENU_LAINNYA_PAGE = "global menu - lainnya page"
    private const val CLICK_ON_CATEGORY_MENU = "click on category menu"
    private const val CLICK_ON_CLOSE_BUTTON = "click on close button"
    private const val CLICK_ON_LAINNYA_MENU = "click on lainnya menu"

    fun onClickItem(categoryId: String, userId: String){
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU_CATEGORY_PAGE)
                .appendEventAction(CLICK_ON_CATEGORY_MENU)
                .appendEventLabel(categoryId)
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build())
    }

    fun onClickLainnyaItem(lainnyaMenu: String, userId: String){
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU_LAINNYA_PAGE)
                .appendEventAction(CLICK_ON_LAINNYA_MENU)
                .appendEventLabel(lainnyaMenu)
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build())
    }

    fun onClickCloseCategory(userId: String){
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU_CATEGORY_PAGE)
                .appendEventAction(CLICK_ON_CLOSE_BUTTON)
                .appendEventLabel(Label.NONE)
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build())
    }

    fun onClickCloseLainnya(userId: String){
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU_LAINNYA_PAGE)
                .appendEventAction(CLICK_ON_CLOSE_BUTTON)
                .appendEventLabel(Label.NONE)
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build())
    }
}