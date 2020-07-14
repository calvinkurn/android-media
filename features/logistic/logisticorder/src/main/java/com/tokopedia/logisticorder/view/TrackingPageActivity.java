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

    @Override
    protected Fragment getNewFragment() {
        return TrackingPageFragment.createFragment(
                getIntent().getData().getLastPathSegment(),
                getIntent().getData().getQueryParameter(URL_LIVE_TRACKING),
                getIntent().getData().getQueryParameter(ORDER_CALLER)
        );
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
