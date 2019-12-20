package com.tokopedia.tkpd.deeplink.presenter;

import android.app.Activity;
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
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConsInternalHome;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.internal.ApplinkConstInternalTravel;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.model.AccountsModel;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.SecurityModel;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.catalog.fragment.CatalogDetailListFragment;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryActivity;
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase;
import com.tokopedia.product.detail.common.data.model.product.ProductInfo;
import com.tokopedia.session.domain.interactor.SignInInteractor;
import com.tokopedia.session.domain.interactor.SignInInteractorImpl;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.di.component.DaggerDeeplinkComponent;
import com.tokopedia.tkpd.deeplink.di.component.DeeplinkComponent;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.home.ReactNativeDiscoveryActivity;
import com.tokopedia.tkpd.utils.ProductNotFoundException;
import com.tokopedia.tkpd.utils.ShopNotFoundException;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.track.TrackApp;
import com.tokopedia.webview.download.BaseDownloadAppLinkActivity;
import com.tokopedia.webview.ext.UrlEncoderExtKt;

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

import static com.tokopedia.webview.ConstantKt.KEY_ALLOW_OVERRIDE;
import static com.tokopedia.webview.ConstantKt.KEY_NEED_LOGIN;
import static com.tokopedia.webview.ConstantKt.KEY_TITLEBAR;


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
    private static final String PARAM_EXTRA_REVIEW = "REVIEW_CLICK_AT";

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

        Bundle queryParamBundle = RouteManager.getBundleFromAppLinkQueryParams(uriData);
        Bundle defaultBundle = new Bundle();
        defaultBundle.putBundle(RouteManager.QUERY_PARAM, queryParamBundle);
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
                    openHomepage(defaultBundle);
                    break;
                case DeepLinkChecker.CATEGORY:
                    openCategory(uriData.toString(), defaultBundle);
                    screenName = AppScreen.SCREEN_BROWSE_PRODUCT;
                    break;
                case DeepLinkChecker.BROWSE:
                    openBrowseProduct(linkSegment, uriData, defaultBundle);
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
                    openHomepageHot(defaultBundle);
                    break;
                case DeepLinkChecker.CATALOG:
                    openCatalogDetail(linkSegment);
                    screenName = AppScreen.SCREEN_CATALOG;
                    break;
                case DeepLinkChecker.DISCOVERY_PAGE:
                    openDiscoveryPage(uriData.toString(), defaultBundle);
                    screenName = AppScreen.SCREEN_DISCOVERY_PAGE;
                    break;
                case DeepLinkChecker.CONTACT_US:
                    URLParser urlParser = new URLParser(uriData.toString());
                    RouteManager.route(context, defaultBundle, ApplinkConstInternalMarketplace.CONTACT_US, urlParser.getSetQueryValue().get(1));
                    screenName = AppScreen.SCREEN_CONTACT_US;
                    break;
                case DeepLinkChecker.PRODUCT:
                    if (linkSegment.size() >= 2
                            && (linkSegment.get(1).equals("info") || isEtalase(linkSegment) || isShopHome(linkSegment))) {
                        openShopInfo(linkSegment, uriData, defaultBundle);
                        screenName = AppScreen.SCREEN_SHOP_INFO;
                    } else {
                        openProduct(linkSegment, uriData, defaultBundle);
                        screenName = AppScreen.SCREEN_PRODUCT_INFO;
                    }
                    break;
                case DeepLinkChecker.ETALASE:
                case DeepLinkChecker.SHOP:
                    openShopInfo(linkSegment, uriData, defaultBundle);
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
                case DeepLinkChecker.RECOMMENDATION:
                    openHomeRecommendation(linkSegment, uriData, defaultBundle);
                    screenName = AppScreen.SCREEN_RECOMMENDATION;
                    break;
                case DeepLinkChecker.SIMILAR_PRODUCT:
                    openSimilarProduct(linkSegment, uriData, defaultBundle);
                    screenName = AppScreen.SCREEN_SIMILAR_PRODUCT;
                    break;
                case DeepLinkChecker.OTHER:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
                case DeepLinkChecker.INVOICE:
                    openInvoice(linkSegment, uriData);
                    screenName = AppScreen.SCREEN_DOWNLOAD_INVOICE;
                    break;
                case DeepLinkChecker.HOTEL:
                    openHotel(uriData, defaultBundle);
                    screenName = "";
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
                    openPeluangPage(uriData.getPathSegments(), uriData, defaultBundle);
                    break;
                case DeepLinkChecker.GROUPCHAT:
                    openGroupChat(linkSegment, defaultBundle);
                    screenName = AppScreen.GROUP_CHAT;
                    break;
                case DeepLinkChecker.PROMO_DETAIL:
                    DeepLinkChecker.openPromoDetail(uriData.toString(), context, defaultBundle);
                    context.finish();
                    screenName = "";
                    break;
                case DeepLinkChecker.PROMO_LIST:
                    DeepLinkChecker.openPromoList(uriData.toString(), context, defaultBundle);
                    context.finish();
                    screenName = "";
                    break;
                case DeepLinkChecker.SALE:
                    openSale(linkSegment, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.FLIGHT:
                    openFlight(defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.PROFILE:
                    openProfile(linkSegment, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.CONTENT:
                    openContent(linkSegment, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.SMCREFERRAL:
                    openSmcReferralPage(linkSegment, uriData, defaultBundle);
                    screenName = AppScreen.SCREEN_WEBVIEW;
                    break;
                case DeepLinkChecker.PRODUCT_REVIEW:
                    openReview(uriData.toString(), defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.ORDER_LIST:
                    Bundle bundle =  new Bundle();
                    bundle.putString("query_param",uriData.getQuery());
                    RouteManager.route(context,bundle,ApplinkConst.ORDER_LIST_WEBVIEW);
                    screenName = "";
                    break;
                default:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
            }
            sendCampaignGTM(activity, uriData.toString(), screenName);
        }
    }

    private void openReview(String uriData, Bundle defaultBundle) {
        List<String> segments = Uri.parse(uriData).getPathSegments();

        if (segments.size() >= 4) {
            String reputationId = segments.get(segments.size() - 2);
            String productId = segments.get(segments.size() - 1);


            String uriReview = UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId, productId);
            Intent intent = RouteManager.getIntent(
                    context,
                    uriReview);
            intent.putExtras(defaultBundle);
            intent.putExtra(PARAM_EXTRA_REVIEW, 5);
            viewListener.goToPage(intent);
        }
    }

    private void openGroupChat(List<String> linkSegment, Bundle bundle) {
        int SEGMENT_GROUPCHAT = 2;
        Intent intent;
        if (linkSegment.size() == SEGMENT_GROUPCHAT) {
            intent = ((TkpdCoreRouter) context.getApplication()).getGroupChatIntent(
                    context, linkSegment.get(1));
        } else {
            intent = ((TkpdCoreRouter) context.getApplication()).getInboxChannelsIntent(
                    context);
        }
        intent.putExtras(bundle);
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

    private void openPeluangPage(List<String> linkSegment, Uri uriData, Bundle bundle) {
        String query = uriData.getQueryParameter("q");
        Intent intent = SellerRouter.getActivitySellingTransactionOpportunity(context, query);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.finish();
    }

    private void openHotel(Uri uri, Bundle bundle) {
        RouteManager.route(context, bundle, getApplinkWithUriQueryParams(uri, ApplinkConstInternalTravel.DASHBOARD_HOTEL));
        context.finish();
    }

    private void openSale(List<String> linkSegment, Bundle bundle) {
        Intent intent;
        if (linkSegment.size() <= 1) {
            intent = RouteManager.getIntent(context, ApplinkConst.PROMO_LIST);
        } else {
            String SLUG_PARAM = "{slug}";
            String applink = ApplinkConst.PROMO_SALE_NO_SLASH.
                    replace(SLUG_PARAM, linkSegment.get(1));
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(applink));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.finish();
    }

    private void openFlight(Bundle bundle) {
        Intent intent = RouteManager.getIntent(context, ApplinkConstInternalTravel.DASHBOARD_FLIGHT);
        intent.putExtras(bundle);
        viewListener.goToPage(intent);
    }

    private void openProfile(List<String> linkSegment, Bundle bundle) {
        if (linkSegment.size() >= 2) {
            String userId = linkSegment.get(1);
            Intent intent = RouteManager.getIntent(
                    context,
                    ApplinkConst.PROFILE.replace("{user_id}", userId)
            );
            intent.putExtras(bundle);
            viewListener.goToPage(intent);
        }
    }

    private void openContent(List<String> linkSegment, Bundle bundle) {
        if (linkSegment.size() >= 2) {
            String contentId = linkSegment.get(1);
            Intent intent = RouteManager.getIntent(context, ApplinkConst.CONTENT_DETAIL, contentId);
            intent.putExtras(bundle);
            viewListener.goToPage(intent);
        }
    }

    private void openSmcReferralPage(List<String> linkSegment, Uri uriData, Bundle bundle) {
        if (linkSegment != null && linkSegment.size() > 0) {
            String url = ApplinkConst.SMC_REFERRAL + "?url=" + uriData.toString();
            Intent intent = RouteManager.getIntent(context, url);
            intent.putExtras(bundle);
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

        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CAMPAIGN,
                AppEventTracking.Category.CAMPAIGN,
                AppEventTracking.Action.DEEPLINK,
                campaignUri
        ).getEvent());
    }


    private void prepareOpenWebView(Uri uriData) {
        boolean isAllowOverride = false;
        String allowOverride = uriData.getQueryParameter(OVERRIDE_URL);
        if ("1".equalsIgnoreCase(allowOverride) || "true".equalsIgnoreCase(allowOverride)) {
            isAllowOverride = true;
        }
        openWebView(uriData, isAllowOverride,
                uriData.getQueryParameter(PARAM_TITLEBAR) == null ||
                        "true".equalsIgnoreCase(uriData.getQueryParameter(PARAM_TITLEBAR)),
                uriData.getQueryParameter(PARAM_NEED_LOGIN) != null &&
                        "true".equalsIgnoreCase(uriData.getQueryParameter(PARAM_NEED_LOGIN))
        );
    }

    private static boolean isPromo(List<String> linkSegment) {
        return linkSegment.size() > 0 && (linkSegment.get(0).equals("promo"));
    }

    private void openWebView(Uri uri, boolean allowingOverriding, boolean showTitlebar,
                             boolean needLogin) {
        String encodedUri = UrlEncoderExtKt.encodeOnce(uri.toString());
        Intent intent = RouteManager.getIntentNoFallback(context, ApplinkConstInternalGlobal.WEBVIEW,
                encodedUri);
        if (intent!=null) {
            intent.putExtra(KEY_ALLOW_OVERRIDE, allowingOverriding);
            intent.putExtra(KEY_NEED_LOGIN, needLogin);
            intent.putExtra(KEY_TITLEBAR, showTitlebar);
            context.startActivity(intent);
            context.finish();
        }
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

    private void openShopInfo(final List<String> linkSegment, final Uri uriData, Bundle bundle) {
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
                    String shopId = shopInfo.getInfo().getShopId();
                    String lastSegment = linkSegment.get(linkSegment.size() - 1);
                    if (isEtalase(linkSegment)) {
                        RouteManager.route(context,
                                bundle,
                                ApplinkConst.SHOP_ETALASE,
                                shopId,
                                lastSegment);
                    } else if (lastSegment.equals("info")) {
                        RouteManager.route(context,
                                bundle,
                                ApplinkConst.SHOP_INFO,
                                shopId);
                    } else if (isShopHome(linkSegment)) {
                        RouteManager.route(context,
                                bundle,
                                ApplinkConst.SHOP_HOME,
                                shopId);
                    } else {
                        Intent intent = ((TkpdCoreRouter) context.getApplication()).getShopPageIntent(context, shopId);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

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

    private boolean isEtalase(List<String> linkSegment) {
        String lastSegment = linkSegment.get(linkSegment.size() - 1);
        return lastSegment.equals("preorder")
                || lastSegment.equals("sold")
                || (linkSegment.size() > 1 && linkSegment.get(1).equals("etalase"));
    }

    private boolean isShopHome(List<String> linkSegment) {
        String lastSegment = linkSegment.get(linkSegment.size() - 1);
        return lastSegment.equalsIgnoreCase("home");
    }

    private void openHomeRecommendation(final List<String> linkSegment, final Uri uriData, Bundle bundle) {
        String source = uriData.getQueryParameter("ref");
        String productId = linkSegment.size() > 1 ? linkSegment.get(1) : "";
        Intent intent = RouteManager.getIntent(context, ApplinkConsInternalHome.HOME_RECOMMENDATION);
        intent.putExtra(context.getString(R.string.home_recommendation_extra_product_id), productId);
        intent.putExtra(context.getString(R.string.home_recommendation_extra_ref), source == null ? "" : source);
        intent.putExtras(bundle);
        intent.setData(uriData);
        context.startActivity(intent);
        context.finish();
    }

    private void openSimilarProduct(final List<String> linkSegment, final Uri uriData, Bundle bundle) {
        String source = uriData.getQueryParameter("ref");
        String productId = linkSegment.size() > 2 ? linkSegment.get(1) : "";
        Intent intent = RouteManager.getIntent(context, ApplinkConsInternalHome.HOME_SIMILAR_PRODUCT);
        intent.putExtra(context.getString(R.string.home_recommendation_extra_product_id), productId);
        intent.putExtra(context.getString(R.string.home_recommendation_extra_ref), source == null ? "" : source);
        intent.putExtras(bundle);
        intent.setData(uriData);
        context.startActivity(intent);
        context.finish();
    }

    private void openProduct(final List<String> linkSegment, final Uri uriData, Bundle bundle) {
        RequestParams params = RequestParams.create();
        params.putString("shop_domain", linkSegment.get(0));
        getShopInfoUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(linkSegment.get(0)), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                viewListener.finishLoading();
                Intent intent = BaseDownloadAppLinkActivity.newIntent(context, uriData.toString(), true);
                context.startActivity(intent);
                context.finish();
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                viewListener.finishLoading();
                if (shopInfo != null && shopInfo.getInfo() != null) {
                    //Add Affiliate string for tracking
                    String affiliateString = "";
                    if (!TextUtils.isEmpty(uriData.getQueryParameter("aff"))) {
                        affiliateString = uriData.getQueryParameter("aff");
                    }

                    context.startActivity(RouteManager.getIntent(context,
                            ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE,
                            uriData,
                            linkSegment.get(0),
                            linkSegment.get(1),
                            affiliateString));
                } else {
                    if (!GlobalConfig.DEBUG) {
                        Crashlytics.logException(new ShopNotFoundException(linkSegment.get(0)));
                        Crashlytics.logException(new ProductNotFoundException(linkSegment.get(0) + "/" + linkSegment.get(1)));
                    }
                    Intent intent = BaseDownloadAppLinkActivity.newIntent(context, uriData.toString(),true);
                    context.startActivity(intent);
                }
                context.finish();
            }
        });
    }

    private String getApplinkWithUriQueryParams(Uri uri, String applink) {
        StringBuilder builder = new StringBuilder(applink);
        if (uri != null) {
            String queryToAppend = uri.getQuery();
            if (!TextUtils.isEmpty(queryToAppend)) {
                if (builder.toString().contains("?")) {
                    builder.append("&");
                } else {
                    builder.append("?");
                }
                builder.append(queryToAppend);
            }
        }
        return builder.toString();
    }

    private void openCatalogDetail(List<String> linkSegment) {
        try {
            String catalogId = linkSegment.get(1);
            viewListener.inflateFragment(
                    CatalogDetailListFragment.newInstance(catalogId),
                    TAG_FRAGMENT_CATALOG_DETAIL
            );
        } catch (Exception e) {
            Crashlytics.log(e.getLocalizedMessage());
        }
    }

    private void openHotProduct(List<String> linkSegment, Uri uriData) {
        if (linkSegment.size() > 1) {
            RouteManager.route(context, DeeplinkMapper.getRegisteredNavigation(context, ApplinkConst.HOME_HOTLIST + "/" + linkSegment.get(1)));
            context.finish();
        }
    }

    private void openHomepage(Bundle defaultBundle) {
        Intent intent = new Intent(context, HomeRouter.getHomeActivityClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(defaultBundle);
        context.startActivity(intent);
        context.finish();
    }

    private void openDiscoveryPage(String url, Bundle bundle) {
        String pageId;
        if (DeepLinkChecker.getDeepLinkType(context, url) != DeepLinkChecker.DISCOVERY_PAGE) {
            pageId = "";
        } else {
            Uri uriData = Uri.parse(url);
            List<String> linkSegment = uriData.getPathSegments();
            pageId = linkSegment.get(1);
        }
        Intent intent = ReactNativeDiscoveryActivity.createCallingIntent(
                context,
                ReactConst.Screen.DISCOVERY_PAGE,
                "",
                pageId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void openHomepageHot(Bundle bundle) {
        Intent intent = HomeRouter.getHomeActivityInterfaceRouter(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_HOTLIST);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.finish();
    }

    private void openCategory(String uriData, Bundle bundle) {
        URLParser urlParser = new URLParser(uriData);
        if (!urlParser.getParamKeyValueMap().isEmpty()) {
            CategoryActivity.moveTo(
                    context,
                    uriData,
                    bundle
            );
        } else {
            RouteManager.route(context, bundle, ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL, urlParser.getDepIDfromURI(context));
        }
        context.finish();
    }

    private void openBrowseProduct(List<String> linkSegment, Uri uriData, Bundle defaultBundle) {

        String departmentId = uriData.getQueryParameter("sc");
        Bundle bundle = RouteManager.getBundleFromAppLinkQueryParams(uriData.toString());
        bundle.putBoolean(IS_DEEP_LINK_SEARCH, true);

        Intent intent;
        if (TextUtils.isEmpty(departmentId)) {
            intent = RouteManager.getIntent(context, constructSearchApplink(uriData));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            intent.putExtras(defaultBundle);
            context.startActivity(intent);
            context.finish();
        } else {
            IntermediaryActivity.moveToClear(context, departmentId);
        }
    }

    private static String constructSearchApplink(Uri uriData) {
        String q = uriData.getQueryParameter("q");

        String applink = TextUtils.isEmpty(q) ?
                ApplinkConstInternalDiscovery.AUTOCOMPLETE :
                ApplinkConstInternalDiscovery.SEARCH_RESULT;

        return applink + "?" + uriData.getQuery();
    }

    private boolean isHotBrowse(List<String> linkSegment, Uri uriData) {
        return (linkSegment.size() == 1 && !isHotAlias(uriData));
    }

    private boolean isHotAlias(Uri uri) {
        return uri.getQueryParameter("alk") != null;
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
