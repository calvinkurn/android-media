package com.tokopedia.nps.presentation.view.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.nps.R;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.concurrent.TimeUnit;

import static com.tokopedia.nps.NpsConstant.Analytic.*;
import static com.tokopedia.nps.NpsConstant.Key.*;

/**
 * Created by okasurya on 11/29/17.
 */

public class SimpleAppRatingDialog extends AppRatingDialog {
    private static final String HIDE_SIMPLE_APP_RATING = "HideSimpleAppRating";
    private static final String DEFAULT_VALUE = "1";
    private static final long EXPIRED_TIME = TimeUnit.DAYS.toMillis(7);


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
                        RemoteConfigKey.MAINAPP_RATING_TITLE,
                        activity.getString(R.string.app_rating_title)
                    )
                )
                .setMessage(
                    remoteConfig.getString(
                        RemoteConfigKey.MAINAPP_RATING_MESSAGE,
                        activity.getString(R.string.app_rating_message)
                    )
                )
                .setPositiveButton(R.string.app_rating_button_rate, (dialog, which) -> {
                    openPlayStore(activity);
                    saveVersionCodeForState();
                    npsAnalytics.eventClickAppRating(CLICK_APP_RATING);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.app_rating_button_later, (dialog, which) -> {
                    hideDialog();
                    npsAnalytics.eventCancelAppRating(CANCEL_APP_RATING);
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create();
    }

    @Override
    protected boolean isDialogNeedToBeShown() {
        if(remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_SHOW_SIMPLE_APP_RATING, false)
                && PersistentCacheManager.instance.isExpired(HIDE_SIMPLE_APP_RATING)) {
            Integer appRatingVersion = cacheHandler.getInt(KEY_APP_RATING_VERSION);
            return appRatingVersion == null || appRatingVersion == -1;
        }

        return false;
    }

    @Override
    protected void onShowDialog() {
        npsAnalytics.eventAppRatingImpression(this.getClass().getSimpleName());
    }

    private void hideDialog() {
        PersistentCacheManager.instance.put(HIDE_SIMPLE_APP_RATING, DEFAULT_VALUE, EXPIRED_TIME);
    }

    /**
     * The value saved for state is app version code, for easier development in the future.
     */
    private void saveVersionCodeForState() {
        cacheHandler.putInt(KEY_APP_RATING_VERSION, GlobalConfig.VERSION_CODE);
        cacheHandler.applyEditor();
    }
}
