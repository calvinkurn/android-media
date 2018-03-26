package com.tokopedia.core.apprating;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.apprating.nps.FeedbackActivity;
import com.tokopedia.core.apprating.nps.FeedbackThankPageActivity;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;

import java.util.concurrent.TimeUnit;

/**
 * Created by okasurya on 1/10/18.
 */

public class AdvancedAppRatingDialog extends AppRatingDialog {

    public static final int MIN_RATING = 3;

    private static final String LABEL_CLICK_ADVANCED_APP_RATING = "ClickAdvancedAppRating: ";
    private static final String LABEL_CANCEL_ADVANCED_APP_RATING = "CancelAdvancedAppRating";
    private static final String HIDE_ADVANCED_APP_RATING = "HideAdvancedAppRating";
    private static final String DEFAULT_VALUE = "1";
    private static final long EXPIRED_TIME = TimeUnit.DAYS.toSeconds(7);

    private AlertDialog dialog;
    private Button buttonSend;
    private Button buttonClose;
    private AppRatingView appRatingView;

    public static void show(Activity activity) {
        AdvancedAppRatingDialog dialog = new AdvancedAppRatingDialog(activity);
        dialog.showDialog();
    }

    public static void show(Activity activity, AppRatingListener listener) {
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

        buttonSend = dialogView.findViewById(R.id.button_send);
        buttonClose = dialogView.findViewById(R.id.button_close);
        appRatingView = dialogView.findViewById(R.id.view_app_rating);

        dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .create();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventClickAppRating(LABEL_CLICK_ADVANCED_APP_RATING + appRatingView.getRating());
                dialog.dismiss();
                saveVersionCodeForState();
                if(appRatingView.getRating() > MIN_RATING) {
                    FeedbackThankPageActivity.startActivity(activity, appRatingView.getRating());
                } else {
                    FeedbackActivity.startActivity(activity, appRatingView.getRating());
                }
            }
        });

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
                UnifyTracking.eventCancelAppRating(LABEL_CANCEL_ADVANCED_APP_RATING);
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void hideDialog() {
        globalCacheManager.setCacheDuration(EXPIRED_TIME);
        globalCacheManager.setKey(HIDE_ADVANCED_APP_RATING);
        globalCacheManager.setValue(DEFAULT_VALUE);
        globalCacheManager.store();
    }

    private void saveVersionCodeForState() {
        cacheHandler.putInt(getLocalKey(), GlobalConfig.VERSION_CODE);
        cacheHandler.applyEditor();
    }

    private String getLocalKey(){
        return TkpdCache.Key.KEY_ADVANCED_APP_RATING_VERSION;
    }

    private String getRemoteConfigKey(){
        if (GlobalConfig.isSellerApp()) {
            return TkpdCache.RemoteConfigKey.SELLERAPP_SHOW_ADVANCED_APP_RATING;
        } else {
            return TkpdCache.RemoteConfigKey.MAINAPP_SHOW_ADVANCED_APP_RATING;
        }
    }

    @Override
    protected boolean isDialogNeedToBeShown() {
        if (remoteConfig.getBoolean(getRemoteConfigKey(), false)
                && globalCacheManager.isExpired(HIDE_ADVANCED_APP_RATING)) {
            Integer appRatingVersion = cacheHandler.getInt(getLocalKey());
            return appRatingVersion == null || appRatingVersion == -1;
        }
        return false;
    }

    @Override
    protected void onShowDialog() {
        UnifyTracking.eventAppRatingImpression(this.getClass().getSimpleName());
    }
}
