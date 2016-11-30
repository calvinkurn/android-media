package com.tokopedia.sellerapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.core.SplashScreen;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;
import com.tokopedia.session.session.activity.Login;

/**
 * Created by normansyahputa on 11/29/16.
 */

public class SplashScreenActivity extends SplashScreen {

    @Override
    public void finishSplashScreen() {
        if(!sessionHandler.getShopID().isEmpty() && !sessionHandler.getShopID().equals("0")) {
            // Means it is a Seller
            startActivity(new Intent(SplashScreenActivity.this, SellerHomeActivity.class));
        } else {
            // Means it is buyer
            if(!TextUtils.isEmpty(sessionHandler.getLoginID())) {
                Intent intent = moveToCreateShop(this);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }

    @NonNull
    public static Intent moveToCreateShop(Context context) {
        if(context == null)
            return null;

        if(SessionHandler.isMsisdnVerified()) {
            Intent intent = SellerRouter.getAcitivityShopCreateEdit(context);
            intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                    SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return intent;
        }else{
            // TODO move to msisdn activity
            /*Intent intent;
            intent = new Intent(context, MsisdnActivity.class);
            intent.putExtra(MsisdnActivity.SOURCE, Login.class.getSimpleName());
            return intent;*/

            return null;
        }
    }
}
