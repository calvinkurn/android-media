package com.tokopedia.shop.note.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.note.view.fragment.ShopNoteDetailFragment;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class ShopNoteDetailActivity extends BaseSimpleActivity {

    private String shopNoteId;
    private String shopId;
    public static Intent createIntent(Context context, String shopId, String shopNoteId) {
        Intent intent = new Intent(context, ShopNoteDetailActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_NOTE_ID, shopNoteId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_ID);
        shopNoteId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_NOTE_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopNoteDetailFragment.newInstance(shopId, shopNoteId);
    }
}
