package com.tokopedia.tkpd.tkpdreputation.review.shop.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopInfoActivity extends BaseSimpleActivity {

    private String shopId;
    private String shopDomain;

    public static Intent createIntent(Context context, String shopId, String shopDomain){
        Intent intent = new Intent(context, ReviewShopInfoActivity.class);
        intent.putExtra(ReviewShopFragment.SHOP_ID, shopId);
        intent.putExtra(ReviewShopFragment.SHOP_DOMAIN, shopDomain);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            shopId = getIntent().getStringExtra(ReviewShopFragment.SHOP_ID);
            shopDomain = getIntent().getStringExtra(ReviewShopFragment.SHOP_DOMAIN);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ReviewShopFragment.createInstance(shopId, shopDomain);
    }
}
