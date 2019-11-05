package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.tokopoints.ApplinkConstant;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.fragment.CouponCatalogFragment;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

public class CouponCatalogDetailsActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent> {
    private TokoPointComponent tokoPointComponent;
    private Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forDeeplink();
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_title_detail));

    }

    private void forDeeplink() {
        bundle = getIntent().getExtras();
        if (bundle == null)
            bundle = new Bundle();
        if (getIntent().getData() != null){
            UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_CATALOG_DETAIL,getIntent().getData(),bundle);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return CouponCatalogFragment.newInstance(bundle);
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
