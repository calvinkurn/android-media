package com.tokopedia.logisticorder.view;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPageActivity extends BaseSimpleActivity {

    public static final String ORDER_ID_KEY = "order_id";
    public static final String URL_LIVE_TRACKING = "url_live_tracking";
    public static final String ORDER_CALLER = "caller";

    String orderId, urlLiveTracking, orderCaller;

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if (fragment != null) {
            return fragment;
        } else {
            if(getIntent() != null && getIntent().getExtras() != null && getIntent().getData().getLastPathSegment() != null) {
                orderId = getIntent().getData().getLastPathSegment();
                urlLiveTracking = getIntent().getData().getQueryParameter(URL_LIVE_TRACKING);
                orderCaller = getIntent().getData().getQueryParameter(ORDER_CALLER);
            }
            fragment = TrackingPageFragment.createFragment(orderId, urlLiveTracking, orderCaller);
        }
        return fragment;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
