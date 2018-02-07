package com.tokopedia.core.apprating;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created by okasurya on 1/10/18.
 */

public abstract class AppRatingDialog {

    protected static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    protected static final String PACKAGE_CONSUMER_APP = "com.tokopedia.tkpd";
    protected static final String APPLINK_PLAYSTORE = "market://details?id=";
    protected static final String URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";

    protected Activity activity;
    protected RemoteConfig remoteConfig;
    protected LocalCacheHandler cacheHandler;
    @Nullable
    protected AppRatingListener listener;

    protected AppRatingDialog(Activity activity) {
        this.activity = activity;
        this.remoteConfig = new FirebaseRemoteConfigImpl(activity);
        cacheHandler = new LocalCacheHandler(activity, TkpdCache.APP_RATING);
    }

    protected void openPlayStore() {
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

    private String getAppPackageName() {
        if(GlobalConfig.isSellerApp()) {
            return PACKAGE_SELLER_APP;
        }

        return PACKAGE_CONSUMER_APP;
    }

    protected void showDialog() {
        if(isDialogNeedToBeShown()) {
            AlertDialog alertDialog = buildAlertDialog();
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dismissDialog();
                    }
                });

            alertDialog.show();
            onShowDialog();
        } else {
            dismissDialog();
        }
    }

    private void dismissDialog() {
        if(listener != null) {
            listener.onDismiss();
        }
    }

    protected void setListener(AppRatingListener listener) {
        this.listener = listener;
    }

    protected abstract AlertDialog buildAlertDialog();

    protected abstract boolean isDialogNeedToBeShown();

    /**
     * This will be executed when dialog appear
     */
    protected abstract void onShowDialog();

    public interface AppRatingListener {
        void onDismiss();
    }
}
