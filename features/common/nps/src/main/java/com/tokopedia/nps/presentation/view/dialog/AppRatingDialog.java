package com.tokopedia.nps.presentation.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.nps.NpsAnalytics;
import com.tokopedia.nps.presentation.di.DaggerFeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackModule;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.abstraction.AbstractionRouter;

import javax.inject.Inject;

import static com.tokopedia.nps.NpsConstant.Key.APP_RATING;

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
    protected DialogInterface.OnDismissListener listener;

    @Inject NpsAnalytics npsAnalytics;

    protected AppRatingDialog(Activity activity) {
        this.activity = activity;
        this.initInjector();
        this.remoteConfig = new FirebaseRemoteConfigImpl(activity);
        cacheHandler = new LocalCacheHandler(activity, APP_RATING);
    }

    private void initInjector() {
        FeedbackComponent component = DaggerFeedbackComponent.builder()
                .feedbackModule(new FeedbackModule(this.activity))
                .build();
        component.inject(this);
    }

    public static void openPlayStore(Context context) {
        try {
            context.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(APPLINK_PLAYSTORE + getAppPackageName())
                    )
            );
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(URL_PLAYSTORE + getAppPackageName())
                    )
            );
        }
    }

    public static String getAppPackageName() {
        if(GlobalConfig.isSellerApp()) {
            return PACKAGE_SELLER_APP;
        }

        return PACKAGE_CONSUMER_APP;
    }

    protected void showDialog() {
        if(isDialogNeedToBeShown()) {
            AlertDialog alertDialog = buildAlertDialog();
            alertDialog.setOnDismissListener(listener);
            alertDialog.show();
            onShowDialog();
        } else if(listener != null) {
            listener.onDismiss(null);
        }
    }

    protected void setListener(@Nullable DialogInterface.OnDismissListener listener) {
        this.listener = listener;
    }

    protected abstract AlertDialog buildAlertDialog();

    protected abstract boolean isDialogNeedToBeShown();

    /**
     * This will be executed when dialog appear
     */
    protected abstract void onShowDialog();
}
