package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.DrawerHelper;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.tkpd.drawer.DrawerBuyerHelper;
import com.tokopedia.tkpd.home.recharge.fragment.RechargeCategoryFragment;
import com.tokopedia.tkpd.home.ParentIndexHome;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class ConsumerRouterApplication extends MainApplication implements
        SellerModuleRouter,
        IConsumerModuleRouter,
        TkpdCoreRouter{
    @Override
    public void goToHome(Context context) {
        Intent intent = new Intent(context,
                ParentIndexHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public Fragment getRechargeCategoryFragment() {
        Bundle bundle = new Bundle();
        return RechargeCategoryFragment.newInstance(bundle);
    }

    @Override
    public DrawerHelper getDrawer(AppCompatActivity activity) {
        CommonUtils.dumper("NISNIS " + activity.getClass().getSimpleName());
        return DrawerBuyerHelper.createInstance(activity);
    }
}
