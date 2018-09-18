package com.tokopedia.browse.common.util;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel;

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

    public void eventPromoImpressionPopularBrand() {

    }

    public void eventPromoClickPopularBrand() {

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

}
