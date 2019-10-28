package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.fragment.HomepageFragment;
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

@DeepLink(ApplinkConst.TOKOPOINTS)
public class TokoPointsHomeActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent>, onAppBarCollapseListener {
    private TokoPointComponent tokoPointComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_title_tokopoints));
    }

    @Override
    protected Fragment getNewFragment() {
            if (getApplicationContext() instanceof TokopointRouter
                    && ((TokopointRouter) getApplicationContext())
                    .getBooleanRemoteConfig(CommonConstant.TOKOPOINTS_NEW_HOME, false)) {
                finish();
                startActivity(new Intent(this, TokoPointsHomeNewActivity.class));
                return null;
            } else {
                return HomepageFragment.newInstance();
            }
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    @DeepLink({ApplinkConstant.HOMEPAGE, ApplinkConstant.HOMEPAGE2})
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TokoPointsHomeActivity.class);
    }

    private void initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tp_menu_homepage, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            ((TokopointRouter) getApplicationContext()).openTokoPoint(this, CommonConstant.WebLink.INFO);

            AnalyticsTrackerUtil.sendEvent(this,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_BANTUAN,
                    "");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void openApplink(String applink) {
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(this, applink);
        }
    }

    @Override
    public void showToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(getResources().getDimension(R.dimen.dp_4));
        }
    }

    @Override
    public void hideToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(getResources().getDimension(R.dimen.dp_0));
        }
    }
}
