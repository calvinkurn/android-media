package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.seller.SellerModuleRouter;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class ConsumerRouterApplication extends MainApplication implements SellerModuleRouter {
    @Override
    public void goToHome(Context context) {
        Intent intent2 = new Intent(context,
                HomeRouter.getHomeActivityClass());
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent2);
    }
}
