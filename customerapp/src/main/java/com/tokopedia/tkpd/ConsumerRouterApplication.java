package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.tkpd.home.ParentIndexHome;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class ConsumerRouterApplication extends MainApplication implements SellerModuleRouter {
    @Override
    public void goToHome(Context context) {
        Intent intent = new Intent(context,
                ParentIndexHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
