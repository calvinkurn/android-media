package com.tokopedia.sellerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.sellerapp.welcome.WelcomeActivity;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.sellerapp.deeplink.DeepLinkDelegate;
import com.tokopedia.sellerapp.deeplink.DeepLinkHandlerActivity;

import org.json.JSONObject;

/**
 * Created by normansyahputa on 11/29/16.
 */

public class SplashScreenActivity extends SplashScreen {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (checkIfInstalledFromInvalidStore()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tokopedia App is not valid");
            builder.setMessage("Install Tokopedia from Google Play Store to continue");
            builder.setPositiveButton("OK", (DialogInterface dialogInterface, int i) -> {
                startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="
                                + getApplication().getPackageName()))
                );
                dialogInterface.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception ignored) { }
    }

    private boolean checkIfInstalledFromInvalidStore() {
        try {
            return getResources().getResourceName(R.drawable.logo_tkpd_white) != null;
        } catch (Throwable throwable) {
            return true;
        }
    }

    @Override
    public void finishSplashScreen() {
        if (SessionHandler.isUserHasShop(this)) {
            if (getIntent().hasExtra(Constants.EXTRA_APPLINK)) {
                String applinkUrl = getIntent().getStringExtra(Constants.EXTRA_APPLINK);
                DeepLinkDelegate delegate = DeepLinkHandlerActivity.getDelegateInstance();
                if (delegate.supportsUri(applinkUrl)) {
                    Intent intent = getIntent();
                    intent.setData(Uri.parse(applinkUrl));
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, true);
                    intent.putExtras(bundle);
                    delegate.dispatchFrom(this, intent);
                } else {
                    startActivity(DashboardActivity.createInstance(this));
                }
            } else {
                // Means it is a Seller
                startActivity(DashboardActivity.createInstance(this));
            }
        } else if (!TextUtils.isEmpty(SessionHandler.getLoginID(this))) {
            Intent intent = moveToCreateShop(this);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @NonNull
    public static Intent moveToCreateShop(Context context) {
        Intent intent = SellerRouter.getActivityShopCreateEdit(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
