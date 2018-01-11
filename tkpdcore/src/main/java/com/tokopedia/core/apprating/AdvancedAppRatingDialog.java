package com.tokopedia.core.apprating;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created by okasurya on 1/10/18.
 */

public class AdvancedAppRatingDialog extends AppRatingDialog {

    private static final String LABEL_CLICK_ADVANCED_APP_RATING = "ClickAdvancedAppRating";
    private static final String LABEL_CANCEL_ADVANCED_APP_RATING = "CancelAdvancedAppRating";

    public static void show(Activity activity) {
        AdvancedAppRatingDialog dialog = new AdvancedAppRatingDialog(activity);
        dialog.showDialog();
    }

    private AdvancedAppRatingDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected AlertDialog buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setView(R.layout.dialog_app_rating);


        return builder.create();
    }

    private void saveVersionCodeForState() {
        cacheHandler.putInt(TkpdCache.Key.KEY_ADVANCED_APP_RATING_VERSION, GlobalConfig.VERSION_CODE);
        cacheHandler.applyEditor();
    }

    @Override
    protected boolean isDialogNeedToBeShown() {
//        if(remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.MAINAPP_SHOW_ADVANCED_APP_RATING, false)) {
//            Integer appRatingVersion = cacheHandler.getInt(TkpdCache.Key.KEY_ADVANCED_APP_RATING_VERSION);
//            return appRatingVersion == null || appRatingVersion == -1;
//        }

        return true;
    }

    @Override
    protected void onShowDialog() {
        UnifyTracking.eventAppRatingImpression(this.getClass().getSimpleName());
    }
}
