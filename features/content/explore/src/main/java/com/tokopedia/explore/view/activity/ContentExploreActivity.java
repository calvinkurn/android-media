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

    @DeepLink({ApplinkConst.CONTENT_EXPLORE})
    public static Intent newInstance(Context context, Bundle bundle) {
        int categoryId = bundle.getInt(PARAM_CATEGORY_ID, 0);
        return newInstance(context, categoryId);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ContentExploreActivity.class);
        intent.putExtra(PARAM_CATEGORY_ID, 0);
        return intent;
    }

    public static Intent newInstance(Context context, int categoryId) {
        Intent intent = new Intent(context, ContentExploreActivity.class);
        intent.putExtra(PARAM_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ContentExploreFragment.newInstance();
    }
}
