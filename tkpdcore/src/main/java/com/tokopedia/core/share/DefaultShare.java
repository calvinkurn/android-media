package com.tokopedia.core.share;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.tokopedia.abstraction.ActionInterfaces.ActionCreator;
import com.tokopedia.abstraction.ActionInterfaces.ActionUIDelegate;
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.referral.Constants;
import com.tokopedia.referral.ReferralAction;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.user.session.UserSession;

/**
 * Created by meta on 18/05/18.
 */
public class DefaultShare implements ActionCreator<String, Integer> {

    private static final String TYPE = "text/plain";
    public static final String KEY_OTHER = "lainnya";
    public static final String TITLE_OTHER = "Lainnya";
    private String fireBaseShareMsgKey = "app_referral_product_share_format";
    private String fireBaseGuestShareMsgKey = "app_pdp_share_msg_guest";

    private ShareData shareData;
    private Activity activity;
    private ActionUIDelegate actionUIDelegate;

    public DefaultShare(Activity activity, ShareData data) {
        this.shareData = data;
        this.activity = activity;
    }

    //This functionality is to be the part of Share module which is to be separated hence for now it is part of DefaultShare functionality
    public void show() {
        if(!TextUtils.isEmpty(this.shareData.getType()) && ShareData.PRODUCT_TYPE.equalsIgnoreCase(this.shareData.getType()) && activity != null) {
            checkAndExecuteReferralAction();
        }
        else {
            executeShare();
        }
    }

    private void executeShare(){
        BranchSdkUtils.generateBranchLink(shareData, activity,
                (shareContents, shareUri, branchUrl) -> {
                    Intent intent = getIntent(shareContents);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        Intent receiver = new Intent(activity, ShareBroadcastReceiver.class);
                        receiver.putExtra(ShareBroadcastReceiver.KEY_TYPE, shareData.getType());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0,
                                receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                        activity.startActivity(Intent.createChooser(intent, TITLE_OTHER,
                                pendingIntent.getIntentSender()));

                    } else {
                        activity.startActivity(Intent.createChooser(intent, TITLE_OTHER));
                    }
                    sendTracker();
                });
    }

    private Intent getIntent(String contains) {
        final Intent mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType(TYPE);

        String title = "";
        if (shareData != null) {
            title = shareData.getName();
        }

        mIntent.putExtra(Intent.EXTRA_TITLE, title);
        mIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        mIntent.putExtra(Intent.EXTRA_TEXT, contains);
        return mIntent;
    }

    private void sendTracker() {
        if (shareData.getType().equals(ShareData.CATEGORY_TYPE)) {
            shareCategory(shareData);
        } else {
            sendAnalyticsToGtm(shareData.getType());
        }
    }

    private void shareCategory(ShareData data) {
        String[] shareParam = data.getSplittedDescription(",");
        if (shareParam.length == 2) {
            UnifyTracking.eventShareCategory(activity, shareParam[0], shareParam[1] + "-" + KEY_OTHER);
        }
    }

    private void sendAnalyticsToGtm(String type) {
        switch (type) {
            case ShareData.REFERRAL_TYPE:
                UnifyTracking.eventReferralAndShare(activity, AppEventTracking.Action.SELECT_CHANNEL, KEY_OTHER);
                TrackingUtils.sendMoEngageReferralShareEvent(activity, KEY_OTHER);
                break;
            case ShareData.APP_SHARE_TYPE:
                UnifyTracking.eventAppShareWhenReferralOff(activity, AppEventTracking.Action.SELECT_CHANNEL,
                        KEY_OTHER);
                break;
            default:
                UnifyTracking.eventShare(activity, KEY_OTHER);
                break;
        }
    }

    @Override
    public void actionSuccess(int actionId, String dataObj) {
        if(!TextUtils.isEmpty(dataObj)) {
            shareData.setTextContent(FindAndReplaceHelper.findAndReplacePlaceHolders(shareData.getTextContent(activity),
                    ShareData.PLACEHOLDER_REFERRAL_CODE, dataObj));
            TrackingUtils.sendMoEngagePDPReferralCodeShareEvent(activity, KEY_OTHER);

        }
        executeShare();
    }

    @Override
    public void actionError(int actionId, Integer dataObj) {
        executeShare();
    }

    private void checkAndExecuteReferralAction(){
        UserSession userSession = new UserSession(activity);
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(activity);
        String fireBaseRemoteMsgGuest = remoteConfig.getString(fireBaseShareMsgKey, "");
        if(!TextUtils.isEmpty(fireBaseRemoteMsgGuest)) shareData.setTextContent(fireBaseRemoteMsgGuest);

        if(userSession.isLoggedIn()) {
            String fireBaseRemoteMsg = remoteConfig.getString(fireBaseShareMsgKey, "");
            if (!TextUtils.isEmpty(fireBaseRemoteMsg)) {
                if (fireBaseRemoteMsg.contains(ShareData.PLACEHOLDER_REFERRAL_CODE)) {
                    shareData.setTextContent(fireBaseRemoteMsg);
                    ReferralAction<Context, String, Integer, String, String, String, Context> referralAction = new ReferralAction<>();
                    String referralCode = referralAction.getData(Constants.Action.ACTION_GET_REFERRAL_CODE_IF_EXIST, activity);
                    if (!TextUtils.isEmpty(referralCode)) {
                        shareData.setTextContent(FindAndReplaceHelper.findAndReplacePlaceHolders(shareData.getTextContent(activity),
                                ShareData.PLACEHOLDER_REFERRAL_CODE, referralCode));
                        TrackingUtils.sendMoEngagePDPReferralCodeShareEvent(activity, KEY_OTHER);
                        executeShare();
                    } else {
                        referralAction.doAction(Constants.Action.ACTION_GET_REFERRAL_CODE, activity,
                                this,
                                activity instanceof ActionUIDelegate ? (ActionUIDelegate<String, String>) activity : null);
                    }
                }
                else {
                    executeShare();
                }
            }
            else {
                executeShare();
            }
        }
        else {
            executeShare();
        }
    }
}
