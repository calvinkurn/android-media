package com.tokopedia.gm.statistic.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.R;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticDashboardFragment;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatisticDashboardActivity extends BaseSimpleActivity implements HasComponent<GMComponent> {

    public static final String TAG = GMStatisticDashboardActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getScreenName() {
        return AppScreen.STATISTIC_PAGE;
    }

    @Override
    public GMComponent getComponent() {
        return ((GMModuleRouter) getApplication()).getGMComponent();
    }

    @Nullable
    @Override
    protected Fragment getNewFragment() {
        return new GMStatisticDashboardFragment();
    }
}
