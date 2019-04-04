package com.tokopedia.tokopoints.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.fragment.SendGiftFragment;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

public class SendGiftActivity extends BaseSimpleActivity implements HasComponent<TokoPointComponent> {
    private TokoPointComponent tokoPointComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.tp_title_send_coupon));
        toolbar.setNavigationIcon(R.drawable.navigation_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsTrackerUtil.sendEvent(SendGiftActivity.this,
                        AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                        AnalyticsTrackerUtil.CategoryKeys.POPUP_KIRIM_KUPON,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_CLOSE_BUTTON,
                        getCouponTitle());
                onBackPressed();
            }
        });
    }

    @Override
    protected Fragment getNewFragment() {
        return SendGiftFragment.newInstance(getIntent().getExtras());
    }

    private String getCouponTitle() {
        if (getIntent() != null) {
            String couponTitle = getIntent().getStringExtra(CommonConstant.EXTRA_COUPON_TITLE);
            if (couponTitle != null)
                return couponTitle;
        }
        return "";
    }

    @Override
    public TokoPointComponent getComponent() {
        if (tokoPointComponent == null) initInjector();
        return tokoPointComponent;
    }

    public static Intent getCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, SendGiftActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    private void initInjector() {
        tokoPointComponent = DaggerTokoPointComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }
}
