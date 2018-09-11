package com.tokopedia.mitra.digitalcategory.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.mitra.digitalcategory.presentation.fragment.MitraDigitalCategoryFragment;

/**
 * Created by Rizky on 30/08/18.
 */
public class MitraDigitalCategoryActivity extends BaseSimpleActivity {

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

    private int categoryId;

    public static Intent newInstance(Activity activity, int categoryId) {
        Intent intent = new Intent(activity, MitraDigitalCategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 0);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return MitraDigitalCategoryFragment.newInstance(categoryId);
    }

}
