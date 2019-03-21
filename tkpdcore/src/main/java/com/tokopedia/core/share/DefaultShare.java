package com.tokopedia.core.share;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.util.DataMapper;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareData;
import com.tokopedia.linker.model.LinkerShareResult;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

/**
 * Created by meta on 18/05/18.
 */
public class DefaultShare implements ShareCallback {

    private static final String TYPE = "text/plain";
    public static final String KEY_OTHER = "lainnya";
    public static final String TITLE_OTHER = "Lainnya";

    private LinkerData shareData;
    private Activity activity;

    public DefaultShare(Activity activity, LinkerData data) {
        this.shareData = data;
        this.activity = activity;
    }

    public void show() {
        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                DataMapper.getLinkerShareData(shareData), this));
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
        if (shareData.getType().equals(LinkerData.CATEGORY_TYPE)) {
            shareCategory(shareData);
        } else {
            sendAnalyticsToGtm(shareData.getType());
        }
    }

    private void shareCategory(LinkerData data) {
        String[] shareParam = data.getSplittedDescription(",");
        if (shareParam.length == 2) {
            eventShareCategory(shareParam[0], shareParam[1] + "-" + KEY_OTHER);
        }
    }

    public static void eventShareCategory(String parentCat, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_SHARE,
                label);
    }

    private void sendAnalyticsToGtm(String type) {
        switch (type) {
            case LinkerData.REFERRAL_TYPE:
                UnifyTracking.eventReferralAndShare(activity, AppEventTracking.Action.SELECT_CHANNEL, KEY_OTHER);
                TrackingUtils.sendMoEngageReferralShareEvent(activity, KEY_OTHER);
                break;
            case LinkerData.APP_SHARE_TYPE:
                UnifyTracking.eventAppShareWhenReferralOff(activity, AppEventTracking.Action.SELECT_CHANNEL,
                        KEY_OTHER);
                break;
            default:
                UnifyTracking.eventShare(activity, KEY_OTHER);
                break;
        }
    }

    @Override
    public void urlCreated(LinkerShareResult linkerShareData) {
        Intent intent = getIntent(linkerShareData.getShareContents());
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
    }

    @Override
    public void onError(LinkerError linkerError) {

    }
}
