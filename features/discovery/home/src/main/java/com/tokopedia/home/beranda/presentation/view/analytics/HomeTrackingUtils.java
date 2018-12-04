package com.tokopedia.home.beranda.presentation.view.analytics;

import android.content.Context;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationProductViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ashwanityagi on 19/03/18.
 */

public class HomeTrackingUtils {

    private static String SLIDINGBANNER ="sliding baaner";
    private static String HOMERECOMMENDATION ="Home page recommendation";
    private static String PRODUCTDETAILSSCREEN ="Product details screen";
    private static String DISCOVERYWIDGET ="Discovery Widget";

    public static void homeSlidingBannerImpression(Context context,
                                                   BannerSlidesModel bannerSlidesModel,
                                                   int position) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.BANNER_CREATIVE, bannerSlidesModel.getCreativeName());
        map.put(FirebaseParams.Home.BANNER_NAME, bannerSlidesModel.getTitle());
        map.put(FirebaseParams.Home.BANNER_LIST_NAME, SLIDINGBANNER);
        map.put(FirebaseParams.Home.BANNER_POSITION, position);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_IMPRESSION, map);
    }

    public static void homeSlidingBannerClick(Context context,
                                              BannerSlidesModel bannerSlidesModel,
                                              int position) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.BANNER_CREATIVE, bannerSlidesModel.getCreativeName());
        map.put(FirebaseParams.Home.BANNER_NAME, bannerSlidesModel.getTitle());
        map.put(FirebaseParams.Home.BANNER_LIST_NAME, SLIDINGBANNER);
        map.put(FirebaseParams.Home.BANNER_POSITION, position);
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, bannerSlidesModel.getApplink());
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_CLICK, map);
    }

    public static void homeViewAllPromotions(Context context, String landingScreenName) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreenName);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_SLIDING_BANNER_VIEW_ALL_CLICK, map);
    }


    public static void homeUsedCaseImpression(Context context, List<LayoutSections> sectionList) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < sectionList.size(); i++) {
            map.put(FirebaseParams.Home.ICON_NAME + "_" + i + 1, sectionList.get(i).getTitle());
            map.put(FirebaseParams.Home.ICON_POSITION + "_" + i + 1, i + 1);
        }

        sendEventToAnalytics(context, FirebaseEvent.Home.HOMEPAGE_USED_CASE_IMPRESSION, map);
    }

    public static void homeUsedCaseClick(Context context,
                                         String iconName,
                                         int iconPosition,
                                         String landingScreenName) {
        Map<String, Object> map = new HashMap<>();

        map.put(FirebaseParams.Home.ICON_NAME, iconName);
        map.put(FirebaseParams.Home.ICON_POSITION, iconPosition);
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreenName);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_USED_CASE_CLICK, map);
    }


    public static void homeSprintSaleImpression(Context context,
                                                DynamicHomeChannel.Grid[] grids ,
                                                String category) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < grids.length; i++) {
            map.put(FirebaseParams.Home.PRODUCT_ID,grids[i].getId());
            map.put(FirebaseParams.Home.PRODUCT_NAME, grids[i].getLabel());
            map.put(FirebaseParams.Home.PRODUCT_CATEGORY, category);
        }
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_IMPRESSION, map);
    }


    public static void homeSprintSaleViewAll(Context context,
                                             String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_VIEW_ALL_CLICK, map);
    }


    public static void homeSprintSaleClick(Context context,
                                           int position,
                                           DynamicHomeChannel.Channels channel,
                                           DynamicHomeChannel.Grid grid,
                                           String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.PRODUCT_ID, grid.getId());
        map.put(FirebaseParams.Home.PRODUCT_NAME, grid.getLabel());
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_SPRINT_SALE_CLICK + "_" + position, map);
    }


    public static void homeDiscoveryWidgetImpression(Context context,
                                                     int position,
                                                     DynamicHomeChannel.Channels channel) {
        Map<String, Object> map = new HashMap<>();
        DynamicHomeChannel.Grid[] grids = channel.getGrids();
        for (int i = 0; i < grids.length; i++) {
            map.put(FirebaseParams.Home.PRODUCT_ID + "_" + i, grids[i].getId());
            map.put(FirebaseParams.Home.PRODUCT_NAME+ "_" + i, grids[i].getName());
        }

        map.put(FirebaseParams.Home.PRODUCT_LIST_NAME, channel.getPromoName());
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, channel.getType());
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_IMPRESSION + "_" + position,
                map);
    }


    public static void homeDiscoveryWidgetClick(Context context,
                                                int position,
                                                DynamicHomeChannel.Grid grid,
                                                String landingScreen,
                                                String category) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.PRODUCT_ID, grid.getId());
        map.put(FirebaseParams.Home.PRODUCT_NAME, grid.getLabel());
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, category);
        map.put(FirebaseParams.Home.PRODUCT_LIST_NAME, DISCOVERYWIDGET);
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_CLICK + "_" + position,
                map);
    }


    public static void homeDiscoveryWidgetViewAll(Context context,
                                                  String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_DISCOVERY_WIDGET_VIEW_ALL_CLICK,
                map);
    }


    public static void homeNavTopHomeClick(Context context, String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_TOP_NAV_HOME_CLICKED, map);
    }


    public static void homeNavTopHotlistClick(Context context, String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_TOP_NAV_HOTLIST_CLICKED, map);
    }


    public static void homeNavTopFeedClick(Context context,
                                           String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_TOP_NAV_FEED_CLICKED, map);
    }


    public static void homeNavTopFavoriteClick(Context context,
                                               String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context,
                FirebaseEvent.Home.HOMEPAGE_TOP_NAV_FAVORIT_CLICKED, map);
    }

    public static void cartIconClicked(Context context,
                                       String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context, FirebaseEvent.Home.CART_ICON_CLICKED, map);
    }

    public static void homeSearchIconClicked(Context context,
                                             String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context, FirebaseEvent.Home.HOMEPAGE_SEARCH_ICON_CLICKED, map);
    }

    public static void homepageRecommedationClicked(Context context,
                                                    InspirationProductViewModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.PRODUCT_ID, model.getProductId());
        map.put(FirebaseParams.Home.PRODUCT_NAME, model.getName());
        map.put(FirebaseParams.Home.PRODUCT_LIST_NAME, HOMERECOMMENDATION);
        map.put(FirebaseParams.Home.PRODUCT_CATEGORY, model.getRecommedationType());
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, PRODUCTDETAILSSCREEN);
        sendEventToAnalytics(context, FirebaseEvent.Home.HOMEPAGE_RECOMMEDATION_CLICKED, map);
    }

    public static void homepageHamburgerClick(Context context) {
        Map<String, Object> map = new HashMap<>();
        sendEventToAnalytics(context, FirebaseEvent.Home.HOMEPAGE_HAMBURGER_CLICK, map);
    }


    public static void sendEventToAnalytics(Context context, String eventName, Map<String, Object> data) {
        TrackAnalytics.sendEvent(eventName, data, context);
    }

}
