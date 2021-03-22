package com.tokopedia.linker;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
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
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.ServerRequestGetLATD;
import io.branch.referral.util.LinkProperties;


public class BranchWrapper implements WrapperInterface {

    private String deferredDeeplinkPath;
    private String DESKTOP_GROUPCHAT_URL = "https://www.tokopedia.com/play/redirect?plain=1&url=https://www.tokopedia.link/playblog?";
    private static boolean isBranchInitialized = false;

    @Override
    public void init(Context context) {
        if (Branch.getInstance() == null) {
            Branch.getAutoInstance(context);
            if (GlobalConfig.isAllowDebuggingTools()) {
                Branch.enableLogging();
            }
        }
    }

    @Override
    public String getDefferedDeeplinkForSession() {
        return deferredDeeplinkPath;
    }

    @Override
    public void createShareUrl(LinkerShareRequest linkerShareRequest, Context context) {
        if (linkerShareRequest != null && linkerShareRequest.getDataObj() != null && linkerShareRequest.getDataObj() instanceof LinkerShareData) {
            generateBranchLink(((LinkerShareData) linkerShareRequest.getDataObj()).getLinkerData(),
                    context, linkerShareRequest.getShareCallbackInterface(),
                    ((LinkerShareData) linkerShareRequest.getDataObj()).getUserData());
        }
    }

    @Override
    public void handleDefferedDeeplink(LinkerDeeplinkRequest linkerDeeplinkRequest, Context context) {
        Branch branch = Branch.getInstance();
        CheckBranchLinkUTMParams(linkerDeeplinkRequest);
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
                        isBranchInitialized = true;
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
                    if (linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                        linkerDeeplinkRequest.getDefferedDeeplinkCallback().onDeeplinkSuccess(
                                LinkerUtils.createDeeplinkData(deeplink, promoCode));
                    }

                    checkAndSendUtmParams(context, referringParams);
                } else {
                    if (linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                        linkerDeeplinkRequest.getDefferedDeeplinkCallback().onError(
                                LinkerUtils.createLinkerError(BranchError.ERR_BRANCH_NO_SHARE_OPTION, null));
                    }
                }
            }
        };
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
                branchUniversalObject.generateShortUrl(context, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            if (shareCallback != null) {
                                shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContentForBranch(url), url, url));
                            }
                        } else {
                            if (shareCallback != null) {
                                if (data.isThrowOnError()) {
                                    shareCallback.onError(LinkerUtils.createLinkerError(LinkerConstants.ERROR_SOMETHING_WENT_WRONG, null));
                                } else {
                                    shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContent(), data.getDesktopUrl(), data.getDesktopUrl()));
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
                desktopUrl = DESKTOP_GROUPCHAT_URL;
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
        } else if (LinkerData.PLAY_BROADCASTER.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(LinkerConstants.ANDROID_DESKTOP_URL_KEY, desktopUrl);
            linkProperties.addControlParameter(LinkerConstants.IOS_DESKTOP_URL_KEY, desktopUrl);
        } else if (LinkerData.HOTEL_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.setFeature(LinkerConstants.FEATURE_TYPE_HOTEL);
            linkProperties.addTag(LinkerConstants.HOTEL_LABEL);
            linkProperties.addTag(LinkerConstants.PDP_LABEL);
            if (!data.getCustmMsg().isEmpty()) linkProperties.addTag(data.getCustmMsg());
            linkProperties.setCampaign(LinkerConstants.SHARE_LABEL);
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
            String utmSource;
            String utmCampaign;
            String utmMedium;
            String utmTerm = null;
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
            convertToCampaign(context, utmSource, utmCampaign, utmMedium, utmTerm);


    }

    private void convertToCampaign(Context context, String utmSource, String utmCampaign, String utmMedium, String utmTerm) {
        if (context == null) return;
        if (!(TextUtils.isEmpty(utmSource) || TextUtils.isEmpty(utmMedium))) {
            Map<String, Object> param = new HashMap<>();
            param.put(LinkerConstants.SCREEN_NAME_KEY, LinkerConstants.SCREEN_NAME_VALUE);
            param.put(LinkerConstants.UTM_SOURCE, utmSource);
            param.put(LinkerConstants.UTM_CAMPAIGN, utmCampaign);
            param.put(LinkerConstants.UTM_MEDIUM, utmMedium);
            if (!TextUtils.isEmpty(utmTerm)) {
                param.put(LinkerConstants.UTM_TERM, utmTerm);
            }

            sendCampaignToGTM(context, param);
        }
    }

    private void sendCampaignToGTM(Context context, Map<String,Object> param) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        if (!(remoteConfig.getBoolean(RemoteConfigKey.ENABLE_BRANCH_UTM_ONLY_BRANCH_LINK) && LinkerUtils.APP_OPEN_FROM_BRANCH_LINK)) {
            return;
        }
        if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_BRANCH_UTM_SUPPORT)) {
            TrackApp.getInstance().getGTM().sendCampaign(param);
        }
    }

    private void CheckBranchLinkUTMParams(LinkerDeeplinkRequest linkerDeeplinkRequest){
        Activity activity= ((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getActivity();
        if(activity != null && activity.getIntent().getData()!= null && activity.getIntent().getData().toString().contains("tokopedia.link/")){
            if (DeeplinkUTMUtils.isValidCampaignUrl(activity.getIntent().getData())) {
                sendCampaignGTM(activity, activity.getIntent().getData().toString(), AppScreen.SCREEN_DEEPLINK_APPLINKHANDLER);
                LinkerUtils.APP_OPEN_FROM_BRANCH_LINK = true;
            }
        }
    }


    private void sendCampaignGTM(Activity activity, String campaignUri, String screenName) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(activity, Uri.parse(campaignUri));
        campaign.setScreenName(LinkerConstants.SCREEN_NAME_VALUE);
        sendCampaignToGTM(activity, campaign.getCampaign());
    }
}
