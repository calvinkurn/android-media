package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.DigitalCategoryNoStyleFragment;

/**
 * Created by Rizky on 30/08/18.
 */
public class DigitalCategoryNoStyleActivity extends BaseSimpleActivity
        implements DigitalCategoryNoStyleFragment.ActionListener {

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

    private static final String APPLINK_PARAM_CATEGORY_ID = "category_id";

    private String categoryId;

    @SuppressWarnings("unused")
    public static Intent getcallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, DigitalCategoryNoStyleActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, extras.getString(APPLINK_PARAM_CATEGORY_ID));
        return intent
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent newInstance(Activity activity, int categoryId) {
        Intent intent = new Intent(activity, DigitalCategoryNoStyleActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra(EXTRA_CATEGORY_ID);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return DigitalCategoryNoStyleFragment.newInstance(Integer.valueOf(categoryId));
    }

    @Override
    public void updateToolbarTitle(String toolbarTitle) {
        updateTitle(toolbarTitle);
    }

}