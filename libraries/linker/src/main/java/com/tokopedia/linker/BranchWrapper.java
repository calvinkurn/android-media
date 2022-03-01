package com.tokopedia.linker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.deprecated.LocalCacheHandler;
import com.tokopedia.linker.helper.BranchHelper;
import com.tokopedia.linker.helper.RechargeBranchHelper;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.interfaces.WrapperInterface;
import com.tokopedia.linker.model.LinkerCommerceData;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerShareData;
import com.tokopedia.linker.model.RechargeLinkerData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.linker.requests.LinkerDeeplinkRequest;
import com.tokopedia.linker.requests.LinkerGenericRequest;
import com.tokopedia.linker.requests.LinkerShareRequest;
import com.tokopedia.linker.validation.BranchHelperValidation;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.ServerRequestGetLATD;
import io.branch.referral.util.LinkProperties;
import timber.log.Timber;


public class BranchWrapper implements WrapperInterface {

    private String deferredDeeplinkPath;
    private static boolean isBranchInitialized = false;
    private RemoteConfig remoteConfig;
    private static Boolean APP_OPEN_FROM_BRANCH_LINK = false;
    private final String APP_BRANCH_CALLBACK_TIMEOUT_KEY = "android_branch_callback_timeout_key";
    private final String APP_ENABLE_BRANCH_VALID_CAMPAIGN_LOGGING = "android_enable_branch_valid_campaign_logging";
    private android.os.Handler handler;
    private String KEY_BRANCH_IO_PREF_FILE_NAME = "branch_io_pref";
    private String KEY_APP_FIRST_OPEN = "app_first_open";
    private LocalCacheHandler localCacheHandler;
    private boolean lastFirstOpenUpdatedValue;

    @Override
    public void init(Context context) {
        if (Branch.getInstance() == null) {
            Branch.getAutoInstance(context);
            if (GlobalConfig.isAllowDebuggingTools()) {
                Branch.enableLogging();
            }
            sendPreInstallData(context);
        }
    }

    private boolean isXiaomiPreInstallApp(String pkgName) {
        try {
            Class<?> miui = Class.forName("miui.os.MiuiInit");
            Method method = miui.getMethod("isPreinstalledPAIPackage", String.class);
            return (Boolean) method.invoke(null, pkgName);
        }catch(Exception ex){
            Timber.w("P2#PRE_INSTALL_XIAOMI#error;error='%s'", ex.getMessage());
        }
        return false;
    }

    private void sendPreInstallData(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (remoteConfig.getBoolean(LinkerConstants.ENABLE_XIAOMI_PAI_TRACKING, true) && isXiaomiPreInstallApp(context.getPackageName())) {
            Branch.getInstance().setPreinstallCampaign("xiaomipreinstallol-dp_int-tp-10001511-0000-alon-alon");
            Branch.getInstance().setPreinstallPartner("a_custom_885438735322423255");
        }
    }

    @Override
    public String getDefferedDeeplinkForSession() {
        return deferredDeeplinkPath;
    }

    @Override
    public void createShareUrl(LinkerShareRequest linkerShareRequest, Context context) {
        if (linkerShareRequest != null && linkerShareRequest.getDataObj() != null && linkerShareRequest.getDataObj() instanceof LinkerShareData) {

            if(((LinkerShareData)linkerShareRequest.getDataObj()).getLinkerData().isAffiliate()){
                generateAffiliateLink(((LinkerShareData) linkerShareRequest.getDataObj()).getLinkerData(),
                        context, linkerShareRequest.getShareCallbackInterface(),
                        ((LinkerShareData) linkerShareRequest.getDataObj()).getUserData());
            }
            else if (isFDLActivated(context)){
                generateFirebaseLink(((LinkerShareData) linkerShareRequest.getDataObj()).getLinkerData(),
                        context, linkerShareRequest.getShareCallbackInterface(),
                        ((LinkerShareData) linkerShareRequest.getDataObj()).getUserData());
            }else {
                generateBranchLink(((LinkerShareData) linkerShareRequest.getDataObj()).getLinkerData(),
                        context, linkerShareRequest.getShareCallbackInterface(),
                        ((LinkerShareData) linkerShareRequest.getDataObj()).getUserData());
            }
        }
    }

    @Override
    public void handleDefferedDeeplink(LinkerDeeplinkRequest linkerDeeplinkRequest, Context context) {
        Branch branch = Branch.getInstance();
        checkBranchLinkUTMParams(((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getActivity());
        handleDeferredDeeplinkFDL(linkerDeeplinkRequest);
        if (branch == null) {
            if (linkerDeeplinkRequest != null && linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                linkerDeeplinkRequest.getDefferedDeeplinkCallback().onError(
                        LinkerUtils.createLinkerError(BranchError.ERR_BRANCH_INIT_FAILED, null));
            }
        } else {
            try {
                if (linkerDeeplinkRequest != null && linkerDeeplinkRequest.getDataObj() != null &&
                        linkerDeeplinkRequest.getDataObj() instanceof LinkerDeeplinkData) {
                    if (isBranchInitialized) {
                        Branch.sessionBuilder(((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getActivity())
                                .withCallback(getBranchCallback(linkerDeeplinkRequest, context))
                                .withData(((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getReferrable())
                                .reInit();
                    } else {
                        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
                        if (remoteConfig.getBoolean(LinkerConstants.enableBranchReinitFlow)) {
                            isBranchInitialized = true;
                        }
                        Branch.sessionBuilder(((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getActivity()).withCallback(getBranchCallback(linkerDeeplinkRequest, context)).
                                withData(((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getReferrable()).init();
                    }
                }
            } catch (Exception e) {
                if (linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                    linkerDeeplinkRequest.getDefferedDeeplinkCallback().onError(
                            LinkerUtils.createLinkerError(LinkerConstants.ERROR_SOMETHING_WENT_WRONG, null));
                }
            }
        }
    }

    private void handleDeferredDeeplinkFDL(LinkerDeeplinkRequest linkerDeeplinkRequest) {
        new FirebaseDLWrapper().getFirebaseDynamicLink(((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getActivity(), ((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getActivity().getIntent());
    }

    private Branch.BranchReferralInitListener getBranchCallback(LinkerDeeplinkRequest linkerDeeplinkRequest, Context context) {
        return new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    String deeplink = referringParams.optString(LinkerConstants.KEY_ANDROID_DEEPLINK_PATH);
                    String promoCode = referringParams.optString(LinkerConstants.BRANCH_PROMOCODE_KEY);

                    if (!deeplink.startsWith(LinkerConstants.APPLINKS + "://") &&
                            !TextUtils.isEmpty(deeplink)) {
                        deferredDeeplinkPath = LinkerConstants.APPLINKS + "://" + deeplink;
                    }
                    else {
                        deferredDeeplinkPath = deeplink;
                    }
                    if (linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                        linkerDeeplinkRequest.getDefferedDeeplinkCallback().onDeeplinkSuccess(
                                LinkerUtils.createDeeplinkData(deeplink, promoCode));
                    }
                    checkAndSendUtmParams(context, referringParams);
                    if (!TextUtils.isEmpty(deeplink)) {
                        logNonBranchLinkData(context, referringParams);
                    }
                } else {
                    if (linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                        linkerDeeplinkRequest.getDefferedDeeplinkCallback().onError(
                                LinkerUtils.createLinkerError(BranchError.ERR_BRANCH_NO_SHARE_OPTION, null));
                    }
                }
                //this method always call after needSkipDeeplinkFromNonBranch()
                updateFirstOpenCache(context);
            }
        };
    }

    private Branch.BranchReferralInitListener getBranchCallbackForUtmParams(Context context, boolean uriHaveCampaignData) {
        return new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    if(!uriHaveCampaignData && referringParams != null && referringParams.optBoolean("+clicked_branch_link")) {
                        sendUtmParameters(context, referringParams);
                    }else {
                        logNonBranchLinkData(context, referringParams);
                    }
                }
            }
        };
    }

    private void sendUtmParameters(Context context, JSONObject referringParams){
        String utmSource;
        String utmCampaign;
        String utmMedium;
        String utmTerm = null;
        String clickTime;
        utmSource = referringParams.optString(LinkerConstants.UTM_SOURCE);
        if (!TextUtils.isEmpty(utmSource)) {
            utmCampaign = referringParams.optString(LinkerConstants.UTM_CAMPAIGN);
            utmMedium = referringParams.optString(LinkerConstants.UTM_MEDIUM);
            utmTerm = referringParams.optString(LinkerConstants.UTM_TERM);
        } else {
            utmSource = referringParams.optString(LinkerConstants.BRANCH_UTM_SOURCE);
            utmCampaign = referringParams.optString(LinkerConstants.BRANCH_CAMPAIGN);
            utmMedium = referringParams.optString(LinkerConstants.BRANCH_UTM_MEDIUM);
        }
        clickTime = referringParams.optString(LinkerConstants.CLICK_TIME);
        convertToCampaign(context, utmSource, utmCampaign, utmMedium, utmTerm, clickTime);
    }

    private void fetchLastAttributeTouchData() {
        Branch.getInstance().getLastAttributedTouchData(
                new ServerRequestGetLATD.BranchLastAttributedTouchDataListener() {
                    @Override
                    public void onDataFetched(JSONObject jsonObject, BranchError error) {
                        if (error == null) {

                        }
                    }
                });
    }

    @Override
    public void setGaClientId(String gaClientId) {
        Branch branch = Branch.getInstance();
        branch.setRequestMetadata(LinkerConstants.KEY_GA_CLIENT_ID, gaClientId);
    }

    @Override
    public void initSession() {
        Branch.getInstance().initSession();
    }

    @Override
    public void initSession(Activity activity, boolean uriHaveCampaignData){
        checkBranchLinkUTMParams(activity);
        Branch.sessionBuilder(activity)
                .withCallback(getBranchCallbackForUtmParams(activity, uriHaveCampaignData))
                .init();
    }

    @Override
    public void sendEvent(LinkerGenericRequest linkerGenericRequest, Context context) {
        switch (linkerGenericRequest.getEventId()) {
            case LinkerConstants.EVENT_USER_IDENTITY:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof UserData) {
                    UserData userData = (UserData) linkerGenericRequest.getDataObj();
                    if (userData != null) {
                        String userId = userData.getUserId();
                        if (userId != null && !userId.isEmpty() && !(" ".equals(userId))) {
                            BranchHelper.sendIdentityEvent(userId);
                        }
                    }
                }
                break;
            case LinkerConstants.EVENT_USER_REGISTRATION_VAL:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof UserData) {
                    BranchHelper.sendRegisterEvent(
                            (UserData) linkerGenericRequest.getDataObj(),
                            context);
                }
                break;
            case LinkerConstants.EVENT_LOGOUT_VAL:
                BranchHelper.sendLogoutEvent();
                break;
            case LinkerConstants.EVENT_LOGIN_VAL:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof UserData) {
                    BranchHelper.sendLoginEvent(context, (UserData) linkerGenericRequest.getDataObj());
                }
                break;
            case LinkerConstants.EVENT_COMMERCE_VAL:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof LinkerCommerceData) {
                    BranchHelper.sendCommerceEvent(
                            ((LinkerCommerceData) linkerGenericRequest.getDataObj()).getPaymentData(),
                            context, ((LinkerCommerceData) linkerGenericRequest.getDataObj()).getUserData());
                }
                break;
            case LinkerConstants.EVENT_ITEM_VIEW:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof LinkerData) {
                    BranchHelper.sendItemViewEvent(context, (LinkerData) linkerGenericRequest.getDataObj());
                }
                break;
            case LinkerConstants.EVENT_ADD_TO_CART:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof LinkerData) {
                    BranchHelper.sendAddToCartEvent(context, (LinkerData) linkerGenericRequest.getDataObj());
                }
                break;
            case LinkerConstants.EVENT_ADD_TO_WHISHLIST:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof LinkerData) {
                    BranchHelper.sendAddToWishListEvent(context, (LinkerData) linkerGenericRequest.getDataObj());
                }
                break;
            case LinkerConstants.EVENT_PURCHASE_FLIGHT:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof LinkerData) {
                    BranchHelper.sendFlightPurchaseEvent(context, (LinkerData) linkerGenericRequest.getDataObj());
                }
                break;
            // Recharge
            case LinkerConstants.EVENT_DIGITAL_HOMEPAGE:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof LinkerData) {
                    RechargeBranchHelper.sendDigitalHomepageLaunchEvent(context,
                            (LinkerData) linkerGenericRequest.getDataObj()
                    );
                }
                break;
            case LinkerConstants.EVENT_DIGITAL_SCREEN_LAUNCH:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof RechargeLinkerData) {
                    RechargeBranchHelper.sendDigitalScreenLaunchEvent(context,
                            (RechargeLinkerData) linkerGenericRequest.getDataObj()
                    );
                }
                break;
            case LinkerConstants.EVENT_SEARCH:
                if (linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof ArrayList) {
                    BranchHelper.sendSearchEvent(context, (ArrayList<String>) linkerGenericRequest.getDataObj());
                }
                break;
        }
    }

    private BranchUniversalObject createBranchUniversalObject(LinkerData data) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(data.getId() == null ? data.getType() : data.getId())
                .setTitle(data.getName())
                .setContentDescription(data.getDescription())
                .setContentImageUrl(data.getImgUri())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
        return branchUniversalObject;
    }

    private void generateAffiliateLink(){

    }

    private void generateFirebaseLink(final LinkerData data, final Context context,
                                      final ShareCallback shareCallback, final UserData userData) {
        new FirebaseDLWrapper().createShortLink(shareCallback,data);

    }

    private void generateAffiliateLink(final LinkerData data, final Context context,
                                      final ShareCallback shareCallback, final UserData userData) {
        new AffiliateWrapper().executeAffiliateUseCase(data, shareCallback, context);
    }

    private void generateBranchLink(final LinkerData data, final Context context,
                                    final ShareCallback shareCallback, final UserData userData) {

        if (isBranchUrlActivated(context, data.getType()) && !LinkerData.RIDE_TYPE.equalsIgnoreCase(data.getType())) {
            if (LinkerData.REFERRAL_TYPE.equalsIgnoreCase(data.getType()) && !TextUtils.isEmpty(data.getshareUrl())) {
                if (shareCallback != null) {
                    shareCallback.urlCreated(LinkerUtils.createShareResult(
                            data.getTextContentForBranch(""), data.getTextContentForBranch(""), data.getshareUrl()));
                }
            } else {
                BranchUniversalObject branchUniversalObject = createBranchUniversalObject(data);
                LinkProperties linkProperties = createLinkProperties(data, data.getSource(), context, userData);
                setBranchCallbackTimeOutFunction(shareCallback, data, getRemoteConfigTimeOutValue(context, APP_BRANCH_CALLBACK_TIMEOUT_KEY));
                branchUniversalObject.generateShortUrl(context, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        removeHandlerTimeoutMessage();
                        if (error == null) {
                            if (shareCallback != null) {
                                if(!TextUtils.isEmpty(url)) {
                                    shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContentForBranch(url), url, url));
                                } else {
                                    shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContent(), data.renderShareUri(), data.renderShareUri()));
                                }
                            }
                        } else {
                            if (shareCallback != null) {
                                if (data.isThrowOnError()) {
                                    shareCallback.onError(LinkerUtils.createLinkerError(LinkerConstants.ERROR_SOMETHING_WENT_WRONG, null));
                                } else {
                                    shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContent(), getFallbackUrl(data), getFallbackUrl(data)));
                                }
                            }
                        }
                    }
                });
            }
        } else {
            shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContent(), data.getDesktopUrl(), data.getDesktopUrl()));
        }
    }

    private String getFallbackUrl(LinkerData data){
        String fallbackUrl = data.renderShareUri();
        if(TextUtils.isEmpty(fallbackUrl)
                && !TextUtils.isEmpty(data.getDesktopUrl())){
            fallbackUrl = data.getDesktopUrl();
        }
        return fallbackUrl;
    }

    private LinkProperties createLinkProperties(LinkerData data, String channel, Context context, UserData userData) {
        LinkProperties linkProperties = new LinkProperties();
        String deeplinkPath = getApplinkPath(data.renderShareUri(), "");
        String desktopUrl = data.getDesktopUrl();

        BranchHelper.setCommonLinkProperties(linkProperties, data);

        if (LinkerData.PRODUCT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.PRODUCT_INFO, data.getId());
        } else if (LinkerData.SHOP_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.SHOP, data.getId());//"shop/" + data.getId();
        } else if (LinkerData.HOTLIST_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.DISCOVERY_HOTLIST_DETAIL, data.getId());//"hot/" + data.getId();
        } else if (LinkerData.CATALOG_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.DISCOVERY_CATALOG, data.getId());
        } else if (LinkerData.PROMO_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.PROMO_DETAIL, data.getId());
        } else if (LinkerData.PLAY_BROADCASTER.equalsIgnoreCase(data.getType())) {
            deeplinkPath = data.getUri();
        } else if (LinkerData.PLAY_VIEWER.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.PLAY, data.getId());
        } else if (LinkerData.MERCHANT_VOUCHER.equalsIgnoreCase(data.getType())) {
            deeplinkPath = data.getDeepLink();
        } else if (isAppShowReferralButtonActivated(context) && LinkerData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.REFERRAL_WELCOME, data.getId());
            if (userData != null) {
                deeplinkPath = deeplinkPath.replaceFirst(LinkerConstants.REGEX_APP_LINK,
                        userData.getName() == null ? "" : userData.getName());
            }
        } else if (LinkerData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.GROUPCHAT, data.getId());
            if (context.getApplicationContext() instanceof LinkerRouter) {
                desktopUrl = LinkerConstants.DESKTOP_GROUPCHAT_URL;
                linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, desktopUrl);
                linkProperties.addControlParameter(LinkerConstants.ANDROID_DESKTOP_URL_KEY, desktopUrl);
                linkProperties.addControlParameter(LinkerConstants.IOS_DESKTOP_URL_KEY, desktopUrl);
                linkProperties.addTag(String.format(LinkerConstants.STRING_FORMAT_TAG, data.getId(), data.getSource()));
                linkProperties.setFeature(data.getPrice());
                linkProperties.setCampaign(String.format(LinkerConstants.STRING_FORMAT_TAG, data.getType(), data.getId()));
                linkProperties.setChannel(String.format(LinkerConstants.STRING_FORMAT_CHANNEL, data.getType()));
                linkProperties.addControlParameter(LinkerConstants.KEY_URI_REDIRECT_MODE, LinkerConstants.VALUE_URI_REDIRECT_MODE);
            }
        } else if (LinkerData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = data.getDeepLink();
        } else if (LinkerData.PLAY_BROADCASTER.equalsIgnoreCase(data.getType()) ||
                    LinkerData.PLAY_VIEWER.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(LinkerConstants.ANDROID_DESKTOP_URL_KEY, desktopUrl);
            linkProperties.addControlParameter(LinkerConstants.IOS_DESKTOP_URL_KEY, desktopUrl);
        } else if (LinkerData.HOTEL_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.setFeature(LinkerConstants.FEATURE_TYPE_HOTEL);
            linkProperties.addTag(LinkerConstants.HOTEL_LABEL);
            linkProperties.addTag(LinkerConstants.PDP_LABEL);
            if (!desktopUrl.isEmpty()) {
                linkProperties.addControlParameter(LinkerConstants.ANDROID_DESKTOP_URL_KEY, desktopUrl);
                linkProperties.addControlParameter(LinkerConstants.IOS_DESKTOP_URL_KEY, desktopUrl);
            }
            if (!data.getCustmMsg().isEmpty()) linkProperties.addTag(data.getCustmMsg());
            linkProperties.setCampaign(LinkerConstants.SHARE_LABEL);
            deeplinkPath = data.getDeepLink();
        } else if (LinkerData.ENTERTAINMENT_TYPE.equalsIgnoreCase(data.getType())){
            if (!desktopUrl.isEmpty()) {
                linkProperties.addControlParameter(LinkerConstants.ANDROID_DESKTOP_URL_KEY, desktopUrl);
                linkProperties.addControlParameter(LinkerConstants.IOS_DESKTOP_URL_KEY, desktopUrl);
            }
            deeplinkPath = data.getDeepLink();
        }else if (LinkerData.USER_PROFILE_SOCIAL.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.USER_PROFILE_SOCIAL, data.getId());
        } else if (LinkerData.FEED_TYPE.equalsIgnoreCase(data.getType()) && !TextUtils.isEmpty(data.getDeepLink())){
            deeplinkPath = data.getDeepLink();
        }

        if (LinkerData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, LinkerConstants.CHALLENGES_DESKTOP_URL);
        } else if (LinkerData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, LinkerConstants.REFERRAL_DESKTOP_URL);
        } else if (!TextUtils.isEmpty(desktopUrl)) {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, desktopUrl);
        } else {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, data.renderShareUri());
        }

        linkProperties.addControlParameter(LinkerConstants.KEY_ANDROID_DEEPLINK_PATH, deeplinkPath == null ? "" : deeplinkPath);
        linkProperties.addControlParameter(LinkerConstants.KEY_IOS_DEEPLINK_PATH, deeplinkPath == null ? "" : deeplinkPath);

        if (isAndroidIosUrlActivated(context) && !(LinkerData.REFERRAL_TYPE.equalsIgnoreCase(data.getType()) ||
                LinkerData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType()) ||
                LinkerData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType()) ||
                LinkerData.PLAY_BROADCASTER.equalsIgnoreCase(data.getType()))) {
            linkProperties.addControlParameter(LinkerConstants.ANDROID_DESKTOP_URL_KEY, data.renderShareUri());
            linkProperties.addControlParameter(LinkerConstants.IOS_DESKTOP_URL_KEY, data.renderShareUri());
        }

        if (LinkerData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType())) {
            String connector;
            String renderedUrl;
            String tempUri = data.getUri();

            if (tempUri.contains("?")) {
                connector = "&";
            } else {
                connector = "?";
            }
            String tags = "";
            if (linkProperties.getTags().size() > 0) {
                tags = linkProperties.getTags().get(0);
            }
            Uri uri = Uri.parse(String.format(LinkerConstants.STRING_FORMAT_GROUP_CHAT,
                    tempUri, connector, linkProperties.getChannel(), linkProperties.getFeature(), linkProperties.getCampaign(), tags));
            try {
                renderedUrl = uri.toString().replace(" ", "%20");
            } catch (Exception e) {
                e.printStackTrace();
                renderedUrl = uri.toString();
            }

            String temp = String.format(LinkerConstants.STRING_FORMAT_UTM,
                    linkProperties.getChannel(), linkProperties.getFeature(), linkProperties.getCampaign(), tags);
            try {
                temp = URLEncoder.encode(temp, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                temp = temp.replace(" ", "");
            }
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, String.format(LinkerConstants.STRING_FORMAT_DESKTOP_URL, desktopUrl, temp));
            linkProperties.addControlParameter(LinkerConstants.KEY_ANDROID_DEEPLINK_PATH, renderedUrl);
            linkProperties.addControlParameter(LinkerConstants.KEY_IOS_DEEPLINK_PATH, renderedUrl);
        }

        return linkProperties;
    }

    private static boolean isBranchUrlActivated(Context context, String type) {
        if (LinkerData.APP_SHARE_TYPE.equalsIgnoreCase(type)
                || LinkerData.REFERRAL_TYPE.equalsIgnoreCase(type)
                || LinkerData.GROUPCHAT_TYPE.equalsIgnoreCase(type)
                || LinkerData.PLAY_BROADCASTER.equalsIgnoreCase(type)) {
            return true;
        } else {
            return ((LinkerRouter) context.getApplicationContext()).
                    getBooleanRemoteConfig(LinkerConstants.FEATURE_GATE_BRANCH_LINKS, true);
        }
    }

    private static String getApplinkPath(String url, String id) {
        if (url.contains(LinkerConstants.APPLINKS + "://")) {
            url = url.replace(LinkerConstants.APPLINKS + "://", "");
            url = url.replaceFirst(LinkerConstants.REGEX_APP_LINK, id == null ? "" : id);
        } else if (url.contains(LinkerConstants.WEB_DOMAIN)) {
            url = url.replace(LinkerConstants.WEB_DOMAIN, "");
        } else if (url.contains(LinkerConstants.MOBILE_DOMAIN)) {
            url = url.replace(LinkerConstants.MOBILE_DOMAIN, "");
        }
        return url;
    }

    public static Boolean isAndroidIosUrlActivated(Context context) {
        return ((LinkerRouter) context.getApplicationContext()).
                getBooleanRemoteConfig(LinkerConstants.FIREBASE_KEY_INCLUDEMOBILEWEB, true);
    }

    public static Boolean isAppShowReferralButtonActivated(Context context) {
        return ((LinkerRouter) context.getApplicationContext()).
                getBooleanRemoteConfig(LinkerConstants.APP_SHOW_REFERRAL_BUTTON, false);
    }


    private void checkAndSendUtmParams(Context context, JSONObject referringParams) {
        if (context == null) return;
        if (isSkipUtmEvent(context)) return;
        sendUtmParameters(context, referringParams);
    }

    private void convertToCampaign(Context context, String utmSource, String utmCampaign, String utmMedium, String utmTerm, String clickTime) {
        if (!(TextUtils.isEmpty(utmSource) || TextUtils.isEmpty(utmMedium))) {
            Map<String, Object> param = new HashMap<>();
            param.put(LinkerConstants.SCREEN_NAME_KEY, mapDeeplinkToScreenName());
            param.put(LinkerConstants.UTM_SOURCE, utmSource);
            param.put(LinkerConstants.UTM_CAMPAIGN, utmCampaign);
            param.put(LinkerConstants.UTM_MEDIUM, utmMedium);
            if (!TextUtils.isEmpty(utmTerm)) {
                param.put(LinkerConstants.UTM_TERM, utmTerm);
            }

            sendCampaignToTrackApp(context, param);
            logValidCampaignUtmParams(context, utmSource, utmMedium, utmCampaign, clickTime);
        }
    }

    private String mapDeeplinkToScreenName(){
        if(!TextUtils.isEmpty(getDefferedDeeplinkForSession())
                && getDefferedDeeplinkForSession().startsWith(LinkerConstants.TOKOPEDIA_SCHEME)
                && getDefferedDeeplinkForSession().contains(LinkerConstants.DISCOVERY_PATH)) {
            String[] deeplinkArray = getDefferedDeeplinkForSession().split(LinkerConstants.QUERY_PARAM_SEPARATOR);
            if (deeplinkArray.length > 0 && !TextUtils.isEmpty(deeplinkArray[0])) {
                return LinkerConstants.DEEPLINK_VALUE + deeplinkArray[0].replace(LinkerConstants.TOKOPEDIA_SCHEME, "");
            }
        }
        return LinkerConstants.SCREEN_NAME_VALUE;
    }

    private void sendCampaignToTrackApp(Context context, Map<String, Object> param) {
        if (isBranchUtmSupportActivated(context)) {
            TrackApp.getInstance().getGTM().sendCampaign(param);
        }
    }

    private void checkBranchLinkUTMParams(Activity activity) {
        APP_OPEN_FROM_BRANCH_LINK = false;
        if (activity != null && activity.getIntent().getData() != null){
            String intentDataStr = activity.getIntent().getData().toString();
            if(intentDataStr.contains(LinkerConstants.BRANCH_LINK_DOMAIN_1) ||
                    intentDataStr.contains(LinkerConstants.BRANCH_LINK_DOMAIN_2) ||
                    intentDataStr.contains(LinkerConstants.BRANCH_LINK_DOMAIN_3)) {
                APP_OPEN_FROM_BRANCH_LINK = true;
            }
        }
    }


    private Boolean isBranchUtmSupportActivated(Context context) {
        return getBooleanValue(context, RemoteConfigKey.ENABLE_BRANCH_UTM_SUPPORT, true);
    }

    private Boolean isBranchUtmOnlyBranchLinkActivated(Context context) {
        return getBooleanValue(context, RemoteConfigKey.ENABLE_BRANCH_UTM_ONLY_BRANCH_LINK, false);
    }

    private Boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        if (remoteConfig == null)
            remoteConfig = new FirebaseRemoteConfigImpl(context);
        return remoteConfig.getBoolean(key, defaultValue);

    }

    private boolean isSkipUtmEvent(Context context) {
        if(isFirstOpen(context)) return false;
        if (isBranchUtmOnlyBranchLinkActivated(context) && !APP_OPEN_FROM_BRANCH_LINK) {
            return true;
        }
        return false;
    }

    private void logNonBranchLinkData(Context context, JSONObject referringParams) {
        if (!APP_OPEN_FROM_BRANCH_LINK) {
            new BranchHelperValidation().logSkipDeeplinkNonBranchLink(referringParams, isFirstOpen(context));
        }

    }

    private void logValidCampaignUtmParams(Context context, String utmSource, String utmMedium, String utmCampaign, String clickTime) {
        if(getRemoteConfig(context).getBoolean(APP_ENABLE_BRANCH_VALID_CAMPAIGN_LOGGING, true)) {
            new BranchHelperValidation().logValidCampaignData(utmSource, utmMedium, utmCampaign, clickTime, isFirstOpen(context), APP_OPEN_FROM_BRANCH_LINK);
        }
    }

    private void updateFirstOpenCache(Context context) {
        if (isFirstOpen(context)) {
            getLocalCacheHandler(context).putBoolean(KEY_APP_FIRST_OPEN, true);
            getLocalCacheHandler(context).applyEditor();
        }
    }

    private boolean isFirstOpen(Context context) {
        if (!lastFirstOpenUpdatedValue) {
            lastFirstOpenUpdatedValue = getLocalCacheHandler(context).getBoolean(KEY_APP_FIRST_OPEN);
        }
        return !lastFirstOpenUpdatedValue;
    }

    private LocalCacheHandler getLocalCacheHandler(Context context) {
        if (localCacheHandler == null) {
            localCacheHandler = new LocalCacheHandler(context, KEY_BRANCH_IO_PREF_FILE_NAME);
        }
        return localCacheHandler;
    }

    private long getRemoteConfigTimeOutValue(Context context, String key){
        return getRemoteConfig(context).getLong(key, 5000);
    }

    private RemoteConfig getRemoteConfig(Context context){
        if(remoteConfig == null){
            remoteConfig = new FirebaseRemoteConfigImpl(context);
        }
        return remoteConfig;
    }

    private void setBranchCallbackTimeOutFunction(ShareCallback shareCallback, LinkerData data, long timeoutDuration){
        handler =  new android.os.Handler(Looper.getMainLooper());
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContent(), data.renderShareUri(), data.renderShareUri()));
                        Timber.w("P2#BRANCH_LINK_TIMEOUT#error;linkdata='%s'", data.getId());
                    }
                },
                timeoutDuration);
    }

    //Remove all the pending runnable calls
    private void removeHandlerTimeoutMessage(){
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    private Boolean isFDLActivated(Context context) {
        return ((LinkerRouter) context.getApplicationContext()).
                getBooleanRemoteConfig(LinkerConstants.FIREBASE_KEY_FDL_ENABLE, false);
    }
}