package com.tokopedia.tkpd.deeplink.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.DeepLinkChecker;
import com.tokopedia.applink.DeeplinkMapper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.constant.DeeplinkConstant;
import com.tokopedia.applink.internal.ApplinkConsInternalHome;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.internal.ApplinkConstInternalOrder;
import com.tokopedia.applink.internal.ApplinkConstInternalTravel;
import com.tokopedia.applink.travel.DeeplinkMapperTravel;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.customer_mid_app.R;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.network.data.model.response.ResponseV4ErrorException;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.di.component.DaggerDeeplinkComponent;
import com.tokopedia.tkpd.deeplink.di.component.DeeplinkComponent;
import com.tokopedia.tkpd.deeplink.listener.DeepLinkView;
import com.tokopedia.tkpd.deeplink.utils.URLParser;
import com.tokopedia.tkpd.utils.ProductNotFoundException;
import com.tokopedia.tkpd.utils.ShopNotFoundException;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.webview.download.BaseDownloadAppLinkActivity;
import com.tokopedia.webview.ext.UrlEncoderExtKt;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import timber.log.Timber;

import static com.tokopedia.webview.ConstantKt.KEY_ALLOW_OVERRIDE;
import static com.tokopedia.webview.ConstantKt.KEY_NEED_LOGIN;
import static com.tokopedia.webview.ConstantKt.KEY_TITLE;
import static com.tokopedia.webview.ConstantKt.KEY_TITLEBAR;
import static com.tokopedia.webview.ConstantKt.KEY_URL;


/**
 * @author by Angga.Prasetiyo on 14/12/2015.
 */
public class DeepLinkPresenterImpl implements DeepLinkPresenter {

    public static final String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
    public static final int INIT_STATE_FRAGMENT_HOTLIST = 3;
    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";

    private static final String TAG = DeepLinkPresenterImpl.class.getSimpleName();
    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String AF_ONELINK_HOST = "tokopedia.onelink.me";
    private static final String OVERRIDE_URL = "override_url";
    private static final String ALLOW_OVERRIDE = "allow_override";
    private static final String PARAM_TITLEBAR = "titlebar";
    private static final String PARAM_NEED_LOGIN = "need_login";
    private static final String PARAM_EXTRA_REVIEW = "rating";
    private static final String PARAM_EXTRA_UTM_SOURCE = "utm_source";

    private final Activity context;
    private final DeepLinkView viewListener;
    private final FirebaseCrashlytics crashlytics;

    @Inject
    GetShopInfoByDomainUseCase getShopInfoUseCase;

    @Inject
    UserSessionInterface userSession;

    public DeepLinkPresenterImpl(DeepLinkActivity activity) {
        this.viewListener = activity;
        this.context = activity;
        this.crashlytics = FirebaseCrashlytics.getInstance();
        initInjection(activity);
    }

    @Override
    public void checkUriLogin(Uri uriData) {
        UserSessionInterface userSession = new UserSession(context);
        if (DeepLinkChecker.getDeepLinkType(context, uriData.toString()) == DeepLinkChecker.ACCOUNTS && uriData.getPath().contains("activation")) {
            if (!userSession.isLoggedIn()) {
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

    public void processDeepLinkAction(Activity activity, Uri uriData, boolean isAmp) {

        Bundle queryParamBundle = RouteManager.getBundleFromAppLinkQueryParams(uriData);
        Bundle defaultBundle = new Bundle();
        defaultBundle.putBundle(RouteManager.QUERY_PARAM, queryParamBundle);
        if (uriData.getHost().equals(AF_ONELINK_HOST)) {
            Timber.d("URI DATA = " + uriData.toString());
            processAFlistener();
        } else {
            List<String> linkSegment = uriData.getPathSegments();
            String screenName;
            int type = DeepLinkChecker.getDeepLinkType(context, uriData.toString());
            Timber.d("FCM wvlogin deeplink type " + type);
            boolean keepActivityOn = false;
            switch (type) {
                case DeepLinkChecker.HOME:
                    screenName = AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_BERANDA;
                    openHomepage(defaultBundle);
                    break;
                case DeepLinkChecker.CATEGORY:
                    openInternalDeeplink(uriData.toString(), defaultBundle);
                    screenName = AppScreen.SCREEN_BROWSE_PRODUCT;
                    break;
                case DeepLinkChecker.BROWSE:
                    openBrowseProduct(linkSegment, uriData, defaultBundle);
                    screenName = AppScreen.SCREEN_BROWSE_PRODUCT;
                    break;
                case DeepLinkChecker.HOT:
                    screenName = AppScreen.SCREEN_BROWSE_HOT_LIST;
                    openHotProduct(linkSegment, uriData);
                    break;
                case DeepLinkChecker.HOT_LIST:
                    screenName = AppScreen.SCREEN_HOME_HOTLIST;
                    openHomepageHot(defaultBundle);
                    break;
                case DeepLinkChecker.FIND:
                    screenName = AppScreen.SCREEN_FIND;
                    DeepLinkChecker.openFind(uriData.toString(), context);
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
                    keepActivityOn = true;
                    if (linkSegment.size() >= 2
                            && (linkSegment.get(1).equals("info") || isEtalase(linkSegment) || isShopHome(linkSegment) || isShopReview(linkSegment) || isShopProduct(linkSegment) || isShopFeed(linkSegment))) {
                        openShopInfo(linkSegment, uriData, defaultBundle);
                        screenName = AppScreen.SCREEN_SHOP_INFO;
                    } else {
                        openProduct(linkSegment, uriData, defaultBundle);
                        screenName = AppScreen.SCREEN_PRODUCT_INFO;
                    }
                    break;
                case DeepLinkChecker.ETALASE:
                case DeepLinkChecker.SHOP:
                    keepActivityOn = true;
                    openShopInfo(linkSegment, uriData, defaultBundle);
                    screenName = AppScreen.SCREEN_SHOP_INFO;
                    break;
                case DeepLinkChecker.ACCOUNTS:
                    if (!uriData.getPath().contains("activation")) {
                        prepareOpenWebView(uriData);
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
                    openInvoice(uriData);
                    screenName = AppScreen.SCREEN_DOWNLOAD_INVOICE;
                    break;
                case DeepLinkChecker.HOTEL:
                    openHotel(uriData, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.APPLINK:
                    if (linkSegment != null && linkSegment.size() > 0) {
                        openWebView(Uri.parse(String.valueOf(linkSegment.get(0))), false, true,
                                false);
                        screenName = AppScreen.SCREEN_WEBVIEW;
                    } else {
                        return;
                    }
                    break;
                case DeepLinkChecker.PROMO_DETAIL:
                    DeepLinkChecker.openPromoDetail(uriData.toString(), context, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.PROMO_LIST:
                    DeepLinkChecker.openPromoList(uriData.toString(), context, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.FLIGHT:
                    openFlight(uriData, defaultBundle);
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
                    openOrderList(uriData);
                    screenName = "";
                    break;
                case DeepLinkChecker.DEALS:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.DEALS_PAGE;
                    break;
                case DeepLinkChecker.TRAVEL_HOMEPAGE:
                    openTravelHomepage(linkSegment, uriData, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.NATIVE_THANK_YOU:
                    openNativeThankYouPage(linkSegment, defaultBundle);
                    screenName = "";
                    break;
                case DeepLinkChecker.LOGIN_BY_QR:
                    openLoginByQr(uriData);
                    screenName = "";
                    break;
                default:
                    prepareOpenWebView(uriData);
                    screenName = AppScreen.SCREEN_DEEP_LINK;
                    break;
            }
            sendCampaignGTM(activity, uriData.toString(), screenName, isAmp);
            if (!keepActivityOn && context != null) {
                context.finish();
            }
        }
    }

    private void openLoginByQr(Uri uriData) {
        RouteManager.route(context, ApplinkConst.QR_LOGIN + "?" + uriData.getQuery());
    }

    private void openNativeThankYouPage(List<String> linkSegment, Bundle defaultBundle) {
        if (linkSegment.size() == 4) {
            String merchantCode = linkSegment.get(2);
            String paymentID = linkSegment.get(3);
            Intent intent = RouteManager.getIntent(context, ApplinkConst.THANKYOU_PAGE_NATIVE,
                    paymentID, merchantCode);
            intent.putExtras(defaultBundle);
            viewListener.goToPage(intent);
        }
    }

    private void openOrderList(Uri uriData) {
        String category = uriData.getQueryParameter("category");
        if (category != null && category.equals("train")) {
            Intent intent = RouteManager.getIntent(context, ApplinkConst.TRAIN_ORDER);
            viewListener.goToPage(intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("url", uriData.toString());
            Intent intent = RouteManager.getIntent(context, ApplinkConst.ORDER_LIST_WEBVIEW);
            intent.putExtras(bundle);
            viewListener.goToPage(intent);
        }
    }

    private void openReview(String uriData, Bundle defaultBundle) {
        Uri uri = Uri.parse(uriData);
        List<String> segments = uri.getPathSegments();

        if (segments.size() >= 4) {
            String reputationId = segments.get(segments.size() - 2);
            String productId = segments.get(segments.size() - 1);

            String rating;
            if (!TextUtils.isEmpty(uri.getQueryParameter(PARAM_EXTRA_REVIEW))) {
                rating = uri.getQueryParameter(PARAM_EXTRA_REVIEW);
            } else {
                rating = "5";
            }

            int ratingNumber;
            try {
                ratingNumber = Integer.parseInt(rating != null ? rating : "5");
            } catch (NumberFormatException e) {
                ratingNumber = 5;
            }

            String utmSource;
            if (!TextUtils.isEmpty(uri.getQueryParameter(PARAM_EXTRA_UTM_SOURCE))) {
                utmSource = uri.getQueryParameter(PARAM_EXTRA_UTM_SOURCE);
            } else {
                utmSource = "";
            }
            String newUri = UriUtil.buildUri(ApplinkConstInternalMarketplace.CREATE_REVIEW, reputationId, productId);
            String uriReview = Uri.parse(newUri)
                    .buildUpon()
                    .appendQueryParameter(PARAM_EXTRA_REVIEW, rating)
                    .appendQueryParameter(PARAM_EXTRA_UTM_SOURCE, utmSource)
                    .build()
                    .toString();
            Intent intent = RouteManager.getIntent(
                    context,
                    uriReview);
            intent.putExtras(defaultBundle);
            intent.putExtra(PARAM_EXTRA_REVIEW, ratingNumber);
            viewListener.goToPage(intent);
        }
    }

    private void openHotel(Uri uri, Bundle bundle) {
        List<String> linkSegment = uri.getPathSegments();
        if (linkSegment.size() > 1) {
            if (linkSegment.get(1).equals("search")) {
                String applink = DeeplinkMapperTravel.getRegisteredNavigationTravel(context, ApplinkConst.HOTEL_SRP);
                RouteManager.route(context, applink + "?" + uri.getQuery());
                context.finish();
            } else if (linkSegment.size() >= 4 && linkSegment.get(2).equals("h")) {
                // eg : https://www.tokopedia.com/hotel/Indonesia/h/the-apurva-kempinski-bali-960088/
                String[] hotelNames = linkSegment.get(3).split("-");
                String hotelId = hotelNames[hotelNames.length - 1];
                if (uri.getQuery() != null) {
                    RouteManager.route(context, ApplinkConstInternalTravel.HOTEL_DETAIL + "/" + hotelId + "?" + uri.getQuery());
                } else {
                    RouteManager.route(context, ApplinkConstInternalTravel.HOTEL_DETAIL + "/" + hotelId);
                }
                context.finish();
            } else {
                RouteManager.route(context, bundle, getApplinkWithUriQueryParams(uri, ApplinkConstInternalTravel.DASHBOARD_HOTEL));
                context.finish();
            }
        } else {
            RouteManager.route(context, bundle, getApplinkWithUriQueryParams(uri, ApplinkConstInternalTravel.DASHBOARD_HOTEL));
            context.finish();
        }
    }

    private void openTravelHomepage(List<String> linkSegment, Uri uri, Bundle bundle) {
        if (linkSegment.size() == 1)
            RouteManager.route(context, bundle, getApplinkWithUriQueryParams(uri, ApplinkConstInternalTravel.HOME_TRAVEL_HOMEPAGE));
        else {
            Intent intent = RouteManager.getIntent(context, ApplinkConstInternalTravel.CITY_PAGE_TRAVEL_HOMEPAGE);
            intent.putExtra(ApplinkConstInternalTravel.EXTRA_DESTINATION_WEB_URL, uri.toString());
            context.startActivity(intent);
        }
        context.finish();
    }

    private void openFlight(Uri uri, Bundle bundle) {
        RouteManager.route(context, bundle, getApplinkWithUriQueryParams(uri, ApplinkConstInternalTravel.DASHBOARD_FLIGHT));
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
        Intent intent = RouteManager.getIntent(context, ApplinkConst.LOGIN);
        context.startActivity(intent);
        context.finish();
    }

    private void openInvoice(Uri uriData) {
        Intent intent = RouteManager.getIntent(context, ApplinkConstInternalOrder.INVOICE);
        intent.putExtra(KEY_URL, uriData.toString());
        intent.putExtra(KEY_TITLE, "Invoice");
        context.startActivity(intent);
        context.finish();
    }

    @Override
    public void sendCampaignGTM(Activity activity, String campaignUri, String screenName, boolean isAmp) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(activity, Uri.parse(campaignUri), isAmp);
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
        String overrideUrl = uriData.getQueryParameter(OVERRIDE_URL);
        String allowOverride = uriData.getQueryParameter(ALLOW_OVERRIDE);
        if ("1".equalsIgnoreCase(overrideUrl) ||
                "true".equalsIgnoreCase(overrideUrl) ||
                "1".equalsIgnoreCase(allowOverride) ||
                "true".equalsIgnoreCase(allowOverride)) {
            isAllowOverride = true;
        }
        openWebView(uriData, isAllowOverride,
                uriData.getQueryParameter(PARAM_TITLEBAR) == null ||
                        "true".equalsIgnoreCase(uriData.getQueryParameter(PARAM_TITLEBAR)),
                uriData.getQueryParameter(PARAM_NEED_LOGIN) != null &&
                        "true".equalsIgnoreCase(uriData.getQueryParameter(PARAM_NEED_LOGIN))
        );
        context.finish();
    }

    private void openWebView(Uri uri, boolean allowingOverriding, boolean showTitlebar,
                             boolean needLogin) {
        String encodedUri = UrlEncoderExtKt.encodeOnce(uri.toString());
        Intent intent = RouteManager.getIntentNoFallback(context, ApplinkConstInternalGlobal.WEBVIEW,
                encodedUri);
        if (intent != null) {
            intent.putExtra(KEY_ALLOW_OVERRIDE, allowingOverriding);
            intent.putExtra(KEY_NEED_LOGIN, needLogin);
            intent.putExtra(KEY_TITLEBAR, showTitlebar);
            context.startActivity(intent);
            context.finish();
        }
    }

    private void openShopInfo(final List<String> linkSegment, final Uri uriData, Bundle bundle) {
        getShopInfoUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(linkSegment.get(0), userSession.getUserId(), userSession.getDeviceId()), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (context != null) {
                    prepareOpenWebView(uriData);
                }
                if (e instanceof ResponseV4ErrorException) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "OneSegment");
                    messageMap.put("link_segment", linkSegment.get(0));
                    messageMap.put("uri", uriData.toString());
                    ServerLogger.log(Priority.P1, "DEEPLINK_OPEN_WEBVIEW", messageMap);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                if (context != null) {
                    if (shopInfo != null && shopInfo.getInfo() != null) {
                        String shopId = "";
                        if(shopInfo.getInfo().getShopId() != null){
                            shopId = shopInfo.getInfo().getShopId();
                        }
                        String lastSegment = linkSegment.get(linkSegment.size() - 1);
                        if (shopId.equals(ApplinkConst.TokoNow.TOKONOW_PRODUCTION_SHOP_ID)) {
                            RouteManager.route(context,
                                    bundle,
                                    ApplinkConst.TokoNow.HOME);
                        } else if (isEtalase(linkSegment)) {
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
                        } else if (isShopReview(linkSegment)) {
                            RouteManager.route(context,
                                    bundle,
                                    ApplinkConst.SHOP_REVIEW,
                                    shopId);
                        } else if (isShopProduct(linkSegment)) {
                            RouteManager.route(context,
                                    bundle,
                                    ApplinkConst.SHOP_PRODUCT,
                                    shopId);
                        } else if (isShopFeed(linkSegment)) {
                            RouteManager.route(context,
                                    bundle,
                                    ApplinkConst.SHOP_FEED,
                                    shopId);
                        } else {
                            Intent intent = RouteManager.getIntent(context, ApplinkConst.SHOP, shopId);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }

                        context.finish();
                    } else {
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("type", "OneSegment");
                        messageMap.put("link_segment", linkSegment.get(0));
                        messageMap.put("uri", uriData.toString());
                        ServerLogger.log(Priority.P1, "DEEPLINK_OPEN_WEBVIEW", messageMap);
                        if (!GlobalConfig.DEBUG) {
                            crashlytics.recordException(new ShopNotFoundException(linkSegment.get(0)));
                        }
                        prepareOpenWebView(uriData);
                    }
                }
            }
        });
    }

    private boolean isEtalase(List<String> linkSegment) {
        String lastSegment = linkSegment.get(linkSegment.size() - 1);
        return lastSegment.equals("preorder")
                || lastSegment.equals("sold")
                || lastSegment.equals("discount")
                || (linkSegment.size() > 1 && (linkSegment.get(1).equals("etalase") || linkSegment.get(1).equals("campaign")));
    }

    private boolean isShopHome(List<String> linkSegment) {
        String lastSegment = linkSegment.get(linkSegment.size() - 1);
        return lastSegment.equalsIgnoreCase("home");
    }

    private boolean isShopReview(List<String> linkSegment) {
        String lastSegment = linkSegment.get(linkSegment.size() - 1);
        return lastSegment.equalsIgnoreCase("review");
    }

    private boolean isShopProduct(List<String> linkSegment) {
        String lastSegment = linkSegment.get(linkSegment.size() - 1);
        return lastSegment.equalsIgnoreCase("product");
    }

    private boolean isShopFeed(List<String> linkSegment) {
        String lastSegment = linkSegment.get(linkSegment.size() - 1);
        return lastSegment.equalsIgnoreCase("feed");
    }

    private void openHomeRecommendation(final List<String> linkSegment, final Uri uriData, Bundle bundle) {
        String internalSource = uriData.getQueryParameter("search_ref");
        String source = uriData.getQueryParameter("ref");
        String productId = linkSegment.size() > 1 ? linkSegment.get(1) : "";
        Intent intent = RouteManager.getIntent(context, ApplinkConsInternalHome.HOME_RECOMMENDATION);
        intent.putExtra(context.getString(R.string.home_recommendation_extra_product_id), productId);
        intent.putExtra(context.getString(R.string.home_recommendation_extra_ref), source == null ? "" : source);
        intent.putExtra(context.getString(R.string.home_recommendation_extra_internal_ref), source == null ? "" : internalSource);
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
        getShopInfoUseCase.execute(GetShopInfoByDomainUseCase.createRequestParam(linkSegment.get(0), userSession.getUserId(), userSession.getDeviceId()), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (context != null) {
                    Intent intent = BaseDownloadAppLinkActivity.newIntent(context, uriData.toString(), true);
                    context.startActivity(intent);
                    context.finish();
                }
                if (e instanceof ResponseV4ErrorException) {
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("type", "TwoSegments");
                    messageMap.put("link_segment", linkSegment.get(0) + "/" + linkSegment.get(1));
                    messageMap.put("uri", uriData.toString());
                    ServerLogger.log(Priority.P1, "DEEPLINK_OPEN_WEBVIEW", messageMap);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                if (context != null) {
                    if (shopInfo != null && shopInfo.getInfo() != null) {
                        //Add Affiliate string for tracking
                        String affiliateString = "";
                        String layoutTesting = "";
                        if (!TextUtils.isEmpty(uriData.getQueryParameter("aff"))) {
                            affiliateString = uriData.getQueryParameter("aff");
                        }

                        if (!TextUtils.isEmpty(uriData.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_LAYOUT_ID))) {
                            layoutTesting = uriData.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_LAYOUT_ID);
                        }

                        Intent productIntent = RouteManager.getIntent(context,
                                ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN_WITH_AFFILIATE,
                                linkSegment.get(0),
                                linkSegment.get(1),
                                affiliateString);
                        productIntent.putExtra("layoutID", layoutTesting);
                        context.startActivity(productIntent);
                    } else {
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("type", "TwoSegments");
                        messageMap.put("link_segment", linkSegment.get(0) + "/" + linkSegment.get(1));
                        messageMap.put("uri", uriData.toString());
                        ServerLogger.log(Priority.P1, "DEEPLINK_OPEN_WEBVIEW", messageMap);
                        if (!GlobalConfig.DEBUG) {
                            crashlytics.recordException(new ShopNotFoundException(linkSegment.get(0)));
                            crashlytics.recordException(new ProductNotFoundException(linkSegment.get(0) + "/" + linkSegment.get(1)));
                        }
                        Intent intent = BaseDownloadAppLinkActivity.newIntent(context, uriData.toString(), true);
                        context.startActivity(intent);
                    }
                    context.finish();
                }
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
            RouteManager.route(context, DeeplinkMapper.getRegisteredNavigation(context, ApplinkConst.DISCOVERY_CATALOG + "/" + catalogId));
        } catch (Exception e) {
            crashlytics.log(e.getLocalizedMessage());
        }
    }

    private void openHotProduct(List<String> linkSegment, Uri uriData) {
        if (linkSegment.size() > 1) {
            String query = linkSegment.get(1).replace("-", "+");
            RouteManager.route(context, DeeplinkMapper.getRegisteredNavigation(context, ApplinkConst.DISCOVERY_SEARCH + "?q=" + query));
        }
    }

    private void openHomepage(Bundle defaultBundle) {
        Intent intent = new Intent(context, ((com.tokopedia.core.TkpdCoreRouter) context.getApplication()).getHomeClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(defaultBundle);
        context.startActivity(intent);
    }

    private void openDiscoveryPage(String url, Bundle bundle) {
        openInternalDeeplink(url, bundle);
        context.finish();
    }

    private void openHomepageHot(Bundle bundle) {
        Intent intent = ((com.tokopedia.core.TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_INIT_FRAGMENT, INIT_STATE_FRAGMENT_HOTLIST);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void openInternalDeeplink(String uriData, Bundle bundle) {
        Uri uri = Uri.parse(uriData);
        String deeplink = DeeplinkConstant.SCHEME_TOKOPEDIA + ":/" + uri.getPath();
        if (uri.getQuery() != null) {
            deeplink = deeplink + "?" + uri.getQuery();
        }
        RouteManager.route(context, deeplink);
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
        } else {
            String deeplink = UriUtil.buildUri(ApplinkConstInternalCategory.INTERNAL_CATEGORY_DETAIL, departmentId);
            RouteManager.route(context, deeplink);
        }
    }

    private static String constructSearchApplink(Uri uriData) {
        String q = uriData.getQueryParameter("q");

        String applink = TextUtils.isEmpty(q) ?
                ApplinkConstInternalDiscovery.AUTOCOMPLETE :
                ApplinkConstInternalDiscovery.SEARCH_RESULT;

        return applink + "?" + uriData.getQuery();
    }

    private boolean isHotAlias(Uri uri) {
        return uri.getQueryParameter("alk") != null;
    }

    @Override
    public void processAFlistener() {
        AppsFlyerLib.getInstance().registerConversionListener(context, new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                if (map.size() > 0) {
                    if (map.get("link") != null) {
                        String oriUri = map.get("link").toString();
                        processDeepLinkAction(context, DeeplinkUTMUtils.simplifyUrl(oriUri), false);
                    }
                }
            }

            @Override
            public void onConversionDataFail(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

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

    @Override
    public void sendAuthenticatedEvent(Uri uriData, Campaign campaign, String screenName) {
        Map<String, Object> campaignMap = campaign.getCampaign();
        if (!TrackingUtils.isValidCampaign(campaignMap)) return;
        try {
            URL obtainedURL = new URL(uriData.toString());
            Map<String, String> customDimension = new HashMap<>();
            customDimension.put(Authenticated.KEY_DEEPLINK_URL, obtainedURL.toString());
            String utmSource = (String) campaignMap.get(AppEventTracking.GTM.UTM_SOURCE);
            String utmMedium = (String) campaignMap.get(AppEventTracking.GTM.UTM_MEDIUM);
            customDimension.put("utmSource", utmSource);
            customDimension.put("utmMedium", utmMedium);

            Object xClid = campaignMap.get(AppEventTracking.GTM.X_CLID);
            if (xClid != null && xClid instanceof String) {
                String xClid_ = (String) xClid;
                customDimension.put(AppEventTracking.GTM.X_CLID, xClid_);
            }
            TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName, customDimension);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}