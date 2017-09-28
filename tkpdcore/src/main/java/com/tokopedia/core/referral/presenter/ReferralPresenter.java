package com.tokopedia.core.referral.presenter;

import android.app.Activity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.var.TkpdCache;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralPresenter implements IReferralPresenter {

    private Activity activity;
    private static final String BRANCH_ANDROID_DEEPLINK_PATH_KEY = "$android_deeplink_path";
    private static final String BRANCH_IOS_DEEPLINK_PATH_KEY = "$ios_deeplink_path";
    private static final String BRANCH_DESKTOP_URL_KEY = "$desktop_url";
    private static final String URI_REDIRECT_MODE_KEY = "$uri_redirect_mode";
    private static final String URI_REDIRECT_MODE_VALUE = "2";

    public ReferralPresenter(BasePresenterFragment presenterFragment) {
        this.activity = presenterFragment.getActivity();
    }

    public BranchUniversalObject generateBranchSharingLink(ShareData data) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("app share")
                .setTitle(data.getName())
                .setContentDescription(data.getDescription()
                );
        return branchUniversalObject;
    }


    @Override
    public void showShareSheet(ShareData data) {
        BranchUniversalObject branchUniversalObject = generateBranchSharingLink(data);
        LinkProperties linkProperties = new LinkProperties();
        linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, getRenderUri(data.getUri()));
        linkProperties.setFeature(data.getType());
        linkProperties.addControlParameter(URI_REDIRECT_MODE_KEY, URI_REDIRECT_MODE_VALUE);
        linkProperties.addControlParameter(BRANCH_ANDROID_DEEPLINK_PATH_KEY, getDeeplink());
        linkProperties.addControlParameter(BRANCH_IOS_DEEPLINK_PATH_KEY, getDeeplink());

        branchUniversalObject.showShareSheet(activity,
                linkProperties,
                new ShareSheetStyle(activity, data.getName(), getAppShareDescription()
                ),
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogDismissed() {
                    }

                    @Override
                    public void onChannelSelected(String s) {

                    }

                    @Override
                    public void onLinkShareResponse(String s, String s1, BranchError branchError) {

                    }

                    @Override
                    public void onShareLinkDialogLaunched() {

                    }
                });
    }

    private String getAppShareDescription() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(activity, TkpdCache.FIREBASE_REMOTE_CONFIG);
        return localCacheHandler.getString(TkpdCache.Key.APP_SHARE_DESCRIPTION_KEY, "") + " \n";

    }

    private String getDeeplink() {
        String deepLink = Constants.Applinks.HOME.replace(Constants.APPLINK_CUSTOMER_SCHEME + "://", "");
        deepLink = getRenderUri(deepLink);
        return deepLink;
    }

    private String getRenderUri(String url) {
        if(url.contains("?")){
            url = url + "&utm_source=Android&utm_campaign=App%20Share&utm_medium=share";
        }else{
            url = url + "?utm_source=Android&utm_campaign=App%20Share&utm_medium=share";
        }

        return url;
    }
}
