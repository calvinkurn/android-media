package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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
import com.tokopedia.tokopoints.view.fragment.CouponDetailFragment;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;

public class CouponDetailActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent> {
    private static final int REQUEST_CODE_LOGIN = 1;
    private TokoPointComponent tokoPointComponent;
    private Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forDeeplink();
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_title_detail));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0f);
        }
    }

    private void forDeeplink() {
        bundle = getIntent().getExtras();
        if ( getIntent().getData() != null){
           bundle = UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_COUPON_DETAIL,getIntent().getData(),bundle);

        }
    }

    @Override
    protected Fragment getNewFragment() {
            return CouponDetailFragment.newInstance(bundle);
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    public static Intent getCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, CouponDetailActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent getCouponDetail(Context context, Bundle extras) {
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

        AnalyticsTrackerUtil.sendEvent(getApplicationContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.CLICK_BACK_ARROW,
                AnalyticsTrackerUtil.EventKeys.BACK_ARROW_LABEL);
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
