package com.tokopedia.core.util;

import android.app.Activity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * Created by ashwanityagi on 04/10/17.
 */

public class BranchShareLinkGenerator {
    private static final String BRANCH_ANDROID_DEEPLINK_PATH_KEY = "$android_deeplink_path";
    private static final String BRANCH_IOS_DEEPLINK_PATH_KEY = "$ios_deeplink_path";
    private static final String BRANCH_DESKTOP_URL_KEY = "$desktop_url";
    private static final String URI_REDIRECT_MODE_KEY = "$uri_redirect_mode";
    private static final String URI_REDIRECT_MODE_VALUE = "2";
    private static final String CAMPAIGN_NAME = "Android App";
    private static String extraDescription = "";

    private static BranchUniversalObject createBranchUniversalObject(ShareData data) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(data.getType())
                .setTitle(data.getName())
                .setContentDescription(data.getDescription())
                .setContentImageUrl(data.getImgUri())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
        return branchUniversalObject;
    }

    public static void generateBranchLink(final ShareData data, final Activity activity, final GenerateShareContents ShareContentsCreateListener) {

        if (ShareData.APP_SHARE_TYPE.equalsIgnoreCase(data.getType())) {
            extraDescription = getAppShareDescription(activity, data.getType());
        } else {
            extraDescription = "";
        }
        if (isBranchUrlActivated(activity, data.getType())) {
            BranchUniversalObject branchUniversalObject = createBranchUniversalObject(data);
            LinkProperties linkProperties = createLinkProperties(data, data.getSource(), activity);
            branchUniversalObject.generateShortUrl(activity, linkProperties, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {

                    if (error == null) {
                        ShareContentsCreateListener.onCreateShareContents(extraDescription + data.getTextContentForBranch(url), extraDescription + url);
                    } else {
                        ShareContentsCreateListener.onCreateShareContents(extraDescription + data.getTextContent(activity), extraDescription + data.renderShareUri());
                    }
                }
            });
        } else {
            ShareContentsCreateListener.onCreateShareContents(extraDescription + data.getTextContent(activity), extraDescription + data.renderShareUri());

        }
    }

    private static LinkProperties createLinkProperties(ShareData data, String channel, Activity activity) {
        LinkProperties linkProperties = new LinkProperties();
        String deeplinkPath;
        String desktopUrl = null;
        if (ShareData.PRODUCT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.PRODUCT_INFO, data.getId());//"product/" + data.getId();
        } else if (ShareData.APP_SHARE_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.HOME, "");//"home";
        } else if (ShareData.SHOP_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.SHOP, data.getId());//"shop/" + data.getId();
        } else if (ShareData.HOTLIST_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.DISCOVERY_HOTLIST_DETAIL, data.getId());//"hot/" + data.getId();
        } else {
            deeplinkPath = getApplinkPath(data.renderShareUri(), "");
        }

        if (desktopUrl == null) {
            linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, data.renderShareUri());

        }

        linkProperties.setCampaign(CAMPAIGN_NAME);
        linkProperties.setChannel(channel);
        linkProperties.setFeature(data.getType());
        linkProperties.addControlParameter(URI_REDIRECT_MODE_KEY, URI_REDIRECT_MODE_VALUE);
        linkProperties.addControlParameter(BRANCH_ANDROID_DEEPLINK_PATH_KEY, data.renderBranchShareUri(deeplinkPath));
        linkProperties.addControlParameter(BRANCH_IOS_DEEPLINK_PATH_KEY, data.renderBranchShareUri(deeplinkPath));
        return linkProperties;
    }

    private static boolean isBranchUrlActivated(Activity activity, String type) {
        if (ShareData.APP_SHARE_TYPE.equalsIgnoreCase(type)) {
            return true;
        } else {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(activity);
            return remoteConfig.getBoolean(TkpdCache.Key.CONFIG_MAINAPP_ACTIVATE_BRANCH_LINKS, true);
        }
    }

    private static String getApplinkPath(String url, String id) {
        if (url.contains(Constants.Schemes.APPLINKS + "://")) {
            url = url.replace(Constants.Schemes.APPLINKS + "://", "");
            url = url.replaceAll("\\{.*?\\} ?", id);
        } else if (url.contains(TkpdBaseURL.WEB_DOMAIN)) {
            url = url.replace(TkpdBaseURL.WEB_DOMAIN, "");
        } else if (url.contains(TkpdBaseURL.MOBILE_DOMAIN)) {
            url = url.replace(TkpdBaseURL.MOBILE_DOMAIN, "");
        }
        return url;
    }

    private static String getAppShareDescription(Activity activity, String type) {
        if (ShareData.APP_SHARE_TYPE.equalsIgnoreCase(type)) {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(activity);
            return remoteConfig.getString(TkpdCache.Key.CONFIG_APP_SHARE_DESCRIPTION) + " \n";
        }

        return "";
    }

    public interface GenerateShareContents {
        void onCreateShareContents(String shareContents, String shareUri);
    }
}
