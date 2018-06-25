package com.tokopedia.digital_deals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.fragment.DealsHomeFragment;

import static com.tokopedia.digital_deals.view.utils.Utils.Constants.DIGITAL_DEALS;

public class DealsHomeActivity extends BaseSimpleActivity {


    public static final int REQUEST_CODE_DEALSLOCATIONACTIVITY = 101;
    public static final int REQUEST_CODE_DEALSSEARCHACTIVITY = 102;
    @DeepLink({DIGITAL_DEALS})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        Intent destination;
        try {
            String deepLink = extras.getString(DeepLink.URI);

            Uri.Builder uri = Uri.parse(deepLink).buildUpon();
            destination = new Intent(context, DealsHomeActivity.class)
                    .setData(uri.build())
                    .putExtras(extras);

        } catch (Exception e) {
            destination = new Intent(context, DealsHomeActivity.class);
        }
        return destination;
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_deals;
    }

    @Override
    protected Fragment getNewFragment() {
        return DealsHomeFragment.createInstance();
    }
}
