package com.tokopedia.linker;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.interfaces.WrapperInterface;
import com.tokopedia.linker.model.LinkerCommerceData;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerShareData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.linker.requests.LinkerDeeplinkRequest;
import com.tokopedia.linker.requests.LinkerGenericRequest;
import com.tokopedia.linker.requests.LinkerShareRequest;

import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;


public class BranchWrapper implements WrapperInterface {

    private String deferredDeeplinkPath;

    @Override
    public void init(Context context) {
        if(Branch.getInstance() == null) {
            Branch.enableBypassCurrentActivityIntentState();
            Branch.getAutoInstance(context);
        }
    }

    @Override
    public String getDefferedDeeplinkForSession() {
        return deferredDeeplinkPath;
    }

    @Override
    public void createShareUrl(LinkerShareRequest linkerShareRequest, Context context) {
        if(linkerShareRequest != null && linkerShareRequest.getDataObj() != null && linkerShareRequest.getDataObj() instanceof LinkerShareData) {
            generateBranchLink(((LinkerShareData) linkerShareRequest.getDataObj()).getLinkerData(),
                    context, linkerShareRequest.getShareCallbackInterface(),
                    ((LinkerShareData) linkerShareRequest.getDataObj()).getUserData());
        }
    }

    @Override
    public void handleDefferedDeeplink(LinkerDeeplinkRequest linkerDeeplinkRequest, Context context) {
        Branch branch = Branch.getInstance();
        if (branch == null) {
            if(linkerDeeplinkRequest != null && linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                linkerDeeplinkRequest.getDefferedDeeplinkCallback().onError(
                        LinkerUtils.createLinkerError(BranchError.ERR_BRANCH_INIT_FAILED, null));
            }
        } else {
            try {
                if(linkerDeeplinkRequest != null && linkerDeeplinkRequest.getDataObj() != null &&
                        linkerDeeplinkRequest.getDataObj() instanceof LinkerDeeplinkData) {
                    branch.setRequestMetadata(LinkerConstants.KEY_GA_CLIENT_ID,
                            ((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getClientId());
                    branch.initSession(new Branch.BranchReferralInitListener() {
                                           @Override
                                           public void onInitFinished(JSONObject referringParams, BranchError error) {
                                               if (error == null) {
                                                   String deeplink = referringParams.optString(LinkerConstants.KEY_ANDROID_DEEPLINK_PATH);
                                                   String promoCode = referringParams.optString(LinkerConstants.BRANCH_PROMOCODE_KEY);
                                                   if (!deeplink.startsWith(LinkerConstants.APPLINKS + "://")&&
                                                           !TextUtils.isEmpty(deeplink)) {
                                                       deferredDeeplinkPath = LinkerConstants.APPLINKS + "://" + deeplink;
                                                   }
                                                   if (linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                                                       linkerDeeplinkRequest.getDefferedDeeplinkCallback().onDeeplinkSuccess(
                                                               LinkerUtils.createDeeplinkData(deeplink, promoCode));
                                                   }
                                               } else {
                                                   if (linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                                                       linkerDeeplinkRequest.getDefferedDeeplinkCallback().onError(
                                                               LinkerUtils.createLinkerError(BranchError.ERR_BRANCH_NO_SHARE_OPTION, null));
                                                   }
                                               }
                                           }
                                       }, ((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getReferrable(),
                            ((LinkerDeeplinkData) linkerDeeplinkRequest.getDataObj()).getActivity());
                }
            } catch (Exception e) {
                if(linkerDeeplinkRequest.getDefferedDeeplinkCallback() != null) {
                    linkerDeeplinkRequest.getDefferedDeeplinkCallback().onError(
                            LinkerUtils.createLinkerError(LinkerConstants.ERROR_SOMETHING_WENT_WRONG, null));
                }
            }
        }
    }

    @Override
    public void sendEvent(LinkerGenericRequest linkerGenericRequest, Context context) {
        switch (linkerGenericRequest.getEventId()){
            case LinkerConstants.EVENT_USER_IDENTITY:
                if(linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
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
                if(linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
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
                if(linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof UserData) {
                    BranchHelper.sendLoginEvent(context, (UserData) linkerGenericRequest.getDataObj());
                }
                break;
            case LinkerConstants.EVENT_COMMERCE_VAL:
                if(linkerGenericRequest != null && linkerGenericRequest.getDataObj() != null &&
                        linkerGenericRequest.getDataObj() instanceof LinkerCommerceData) {
                    BranchHelper.sendCommerceEvent(
                            ((LinkerCommerceData) linkerGenericRequest.getDataObj()).getPaymentData(),
                            context, ((LinkerCommerceData) linkerGenericRequest.getDataObj()).getUserData());
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
                if(shareCallback != null) {
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
                            if(shareCallback != null) {
                                shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContentForBranch(url), url, url));
                            }
                        } else {
                            if(shareCallback != null) {
                                shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContent(), data.renderShareUri(), url));
                            }
                        }
                    }
                });
            }
        } else {
            shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContent(), data.renderShareUri(), data.renderShareUri()));
        }
    }

    private LinkProperties createLinkProperties(LinkerData data, String channel, Context context, UserData userData) {
        LinkProperties linkProperties = new LinkProperties();
        String deeplinkPath = getApplinkPath(data.renderShareUri(), "");
        String desktopUrl = data.getDesktopUrl();

        BranchHelper.setCommonLinkProperties(linkProperties, data);

        if (LinkerData.PRODUCT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.PRODUCT_INFO, data.getId());
        }  else if (LinkerData.SHOP_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.SHOP, data.getId());//"shop/" + data.getId();
        } else if (LinkerData.HOTLIST_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.DISCOVERY_HOTLIST_DETAIL, data.getId());//"hot/" + data.getId();
        } else if (LinkerData.CATALOG_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.DISCOVERY_CATALOG, data.getId());
        } else if (LinkerData.PROMO_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.PROMO_DETAIL, data.getId());
        }

        else if (isAppShowReferralButtonActivated(context) && LinkerData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.REFERRAL_WELCOME, data.getId());
            if(userData != null) {
                deeplinkPath = deeplinkPath.replaceFirst(LinkerConstants.REGEX_APP_LINK,
                        userData.getName() == null ? "" : userData.getName());
            }
        } else if (LinkerData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(LinkerConstants.GROUPCHAT, data.getId());
            if (context.getApplicationContext() instanceof LinkerRouter) {
                desktopUrl = ((LinkerRouter) context.getApplicationContext())
                        .getDesktopLinkGroupChat();
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
        }

        if (LinkerData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, LinkerConstants.CHALLENGES_DESKTOP_URL);
        } else if (LinkerData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, LinkerConstants.REFERRAL_DESKTOP_URL);
        } else if (!TextUtils.isEmpty(desktopUrl)) {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, desktopUrl);
        }else {
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, data.renderShareUri());
        }

        linkProperties.addControlParameter(LinkerConstants.KEY_ANDROID_DEEPLINK_PATH, deeplinkPath == null ? "" : deeplinkPath);
        linkProperties.addControlParameter(LinkerConstants.KEY_IOS_DEEPLINK_PATH, deeplinkPath == null ? "" : deeplinkPath);

        if (isAndroidIosUrlActivated(context) && !(LinkerData.REFERRAL_TYPE.equalsIgnoreCase(data.getType()) ||
                LinkerData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType()) ||
                LinkerData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType()))) {
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
            linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, String.format(LinkerConstants.STRING_FORMAT_DESKTOP_URL, desktopUrl, temp));
            linkProperties.addControlParameter(LinkerConstants.KEY_ANDROID_DEEPLINK_PATH, renderedUrl);
            linkProperties.addControlParameter(LinkerConstants.KEY_IOS_DEEPLINK_PATH, renderedUrl);
        }

        return linkProperties;
    }

    private static boolean isBranchUrlActivated(Context context, String type) {
        if (LinkerData.APP_SHARE_TYPE.equalsIgnoreCase(type)
                || LinkerData.REFERRAL_TYPE.equalsIgnoreCase(type)
                || LinkerData.GROUPCHAT_TYPE.equalsIgnoreCase(type)) {
            return true;
        } else {
            return ((LinkerRouter)context.getApplicationContext()).
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
        return ((LinkerRouter)context.getApplicationContext()).
                getBooleanRemoteConfig(LinkerConstants.FIREBASE_KEY_INCLUDEMOBILEWEB, true);
    }

    public static Boolean isAppShowReferralButtonActivated(Context context) {
        return ((LinkerRouter)context.getApplicationContext()).
                getBooleanRemoteConfig(LinkerConstants.APP_SHOW_REFERRAL_BUTTON, false);
    }
}
