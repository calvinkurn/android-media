package com.tokopedia.topupbills.common.analytics

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.prepaid.model.DigitalTrackProductTelco
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils


/**
 * @author by resakemal on 05/07/19.
 */

class DigitalTopupAnalytics {

    fun eventInputNumberManual(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_MANUAL_NUMBER,
                "${getTrackingCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventInputNumberContactPicker(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_CONTACT,
                "${getTrackingCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventClickOnContactPickerHomepage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_ON_CONTACT,
                ""
        ))
    }

    //not yet
    fun eventInputNumberWidget(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_WIDGET,
                "${getTrackingCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventInputNumberFavorites(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
                "${getTrackingCategoryName(categoryId)} - $operatorName"
        ))
    }

    fun eventClearInputNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLEAR_INPUT_NUMBER,
                ""
        ))
    }

    fun eventClickBackButton(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_BACK_BUTTON,
                getTrackingCategoryName(categoryId)
        ))
    }

    fun eventClickSeeMore(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_SEE_MORE,
                getTrackingCategoryName(categoryId)
        ))
    }

    fun eventCloseDetailProduct(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLOSE_DETAIL_PRODUCT,
                getTrackingCategoryName(categoryId)
        ))
    }

    fun eventClickCopyPromoCode(promoName: String, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.COPY_PROMO_DIGITAL,
                "$promoName - $position"
        ))
    }

    fun eventClickTelcoPrepaidCategory(categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_TELCO_CATEGORY,
                "Prabayar - $categoryName"
        ))
    }

    fun eventClickCheckEnquiry(categoryId: Int, operatorName: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_CHECK_TAGIHAN,
                "${getTrackingCategoryName(categoryId)} - $operatorName")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickTabMenuTelco(categoryId: Int, userId: String, action: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                action,
                "${getTrackingCategoryName(categoryId)}")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickDotsMenuTelco(categoryId: String, userId: String) {
        var category = getTrackingCategoryName(categoryId.toIntOrNull() ?: 0)
        if (categoryId.isEmpty()) category = categoryId

        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_DOTS_MENU,
                category)
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickQuickFilter(categoryId: Int, filterCluster: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_QUICK_FILTER,
                "${getTrackingCategoryName(categoryId)} - ${filterCluster.toLowerCase()}")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickSaveFilter(categoryId: Int, filterCluster: String, valuesFilter: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_SAVE_QUICK_FILTER,
                "${getTrackingCategoryName(categoryId)} - ${filterCluster.toLowerCase()} - $valuesFilter")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickResetFilter(categoryId: Int, filterCluster: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_RESET_QUICK_FILTER,
                "${getTrackingCategoryName(categoryId)} - ${filterCluster.toLowerCase()}")
        sendGeneralEvent(mapEvent, userId)
    }

    fun eventClickResetFilterCluster(categoryId: Int, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_RESET_FILTER_CLUSTER,
                "${getTrackingCategoryName(categoryId)}")
        sendGeneralEvent(mapEvent, userId)
    }

    private fun sendGeneralEvent(mapEvent: MutableMap<String, Any>, userId: String) {
        mapEvent[DigitalTopupEventTracking.Additional.BUSINESS_UNIT] =
                DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE
        mapEvent[DigitalTopupEventTracking.Additional.CURRENT_SITE] =
                DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE
        mapEvent[DigitalTopupEventTracking.Additional.USER_ID] = userId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun impressionFilterCluster(categoryId: Int, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.VIEW_HOMEPAGE_IRIS,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.IMPRESSION_FILTER_CLUSTER,
                getCategoryName(categoryId))
        sendGeneralEvent(mapEvent, userId)
    }

    fun impressionEnhanceCommerceProduct(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>, operatorName: String,
                                         userId: String) {
        val productTelcoList = ArrayList<Bundle>()
        val productListName = ArrayList<String>()

        for (item in digitalTrackProductTelcoList) {
            productListName.add(item.itemProduct.attributes.desc)
        }

        for (element in digitalTrackProductTelcoList) {
            productTelcoList.add(Bundle().apply {
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_NAME, element.itemProduct.attributes.desc)
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_ID, element.itemProduct.id)
                putInt(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_PRICE, element.itemProduct.attributes.pricePlain)
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_BRAND, "none / others")
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_CATEGORY, getTrackingCategoryName(element.itemProduct.attributes.categoryId))
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_VARIANT, "none / others")
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_DIMENSION, "${getTrackingCategoryName(element.itemProduct.attributes.categoryId)} - " +
                        "product ${element.position} - ${element.itemProduct.attributes.desc}")
                putInt(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_INDEX, element.position)
            })
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.VIEW_ITEM_LIST)
            putString(TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE)
            putString(TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.PRODUCT_CARD_IMPRESSION)
            putString(TrackAppUtils.EVENT_LABEL, "${getTrackingCategoryName(digitalTrackProductTelcoList[0].itemProduct.attributes.categoryId)} - $operatorName")
            putString(DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.USER_ID, userId)
            putString(DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.VALUE_ITEM_LIST, productListName.toString())
            putParcelableArrayList(DigitalTopupEventTracking.Additional.VALUE_ITEMS, productTelcoList)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalTopupEventTracking.Event.VIEW_ITEM_LIST, eventDataLayer)
    }

    fun clickEnhanceCommerceProduct(itemProduct: TelcoProduct, position: Int,
                                    operatorName: String, userId: String, itemList: String) {
        val productTelcoList = ArrayList<Bundle>()
        productTelcoList.add(Bundle().apply {
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_NAME, itemProduct.attributes.desc)
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_ID, itemProduct.id)
            putInt(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_PRICE, itemProduct.attributes.pricePlain)
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_BRAND, operatorName)
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_CATEGORY, getTrackingCategoryName(itemProduct.attributes.categoryId))
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_VARIANT, "none / others")
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_DIMENSION, "${itemProduct.attributes.desc}")
            putInt(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_INDEX, position)
        })

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.SELECT_CONTENT)
            putString(TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE)
            putString(TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.CLICK_PRODUCT_CARD)
            putString(TrackAppUtils.EVENT_LABEL, "${getTrackingCategoryName(itemProduct.attributes.categoryId)} - $operatorName - ${itemProduct.attributes.desc}")
            putString(DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.USER_ID, userId)
            putString(DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.VALUE_ITEM_LIST, itemList)
            putParcelableArrayList(DigitalTopupEventTracking.Additional.VALUE_ITEMS, productTelcoList)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalTopupEventTracking.Event.SELECT_CONTENT, eventDataLayer)
    }

    fun pickProductDetail(itemProduct: TelcoProduct,
                          operatorName: String, userId: String) {
        val productTelcoList = ArrayList<Bundle>()
        productTelcoList.add(Bundle().apply {
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_NAME, itemProduct.attributes.desc)
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_ID, itemProduct.id)
            putInt(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_PRICE, itemProduct.attributes.pricePlain)
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_BRAND, operatorName)
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_CATEGORY, getTrackingCategoryName(itemProduct.attributes.categoryId))
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_VARIANT, "none / others")
            putInt(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_QUANTITY, 1)
        })

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.ADD_TO_CART)
            putString(TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE)
            putString(TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.CLICK_DETAIL_CLUSTER)
            putString(TrackAppUtils.EVENT_LABEL, "${getTrackingCategoryName(itemProduct.attributes.categoryId)} - $operatorName - ${itemProduct.attributes.desc}")
            putString(DigitalTopupEventTracking.Additional.SCREEN_NAME, getTrackingCategoryName(itemProduct.attributes.categoryId))
            putString(DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.USER_ID, userId)
            putString(DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE)
            putParcelableArrayList(DigitalTopupEventTracking.Additional.VALUE_ITEMS, productTelcoList)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalTopupEventTracking.Event.ADD_TO_CART, eventDataLayer)
    }

    fun impressionEnhanceCommerceRecentTransaction(topupBillsTrackRecent: List<TopupBillsTrackRecentTransaction>) {
        val recentList = ArrayList<Any>()
        for (element in topupBillsTrackRecent) {
            recentList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, element.itemRecent.clientNumber,
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.PRICE, "0",
                    DigitalTopupEventTracking.EnhanceEccomerce.BRAND, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getTrackingCategoryName(element.categoryId),
                    DigitalTopupEventTracking.EnhanceEccomerce.LIST, "category ${getTrackingCategoryName(element.categoryId)} - " +
                    "product ${element.position} - " +
                    "${element.itemRecent.clientNumber} - ${element.itemRecent.title}",
                    DigitalTopupEventTracking.EnhanceEccomerce.POSITION, element.position))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_VIEW,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.RECENT_ICON_IMPRESSION,
                        "eventLabel", "none",
                        "ecommerce", DataLayer.mapOf(
                        "impressions", DataLayer.listOf(
                        *recentList.toTypedArray()
                )
                )
                )
        )

    }

    fun clickEnhanceCommerceRecentTransaction(itemRecent: TopupBillsRecommendation,
                                              operatorName: String, position: Int) {
        val recentList = ArrayList<Any>()
        recentList.add(DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, itemRecent.clientNumber,
                DigitalTopupEventTracking.EnhanceEccomerce.ID, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.PRICE, "0",
                DigitalTopupEventTracking.EnhanceEccomerce.BRAND, operatorName,
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, getTrackingCategoryName(itemRecent.categoryId),
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PRODUCT_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_RECENT_ICON,
                        "eventLabel", "${getTrackingCategoryName(itemRecent.categoryId)} - $position",
                        "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                        "list", "category id ${itemRecent.categoryId} - " +
                        "product $position - " +
                        "${itemRecent.clientNumber} - ${itemRecent.title}"
                ),
                        "products", recentList.toArray()
                )
                )
                )
        )
    }

    fun impressionEnhanceCommercePromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {
        val promoList = ArrayList<Any>()
        for (i in 0 until topupBillsTrackPromoList.size) {
            val promo = topupBillsTrackPromoList[i]
            promoList.add(DataLayer.mapOf(
                    DigitalTopupEventTracking.EnhanceEccomerce.ID, promo.promoItem.id,
                    DigitalTopupEventTracking.EnhanceEccomerce.NAME, "/deals-popular suggestion",
                    DigitalTopupEventTracking.EnhanceEccomerce.POSITION, promo.position,
                    DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE, promo.promoItem.title,
                    DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, "none",
                    DigitalTopupEventTracking.EnhanceEccomerce.PROMO_ID, promo.promoItem.id,
                    DigitalTopupEventTracking.EnhanceEccomerce.PROMO_CODE, promo.promoItem.promoCode))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PROMO_VIEW,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.PROMO_DIGITAL_IMPRESSION,
                        "eventLabel", "none",
                        "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", promoList.toArray()
                )
                )
                )
        )

    }

    fun clickEnhanceCommercePromo(topupBillsPromo: TopupBillsPromo, position: Int) {
        val promoList = ArrayList<Any>()
        promoList.add(DataLayer.mapOf(
                DigitalTopupEventTracking.EnhanceEccomerce.ID, topupBillsPromo.id,
                DigitalTopupEventTracking.EnhanceEccomerce.NAME, "/deals-popular suggestion",
                DigitalTopupEventTracking.EnhanceEccomerce.POSITION, position,
                DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE, topupBillsPromo.title,
                DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE_URL, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.CATEGORY, "none",
                DigitalTopupEventTracking.EnhanceEccomerce.PROMO_ID, topupBillsPromo.id,
                DigitalTopupEventTracking.EnhanceEccomerce.PROMO_CODE, topupBillsPromo.promoCode))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", DigitalTopupEventTracking.Event.PROMO_CLICK,
                        "eventCategory", DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        "eventAction", DigitalTopupEventTracking.Action.CLICK_PROMO_DIGITAL,
                        "eventLabel", "${topupBillsPromo.promoCode} - $position",
                        "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", promoList.toArray()
                )
                )
                )
        )
    }

    fun impressionViewMccmProduct(mccmProducts: List<DigitalTrackProductTelco>, operatorName: String, userId: String) {
        val mccmTrackingProductList = mccmProducts.map {
            Bundle().apply {
                putString(DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE_NAME, "${getTrackingCategoryName(it.itemProduct.attributes.categoryId)} - $operatorName")
                putString(DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE_SLOT, it.position.toString())
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_ID, it.itemProduct.id)
                putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_NAME, it.itemProduct.attributes.desc)
            }
        }

        var categoryId = 0
        if (mccmProducts.isNotEmpty()) categoryId = mccmProducts.first().itemProduct.attributes.categoryId

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.VIEW_ITEM)
            putString(TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.VIEW_PROMO_CARD)
            putString(TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE)
            putString(TrackAppUtils.EVENT_LABEL, "${getTrackingCategoryName(categoryId)} - $operatorName")
            putString(DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.USER_ID, userId)
            putParcelableArrayList(DigitalTopupEventTracking.Additional.VALUE_PROMOTIONS, ArrayList(mccmTrackingProductList))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalTopupEventTracking.Event.VIEW_ITEM, eventDataLayer)
    }

    fun clickOnMccmProduct(itemProduct: TelcoProduct, operatorName: String, position: Int, userId: String) {
        val categoryName = getTrackingCategoryName(itemProduct.attributes.categoryId)
        val product = Bundle().apply {
            putString(DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE_NAME, "$categoryName - $operatorName")
            putString(DigitalTopupEventTracking.EnhanceEccomerce.CREATIVE_SLOT, position.toString())
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_ID, itemProduct.id)
            putString(DigitalTopupEventTracking.EnhanceEccomerce.PARAM_ITEM_NAME, itemProduct.attributes.desc)
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, DigitalTopupEventTracking.Event.SELECT_CONTENT)
            putString(TrackAppUtils.EVENT_ACTION, DigitalTopupEventTracking.Action.CLICK_PROMO_CARD)
            putString(TrackAppUtils.EVENT_CATEGORY, DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE)
            putString(TrackAppUtils.EVENT_LABEL, "$categoryName - $operatorName")
            putString(DigitalTopupEventTracking.Additional.BUSINESS_UNIT, DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.CURRENT_SITE, DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE)
            putString(DigitalTopupEventTracking.Additional.USER_ID, userId)
            putParcelableArrayList(DigitalTopupEventTracking.Additional.VALUE_PROMOTIONS, arrayListOf(product))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DigitalTopupEventTracking.Event.SELECT_CONTENT, eventDataLayer)
    }

    fun clickSeeMoreOnMccmProductItem(itemProduct: TelcoProduct, operatorName: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(TrackAppUtils.EVENT to DigitalTopupEventTracking.Event.DIGITAL_GENERAL_EVENT,
                        TrackAppUtils.EVENT_ACTION to DigitalTopupEventTracking.Action.CLICK_DETAIL_IN_PROMO_CARD,
                        TrackAppUtils.EVENT_CATEGORY to DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                        TrackAppUtils.EVENT_LABEL to "${getTrackingCategoryName(itemProduct.attributes.categoryId)} - $operatorName",
                        DigitalTopupEventTracking.Additional.BUSINESS_UNIT to DigitalTopupEventTracking.Additional.BUSINESS_UNIT_RECHARGE,
                        DigitalTopupEventTracking.Additional.CURRENT_SITE to DigitalTopupEventTracking.Additional.CURRENT_SITE_DIGITAL_RECHARGE,
                        DigitalTopupEventTracking.Additional.USER_ID to userId,
                )
        )
    }

    private fun getTrackingCategoryName(categoryId: Int): String {
        return getCategoryName(categoryId).toLowerCase()
    }

    fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA
            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA
            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING
            else -> TelcoComponentName.PRODUCT_PASCABAYAR
        }
    }
}
