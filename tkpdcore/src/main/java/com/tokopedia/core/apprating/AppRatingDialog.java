package com.tokopedia.core.apprating;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created by okasurya on 11/29/17.
 */

public class AppRatingDialog {
    private static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    private static final String PACKAGE_CONSUMER_APP = "com.tokopedia.tkpd";
    private static final String APPLINK_PLAYSTORE = "market://details?id=";
    private static final String URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";

    private Activity activity;
    private RemoteConfig remoteConfig;
    private LocalCacheHandler cacheHandler;

    public static void show(Activity activity) {
        AppRatingDialog appRatingDialog = new AppRatingDialog(activity);
        if(appRatingDialog.isDialogNeedToBeShown()) {
            appRatingDialog.buildAlertDialog().show();
            UnifyTracking.eventAppRatingImpression();
        }
    }

    private AppRatingDialog(Activity activity) {
        this.activity = activity;
        this.remoteConfig = new FirebaseRemoteConfigImpl(activity);
        cacheHandler = new LocalCacheHandler(activity, TkpdCache.APP_RATING);
    }

    private AlertDialog buildAlertDialog() {
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
                        openPlayStore();
                        saveVersionCodeForState();
                        UnifyTracking.eventClickAppRating();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.app_rating_button_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UnifyTracking.eventCancelAppRating();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
    }

    private void openPlayStore() {
        try {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(APPLINK_PLAYSTORE + getAppPackageName())
                    )
            );
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(URL_PLAYSTORE + getAppPackageName())
                    )
            );
        }
    }

    /**
     * The value saved for state is app version code, for easier development in the future.
     */
    private void saveVersionCodeForState() {
        cacheHandler.putInt(TkpdCache.Key.KEY_APP_RATING_VERSION, GlobalConfig.VERSION_CODE);
        cacheHandler.applyEditor();
    }

    private String getAppPackageName() {
        if(GlobalConfig.isSellerApp()) {
            return PACKAGE_SELLER_APP;
        }

        return PACKAGE_CONSUMER_APP;
    }

    private boolean isDialogNeedToBeShown() {
        Integer appRatingVersion = cacheHandler.getInt(TkpdCache.Key.KEY_APP_RATING_VERSION);
        return  appRatingVersion == null || appRatingVersion == -1;
    }
}
