package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.fragment.CatalogListingFragment;
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener;
import com.tokopedia.user.session.UserSession;

public class CatalogListingActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent>, onAppBarCollapseListener {
    private static final int REQUEST_CODE_LOGIN = 1;
    private TokoPointComponent tokoPointComponent;
    private UserSession mUserSession;
    private Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserSession = new UserSession(getApplicationContext());
        forDeeplink();
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_label_exchange_points));
    }

    private void forDeeplink() {
        bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();
        if (getIntent().getData() != null){
            UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_CATALOG_LISTING,getIntent().getData(),bundle);
        }
    }

    @Override
    protected Fragment getNewFragment() {
            return CatalogListingFragment.newInstance(bundle);
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    public static Intent getCallingIntent(Context context, Bundle extras) {
        final Intent intent = new Intent(context, CatalogListingActivity.class);
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
    }

    @Override
    public void showToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(getResources().getDimension(com.tokopedia.design.R.dimen.dp_4));
        }
    }

    @Override
    public void hideToolbarElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(getResources().getDimension(com.tokopedia.design.R.dimen.dp_0));
        }
    }

}