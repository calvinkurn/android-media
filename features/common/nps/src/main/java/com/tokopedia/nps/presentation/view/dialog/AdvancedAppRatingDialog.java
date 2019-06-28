package com.tokopedia.nps.presentation.view.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.nps.R;
import com.tokopedia.nps.presentation.widget.AppRatingView;
import com.tokopedia.nps.presentation.view.activity.FeedbackActivity;
import com.tokopedia.nps.presentation.view.activity.FeedbackThankPageActivity;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.concurrent.TimeUnit;

import static com.tokopedia.nps.NpsConstant.Key.*;

/**
 * Created by okasurya on 1/10/18.
 */

public class AdvancedAppRatingDialog extends AppRatingDialog {

    public static final int MIN_RATING = 3;

    private static final String LABEL_CLICK_ADVANCED_APP_RATING = "ClickAdvancedAppRating: ";
    private static final String LABEL_CANCEL_ADVANCED_APP_RATING = "CancelAdvancedAppRating";
    private static final String HIDE_ADVANCED_APP_RATING = "HideAdvancedAppRating";
    private static final String DEFAULT_VALUE = "1";
    private static final long EXPIRED_TIME = TimeUnit.DAYS.toMillis(7);

    private AlertDialog dialog;
    private AppRatingView appRatingView;

    public static void show(Activity activity) {
        AdvancedAppRatingDialog dialog = new AdvancedAppRatingDialog(activity);
        dialog.showDialog();
    }

    public static void show(Activity activity, DialogInterface.OnDismissListener listener) {
        AdvancedAppRatingDialog dialog = new AdvancedAppRatingDialog(activity);
        dialog.setListener(listener);
        dialog.showDialog();
    }

    private AdvancedAppRatingDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected AlertDialog buildAlertDialog() {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_app_rating, null);

        Button buttonSend = dialogView.findViewById(R.id.button_send);
        Button buttonClose = dialogView.findViewById(R.id.button_close);
        appRatingView = dialogView.findViewById(R.id.view_app_rating);

        appRatingView.setDefaultRating(3);

        dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .create();

        buttonSend.setOnClickListener(v -> {
            npsAnalytics.eventClickAppRating(LABEL_CLICK_ADVANCED_APP_RATING + appRatingView.getRating());
            dialog.dismiss();
            saveVersionCodeForState();
            saveRating(appRatingView.getRating());
            if(appRatingView.getRating() > MIN_RATING) {
                FeedbackThankPageActivity.startActivity(activity, appRatingView.getRating());
            } else {
                FeedbackActivity.start(activity, appRatingView.getRating());
            }
        });

        buttonClose.setOnClickListener(v -> {
            hideDialog();
            npsAnalytics.eventCancelAppRating(LABEL_CANCEL_ADVANCED_APP_RATING);
            dialog.dismiss();
        });

        return dialog;
    }

    private void hideDialog() {
        PersistentCacheManager.instance.put(HIDE_ADVANCED_APP_RATING, DEFAULT_VALUE, EXPIRED_TIME);
    }

    private void saveVersionCodeForState() {
        cacheHandler.putInt(getLocalKey(), GlobalConfig.VERSION_CODE);
        cacheHandler.applyEditor();
    }

    private void saveRating(float rating) {
        cacheHandler.putInt(KEY_RATING, Math.round(rating));
        cacheHandler.applyEditor();
    }

    private String getLocalKey(){
        return KEY_ADVANCED_APP_RATING_VERSION;
    }

    private String getRemoteConfigKey(){
        if (GlobalConfig.isSellerApp()) {
            return RemoteConfigKey.SELLERAPP_SHOW_ADVANCED_APP_RATING;
        } else {
            return RemoteConfigKey.MAINAPP_SHOW_ADVANCED_APP_RATING;
        }
    }

    @Override
    protected boolean isDialogNeedToBeShown() {
        if (remoteConfig.getBoolean(getRemoteConfigKey(), false)
                && PersistentCacheManager.instance.isExpired(HIDE_ADVANCED_APP_RATING)) {
            Integer appRatingVersion = cacheHandler.getInt(getLocalKey());
            Integer rating = cacheHandler.getInt(KEY_RATING);
            if (appRatingVersion == null || appRatingVersion == -1 || appRatingVersion < GlobalConfig.VERSION_CODE) {
                 return rating == null || rating <= MIN_RATING;
            }
        }
        return false; // default false
    }

    @Override
    protected void onShowDialog() {
        npsAnalytics.eventAppRatingImpression(this.getClass().getSimpleName());
    }
}
