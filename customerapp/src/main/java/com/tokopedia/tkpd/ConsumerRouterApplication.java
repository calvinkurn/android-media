package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.tkpd.home.recharge.fragment.RechargeCategoryFragment;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class ConsumerRouterApplication extends MainApplication implements SellerModuleRouter, IConsumerModuleRouter {
    @Override
    public void goToHome(Context context) {
        Intent intent2 = new Intent(context,
                HomeRouter.getHomeActivityClass());
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent2);
    }

    @Override
    public Fragment getRechargeCategoryFragment() {
        Bundle bundle = new Bundle();
        return RechargeCategoryFragment.newInstance(bundle);
    }
}
