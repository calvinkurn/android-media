package com.tokopedia.shop.settings.basicinfo.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.basicinfo.view.fragment.ShopEditBasicInfoFragment;

/**
 * Created by hendry on 13/08/18.
 */

public class ShopEditBasicInfoActivity extends BaseSimpleActivity
    implements ShopEditBasicInfoFragment.OnShopEditBasicInfoFragmentListener{

    private TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvSave = findViewById(R.id.tvSave);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopEditBasicInfoFragment.newInstance();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_edit_basic_info;
    }

    @Override
    public void onDataLoaded() {
        tvSave.setVisibility(View.VISIBLE);
    }
}
