package com.tokopedia.explore.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.explore.view.fragment.ContentExploreFragment;

/**
 * @author by milhamj on 01/08/18.
 */

public class ContentExploreActivity extends BaseSimpleActivity {

    public static String PARAM_CATEGORY_ID = "category_id";
    public static String DEFAULT_CATEGORY = "0";

    @DeepLink({ApplinkConst.CONTENT_EXPLORE})
    public static Intent newInstance(Context context, Bundle bundle) {
        String categoryId = bundle.getString(PARAM_CATEGORY_ID, DEFAULT_CATEGORY);
        return newInstance(context, categoryId);
    }

    public static Intent newInstance(Context context) {
        return newInstance(context, DEFAULT_CATEGORY);
    }

    public static Intent newInstance(Context context, String categoryId) {
        Intent intent = new Intent(context, ContentExploreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_CATEGORY_ID, categoryId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        return ContentExploreFragment.newInstance(bundle);
    }
}
