package com.tokopedia.gm.statistic.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticTransactionTableFragment;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableActivity extends BaseSimpleActivity implements HasComponent<GMComponent> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWhiteStatusBar();
    }

    @Override
    protected Fragment getNewFragment() {
        return GMStatisticTransactionTableFragment.createInstance();
    }

    @Override
    public GMComponent getComponent() {
        return ((GMModuleRouter) getApplication()).getGMComponent();
    }

    private void setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
}
