package com.tokopedia.loyalty.view.util;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.track.TrackApp;

import java.util.List;

public class PromoTrackingUtil {
    String CLICK_PROMO_MICRO_SITE = "clickPromoMicrosite";
    String PROMO_MICROSITE_PROMO_LIST = "promo microsite - promo list";
    String PROMO_CLICK_CATEGORY = "user click on category";
    String EVENT_TOKO_POINT = "eventTokopoint";
    String TOKOPOINTS_POP_UP = "tokopoints - pop up";
    String TOKOPOINTS_POP_UP_IMPRESSION = "impression on any pop up";
    String TOKOPOINTS_POP_UP_CLICK = "click any pop up button";
    String LABEL_TOKOPOINTS_POP_UP = "pop up";
    String TOKOPOINTS_POP_UP_BUTTON = "pop up button";
    String PROMO_CLICK_COPY_PROMO_CODE = "user click salin kode";
    String PROMO_MICROSITE_PROMO_TOOLTIP = "promo microsite - promo tooltip";
    String PROMO_CLICK_OPEN_TOOLTIP = "user click on tooltip";
    String PROMO_CLICK_CLOSE_TOOLTIP = "user click tutup";
    String PROMO_CLICK_SUB_CATEGORY = "user click on subcategory";

    private LoyaltyModuleRouter loyaltyModuleRouter;

    public PromoTrackingUtil(LoyaltyModuleRouter loyaltyModuleRouter) {
        this.loyaltyModuleRouter = loyaltyModuleRouter;
    }


    public void eventPromoListClickCategory(Context context, String title) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(CLICK_PROMO_MICRO_SITE, PROMO_MICROSITE_PROMO_LIST, PROMO_CLICK_CATEGORY, title);
    }

    public void eventViewTokopointPopup(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_TOKO_POINT, TOKOPOINTS_POP_UP, TOKOPOINTS_POP_UP_IMPRESSION, LABEL_TOKOPOINTS_POP_UP);
    }

    public void eventClickTokoPointPopup(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_TOKO_POINT, TOKOPOINTS_POP_UP, TOKOPOINTS_POP_UP_CLICK, TOKOPOINTS_POP_UP_BUTTON);
    }

    public void eventImpressionPromoList(Context context, List<Object> dataLayerSinglePromoCodeList, String title) {
        eventImpressionPromoList(dataLayerSinglePromoCodeList, title);
    }

    public void eventImpressionPromoList(List<Object> list, String promoName) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", "promoView",
                        "eventCategory", "promo microsite - promo list",
                        "eventAction", "impression on promo",
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public void eventClickPromoListItem(Context context,List<Object> dataLayerSinglePromoCodeList, String title) {
        eventClickPromoListItem(dataLayerSinglePromoCodeList, title);
    }

    public void eventClickPromoListItem(List<Object> list, String promoName) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        "event", "promoView",
                        "eventCategory", "promo microsite - promo list",
                        "eventAction", "impression on promo",
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public void eventPromoListClickCopyToClipboardPromoCode(Context context,String promoName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_LIST,
                PROMO_CLICK_COPY_PROMO_CODE,
                promoName);
    }

    public void eventPromoTooltipClickOpenTooltip(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_TOOLTIP,
                PROMO_CLICK_OPEN_TOOLTIP,
                "");
    }

    public void eventPromoTooltipClickCloseTooltip(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_TOOLTIP,
                PROMO_CLICK_CLOSE_TOOLTIP,"");
    }

    public void eventPromoListClickSubCategory(Context context,String subCategoryName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_LIST,
                PROMO_CLICK_SUB_CATEGORY,
                subCategoryName);
    }
}
