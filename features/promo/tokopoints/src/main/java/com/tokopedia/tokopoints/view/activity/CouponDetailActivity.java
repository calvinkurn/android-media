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
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.fragment.CouponDetailFragment;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.user.session.UserSession;

public class CouponDetailActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent> {
    private static final int REQUEST_CODE_LOGIN = 1;
    private TokoPointComponent tokoPointComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_title_detail));
    }

    @Override
    protected Fragment getNewFragment() {
        if (((TokopointRouter) getApplicationContext()).getSession().isLoggedIn()) {
            return CouponDetailFragment.newInstance(getIntent().getExtras());
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

    public static Intent getCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, CouponDetailActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    @DeepLink({ApplinkConstant.COUPON_DETAIL3, ApplinkConstant.COUPON_DETAIL4})
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
