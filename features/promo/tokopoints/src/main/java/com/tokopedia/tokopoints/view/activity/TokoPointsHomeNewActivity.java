package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.fragment.HomepageFragment;
import com.tokopedia.tokopoints.view.fragment.TokoPointsHomeFragmentNew;
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener;
import com.tokopedia.user.session.UserSession;

public class TokoPointsHomeNewActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent>, onAppBarCollapseListener {
    private static final int REQUEST_CODE_LOGIN = 1;
    private TokoPointComponent tokoPointComponent;
    private UserSession mUserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserSession = new UserSession(getApplicationContext());
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
        updateTitle(getString(R.string.tp_title_tokopoints));
    }

    @Override
    protected Fragment getNewFragment() {
        if (mUserSession.isLoggedIn()) {
            return TokoPointsHomeFragmentNew.newInstance();
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
            return null;
        }
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TokoPointsHomeNewActivity.class);
    }

    private void initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            inflateFragment();
        } else {
            finish();
        }
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
