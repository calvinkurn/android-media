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
import com.tokopedia.tokopoints.view.fragment.CouponCatalogFragment;
import com.tokopedia.tokopoints.view.fragment.HomepageFragment;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

public class CouponCatalogDetailsActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent> {
    private TokoPointComponent tokoPointComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_title_tokopoints));
        GraphqlClient.init(getApplicationContext());
    }

    @Override
    protected Fragment getNewFragment() {
        return CouponCatalogFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    public static Intent getCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, CouponCatalogDetailsActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    @DeepLink(ApplinkConstant.COUPON_DETAIL)
    public static Intent getCouponDetail(Context context, Bundle extras) {
        return getCallingIntent(context, extras);
    }

    @DeepLink(ApplinkConstant.CATALOG_DETAIL)
    public static Intent getCatalogDetail(Context context, Bundle extras) {
        return getCallingIntent(context, extras);
    }

    private void initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getIntent().getStringExtra(CommonConstant.EXTRA_CATALOG_CODE) == null) {
            AnalyticsTrackerUtil.sendEvent(getApplicationContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_BACK_ARROW,
                    AnalyticsTrackerUtil.EventKeys.BACK_ARROW_LABEL);
        } else {
            AnalyticsTrackerUtil.sendEvent(getApplicationContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_BACK_ARROW,
                    AnalyticsTrackerUtil.EventKeys.BACK_ARROW_LABEL);
        }
    }
}
