package com.tokopedia.core.apprating;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by okasurya on 11/29/17.
 */

public class SimpleAppRatingDialog extends AppRatingDialog {
    private static final String HIDE_SIMPLE_APP_RATING = "HideSimpleAppRating";
    private static final String DEFAULT_VALUE = "1";
    private static final long EXPIRED_TIME = TimeUnit.DAYS.toSeconds(7);

    public static void show(Activity activity) {
        SimpleAppRatingDialog simpleAppRatingDialog = new SimpleAppRatingDialog(activity);
        simpleAppRatingDialog.showDialog();
    }

    private SimpleAppRatingDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected AlertDialog buildAlertDialog() {
        return new AlertDialog.Builder(activity)
                .setTitle(
                    remoteConfig.getString(
                        TkpdCache.RemoteConfigKey.MAINAPP_RATING_TITLE,
                        activity.getString(R.string.app_rating_title)
                    )
                )
                .setMessage(
                    remoteConfig.getString(
                        TkpdCache.RemoteConfigKey.MAINAPP_RATING_MESSAGE,
                        activity.getString(R.string.app_rating_message)
                    )
                )
                .setPositiveButton(R.string.app_rating_button_rate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openPlayStore(activity);
                        saveVersionCodeForState();
                        UnifyTracking.eventClickAppRating(AppEventTracking.Event.CLICK_APP_RATING);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.app_rating_button_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideDialog();
                        UnifyTracking.eventCancelAppRating(AppEventTracking.Event.CANCEL_APP_RATING);
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
    }

    @Override
    protected boolean isDialogNeedToBeShown() {
        if(remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.MAINAPP_SHOW_SIMPLE_APP_RATING, false)
                && globalCacheManager.isExpired(HIDE_SIMPLE_APP_RATING)) {
            Integer appRatingVersion = cacheHandler.getInt(TkpdCache.Key.KEY_APP_RATING_VERSION);
            return appRatingVersion == null || appRatingVersion == -1;
        }

        return false;
    }

    @Override
    protected void onShowDialog() {
        UnifyTracking.eventAppRatingImpression(this.getClass().getSimpleName());
    }

    private void hideDialog() {
        globalCacheManager.setCacheDuration(EXPIRED_TIME);
        globalCacheManager.setKey(HIDE_SIMPLE_APP_RATING);
        globalCacheManager.setValue(DEFAULT_VALUE);
        globalCacheManager.store();
    }

    /**
     * The value saved for state is app version code, for easier development in the future.
     */
    private void saveVersionCodeForState() {
        cacheHandler.putInt(TkpdCache.Key.KEY_APP_RATING_VERSION, GlobalConfig.VERSION_CODE);
        cacheHandler.applyEditor();
    }
}
