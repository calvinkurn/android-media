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
import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeepLinkChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryActivity;
import com.tokopedia.flight.dashboard.view.activity.FlightDashboardActivity;
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase;
import com.tokopedia.home_recom.HomeRecommendationActivity;
import com.tokopedia.loyalty.LoyaltyRouter;
import com.tokopedia.product.detail.common.data.model.product.ProductInfo;
import com.tokopedia.referral.view.activity.ReferralActivity;
import com.tokopedia.session.domain.interactor.SignInInteractor;
import com.tokopedia.session.domain.interactor.SignInInteractorImpl;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.di.component.DaggerDeeplinkComponent;
import com.tokopedia.tkpd.deeplink.di.component.DeeplinkComponent;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.home.ReactNativeDiscoveryActivity;
import com.tokopedia.tkpd.utils.ProductNotFoundException;
import com.tokopedia.tkpd.utils.ShopNotFoundException;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.track.TrackApp;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;


/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 */
public class DeepLinkPresenterImpl implements DeepLinkPresenter {

    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";

    private static final String TAG = DeepLinkPresenterImpl.class.getSimpleName();
    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String AF_ONELINK_HOST = "tokopedia.onelink.me";
    private static final String OVERRIDE_URL = "override_url";
    private static final String PARAM_TITLEBAR = "titlebar";
    private static final String PARAM_NEED_LOGIN = "need_login";

    private static final String TAG_FRAGMENT_CATALOG_DETAIL = "TAG_FRAGMENT_CATALOG_DETAIL";

    private final Activity context;
    private final DeepLinkView viewListener;
    private SignInInteractor interactor;

    @Inject
    GetShopInfoByDomainUseCase getShopInfoUseCase;

    @Inject
    @Named("productUseCase")
    GraphqlUseCase<ProductInfo.Response> getProductUseCase;

    public DeepLinkPresenterImpl(DeepLinkActivity activity) {
        this.viewListener = activity;
        this.context = activity;
        this.interactor = SignInInteractorImpl.createInstance(activity);

        initInjection(activity);
    }

    @Override
    public void checkUriLogin(Uri uriData) {
        if (DeepLinkChecker.getDeepLinkType(context, uriData.toString()) == DeepLinkChecker.ACCOUNTS && uriData.getPath().contains("activation")) {
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
                .baseAppComponent(((BaseMainApplication) activity.getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
    }

    public void processDeepLinkAction(Activity activity, Uri uriData) {
        if (uriData.getHost().equals(AF_ONELINK_HOST)) {
            Log.d(TAG, "URI DATA = " + uriData.toString());
            processAFlistener();
        } else {
            List<String> linkSegment = uriData.getPathSegments();
            String screenName;
            int type = DeepLinkChecker.getDeepLinkType(context, uriData.toString());
            CommonUtils.dumper("FCM wvlogin deeplink type " + type);
            switch (type) {
                case DeepLinkChecker.HOME:
                    screenName = AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_BERANDA;
                    sendCampaignGTM(activity, uriData.toString(), screenName);
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
                    sendCampaignGTM(activity, uriData.toString(), screenName);
                    openHotProduct(linkSegment, uriData);
                    break;
                case DeepLinkChecker.HOT_LIST:
                    screenName = AppScreen.SCREEN_HOME_HOTLIST;
                    sendCampaignGTM(activity, uriData.toString(), screenName);
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
                    openProduct(linkSegment, uriData);
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
                case DeepLinkChecker.HOME_RECOMMENDATION:
                    openHomeRecommendation(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_SHOP_INFO;
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
                        openWebView(Uri.parse(String.valueOf(linkSegment.get(0))), false, true,
                                false);
                        screenName = AppScreen.SCREEN_WEBVIEW;
                    } else {
                        return;
                    }
                    break;
                case DeepLinkChecker.PELUANG:
                    screenName = AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_BERANDA;
                    sendCampaignGTM(activity, uriData.toString(), screenName);
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
                case DeepLinkChecker.PROMO:
                    openPromo(linkSegment);
                    screenName = "";
                    break;
                case DeepLinkChecker.SALE:
                    openSale(linkSegment);
                    screenName = "";
                    break;
                case DeepLinkChecker.FLIGHT:
                    openFlight();
                    screenName = "";
                    break;
                case DeepLinkChecker.PROFILE:
                    openProfile(linkSegment);
                    screenName = "";
                    break;
                case DeepLinkChecker.CONTENT:
                    openContent(linkSegment);
                    screenName = "";
                    break;
                case DeepLinkChecker.SMCREFERRAL:
                    openSmcReferralPage(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_WEBVIEW;
                    break;
                default:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
            }
            sendCampaignGTM(activity, uriData.toString(), screenName);
        }
    }

    private void openGroupChat(List<String> linkSegment) {
        int SEGMENT_GROUPCHAT = 2;
        Intent intent;
        if (linkSegment.size() == SEGMENT_GROUPCHAT) {
            intent = ((TkpdCoreRouter) context.getApplication()).getGroupChatIntent(
                    context, linkSegment.get(1));
        } else {
            intent = ((TkpdCoreRouter) context.getApplication()).getInboxChannelsIntent(
                    context);
        }

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

    private void openPromo(List<String> linkSegment) {
        LoyaltyRouter router = ((LoyaltyRouter) context.getApplication());

        Intent intent;
        if (linkSegment.size() <= 1) {
            intent = router.getPromoListIntent(context);
        } else {
            intent = router.getPromoDetailIntent(context, linkSegment.get(1));
        }

        context.startActivity(intent);
        context.finish();
    }

    private void openSale(List<String> linkSegment) {
        Intent intent;
        if (linkSegment.size() <= 1) {
            LoyaltyRouter router = ((LoyaltyRouter) context.getApplication());
            intent = router.getPromoListIntent(context);
        } else {
            String SLUG_PARAM = "{slug}";
            String applink = ApplinkConst.PROMO_SALE_NO_SLASH.
                    replace(SLUG_PARAM, linkSegment.get(1));
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(applink));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
        context.finish();
    }

    private void openFlight() {
        Intent intent = FlightDashboardActivity.getCallingIntent(context);
        viewListener.goToPage(intent);
    }

    private void openProfile(List<String> linkSegment) {
        if (linkSegment.size() >= 2) {
            String userId = linkSegment.get(1);
            Intent intent = RouteManager.getIntent(
                    context,
                    ApplinkConst.PROFILE.replace("{user_id}", userId)
            );
            viewListener.goToPage(intent);
        }
    }

    private void openContent(List<String> linkSegment) {
        if (linkSegment.size() >= 2) {
            String contentId = linkSegment.get(1);
            Intent intent = RouteManager.getIntent(context, ApplinkConst.CONTENT_DETAIL, contentId);
            viewListener.goToPage(intent);
        }
    }

    private void openSmcReferralPage(List<String> linkSegment, Uri uriData) {
        if (linkSegment != null && linkSegment.size() > 0) {
            String url = ApplinkConst.SMC_REFERRAL + "?url=" + uriData.toString();
            Intent intent = RouteManager.getIntent(context, url);
            viewListener.goToPage(intent);
        }
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
    public void sendCampaignGTM(Activity activity, String campaignUri, String screenName) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(activity, Uri.parse(campaignUri));
        campaign.setScreenName(screenName);
        UnifyTracking.eventCampaign(activity, campaign);
        UnifyTracking.eventCampaign(activity, campaignUri);
    }


    private void prepareOpenWebView(Uri uriData) {
        if (uriData.getQueryParameter(OVERRIDE_URL) != null) {
            openWebView(uriData,
                    uriData.getQueryParameter(OVERRIDE_URL).equalsIgnoreCase("1"),
                    uriData.getQueryParameter(PARAM_TITLEBAR) == null || uriData.getQueryParameter
                            (PARAM_TITLEBAR).equalsIgnoreCase("true"),
                    uriData.getQueryParameter(PARAM_NEED_LOGIN) != null && uriData.getQueryParameter
                            (PARAM_NEED_LOGIN).equalsIgnoreCase("true"));
        } else {
            openWebView(uriData, false,
                    uriData.getQueryParameter(PARAM_TITLEBAR) == null || uriData.getQueryParameter
                            (PARAM_TITLEBAR).equalsIgnoreCase("true"),
                    uriData.getQueryParameter(PARAM_NEED_LOGIN) != null && uriData.getQueryParameter
                            (PARAM_NEED_LOGIN).equalsIgnoreCase("true")
            );
        }
    }

    private static boolean isPromo(List<String> linkSegment) {
        return linkSegment.size() > 0 && (linkSegment.get(0).equals("promo"));
    }

    private void openWebView(Uri encodedUri, boolean allowingOverriding, boolean showTitlebar,
                             boolean needLogin) {
        Fragment fragment = FragmentGeneralWebView.createInstance(Uri.encode(encodedUri.toString
                ()), allowingOverriding, showTitlebar, needLogin);
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
        getShopInfoUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(linkSegment.get(0)), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.finishLoading();
                prepareOpenWebView(uriData);
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                viewListener.finishLoading();
                if (shopInfo != null && shopInfo.getInfo() != null) {
                    Intent intent = ((TkpdCoreRouter) context.getApplication()).getShopPageIntent(context, shopInfo.getInfo().getShopId());
                    context.startActivity(intent);
                    context.finish();
                } else {
                    if (!GlobalConfig.DEBUG) {
                        Crashlytics.logException(new ShopNotFoundException(linkSegment.get(0)));
                    }
                    prepareOpenWebView(uriData);
                }
            }
        });
    }

    private void openHomeRecommendation(final List<String> linkSegment, final Uri uriData) {
        if (linkSegment != null && linkSegment.size() > 0) {
            Intent intent = RouteManager.getIntent(context  , ApplinkConstInternalMarketplace.HOME_RECOMMENDATION);
            intent.putExtra(HomeRecommendationActivity.PRODUCT_ID, linkSegment.get(1));
            context.startActivity(intent);
            context.finish();
        }
    }

    private void openProduct(final List<String> linkSegment, final Uri uriData) {
        RequestParams params = RequestParams.create();
        params.putString("shop_domain", linkSegment.get(0));
        getShopInfoUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(linkSegment.get(0)), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.finishLoading();
                Intent intent = SimpleWebViewWithFilePickerActivity.getIntent(context, uriData.toString());
                context.startActivity(intent);
                context.finish();
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                viewListener.finishLoading();
                if (shopInfo != null && shopInfo.getInfo() != null) {
                    context.startActivity(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN,
                            linkSegment.get(0), linkSegment.get(1)));
                } else {
                    if (!GlobalConfig.DEBUG) {
                        Crashlytics.logException(new ShopNotFoundException(linkSegment.get(0)));
                        Crashlytics.logException(new ProductNotFoundException(linkSegment.get(0) + "/" + linkSegment.get(1)));
                    }
                    Intent intent = SimpleWebViewWithFilePickerActivity.getIntent(context, uriData.toString());
                    context.startActivity(intent);
                }
                context.finish();
            }
        });
    }

    private void openCatalogDetail(List<String> linkSegment, Uri uriData) {
        viewListener.inflateFragment(DetailProductRouter
                .getCatalogDetailFragment(context, linkSegment.get(1)), TAG_FRAGMENT_CATALOG_DETAIL);
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
        String pageId;
        if (DeepLinkChecker.getDeepLinkType(context, url) != DeepLinkChecker.DISCOVERY_PAGE) {
            pageId = "";
        } else {
            Uri uriData = Uri.parse(url);
            List<String> linkSegment = uriData.getPathSegments();
            pageId = linkSegment.get(1);
        }
        context.startActivity(ReactNativeDiscoveryActivity.createCallingIntent(
                context,
                ReactConst.Screen.DISCOVERY_PAGE,
                "",
                pageId)
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
            RouteManager.route(context, ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL,
                    urlParser.getDepIDfromURI(context));
        }
        context.finish();
    }

    private void openBrowseProduct(List<String> linkSegment, Uri uriData) {
        Bundle bundle = new Bundle();
        String departmentId = uriData.getQueryParameter("sc");
        String searchQuery = uriData.getQueryParameter("q");
        String source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;

        bundle.putBoolean(IS_DEEP_LINK_SEARCH, true);

        Intent intent;
        if (TextUtils.isEmpty(departmentId)) {
            intent = RouteManager.getIntent(context, constructSearchApplink(searchQuery, departmentId));
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

    private static String constructSearchApplink(String query, String departmentId) {
        String applink = TextUtils.isEmpty(query) ?
                ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE :
                ApplinkConst.DISCOVERY_SEARCH;

        return applink
                + "?"
                + "q=" + query
                + "&sc=" + departmentId;
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
                        processDeepLinkAction(context, DeeplinkUTMUtils.simplifyUrl(oriUri));
                    }
                }
            }

            @Override
            public void onAttributionFailure(String s) {

            }
        });
    }

    @Override
    public void sendAuthenticatedEvent(Uri uriData, String screenName) {
        try {
            URL obtainedURL = new URL(uriData.toString());
            Map<String, String> customDimension = new HashMap<>();
            customDimension.put(Authenticated.KEY_DEEPLINK_URL, obtainedURL.toString());
            TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName, customDimension);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
