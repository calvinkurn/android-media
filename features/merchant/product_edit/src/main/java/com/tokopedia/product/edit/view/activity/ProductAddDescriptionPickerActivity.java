package com.tokopedia.product.edit.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.product.edit.R;
import com.tokopedia.product.edit.view.fragment.ProductAddDescriptionPickerFragment;

/**
 * Created by nathan on 3/6/18.
 */

public class ProductAddDescriptionPickerActivity extends BaseSimpleActivity {

    public static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";

    private ProductAddDescriptionPickerFragment productAddDescriptionPickerFragment;

    public static Intent start(Activity activity, String description) {
        Intent intent = new Intent(activity, ProductAddDescriptionPickerActivity.class);
        intent.putExtra(PRODUCT_DESCRIPTION, description);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        productAddDescriptionPickerFragment = ProductAddDescriptionPickerFragment.newInstance(
                getIntent().getExtras().getString(PRODUCT_DESCRIPTION));
        return productAddDescriptionPickerFragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_product_edit_with_menu;
    }
}
