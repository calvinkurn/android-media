package com.tokopedia.shopetalasepicker.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.etalase.view.fragment.ShopEtalaseFragment;
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseActivity extends BaseSimpleActivity {

    private String shopId;
    private String selectedEtalaseId;

    public static Intent createIntent(Context context, String shopId, String selectedEtalaseId) {
        Intent intent = new Intent(context, ShopEtalaseActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, selectedEtalaseId);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopEtalaseFragment.createInstance(shopId, selectedEtalaseId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_ID);
        selectedEtalaseId = getIntent().getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
        super.onCreate(savedInstanceState);
    }

}
