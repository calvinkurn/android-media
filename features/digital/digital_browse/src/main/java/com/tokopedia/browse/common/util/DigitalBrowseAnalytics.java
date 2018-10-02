package com.tokopedia.browse.common.util;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.browse.common.data.DigitalBrowsePopularAnalyticsModel;
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.browse.common.constant.DigitalBrowseEventTracking.Action;
import static com.tokopedia.browse.common.constant.DigitalBrowseEventTracking.Event;

/**
 * @author by furqan on 30/08/18.
 */

public class DigitalBrowseAnalytics {

    private String GENERIC_CATEGORY = "homepage";

    private AnalyticTracker analyticTracker;

    @Inject
    public DigitalBrowseAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventClickBackOnBelanjaPage() {
        analyticTracker.sendEventTracking(
                Event.CLICK_BACK,
                GENERIC_CATEGORY,
                Action.CLICK_BACK_BELANJA,
                "");
    }

    public void eventClickViewAllOnBelanjaPage() {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_VIEW_ALL_BELANJA,
                "");
    }

    public void eventPromoImpressionPopularBrand(List<DigitalBrowsePopularAnalyticsModel> promotionDatas) {
        try {
            List<Object> promotions = new ArrayList<>();

            for (DigitalBrowsePopularAnalyticsModel promotionItem : promotionDatas) {
                Object promotion = tranformPromotionModel(promotionItem);

                promotions.add(promotion);
            }

            analyticTracker.sendEnhancedEcommerce(
                    DataLayer.mapOf(
                            "event", Event.IMPRESSION_PROMO,
                            "eventCategory", GENERIC_CATEGORY,
                            "eventAction", Action.IMPRESSION_BRAND_BELANJA,
                            "eventLabel", "",
                            "ecommerce", DataLayer.mapOf(
                                    "promoView", DataLayer.mapOf(
                                            "promotions", DataLayer.listOf(promotions.toArray()))
                            )
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eventPromoClickPopularBrand(DigitalBrowsePopularAnalyticsModel promotionItem) {
        try {
            Object promotion = tranformPromotionModel(promotionItem);

            List<Object> promotions = new ArrayList<>();
            promotions.add(promotion);

            analyticTracker.sendEnhancedEcommerce(
                    DataLayer.mapOf(
                            "event", Event.CLICK_PROMO,
                            "eventCategory", GENERIC_CATEGORY,
                            "eventAction", Action.CLICK_BRAND_BELANJA,
                            "eventLabel", promotionItem.getBrandName(),
                            "ecommerce", DataLayer.mapOf(
                                    "promoClick", DataLayer.mapOf(
                                            "promotions", DataLayer.listOf(promotions.toArray()))
                            )
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eventImpressionHomePage(String iconName, int iconPosition) {
        analyticTracker.sendEventTracking(
                Event.IMPRESSION_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.IMPRESSION_CATEGORY_BELANJA,
                iconName + "_" + iconPosition);
    }

    public void eventClickOnCategoryBelanja(String iconName, int iconPosition) {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_CATEGORY_BELANJA,
                iconName + "_" + iconPosition);
    }

    public void eventClickBackOnLayananPage() {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_BACK_LAYANAN,
                "");
    }

    public void eventClickHeaderTabLayanan(String tabName) {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                Action.CLICK_TAB_LAYANAN,
                tabName);
    }

    public void eventImpressionIconLayanan(DigitalBrowseServiceAnalyticsModel analyticsModel) {
        analyticTracker.sendEventTracking(
                Event.IMPRESSION_HOME_PAGE,
                GENERIC_CATEGORY,
                String.format(Action.IMPRESSION_ICON_LAYANAN, analyticsModel.getHeaderName()),
                analyticsModel.getIconName() + "_" + analyticsModel.getHeaderPosition()
                        + "_" + analyticsModel.getIconPosition());
    }

    public void eventClickIconLayanan(DigitalBrowseServiceAnalyticsModel analyticsModel) {
        analyticTracker.sendEventTracking(
                Event.CLICK_HOME_PAGE,
                GENERIC_CATEGORY,
                String.format(Action.CLICK_ICON_LAYANAN, analyticsModel.getHeaderName()),
                analyticsModel.getIconName() + "_" + analyticsModel.getHeaderPosition()
                        + "_" + analyticsModel.getIconPosition());
    }

    private Object tranformPromotionModel(DigitalBrowsePopularAnalyticsModel promotionItem) {
        return DataLayer.mapOf(
                "id", Long.toString(promotionItem.getBannerId()),
                "name", "/belanja - Brand Pilihan",
                "creative", promotionItem.getBrandName(),
                "position", Integer.toString(promotionItem.getPosition()));
    }

}
