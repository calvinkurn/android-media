package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.tkpd.home.recharge.fragment.RechargeCategoryFragment;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.transaction.webview.WalletWebView;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class ConsumerRouterApplication extends MainApplication implements TkpdCoreRouter,
        SellerModuleRouter,
        IConsumerModuleRouter {
    @Override
    public void goToHome(Context context) {
        Intent intent = new Intent(context,
                ParentIndexHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void goToProductDetail(Context context, String productUrl) {
        throw new RuntimeException("right now, it implement at Seller Application !!");
    }

    @Override
    public Fragment getRechargeCategoryFragment() {
        Bundle bundle = new Bundle();
        return RechargeCategoryFragment.newInstance(bundle);
    }

    @Override
    public DrawerVariable getDrawer(AppCompatActivity activity) {
        return new DrawerVariable(activity);
    }

    @Override
    public void goToWallet(Context context, Bundle bundle) {
        Intent intent = new Intent(context, WalletWebView.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
