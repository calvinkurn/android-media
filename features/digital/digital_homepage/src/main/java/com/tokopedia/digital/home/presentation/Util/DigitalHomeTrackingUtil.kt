package com.tokopedia.digital.home.presentation.Util

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.CREATIVE
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.CREATIVE_URL
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.ECOMMERCE
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.ID
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.NAME
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.POSITION
import com.tokopedia.digital.home.presentation.Util.DigitaHomepageTrackingEEConstant.PROMOTIONS
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BANNER_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BANNER_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_IMPRESSION
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingCategoryConstant.DIGITAL_HOMEPAGE_CATEGORY
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingEventNameConstant.CLICK_HOMEPAGE
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingEventNameConstant.PROMO_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingEventNameConstant.PROMO_VIEW
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.FAVOURITE_NUMBER
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.HELP
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.LANGGANAN
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingLabelConstant.ORDER_LIST
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class DigitalHomeTrackingUtil {

    @Inject
    constructor()

    fun eventBannerImpression(item: DigitalHomePageBannerModel.Banner?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, item?.title,
                POSITION, position,
                ID, item?.id,
                CREATIVE, item?.title,
                CREATIVE_URL, item?.filename
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item?.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun eventBannerClick(item: DigitalHomePageBannerModel.Banner?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, item?.title,
                POSITION, position,
                ID, item?.id,
                CREATIVE, item?.title,
                CREATIVE_URL, item?.filename
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, BANNER_CLICK,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item?.title}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))

    }

    fun eventCategoryImpression(items: List<DigitalHomePageCategoryModel.Submenu?>) {
        val products = mutableListOf<Any>()
        for ((index, item) in items.withIndex()) {
            products.add(DataLayer.mapOf(
                    NAME, item?.name,
                    POSITION, index,
                    ID, item?.id,
                    CREATIVE, item?.name,
                    CREATIVE_URL, item?.icon
            ))
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_VIEW,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, DYNAMIC_ICON_IMPRESSION,
                        TrackAppUtils.EVENT_LABEL, "",
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, products))
                ))
    }

    fun eventCategoryClick(item: DigitalHomePageCategoryModel.Submenu?, position: Int) {
        val products = mutableListOf<Any>()
        products.add(DataLayer.mapOf(
                NAME, item?.name,
                POSITION, position,
                ID, item?.id,
                CREATIVE, item?.name,
                CREATIVE_URL, item?.icon
        ))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, DIGITAL_HOMEPAGE_CATEGORY,
                        TrackAppUtils.EVENT_ACTION, DYNAMIC_ICON_CLICK,
                        TrackAppUtils.EVENT_LABEL, "$position - ${item?.name}",
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, products))
                ))

    }

    fun eventClickOrderList(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, ORDER_LIST)
    }

    fun eventClickLangganan(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, LANGGANAN)
    }

    fun eventClickHelp(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, HELP)
    }

    fun eventClickFavNumber(){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, DIGITAL_HOMEPAGE_CATEGORY, DYNAMIC_ICON_CLICK, FAVOURITE_NUMBER)
    }

}