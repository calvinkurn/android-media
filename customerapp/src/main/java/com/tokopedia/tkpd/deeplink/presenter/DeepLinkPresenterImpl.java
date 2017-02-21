package com.tokopedia.tkpd.deeplink.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.fragment.FragmentShopPreview;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.product.fragment.ProductDetailFragment;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.RechargeRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.session.session.interactor.SignInInteractor;
import com.tokopedia.session.session.interactor.SignInInteractorImpl;
import com.tokopedia.session.session.presenter.Login;
import com.tokopedia.tkpd.IConsumerModuleRouter;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 *         modified by Alvarisi
 */
public class DeepLinkPresenterImpl implements DeepLinkPresenter {
    private static final String TAG = DeepLinkPresenterImpl.class.getSimpleName();
    private static final int HOMEPAGE = 0;
    private static final int BROWSE = 1;
    private static final int HOT = 2;
    private static final int CATALOG = 3;
    private static final int PRODUCT = 4;
    private static final int SHOP = 5;
    private static final int ACCOUNTS = 6;
    private static final int OTHER = 7;
    private static final int INVOICE = 8;
    private static final int RECHARGE = 9;
    private static final String AF_ONELINK_HOST = "tokopedia.onelink.me";
    private static final String DL_TOKOPEDIA_HOST = "apps.tokopedia.com";
    private static final String DF_TOKOPEDIA_HOST = "tokopedia.com";
    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";
    private final Activity context;
    private final DeepLinkView viewListener;
    SignInInteractor interactor;

    public DeepLinkPresenterImpl(DeepLinkActivity activity) {
        this.viewListener = activity;
        this.context = activity;
        this.interactor = SignInInteractorImpl.createInstance(activity);
    }

    @Override
    public boolean isLandingPageWebView(Uri uri) {
        int type = getDeepLinkType(uri);
        switch (type){
            case HOMEPAGE:
                return false;
            case BROWSE:
                return false;
            case HOT:
                return false;
            case CATALOG:
                return false;
            case PRODUCT:
                return false;
            case SHOP:
                return false;
            case ACCOUNTS:
                return true;
            case OTHER:
                return true;
            case INVOICE:
                return false;
            case RECHARGE:
                return false;
            default:
                return true;
        }
    }

    @Override
    public void checkUriLogin(Uri uriData) {
        if(getDeepLinkType(uriData) == ACCOUNTS && uriData.getPath().contains("activation")){
            if(!SessionHandler.isV4Login(context)){
                login(uriData);
            }
        }
    }

    public void processDeepLinkAction(Uri uriData) {
        if (uriData.getHost().equals(AF_ONELINK_HOST)) {
            Log.d(TAG, "URI DATA = " + uriData.toString());
            processAFlistener();
        } else {
            List<String> linkSegment = uriData.getPathSegments();
            String screenName;
            int type = getDeepLinkType(uriData);
            CommonUtils.dumper("FCM wvlogin deeplink type "+type);
            switch (type) {
                case HOMEPAGE:
                    screenName = AppScreen.SCREEN_INDEX_HOME;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openHomepage();
                    break;
                case BROWSE:
                    openBrowseProduct(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_BROWSE_PRODUCT;
                    break;
                case HOT:
                    screenName = AppScreen.SCREEN_BROWSE_HOT_LIST;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openHotProduct(linkSegment, uriData);
                    break;
                case CATALOG:
                    openCatalogProduct(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_CATALOG;
                    break;
                case PRODUCT:
                    openDetailProduct(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_PRODUCT_INFO;
                    break;
                case SHOP:
                    openShopInfo(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_SHOP_INFO;
                    break;
                case ACCOUNTS:
                    if(!uriData.getPath().contains("activation")) {
                        openWebView(uriData);
                    }else {
                        context.finish();
                    }
                    screenName = AppScreen.SCREEN_LOGIN;
                    break;
                case OTHER:
                    openWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
                case INVOICE:
                    openInvoice(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_DOWNLOAD_INVOICE;
                    break;
                case RECHARGE:
                    openRecharge(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_RECHARGE;
                    break;
                default:
                    openWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
            }
            sendCampaignGTM(uriData.toString(), screenName);
        }
    }

    private void login(Uri uriData) {
        interactor.handleAccounts(parseUriData(uriData), new SignInInteractor.SignInListener() {
            @Override
            public void onSuccess(AccountsModel result) {
                finishLogin();
            }

            @Override
            public void onError(String error) {
                finishLogin();
            }

            @Override
            public void moveToSecurityQuestion(SecurityModel securityModel) {
                finishLogin();
            }

            @Override
            public void moveToCreatePassword(InfoModel infoModel) {
                finishLogin();
            }
        });
    }

    private void finishLogin() {
        Intent intent = HomeRouter.getHomeActivity(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        context.finish();
    }

    private AccountsParameter parseUriData(Uri uriData) {
        AccountsParameter data = new AccountsParameter();
        data.setEmail(" ");
        data.setPassword(uriData.getPathSegments().get(1));
        data.setAttempt(uriData.getQueryParameter("a"));
        data.setGrantType(Login.GRANT_PASSWORD);
        data.setPasswordType(SignInInteractor.ACTIVATION_CODE);
        return data;
    }


    private void openInvoice(List<String> linkSegment, Uri uriData) {
        AppUtils.InvoiceDialogDeeplink(context, uriData.toString(), uriData.getQueryParameter("pdf"));
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

    @Override
    public void sendCampaignGTM(String campaignUri, String screenName) {
        if (!isValidCampaignUrl(Uri.parse(campaignUri))) {
            return;
        }
        Campaign campaign = convertUrlCampaign(Uri.parse(campaignUri));
        campaign.setScreenName(screenName);
        UnifyTracking.eventCampaign(campaign);
        UnifyTracking.eventCampaign(campaignUri);
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
        return maps.containsKey("utm_source") && maps.containsKey("utm_medium") &&
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

    private void openWebView(Uri uriData) {
        CommonUtils.dumper("wvlogin URL links "+getUrl(uriData.toString()));
        String url = getUrl(uriData.toString());
        Fragment fragment = FragmentGeneralWebView.createInstance(url);
        viewListener.inflateFragment(fragment, "WEB_VIEW");
    }

    private String getUrl(String data) {
        Log.d(TAG, "getUrl: " + URLGenerator.generateURLSessionLoginV4(data, context));
        return URLGenerator.generateURLSessionLoginV4(data, context);
    }

    private void openShopInfo(List<String> linkSegment, Uri uriData) {
        Fragment fragment = FragmentShopPreview.createInstanceForDeeplink(linkSegment.get(0), uriData.toString());
        viewListener.inflateFragment(fragment, "SHOP_INFO");
    }

    private void openDetailProduct(List<String> linkSegment, Uri uriData) {
        CommonUtils.dumper("wvlogin opened product");
        Fragment fragment = ProductDetailFragment.newInstanceForDeeplink(ProductPass.Builder.aProductPass()
                .setProductKey(linkSegment.get(1))
                .setShopDomain(linkSegment.get(0))
                .setProductUri(uriData.toString())
                .build());
        viewListener.inflateFragment(fragment, "DETAIL_PRODUCT");
    }

    private void openRecharge(List<String> linkSegment, Uri uriData) {
        Bundle bundle = new Bundle();
        if (isValidCampaignUrl(uriData)) {
            Map<String, String> maps = splitQuery(uriData);
            if (maps.get("utm_source") != null) {
                bundle.putString(RechargeRouter.ARG_UTM_SOURCE, maps.get("utm_source"));
            }
            if (maps.get("utm_medium") != null) {
                bundle.putString(RechargeRouter.ARG_UTM_MEDIUM, maps.get("utm_medium"));
            }
            if (maps.get("utm_campaign") != null) {
                bundle.putString(RechargeRouter.ARG_UTM_CAMPAIGN, maps.get("utm_campaign"));
            }
            if (maps.get("utm_content") != null) {
                bundle.putString(RechargeRouter.ARG_UTM_CONTENT, maps.get("utm_content"));
            }
        }
        bundle.putBoolean(RechargeRouter.EXTRA_ALLOW_ERROR, true);
//        RechargeCategoryFragment fragment = RechargeCategoryFragment.newInstance(bundle);
//        viewListener.inflateFragmentV4(RechargeRouter.getRechargeCategoryFragment(context), "RECHARGE");
        viewListener.inflateFragmentV4(((IConsumerModuleRouter)this.context.getApplication()).getRechargeCategoryFragment(),
                "RECHARGE");
    }


    private void openCatalogProduct(List<String> linkSegment, Uri uriData) {
        viewListener.inflateFragment(DetailProductRouter
                .getCatalogDetailListFragment(context, linkSegment.get(1)), "CATALOG_PRODUCT");
    }

    private void openHotProduct(List<String> linkSegment, Uri uriData) {
        Bundle bundle = new Bundle();
        if (isHotBrowse(linkSegment, uriData)) {
            return;
        } else if (isHotAlias(uriData)) {
            bundle.putString(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS, uriData.getQueryParameter("alk"));
        } else if (isHotLink(linkSegment)) {
            bundle.putString(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS, linkSegment.get(1));
        } else return;

        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, "0");
        bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_HOTLIST);
        bundle.putString(BrowseProductRouter.EXTRA_SOURCE,
                BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT);

        Intent intent = BrowseProductRouter.getDefaultBrowseIntent(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.finish();
    }

    private void openHomepage() {
        Intent intent = new Intent(context, HomeRouter.getHomeActivityClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }

    private void openBrowseProduct(List<String> linkSegment, Uri uriData) {
        Bundle bundle = new Bundle();
        String departmentId = "0";
        String searchQuery = "";
        String source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
        if (isSearch(linkSegment)) {
            departmentId = uriData.getQueryParameter("sc");
            searchQuery = uriData.getQueryParameter("q");
            source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
            bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
            bundle.putBoolean(IS_DEEP_LINK_SEARCH, true);
        } else if (isCategory(linkSegment)) {
            String iden = linkSegment.get(1);
            for (int i = 2; i < linkSegment.size(); i++) {
                iden = iden + "_" + linkSegment.get(i);
            }
            CategoryDB dep =
                    DbManagerImpl.getInstance().getCategoryDb(iden);
            if (dep != null) {
                departmentId = dep.getDepartmentId() + "";
                bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
            }
            bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
            source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY;
        }

        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
        bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_HOTLIST);
        bundle.putString(BrowseProductRouter.EXTRAS_SEARCH_TERM, searchQuery);
        bundle.putString(BrowseProductRouter.EXTRA_SOURCE, source);
        Intent intent = BrowseProductRouter.getDefaultBrowseIntent(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.finish();
    }


    private int getDeepLinkType(Uri uriData) {
        List<String> linkSegment = uriData.getPathSegments();
        if (uriData.toString().contains("accounts.tokopedia.com"))
            return ACCOUNTS;
        try {
            if (isExcludedHostUrl(uriData))
                return OTHER;
            else if (isExcludedUrl(uriData))
                return OTHER;
            else if (isInvoice(linkSegment))
                return INVOICE;
            else if (isHomepage(linkSegment))
                return HOMEPAGE;
            else if (isBrowse(linkSegment))
                return BROWSE;
            else if (isHot(linkSegment))
                return HOT;
            else if (isCatalog(linkSegment))
                return CATALOG;
            else if (isProduct(linkSegment))
                return PRODUCT;
            else if (isPulsa(linkSegment))
                return RECHARGE;
            else if (isShop(linkSegment))
                return SHOP;
            else return OTHER;
        } catch (Exception e) {
            e.printStackTrace();
            return OTHER;
        }
    }

    private boolean isHotLink(List<String> linkSegment) {
        return (linkSegment.size() == 2);
    }

    private boolean isHotBrowse(List<String> linkSegment, Uri uriData) {
        return (linkSegment.size() == 1 && !isHotAlias(uriData));
    }

    private boolean isHotAlias(Uri uri) {
        return uri.getQueryParameter("alk") != null;
    }

    private boolean isCategory(List<String> linkSegment) {
        return linkSegment.get(0).equals("p");
    }

    private boolean isSearch(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equals("search");
    }

    private boolean isPulsa(List<String> linkSegment) {
        return linkSegment.size() == 1 && linkSegment.get(0).equals("pulsa") ;
    }

    private boolean isInvoice(List<String> linkSegment) {
        return linkSegment.size() == 1 && linkSegment.get(0).startsWith("invoice.pl") ;
    }

    private boolean isShop(List<String> linkSegment) {
        return (linkSegment.size() > 0
                && !linkSegment.get(0).equals("pulsa")
                && !linkSegment.get(0).equals("iklan")
                && !linkSegment.get(0).equals("newemail.pl")
                && !linkSegment.get(0).equals("search")
                && !linkSegment.get(0).equals("hot")
                && !linkSegment.get(0).equals("about")
                && !linkSegment.get(0).equals("reset.pl")
                && !linkSegment.get(0).equals("activation.pl")
                && !linkSegment.get(0).equals("privacy.pl")
                && !linkSegment.get(0).equals("terms.pl")
                && !linkSegment.get(0).startsWith("invoice.pl"));

    }

    private boolean isProduct(List<String> linkSegment) {
        return (linkSegment.size() == 2
                && !isBrowse(linkSegment)
                && !isHot(linkSegment)
                && !isCatalog(linkSegment)
                && !linkSegment.get(0).equals("pulsa"));
    }

    private boolean isCatalog(List<String> linkSegment) {
        return linkSegment.size() > 0  && linkSegment.get(0).equals("catalog");
    }

    private boolean isHot(List<String> linkSegment) {
        return linkSegment.size() > 0 &&  linkSegment.get(0).equals("hot");
    }

    private boolean isBrowse(List<String> linkSegment) {
        return linkSegment.size() > 0 && (
                linkSegment.get(0).equals("search") || linkSegment.get(0).equals("p")
        );
    }
    private boolean isHomepage(List<String> linkSegment) {
        return linkSegment.size() == 0;
    }

    @Override
    public void processAFlistener() {
        AppsFlyerLib.getInstance().registerConversionListener(context, new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {

            }

            @Override
            public void onInstallConversionFailure(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                if (map.size() > 0) {
                    if (map.get("link") != null) {
                        String oriUri = map.get("link");
                        processDeepLinkAction(simplifyUrl(oriUri));
                    }
                }
            }

            @Override
            public void onAttributionFailure(String s) {

            }
        });
    }
}
