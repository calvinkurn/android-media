package com.tokopedia.loyalty.view.util;

import android.content.Context;

import com.tokopedia.loyalty.router.LoyaltyModuleRouter;

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
        loyaltyModuleRouter.sendEventTracking(CLICK_PROMO_MICRO_SITE, PROMO_MICROSITE_PROMO_LIST, PROMO_CLICK_CATEGORY, title);
    }

    public void eventViewTokopointPopup(Context context) {
        loyaltyModuleRouter.sendEventTracking(EVENT_TOKO_POINT, TOKOPOINTS_POP_UP, TOKOPOINTS_POP_UP_IMPRESSION, LABEL_TOKOPOINTS_POP_UP);
    }

    public void eventClickTokoPointPopup(Context context) {
        loyaltyModuleRouter.sendEventTracking(EVENT_TOKO_POINT, TOKOPOINTS_POP_UP, TOKOPOINTS_POP_UP_CLICK, TOKOPOINTS_POP_UP_BUTTON);
    }

    public void eventImpressionPromoList(Context context, List<Object> dataLayerSinglePromoCodeList, String title) {
        loyaltyModuleRouter.sendEventImpressionPromoList(dataLayerSinglePromoCodeList, title);
    }

    public void eventClickPromoListItem(Context context,List<Object> dataLayerSinglePromoCodeList, String title) {
        loyaltyModuleRouter.eventClickPromoListItem(dataLayerSinglePromoCodeList, title);
    }

    public void eventPromoListClickCopyToClipboardPromoCode(Context context,String promoName) {
        loyaltyModuleRouter.sendEventTracking(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_LIST,
                PROMO_CLICK_COPY_PROMO_CODE,
                promoName);
    }

    public void eventPromoTooltipClickOpenTooltip(Context context) {
        loyaltyModuleRouter.sendEventTracking(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_TOOLTIP,
                PROMO_CLICK_OPEN_TOOLTIP,
                "");
    }

    public void eventPromoTooltipClickCloseTooltip(Context context) {
        loyaltyModuleRouter.sendEventTracking(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_TOOLTIP,
                PROMO_CLICK_CLOSE_TOOLTIP,"");
    }

    public void eventPromoListClickSubCategory(Context context,String subCategoryName) {
        loyaltyModuleRouter.sendEventTracking(CLICK_PROMO_MICRO_SITE,
                PROMO_MICROSITE_PROMO_LIST,
                PROMO_CLICK_SUB_CATEGORY,
                subCategoryName);
    }
}
