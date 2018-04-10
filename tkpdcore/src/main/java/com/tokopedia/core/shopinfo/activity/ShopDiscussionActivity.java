package com.tokopedia.core.shopinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.shopinfo.fragment.ShopTalkFragment;

/**
 * Created by nathan on 3/5/18.
 */

public class ShopDiscussionActivity extends BaseSimpleActivity {

    public static final String SHOP_ID = "SHOP_ID";

    private String shopId;

    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopDiscussionActivity.class);
        intent.putExtra(SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(SHOP_ID);
        updateShopDiscussionIntent();
        super.onCreate(savedInstanceState);
    }

    /**
     * Old Discussion fragment need this intent, need updated code
     * com.tokopedia.core.shopinfo.presenter.ShopTalkPresenterImpl
     */
    @Deprecated
    private void updateShopDiscussionIntent() {
        getIntent().putExtra("shop_id", shopId);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopTalkFragment.createInstance();
    }
}
