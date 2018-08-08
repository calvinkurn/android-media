package com.tokopedia.product.edit.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.product.edit.view.fragment.ProductAddInfoFragment;

/**
 * Created by normansyahputa on 1/4/18.
 */

public class ProductAddDescriptionInfoActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return ProductAddInfoFragment.create("file:///android_asset/add-product-info.html");
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

}
