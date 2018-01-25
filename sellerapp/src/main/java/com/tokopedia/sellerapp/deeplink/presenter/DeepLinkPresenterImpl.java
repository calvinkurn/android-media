package com.tokopedia.sellerapp.deeplink.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkConst;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.listener.DeepLinkView;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupNewPromoActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public class DeepLinkPresenterImpl implements DeepLinkPresenter {

    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final int OTHER = 7;
    private static final int TOPADS = 12;
    private static final int PELUANG = 13;
    public static final String PARAM_AD_ID = "ad_id";
    public static final String PARAM_ITEM_ID = "item_id";
    public static final String TOPADS_VIEW_TYPE = "view";
    public static final String TOPADS_CREATE_TYPE = "create";

    private final Activity context;
    private final DeepLinkView viewListener;
    private static final String TAG = "DeepLinkPresenterImpl";

    public DeepLinkPresenterImpl(DeepLinkActivity activity){
        this.viewListener = activity;
        this.context = activity;
    }

    @Override
    public void processDeepLinkAction(Uri uriData) {

        String screenName;
        int type = getDeepLinkType(uriData);
        switch (type) {
            case TOPADS:
                openTopAds(uriData);
                screenName = AppScreen.SCREEN_TOPADS;
                break;
            case PELUANG:
                screenName = AppScreen.SCREEN_TX_SHOP_TRANSACTION_SELLING_LIST;
                openPeluangPage();
                break;
            case OTHER:
                prepareOpenWebView(uriData);
                screenName = AppScreen.SCREEN_DEEP_LINK;
                break;
            default:
                prepareOpenWebView(uriData);
                screenName = AppScreen.SCREEN_DEEP_LINK;
                break;
        }
        sendCampaignGTM(uriData.toString(), screenName);

    }

    private void prepareOpenWebView(Uri uriData) {
        String url = encodeUrl(uriData.toString());
        openWebView(Uri.parse(url));
    }

    private void openWebView(Uri encodedUri){
        Fragment fragment = FragmentGeneralWebView.createInstance(getUrl(encodedUri.toString()));
        viewListener.inflateFragment(fragment, "WEB_VIEW");
    }

    private String encodeUrl(String url) {
        String encodedUrl;
        try {
            encodedUrl = URLEncoder.encode(url, FORMAT_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return encodedUrl;
    }

    private String getUrl(String data) {
        Log.d(TAG, "getUrl: " + URLGenerator.generateURLSessionLoginV4(data, context));
        return URLGenerator.generateURLSessionLoginV4(data, context);
    }

    private int getDeepLinkType(Uri uriData) {
        List<String> linkSegment = uriData.getPathSegments();

        try {
            if (isTopAds(linkSegment))
                return TOPADS;
            else if (isPeluang(linkSegment))
                return PELUANG;
            else if (isExcludedHostUrl(uriData))
                return OTHER;
            else if (isExcludedUrl(uriData))
                return OTHER;
            else return OTHER;
        } catch (Exception e) {
            e.printStackTrace();
            return OTHER;
        }
    }

    private boolean isTopAds(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equals(DeeplinkConst.URL.TOPADS);
    }

    private boolean isPeluang(List<String> linkSegment) {
        return linkSegment.size() > 0 && (
                linkSegment.get(0).equals("peluang") || linkSegment.get(0).equals("peluang.pl")
        );
    }

    @Override
    public void processAFlistener() {

    }

    @Override
    public void sendCampaignGTM(String campaignUri, String screenName) {
        if (!DeeplinkUTMUtils.isValidCampaignUrl(Uri.parse(campaignUri))) {
            return;
        }
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(Uri.parse(campaignUri));
        campaign.setScreenName(screenName);
        UnifyTracking.eventCampaign(campaign);
        UnifyTracking.eventCampaign(campaignUri);
    }

    @Override
    public boolean isLandingPageWebView(Uri uri) {
        int type = getDeepLinkType(uri);
        switch (type) {
            case TOPADS :
                return false;
            case PELUANG :
                return false;
            case OTHER:
                return true;
            default:
                return true;
        }
    }

    @Override
    public void checkUriLogin(Uri uriData) {

    }

    private boolean isExcludedUrl(Uri uriData) {
        if (!TextUtils.isEmpty(TrackingUtils.getGtmString(AppEventTracking.GTM.EXCLUDED_URL))) {
            List<String> listExcludedString = Arrays.asList(TrackingUtils.getGtmString(AppEventTracking.GTM.EXCLUDED_URL).split(","));
            for (String excludedString : listExcludedString) {
                if (uriData.getPath().endsWith(excludedString)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void openTopAds(Uri uriData) {

        String type = uriData. getQueryParameter(DeeplinkConst.PARAM.TYPE);
        Intent intentToLaunch = null;
        if (!SessionHandler.isUserHasShop(context)) {
            intentToLaunch = new Intent(context, SplashScreenActivity.class);
            intentToLaunch.setData(uriData);
        }
        else if (TextUtils.isEmpty(type)){
            intentToLaunch = new Intent(context, TopAdsDashboardActivity.class);
            intentToLaunch.setData(uriData);
        }
        else if(TOPADS_VIEW_TYPE.equals(type)){
            String adId = uriData.getQueryParameter(PARAM_AD_ID);
            intentToLaunch = new Intent(context, TopAdsDetailProductActivity.class);
            intentToLaunch.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, adId);
            intentToLaunch.setData(uriData);
        } else if (TOPADS_CREATE_TYPE.equals(type)){
            String itemId = uriData.getQueryParameter(PARAM_ITEM_ID);
            intentToLaunch = new Intent(context, TopAdsGroupNewPromoActivity.class);
            intentToLaunch.putExtra(TopAdsExtraConstant.EXTRA_ITEM_ID, itemId);
            intentToLaunch.setData(uriData);
        }
        context.startActivity(intentToLaunch);
        context.finish();

    }

    private boolean isExcludedHostUrl(Uri uriData) {
        if (!TextUtils.isEmpty(TrackingUtils.getGtmString(AppEventTracking.GTM.EXCLUDED_HOST))) {
            List<String> listExcludedString = Arrays.asList(TrackingUtils.getGtmString(AppEventTracking.GTM.EXCLUDED_HOST).split(","));
            for (String excludedString : listExcludedString) {
                if (uriData.getPath().startsWith(excludedString)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void openPeluangPage() {
        Intent intent = SellerRouter.getActivitySellingTransactionOpportunity(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }
}
