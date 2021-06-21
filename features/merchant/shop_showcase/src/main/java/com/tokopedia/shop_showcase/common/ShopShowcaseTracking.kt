package com.tokopedia.shop_showcase.common

import android.content.Context
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface

class ShopShowcaseTracking (context: Context?) {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }
    private var page: String = ""
    private var eventAction: String = ""
    private var screenName: String = ""

    private fun getDataLayer(event: String, eventCategory: String, eventAction: String,
                             eventLabel: String, shopId: String, shopType: String, pageType: String
    ): Map<String, Any> {
        return DataLayer.mapOf(
                EVENT, event,
                EVENT_CATEGORY, eventCategory,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                SHOP_ID, shopId,
                SHOP_TYPE, shopType,
                PAGE_TYPE, pageType
        )
    }

    fun getShopType(userSession: UserSessionInterface): String {
        return when {
            userSession.isGoldMerchant -> ShopType.GOLD_MERCHANT
            userSession.isShopOfficialStore -> ShopType.OFFICIAL_STORE
            else -> ShopType.REGULAR
        }
    }

    fun sendScreenName() {
        screenName = "/add etalase page - start"
        tracker.sendScreenAuthenticated(screenName)
    }

    fun sendScreenNameAddShowcaseProduct() {
        screenName = "/add etalase page - product"
        tracker.sendScreenAuthenticated(screenName)
    }

    fun sendScreenNameFinishAddShowcase() {
        screenName = "/add etalase page - finish"
        tracker.sendScreenAuthenticated(screenName)
    }

    fun addShowcaseClickBackButton(shopId: String, shopType: String, isActionEdit: Boolean) {
        if(isActionEdit) {
            page = EDIT_SHOWCASE_PAGE
            eventAction = "click back on edit product page"
        } else {
            page = ADD_SHOWCASE_PAGE
            eventAction = "click back"
        }
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        eventAction,
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseClickNameField(shopId: String, shopType: String, isActionEdit: Boolean) {
        if(isActionEdit) {
            page = EDIT_SHOWCASE_PAGE
            eventAction = "click etalase field on edit product page"
        } else {
            page = ADD_SHOWCASE_PAGE
            eventAction = "click etalase field"
        }
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        eventAction,
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseClickAddProduct(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ADD_SHOWCASE_PAGE,
                        "click add product",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseProductClickBackButton(shopId: String, shopType: String, isActionEdit: Boolean) {
        page = if(isActionEdit) {
            EDIT_SHOWCASE_PAGE
        } else {
            ADD_SHOWCASE_PAGE
        }
        eventAction = "click back on add product page"
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        eventAction,
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseProductClickSaveButton(shopId: String, shopType: String, isActionEdit: Boolean) {
        page = if(isActionEdit) {
            EDIT_SHOWCASE_PAGE
        } else {
            ADD_SHOWCASE_PAGE
        }
        eventAction = "click save on add product page"
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        eventAction,
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseProductClickSearchbar(shopId: String, shopType: String, isActionEdit: Boolean) {
        page = if(isActionEdit) {
            EDIT_SHOWCASE_PAGE
        } else {
            ADD_SHOWCASE_PAGE
        }
        eventAction = "click search product"
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        eventAction,
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseProductCardClick(shopId: String, shopType: String, isActionEdit: Boolean) {
        page = if(isActionEdit) {
            EDIT_SHOWCASE_PAGE
        } else {
            ADD_SHOWCASE_PAGE
        }
        eventAction = "click product"
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        eventAction,
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun onFinishCreateOrUpdateShowcase(shopId: String, shopType: String, isSuccess: Boolean = false) {
        eventAction = if(isSuccess) {
            CLICK_FINISH_SUCCESS
        } else {
            CLICK_FINISH_ERROR
        }
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ADD_SHOWCASE_PAGE,
                        eventAction,
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseClickChooseProductText(shopId: String, shopType: String, isActionEdit: Boolean) {
        page = if(isActionEdit) {
            EDIT_SHOWCASE_PAGE
        } else {
            ADD_SHOWCASE_PAGE
        }
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        "click add product on edit product page",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    fun addShowcaseClickDeleteButtonProductCard(shopId: String, shopType: String, isActionEdit: Boolean) {
        page = if(isActionEdit) {
            EDIT_SHOWCASE_PAGE
        } else {
            ADD_SHOWCASE_PAGE
        }
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        page,
                        "click remove product",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 19
    fun clickBackButton(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click back",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 20
    fun clickSusun(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click susun",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 21
    fun clickSearchBar(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click search etalase",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 22
    fun clickTambahEtalase(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click add etalase",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 23
    fun clickEtalase(shopId: String, shopType: String, showcaseName: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click etalase $showcaseName",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 24
    fun clickDots(shopId: String, shopType: String, showcaseName: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click dots - etalase $showcaseName",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 25
    fun clickEditMenu(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click edit",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 26
    fun clickDeleteMenu(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click delete",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 27
    fun clickHapusDialog(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click hapus",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

    // No 28
    fun clickBatalDialog(shopId: String, shopType: String) {
        tracker.sendEnhanceEcommerceEvent(
                getDataLayer(
                        CLICK_ETALASE,
                        ETALASE_SETTING_PAGE,
                        "click cancel",
                        "",
                        shopId,
                        shopType,
                        PAGE_TYPE_VALUE
                )
        )
    }

}