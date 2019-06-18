package com.tokopedia.kol.feature.postdetail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.kol.feature.postdetail.view.fragment.KolPostDetailFragment;

/**
 * @author by yfsx on 23/07/18.
 */
public class KolPostDetailActivity extends BaseSimpleActivity {

    public static final String PARAM_POST_ID = "post_id";
    public static final String DEFAULT_POST_ID = "0";

    public static final int RESULT_DELETED = 10;


    @DeepLink({ApplinkConst.CONTENT_DETAIL})
    public static Intent getInstance(Context context, Bundle bundle) {
        return getInstance(context, bundle.getString(PARAM_POST_ID, DEFAULT_POST_ID));
    }

    public static Intent getInstance(Context context, String id) {
        Intent intent = new Intent(context, KolPostDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_POST_ID, id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return KolPostDetailFragment.getInstance(getIntent().getExtras());
    }

}
