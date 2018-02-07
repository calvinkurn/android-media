package com.tokopedia.core.apprating;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created by okasurya on 1/10/18.
 */

public class AdvancedAppRatingDialog extends AppRatingDialog {
    private AlertDialog dialog;
    private Button buttonSend;
    private Button buttonClose;
    private AppRatingView appRatingView;

    private static final String LABEL_CLICK_ADVANCED_APP_RATING = "ClickAdvancedAppRating: ";
    private static final String LABEL_CANCEL_ADVANCED_APP_RATING = "CancelAdvancedAppRating";

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
                if(appRatingView.getRating() > 3) {
                    saveVersionCodeForState();
                    openPlayStore();
                }
            }
        });

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventCancelAppRating(LABEL_CANCEL_ADVANCED_APP_RATING);
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void saveVersionCodeForState() {
        cacheHandler.putInt(TkpdCache.Key.KEY_ADVANCED_APP_RATING_VERSION, GlobalConfig.VERSION_CODE);
        cacheHandler.applyEditor();
    }

    @Override
    protected boolean isDialogNeedToBeShown() {
        if(remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.MAINAPP_SHOW_ADVANCED_APP_RATING, false)) {
            Integer appRatingVersion = cacheHandler.getInt(TkpdCache.Key.KEY_ADVANCED_APP_RATING_VERSION);
            return appRatingVersion == null || appRatingVersion == -1;
        }

        return false;
    }

    @Override
    protected void onShowDialog() {
        UnifyTracking.eventAppRatingImpression(this.getClass().getSimpleName());
    }
}
