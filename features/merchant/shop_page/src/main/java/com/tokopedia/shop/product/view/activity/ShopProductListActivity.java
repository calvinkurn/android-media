package com.tokopedia.shop.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopAppLink;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListActivity extends BaseSimpleActivity implements HasComponent<ShopComponent> {

    private String shopId;
    private String keyword;
    private ShopComponent component;
    private String etalaseId;
    private String sort;
    private String attribution;

    public static Intent createIntent(Context context, String shopId, String keyword,
                                      String etalaseId, String attribution, String sortId) {
        Intent intent = createIntent(context, shopId, keyword, etalaseId, attribution);
        intent.putExtra(ShopParamConstant.EXTRA_SORT_ID, sortId);
        return intent;
    }

    public static Intent createIntent(Context context, String shopId, String keyword,
                                      String etalaseId, String attribution) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        intent.putExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword);
        intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId);
        intent.putExtra(ShopParamConstant.EXTRA_ATTRIBUTION, attribution);
        return intent;
    }


    public static Intent createIntent(Context context, String shopId) {
        Intent intent = new Intent(context, ShopProductListActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        return intent;
    }

    @DeepLink(ShopAppLink.SHOP_ETALASE)
    public static Intent getCallingIntentEtalaseSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopProductListActivity.class)
                .setData(uri.build())
                .putExtra(ShopParamConstant.EXTRA_SHOP_ID, extras.getString(ShopParamConstant.KEY_SHOP_ID))
                .putExtra(ShopParamConstant.EXTRA_ETALASE_ID, extras.getString(ShopParamConstant.KEY_ETALASE_ID));
    }

    @DeepLink(ShopAppLink.SHOP_ETALASE_WITH_KEYWORD_AND_SORT)
    public static Intent getCallingIntentEtalaseSelectedWithKeywordAndSort(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopProductListActivity.class)
                .setData(uri.build())
                .putExtra(ShopParamConstant.EXTRA_SHOP_ID, extras.getString(ShopParamConstant.KEY_SHOP_ID))
                .putExtra(ShopParamConstant.EXTRA_ETALASE_ID, extras.getString(ShopParamConstant.KEY_ETALASE_ID))
                .putExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, extras.getString(ShopParamConstant.KEY_KEYWORD))
                .putExtra(ShopParamConstant.EXTRA_SORT_ID, extras.getString(ShopParamConstant.KEY_SORT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_ID);
        keyword = getIntent().getStringExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD);
        etalaseId = getIntent().getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
        sort = getIntent().getStringExtra(ShopParamConstant.EXTRA_SORT_ID);
        attribution = getIntent().getStringExtra(ShopParamConstant.EXTRA_ATTRIBUTION);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopProductListFragment.createInstance(shopId, keyword, etalaseId, sort, attribution);
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = ShopComponentInstance.getComponent(getApplication());
        }
        return component;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_product_list;
    }
}