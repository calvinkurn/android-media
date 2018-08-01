package com.tokopedia.feedplus;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.feedplus.view.fragment.ReactNativeContentDetailFragment;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

public class ReactNativeContentDetailsActivity extends ReactFragmentActivity<ReactNativeContentDetailFragment> {

    public static final String CONTENT_DETAIL = "content-detail";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String KEY_ID = "post_id";


    //TODO milhamj remove react native applink
//    @DeepLink({Constants.Applinks.CONTENT_DETAIL})
//    public static Intent getContentDetailPageApplinkCallingIntent(Context context, Bundle bundle) {
//        ScreenTracking.screen(CONTENT_DETAIL);
//        bundle.getString(KEY_ID);
//        return ReactNativeContentDetailsActivity.createApplinkCallingIntent(
//                context,
//                ReactConst.Screen.CONTENT_DETAIL,
//                context.getString(R.string.react_native_content_detail_title),
//                bundle
//        );
//    }

    public static Intent createApplinkCallingIntent(Context context, String reactScreenName, String pageTitle, Bundle extras) {
        Intent intent = new Intent(context, ReactNativeContentDetailsActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


    public static Intent createCallingIntent(Context context,
                                             String reactScreenName,
                                             String pageTitle) {
        Intent intent = new Intent(context, ReactNativeContentDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected ReactNativeContentDetailFragment getReactNativeFragment() {
        return ReactNativeContentDetailFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }
        return "";
    }
}