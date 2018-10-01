package com.tokopedia.topchat.attachproduct.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.topchat.R;
import com.tokopedia.topchat.attachproduct.view.fragment.BMAttachProductFragment;
import com.tokopedia.topchat.attachproduct.view.resultmodel.ResultProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BMAttachProductActivity extends AttachProductActivity {
    public static final String TOKOPEDIA_ATTACH_PRODUCT_IDS_KEY = "TKPD_ATTACH_PRODUCT_IDS";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_HASH_KEY = "TKPD_ATTACH_PRODUCT_HASH";

    public static Intent createInstance(Context context, String shopId, String shopName, boolean isSeller) {
        Intent intent = new Intent(context, BMAttachProductActivity.class);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY,shopId);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY,isSeller);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY,shopName);
        return intent;
    }

    public static Intent createInstance(Context context, String shopId, String shopName, boolean isSeller,
                                        List<Integer> ids, ArrayList<HashMap<String, String>> hashProducts) {
        Intent intent = createInstance(context, shopId, shopName, isSeller);
        intent.putIntegerArrayListExtra(TOKOPEDIA_ATTACH_PRODUCT_IDS_KEY, new ArrayList<>(ids));
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_HASH_KEY, hashProducts);
        return intent;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        String subtitle = toolbar.getSubtitle().toString();
        if (!TextUtils.isEmpty(subtitle))
            toolbar.setSubtitle(getString(R.string.template_subtitle_attach_product, subtitle));
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if(fragment != null){
            return fragment;
        }else{
            fragment = BMAttachProductFragment.newInstance(this);
            return fragment;
        }
    }

    @Override
    public void finishActivityWithResult(ArrayList<ResultProduct> products) {
        Intent data = new Intent();
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        for (ResultProduct product : products){
            HashMap<String, String> item = new HashMap<>();
            item.put("id", product.getProductId().toString());
            item.put("name", product.getName());
            item.put("price", product.getPrice());
            item.put("url", product.getProductUrl());
            item.put("thumbnail", product.getProductImageThumbnail());
            results.add(item);
        }

        data.putExtra(TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY,results);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
