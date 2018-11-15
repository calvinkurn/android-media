package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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
import com.tokopedia.tokopoints.view.fragment.CatalogListingFragment;
import com.tokopedia.user.session.UserSession;

public class CatalogListingActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent> {
    private static final int REQUEST_CODE_LOGIN = 1;
    private TokoPointComponent tokoPointComponent;
    private UserSession mUserSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserSession = new UserSession(getApplicationContext());
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_label_exchange_points));
    }

    @Override
    protected Fragment getNewFragment() {
        if (mUserSession.isLoggedIn()) {
            return CatalogListingFragment.newInstance(getIntent().getExtras());
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

    @DeepLink({ApplinkConstant.CATALOG_LISTING, ApplinkConstant.CATALOG_LISTING2, ApplinkConstant.CATALOG_LISTING3, ApplinkConstant.CATALOG_LISTING4})
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, CatalogListingActivity.class);
        intent.putExtras(extras);
        return intent;
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
}