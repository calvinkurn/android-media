package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.tkpd.home.recharge.fragment.RechargeCategoryFragment;
import com.tokopedia.tkpd.home.ParentIndexHome;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class ConsumerRouterApplication extends MainApplication implements IConsumerModuleRouter {
    @Override
    public Fragment getRechargeCategoryFragment() {
        Bundle bundle = new Bundle();
        return RechargeCategoryFragment.newInstance(bundle);
    }
}
