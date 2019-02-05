package com.tokopedia.core.share;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.util.BranchSdkUtils;

/**
 * Created by meta on 18/05/18.
 */
public class DefaultShare {

    private static final String TYPE = "text/plain";
    public static final String KEY_OTHER = "lainnya";
    public static final String TITLE_OTHER = "Lainnya";

    private ShareData shareData;
    private Activity activity;

    public DefaultShare(Activity activity, ShareData data) {
        this.shareData = data;
        this.activity = activity;
    }

    public void show() {
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
}
