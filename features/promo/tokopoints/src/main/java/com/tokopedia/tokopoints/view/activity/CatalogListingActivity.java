package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.fragment.CatalogListingFragment;

public class CatalogListingActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent> {
    private TokoPointComponent tokoPointComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_label_exchange_points));
        GraphqlClient.init(getApplicationContext());
    }

    @Override
    protected Fragment getNewFragment() {
        return CatalogListingFragment.newInstance();
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    @DeepLink(ApplinkConstant.CATALOG_LISTING)
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CatalogListingActivity.class);
    }

    private void initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }
}
