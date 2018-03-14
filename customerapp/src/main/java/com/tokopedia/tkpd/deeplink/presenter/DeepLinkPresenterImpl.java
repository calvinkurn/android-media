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
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.referral.ReferralActivity;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryActivity;
import com.tokopedia.session.domain.interactor.SignInInteractor;
import com.tokopedia.session.domain.interactor.SignInInteractorImpl;
import com.tokopedia.tkpd.deeplink.WhitelistItem;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.di.component.DaggerDeeplinkComponent;
import com.tokopedia.tkpd.deeplink.di.component.DeeplinkComponent;
import com.tokopedia.tkpd.deeplink.domain.GetShopInfoUseCase;
import com.tokopedia.tkpd.deeplink.domain.interactor.MapUrlUseCase;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.home.ReactNativeDiscoveryActivity;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;


/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 *         modified by Alvarisi
 */
public class DeepLinkPresenterImpl implements DeepLinkPresenter {

    private static final String TAG = DeepLinkPresenterImpl.class.getSimpleName();

    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String AF_ONELINK_HOST = "tokopedia.onelink.me";
    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";
    private static final String OVERRIDE_URL = "override_url";


    private final Activity context;
    private final DeepLinkView viewListener;
    private SignInInteractor interactor;
    private MapUrlUseCase mapUrlUseCase;

    @Inject
    GetShopInfoUseCase getShopInfoUseCase;

    public DeepLinkPresenterImpl(DeepLinkActivity activity, MapUrlUseCase mapUrlUseCase) {
        this.viewListener = activity;
        this.context = activity;
        this.interactor = SignInInteractorImpl.createInstance(activity);
        this.mapUrlUseCase = mapUrlUseCase;

        initInjection(activity);
    }

    @Override
    public boolean isLandingPageWebView(Uri uri) {
        int type = DeepLinkChecker.getDeepLinkType(uri.toString());
        switch (type) {
            case DeepLinkChecker.HOME:
                return false;
            case DeepLinkChecker.CATEGORY:
                return false;
            case DeepLinkChecker.BROWSE:
                return false;
            case DeepLinkChecker.HOT:
            case DeepLinkChecker.HOT_LIST:
                return false;
            case DeepLinkChecker.CATALOG:
                return false;
            case DeepLinkChecker.DISCOVERY_PAGE:
                return false;
            case DeepLinkChecker.PRODUCT:
                return false;
            case DeepLinkChecker.SHOP:
                return false;
            case DeepLinkChecker.ACCOUNTS:
                return true;
            case DeepLinkChecker.OTHER:
                return true;
            case DeepLinkChecker.INVOICE:
                return false;
            case DeepLinkChecker.RECHARGE:
                return true;
            case DeepLinkChecker.PELUANG:
                return false;
            case DeepLinkChecker.REFERRAL:
                return false;
            default:
                return true;
        }
    }

    @Override
    public void checkUriLogin(Uri uriData) {
        if (DeepLinkChecker.getDeepLinkType(uriData.toString()) == DeepLinkChecker.ACCOUNTS && uriData.getPath().contains("activation")) {
            if (!SessionHandler.isV4Login(context)) {
                login(uriData);
            }
        }
    }

    @Override
    public void actionGotUrlFromApplink(Uri uriData) {
        prepareOpenWebView(uriData);
    }

    private void initInjection(DeepLinkActivity activity) {
        DeeplinkComponent component = DaggerDeeplinkComponent.builder()
                .appComponent(activity.getApplicationComponent())
                .build();
        component.inject(this);
    }

    @Override
    public void mapUrlToApplink(Uri uri) {
        final List<String> linkSegments = uri.getPathSegments();
        StringBuilder finalSegments = new StringBuilder();
        for (int i = 0; i < linkSegments.size(); i++) {
            if (i != linkSegments.size() - 1) {
                finalSegments.append(linkSegments.get(i)).append("/");
            } else {
                finalSegments.append(linkSegments.get(i));
            }
        }
        mapUrlUseCase.execute(mapUrlUseCase.setRequestParam(finalSegments.toString()), mapUrlToApplinkSubscriber(uri));
    }

    private Subscriber<WhitelistItem> mapUrlToApplinkSubscriber(final Uri uriData) {
        return new Subscriber<WhitelistItem>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WhitelistItem deeplink) {
                if (TextUtils.isEmpty(deeplink.applink)) {
                    viewListener.initDeepLink();
                } else {
                    String screenName = AppScreen.SCREEN_NATIVE_RECHARGE;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openDigitalPage(deeplink.applink);
                }
            }
        };
    }

    public void processDeepLinkAction(Uri uriData) {
        if (uriData.getHost().equals(AF_ONELINK_HOST)) {
            Log.d(TAG, "URI DATA = " + uriData.toString());
            processAFlistener();
        } else {
            List<String> linkSegment = uriData.getPathSegments();
            String screenName;
            int type = DeepLinkChecker.getDeepLinkType(uriData.toString());
            CommonUtils.dumper("FCM wvlogin deeplink type " + type);
            switch (type) {
                case DeepLinkChecker.HOME:
                    screenName = AppScreen.SCREEN_INDEX_HOME;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openHomepage();
                    break;
                case DeepLinkChecker.CATEGORY:
                    openCategory(uriData.toString());
                    screenName = AppScreen.SCREEN_BROWSE_PRODUCT;
                    break;
                case DeepLinkChecker.BROWSE:
                    openBrowseProduct(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_BROWSE_PRODUCT;
                    break;
                case DeepLinkChecker.HOT:
                    screenName = AppScreen.SCREEN_BROWSE_HOT_LIST;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openHotProduct(linkSegment, uriData);
                    break;
                case DeepLinkChecker.HOT_LIST:
                    screenName = AppScreen.SCREEN_HOME_HOTLIST;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openHomepageHot();
                    break;
                case DeepLinkChecker.CATALOG:
                    openCatalogDetail(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_CATALOG;
                    break;
                case DeepLinkChecker.DISCOVERY_PAGE:
                    openDiscoveryPage(uriData.toString());
                    screenName = AppScreen.SCREEN_DISCOVERY_PAGE;
                    break;
                case DeepLinkChecker.PRODUCT:
                    openDetailProduct(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_PRODUCT_INFO;
                    break;
                case DeepLinkChecker.SHOP:
                    openShopInfo(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_SHOP_INFO;
                    break;
                case DeepLinkChecker.ACCOUNTS:
                    if (!uriData.getPath().contains("activation")) {
                        prepareOpenWebView(uriData);
                    } else {
                        context.finish();
                    }
                    screenName = AppScreen.SCREEN_LOGIN;
                    break;
                case DeepLinkChecker.OTHER:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
                case DeepLinkChecker.INVOICE:
                    openInvoice(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_DOWNLOAD_INVOICE;
                    break;
                /*
                case RECHARGE:
                    openRecharge(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_RECHARGE;
                    break;
                   */
                case DeepLinkChecker.APPLINK:
                    if (linkSegment != null && linkSegment.size() > 0) {
                        openWebView(Uri.parse(String.valueOf(linkSegment.get(0))), false);
                        screenName = AppScreen.SCREEN_WEBVIEW;
                    } else {
                        return;
                    }
                    break;
                case DeepLinkChecker.PELUANG:
                    screenName = AppScreen.SCREEN_INDEX_HOME;
                    sendCampaignGTM(uriData.toString(), screenName);
                    openPeluangPage(uriData.getPathSegments(), uriData);
                    break;
                case DeepLinkChecker.REFERRAL:
                    screenName = AppScreen.SCREEN_REFERRAL;
                    openReferralScreen(uriData);
                    break;
                case DeepLinkChecker.GROUPCHAT:
                    openGroupChat(linkSegment);
                    screenName = AppScreen.GROUP_CHAT;
                    break;
                default:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
            }
            sendCampaignGTM(uriData.toString(), screenName);
        }
    }

    private void openGroupChat(List<String> linkSegment) {
        int SEGMENT_GROUPCHAT = 2;
        Intent intent;
        if (linkSegment.size() == SEGMENT_GROUPCHAT) {
            intent = ((TkpdCoreRouter) context).getGroupChatIntent(
                    context, linkSegment.get(1));
        } else {
            intent = ((TkpdCoreRouter) context).getInboxChannelsIntent(
                    context);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }

    private void openDigitalPage(String applink) {
        if (context.getApplication() instanceof IDigitalModuleRouter) {
            if (((IDigitalModuleRouter) context.getApplication())
                    .isSupportedDelegateDeepLink(applink)) {
                Bundle bundle = new Bundle();
                ((IDigitalModuleRouter) context.getApplication()).actionNavigateByApplinksUrl(context,
                        applink, bundle);
                context.finish();
            }
        }
    }

    private void openPeluangPage(List<String> linkSegment, Uri uriData) {
        String query = uriData.getQueryParameter("q");
        Intent intent = SellerRouter.getActivitySellingTransactionOpportunity(context, query);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }

    private void login(Uri uriData) {
        interactor.handleAccounts(parseUriData(uriData), new SignInInteractor.SignInListener() {
            @Override
            public void onSuccess(AccountsModel result) {
                Log.d(TAG, "onSuccess: ");
                if (SessionHandler.isMsisdnVerified()) {
                    finishLogin();
                } else if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
                    Intent intentHome = HomeRouter.getHomeActivity(context);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Intent intentPhoneVerif = ((TkpdCoreRouter) MainApplication.getAppContext())
                            .getPhoneVerificationActivationIntent(context);

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
        data.setGrantType(SignInInteractor.GRANT_PASSWORD);
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
        if (uriData.getQueryParameter(OVERRIDE_URL) != null) {
            openWebView(uriData, uriData.getQueryParameter(OVERRIDE_URL).equalsIgnoreCase("1"));
        } else {
            openWebView(uriData, false);
        }
    }

    private static boolean isPromo(List<String> linkSegment) {
        return linkSegment.size() > 0 && (linkSegment.get(0).equals("promo"));
    }

    private void openWebView(Uri encodedUri, boolean allowingOverriding) {
        Fragment fragment = FragmentGeneralWebView.createInstance(Uri.encode(encodedUri.toString()), allowingOverriding);
        viewListener.inflateFragment(fragment, "WEB_VIEW");
        viewListener.actionChangeToolbarWithBackToNative();
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

    private void openShopInfo(final List<String> linkSegment, final Uri uriData) {
        viewListener.showLoading();
        RequestParams params = RequestParams.create();
        params.putString("shop_domain", linkSegment.get(0));
        getShopInfoUseCase.execute(params, new Subscriber<ShopModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.finishLoading();
                viewListener.networkError(uriData);
//                prepareOpenWebView(uriData);
            }

            @Override
            public void onNext(ShopModel shopModel) {
                viewListener.finishLoading();
                if (shopModel != null && shopModel.info != null) {
                    viewListener.goToActivity(
                            ShopInfoActivity.class,
                            ShopInfoActivity.createBundle(shopModel.info.getShopId(), linkSegment.get(0))
                    );
                } else {
                    prepareOpenWebView(uriData);
                }
            }
        });
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

    private void openCatalogDetail(List<String> linkSegment, Uri uriData) {
        viewListener.inflateFragment(DetailProductRouter
                .getCatalogDetailFragment(context, linkSegment.get(1)), "TAG_FRAGMENT_CATALOG_DETAIL");
    }

    private void openHotProduct(List<String> linkSegment, Uri uriData) {
        if (isHotBrowse(linkSegment, uriData)) {
            return;
        }

        Intent intent = BrowseProductRouter.getHotlistIntent(context, uriData.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

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

    private void openDiscoveryPage(String url) {
        context.startActivity(ReactNativeDiscoveryActivity.createCallingIntent(
                context,
                ReactConst.Screen.DISCOVERY_PAGE,
                "",
                DeepLinkChecker.getDiscoveryPageId(url))
        );
    }

    private void openHomepageHot() {
        Intent intent = HomeRouter.getHomeActivityInterfaceRouter(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_HOTLIST);
        context.startActivity(intent);
        context.finish();
    }

    private void openCategory(String uriData) {
        URLParser urlParser = new URLParser(uriData);
        if (!urlParser.getParamKeyValueMap().isEmpty()) {
            CategoryActivity.moveTo(
                    context,
                    uriData
            );
        } else {
            context.startActivity(
                    BrowseProductRouter.getIntermediaryIntent(context, urlParser.getDepIDfromURI(context))
            );
        }
        context.finish();
    }

    private void openBrowseProduct(List<String> linkSegment, Uri uriData) {
        Bundle bundle = new Bundle();
        String departmentId = uriData.getQueryParameter("sc");
        String searchQuery = uriData.getQueryParameter("q");
        String source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;

        bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
        bundle.putBoolean(IS_DEEP_LINK_SEARCH, true);
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
        bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_HOTLIST);
        bundle.putString(BrowseProductRouter.EXTRAS_SEARCH_TERM, searchQuery);
        bundle.putString(BrowseProductRouter.EXTRA_SOURCE, source);

        Intent intent;
        if (TextUtils.isEmpty(departmentId)) {
            intent = BrowseProductRouter.getSearchProductIntent(context);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            context.startActivity(intent);
            context.finish();
        } else {
            IntermediaryActivity.moveToClear(context, departmentId);
        }
    }

    private void openReferralScreen(Uri uriData) {
        context.startActivity(ReferralActivity.getCallingIntent(context));
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
                && !linkSegment.get(0).equals("discovery")
                && !linkSegment.get(0).equals("hot")
                && !linkSegment.get(0).equals("blog")
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
                && !isBlog(linkSegment)
                && !linkSegment.get(0).equals("pulsa"));
    }

    private boolean isBlog(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equals("blog");
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
