package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.sellerapp.drawer.DrawerVariableSeller;
import com.tokopedia.sellerapp.gmsubscribe.GMSubscribeActivity;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class SellerRouterApplication extends MainApplication implements TkpdCoreRouter, SellerModuleRouter {
    @Override
    public DrawerVariable getDrawer(AppCompatActivity activity) {
        return new DrawerVariableSeller(activity);
    }

    @Override
    public void goToHome(Context context) {
        Intent intent = new Intent(context,
                SellerHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void goToGMSubscribe(Context context) {
        if(context == null)
            throw new RuntimeException("unable to process to next view !!");

        context.startActivity(new Intent(context, GMSubscribeActivity.class));
    }
}
