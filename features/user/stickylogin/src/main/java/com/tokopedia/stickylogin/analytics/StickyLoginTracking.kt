package com.tokopedia.stickylogin.analytics

import com.tokopedia.stickylogin.common.StickyLoginConstant.Page
import com.tokopedia.track.TrackApp

class StickyLoginTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun viewOnPage(page: Page) {
        when (page) {
            Page.HOME -> {
                tracker.sendGeneralEvent(EVENT.VIEW_HOME_PAGE, CATEGORY.HOME_PAGE, ACTION.VIEW_STICKY, LABEL.EMPTY)
            }
            Page.PDP -> {
                tracker.sendGeneralEvent(EVENT.VIEW_PDP, CATEGORY.PDP, ACTION.VIEW_STICKY, LABEL.EMPTY)
            }
            Page.SHOP -> {
                tracker.sendGeneralEvent(EVENT.VIEW_SHOP, CATEGORY.SHOP_PAGE, ACTION.VIEW_STICKY, LABEL.EMPTY)
            }
        }
    }

    fun clickOnLogin(page: Page) {
        when (page) {
            Page.HOME -> {
                tracker.sendGeneralEvent(EVENT.CLICK_HOME_PAGE, CATEGORY.HOME_PAGE, ACTION.CLICK_ON_LOGIN, LABEL.CLICK)
            }
            Page.PDP -> {
                tracker.sendGeneralEvent(EVENT.CLICK_PDP, CATEGORY.PDP, ACTION.CLICK_ON_LOGIN, LABEL.CLICK)
            }
            Page.SHOP -> {
                tracker.sendGeneralEvent(EVENT.CLICK_SHOP, CATEGORY.SHOP_PAGE, ACTION.CLICK_ON_LOGIN, LABEL.CLICK)
            }
        }
    }

    fun clickOnDismiss(page: Page) {
        when (page) {
            Page.HOME -> {
                tracker.sendGeneralEvent(EVENT.CLICK_HOME_PAGE, CATEGORY.HOME_PAGE, ACTION.CLICK_ON_CLOSE, LABEL.EMPTY)
            }
            Page.PDP -> {
                tracker.sendGeneralEvent(EVENT.CLICK_PDP, CATEGORY.PDP, ACTION.CLICK_ON_CLOSE, LABEL.EMPTY)
            }
            Page.SHOP -> {
                tracker.sendGeneralEvent(EVENT.CLICK_SHOP, CATEGORY.SHOP_PAGE, ACTION.CLICK_ON_CLOSE, LABEL.EMPTY)
            }
        }
    }

    companion object {

        object EVENT {
            const val CLICK_HOME_PAGE = "clickHomepage"
            const val VIEW_HOME_PAGE = "viewHomepageIris"

            const val CLICK_PDP = "clickPDP"
            const val VIEW_PDP = "viewPDP"

            const val CLICK_SHOP = "clickShopPage"
            const val VIEW_SHOP = "viewShopPage"
        }

        object CATEGORY {
            const val HOME_PAGE = "homepage"
            const val PDP = "product detail page"
            const val SHOP_PAGE = "shop page"
        }

        object ACTION {
            const val CLICK_ON_LOGIN = "click on login sticky widget"
            const val CLICK_ON_CLOSE = "click on button close login sticky widget"
            const val VIEW_STICKY = "view login sticky widget"
        }

        object LABEL {
            const val EMPTY = ""
            const val CLICK = "click"
        }
    }
}