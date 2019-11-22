package com.tokopedia.product.manage.list.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.list.view.fragment.ProductManageFilterFragment;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.product.manage.view.model.ProductManageFilterModel;

import static com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_FILTER_SELECTED;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageFilterActivity extends BaseSimpleActivity {

    TextView optionReset;

    public static Intent createIntent(Context context, ProductManageFilterModel productManageFilterModel){
        Intent intent = new Intent(context, ProductManageFilterActivity.class);
        intent.putExtra(EXTRA_FILTER_SELECTED, productManageFilterModel);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_filter_product_manage;
    }

    @Override
    protected int getToolbar() {
        return R.id.toolbar;
    }

    @Override
    protected int getParent_view() {
        return R.id.parent_view;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        optionReset = (TextView) findViewById(com.tokopedia.product.manage.list.R.id.option_menu_reset);
        optionReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragment() != null && !getFragment().isDetached()){
                    ((ProductManageFilterFragment) getFragment()).onResetFilter();
                }
            }
        });
    }

    @Override
    protected Fragment getNewFragment() {
        ProductManageFilterModel productManageFilterModel = getIntent().getParcelableExtra(EXTRA_FILTER_SELECTED);
        return ProductManageFilterFragment.createInstance(productManageFilterModel);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
