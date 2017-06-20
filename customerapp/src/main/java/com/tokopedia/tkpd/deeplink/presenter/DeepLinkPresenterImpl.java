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
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.fragment.FragmentShopPreview;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.RechargeRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.session.session.interactor.SignInInteractor;
import com.tokopedia.session.session.interactor.SignInInteractorImpl;
import com.tokopedia.session.session.presenter.Login;
import com.tokopedia.tkpd.IConsumerModuleRouter;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
    private static final String FORMAT_UTF_8 = "UTF-8";
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
    private static final int APPLINK = 10;
    private static final int CATEGORY = 11;
    private static final int PROMO = 12;
    private static final String AF_ONELINK_HOST = "tokopedia.onelink.me";
    private static final String DL_TOKOPEDIA_HOST = "apps.tokopedia.com";
    private static final String DF_TOKOPEDIA_HOST = "tokopedia.com";
    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";
    private static final String TOKOPEDIA_HOST = "tokopedia";
    private static final String OVERRIDE_URL = "override_url";
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
        switch (type) {
            case HOMEPAGE:
                return false;
            case CATEGORY:
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
                return true;
            default:
                return true;
        }
    }

    @Override
    public void checkUriLogin(Uri uriData) {
        if (getDeepLinkType(uriData) == ACCOUNTS && uriData.getPath().contains("activation")) {
            if (!SessionHandler.isV4Login(context)) {
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
            CommonUtils.dumper("FCM wvlogin deeplink type " + type);
            switch (type) {
                case HOMEPAGE:
                    screenName = AppScreen.SCREEN_INDEX_HOME;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openHomepage();
                    break;
                case CATEGORY:
                    openCategory(linkSegment);
                    screenName = AppScreen.SCREEN_BROWSE_PRODUCT;
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
                    if (!uriData.getPath().contains("activation")) {
                        prepareOpenWebView(uriData);
                    } else {
                        context.finish();
                    }
                    screenName = AppScreen.SCREEN_LOGIN;
                    break;
                case OTHER:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
                case INVOICE:
                    openInvoice(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_DOWNLOAD_INVOICE;
                    break;
                /*
                case RECHARGE:
                    openRecharge(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_RECHARGE;
                    break;
                   */
                case APPLINK:
                    if (linkSegment != null && linkSegment.size() > 0) {
                        openWebView(Uri.parse(String.valueOf(linkSegment.get(0))), false);
                        screenName = AppScreen.SCREEN_WEBVIEW;
                    } else {
                        return;
                    }
                    break;
                default:
                    prepareOpenWebView(uriData);
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
                Log.d(TAG, "onSuccess: ");
                if (SessionHandler.isMsisdnVerified()) {
                    finishLogin();
                } else {
                    Intent intentHome = HomeRouter.getHomeActivity(context);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Intent intentPhoneVerif = SessionRouter.getPhoneVerificationActivationActivityIntent(context);

                    context.startActivities(new Intent[]
                            {
                                    intentHome,
                                    intentPhoneVerif
                            });
                    context.finish();
                }
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onError: " + error);
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

    private void prepareOpenWebView(Uri uriData) {
        String url = encodeUrl(uriData.toString());
        if (uriData.getQueryParameter(OVERRIDE_URL) != null) {
            openWebView(Uri.parse(url), uriData.getQueryParameter(OVERRIDE_URL).equalsIgnoreCase("1"));
        } else {
            openWebView(Uri.parse(url), false);
        }
    }

    private static boolean isPromo(List<String> linkSegment) {
        return linkSegment.size() > 0 && (linkSegment.get(0).equals("promo"));
    }

    private void openWebView(Uri encodedUri, boolean allowingOverriding) {
        Fragment fragment = FragmentGeneralWebView.createInstance(encodedUri.toString(), allowingOverriding);
        viewListener.inflateFragment(fragment, "WEB_VIEW");
    }

    private String getUrl(String data) {
        Log.d(TAG, "getUrl: " + URLGenerator.generateURLSessionLoginV4(data, context));
        return URLGenerator.generateURLSessionLoginV4(data, context);
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

    private void openShopInfo(List<String> linkSegment, Uri uriData) {
        Fragment fragment = FragmentShopPreview.createInstanceForDeeplink(linkSegment.get(0), uriData.toString());
        viewListener.inflateFragment(fragment, "SHOP_INFO");
    }

    private void openDetailProduct(List<String> linkSegment, Uri uriData) {
        CommonUtils.dumper("wvlogin opened product");
        Fragment fragment = ProductDetailRouter.instanceDeeplink(
                context,
                ProductPass.Builder.aProductPass()
                        .setProductKey(linkSegment.get(1))
                        .setShopDomain(linkSegment.get(0))
                        .setProductUri(uriData.toString())
                        .build());
        viewListener.inflateFragment(fragment, "DETAIL_PRODUCT");
        viewListener.hideActionBar();
    }

    private void openRecharge(List<String> linkSegment, Uri uriData) {
        Bundle bundle = new Bundle();
        if (DeeplinkUTMUtils.isValidCampaignUrl(uriData)) {
            Map<String, String> maps = DeeplinkUTMUtils.splitQuery(uriData);
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
        viewListener.inflateFragmentV4(((IConsumerModuleRouter) this.context.getApplication()).getRechargeCategoryFragment(),
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

    private void openCategory(List<String> linkSegment) {
        String departmentId = "0";
        String iden = linkSegment.get(1);
        CategoryDB dep =
                DbManagerImpl.getInstance().getCategoryDb(iden);
        if (dep != null) {
            departmentId = dep.getDepartmentId() + "";
        }
        IntermediaryActivity.moveTo(
                context,
                departmentId
        );
        context.finish();
    }


    private void openBrowseProduct(List<String> linkSegment, Uri uriData) {
        Bundle bundle = new Bundle();
        String departmentId = "0";
        String searchQuery = "";
        departmentId = uriData.getQueryParameter("sc");
        searchQuery = uriData.getQueryParameter("q");
        String source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
        bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        bundle.putBoolean(IS_DEEP_LINK_SEARCH, true);

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
        else if (uriData.getScheme().equals(TOKOPEDIA_HOST))
            return APPLINK;

        try {
            if (isExcludedHostUrl(uriData))
                return OTHER;
            else if (isExcludedUrl(uriData))
                return OTHER;
            else if (isPromo(linkSegment))
                return PROMO;
            else if (isInvoice(linkSegment))
                return INVOICE;
            else if (isHomepage(linkSegment))
                return HOMEPAGE;
            else if (isCategory(linkSegment))
                return CATEGORY;
            else if (isBrowse(linkSegment))
                return BROWSE;
            else if (isHot(linkSegment))
                return HOT;
            else if (isCatalog(linkSegment))
                return CATALOG;
            else if (isProduct(linkSegment))
                return PRODUCT;
            else if (isPulsa(linkSegment))
                return OTHER;
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


    private boolean isSearch(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equals("search");
    }

    private boolean isPulsa(List<String> linkSegment) {
        return linkSegment.size() == 1 && linkSegment.get(0).equals("pulsa");
    }

    private boolean isInvoice(List<String> linkSegment) {
        return linkSegment.size() == 1 && linkSegment.get(0).startsWith("invoice.pl");
    }

    private boolean isShop(List<String> linkSegment) {
        return (linkSegment.size() == 1
                && !linkSegment.get(0).equals("pulsa")
                && !linkSegment.get(0).equals("iklan")
                && !linkSegment.get(0).equals("newemail.pl")
                && !linkSegment.get(0).equals("search")
                && !linkSegment.get(0).equals("hot")
                && !linkSegment.get(0).equals("about")
                && !linkSegment.get(0).equals("kartu-kredit")
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
        return linkSegment.size() > 0 && linkSegment.get(0).equals("catalog");
    }

    private boolean isHot(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equals("hot");
    }

    private boolean isBrowse(List<String> linkSegment) {
        return linkSegment.size() > 0 && (
                linkSegment.get(0).equals("search")
        );
    }

    private boolean isCategory(List<String> linkSegment) {
        return linkSegment.size() > 0 && (
                linkSegment.get(0).equals("p")
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
                        processDeepLinkAction(DeeplinkUTMUtils.simplifyUrl(oriUri));
                    }
                }
            }

            @Override
            public void onAttributionFailure(String s) {

            }
        });
    }
}
