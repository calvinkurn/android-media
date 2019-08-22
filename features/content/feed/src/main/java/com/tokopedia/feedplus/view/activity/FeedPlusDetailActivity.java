package com.tokopedia.feedplus.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailActivity extends BaseSimpleActivity {

    public static final String EXTRA_DETAIL_ID = "extra_detail_id";
    public static final String EXTRA_ANALYTICS_PAGE_ROW_NUMBER = "EXTRA_ANALYTICS_PAGE_ROW_NUMBER";

    @DeepLink(ApplinkConst.FEED_DETAILS)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, FeedPlusDetailActivity.class)
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent getIntent(FragmentActivity activity, String detailId,
                                   String pageRowNumber) {
        Intent intent = new Intent(activity, FeedPlusDetailActivity.class);
        intent.putExtra(EXTRA_DETAIL_ID, detailId);
        intent.putExtra(EXTRA_ANALYTICS_PAGE_ROW_NUMBER, pageRowNumber);

        return intent;
    }


    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        return FeedPlusDetailFragment.createInstance(bundle);
    }

}