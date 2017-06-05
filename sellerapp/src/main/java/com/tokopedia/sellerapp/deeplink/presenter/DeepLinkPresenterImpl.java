package com.tokopedia.sellerapp.deeplink.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.activity.TopAdsDashboardActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupNewPromoActivity;
import com.tokopedia.sellerapp.SplashScreenActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkActivity;
import com.tokopedia.sellerapp.deeplink.listener.DeepLinkView;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Herdi_WORK on 10.05.17.
 */

public class DeepLinkPresenterImpl implements DeepLinkPresenter {

    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final int OTHER = 7;
    private static final int TOPADS = 12;
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
        CommonUtils.dumper("wvlogin URL links " + getUrl(uriData.toString()));
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
        return linkSegment.size() > 0 && linkSegment.get(0).equals("topads");
    }

    @Override
    public void processAFlistener() {

    }

    @Override
    public void sendCampaignGTM(String campaignUri, String screenName) {

    }

    @Override
    public boolean isLandingPageWebView(Uri uri) {
        int type = getDeepLinkType(uri);
        switch (type) {
            case TOPADS :
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

    private Uri simplifyUrl(String url) {
        Uri afUri = Uri.parse(url);
        String newUri = afUri.getQueryParameter("af_dp");
        Map<String, String> maps = splitQuery(afUri);
        for (Map.Entry<String, String> imap : maps.entrySet()) {
            switch (imap.getKey()) {
                case "utm_source":
                    newUri += "&utm_source=" + imap.getValue();
                    break;
                case "utm_medium":
                    newUri += "&utm_medium=" + imap.getValue();
                    break;
                case "utm_term":
                    newUri += "&utm_term=" + imap.getValue();
                    break;
                case "utm_content":
                    newUri += "&utm_content=" + imap.getValue();
                    break;
                case "utm_campaign":
                    newUri += "&utm_campaign=" + imap.getValue();
                    break;
                case "gclid":
                    newUri += "&gclid=" + imap.getValue();
                    break;
            }
        }
        return Uri.parse(newUri);
    }

    private Map<String, String> splitQuery(Uri url) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        queryPairs.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryPairs;
    }

    private boolean isValidCampaignUrl(Uri uri) {
        Map<String, String> maps = splitQuery(uri);
        return maps.containsKey("utm_source") &&
                maps.containsKey("utm_medium") &&
                maps.containsKey("utm_campaign");
    }

    private Campaign convertUrlCampaign(Uri uri) {
        Map<String, String> maps = splitQuery(uri);
        Campaign campaign = new Campaign();
        campaign.setUtmSource(maps.get("utm_source") != null ?
                maps.get("utm_source") : "");
        campaign.setUtmMedium(maps.get("utm_medium") != null ?
                maps.get("utm_medium") : "");
        campaign.setUtmCampaign(maps.get("utm_campaign") != null ?
                maps.get("utm_campaign") : "");
        campaign.setUtmContent(maps.get("utm_content") != null ?
                maps.get("utm_content") : "");
        campaign.setUtmTerm(maps.get("utm_term") != null ?
                maps.get("utm_term") : "");
        campaign.setGclid(maps.get("gclid") != null ?
                maps.get("gclid") : "");
        return campaign;
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

        String type = uriData. getQueryParameter("type");
        Intent intentToLaunch = null;
        String shopId = SessionHandler.getShopID(context);
        if (TextUtils.isEmpty(shopId)) {
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

}
