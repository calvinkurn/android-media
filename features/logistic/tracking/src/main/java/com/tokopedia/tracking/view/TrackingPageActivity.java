package com.tokopedia.tracking.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPageActivity extends BaseSimpleActivity {

    public static final String ORDER_ID_KEY = "order_id";
    public static final String URL_LIVE_TRACKING = "url_live_tracking";
    public static final String ORDER_CALLER = "caller";

    @DeepLink(ApplinkConst.ORDER_TRACKING)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        return createIntent(context,
                extras.getString(ApplinkConst.Query.ORDER_TRACKING_ORDER_ID),
                extras.getString(ApplinkConst.Query.ORDER_TRACKING_URL_LIVE_TRACKING),
                extras.getString(ApplinkConst.Query.ORDER_TRACKING_CALLER)
        );
    }

    public static Intent createIntent(Context context, String orderId, String liveTrackingUrl,
                                      String caller) {
        Intent intent = new Intent(context, TrackingPageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_KEY, orderId);
        bundle.putString(URL_LIVE_TRACKING, liveTrackingUrl);
        bundle.putString(ORDER_CALLER, caller);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrackingPageFragment.createFragment(
                getIntent().getExtras().getString(ORDER_ID_KEY, ""),
                getIntent().getExtras().getString(URL_LIVE_TRACKING, ""),
                getIntent().getExtras().getString(ORDER_CALLER, "")
        );
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
