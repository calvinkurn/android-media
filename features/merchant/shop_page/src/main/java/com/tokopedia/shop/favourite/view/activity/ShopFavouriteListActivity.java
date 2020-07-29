package com.tokopedia.shop.favourite.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.favourite.view.fragment.ShopFavouriteListFragment;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopFavouriteListActivity extends BaseSimpleActivity implements HasComponent<ShopComponent> {

    private String shopId;

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopFavouriteListActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopFavouriteListFragment.createInstance(shopId);
    }

    @Override
    public ShopComponent getComponent() {
        return ShopComponentInstance.getComponent(getApplication(), this);
    }
}
