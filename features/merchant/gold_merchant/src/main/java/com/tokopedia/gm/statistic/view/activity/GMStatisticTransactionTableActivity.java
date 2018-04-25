package com.tokopedia.gm.statistic.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticTransactionTableFragment;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableActivity extends BaseSimpleActivity implements HasComponent<GMComponent> {

    @Override
    protected Fragment getNewFragment() {
        return GMStatisticTransactionTableFragment.createInstance();
    }

    @Override
    public GMComponent getComponent() {
        return ((GMModuleRouter) getApplication()).getGMComponent();
    }
}
