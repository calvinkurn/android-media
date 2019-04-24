package com.tokopedia.shop.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentInstance;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.page.view.activity.ShopPageActivity;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListActivity extends BaseSimpleActivity
        implements HasComponent<ShopComponent>,
        ShopProductListFragment.OnShopProductListFragmentListener{

    public static final String SAVED_KEYWORD = "svd_keyword";

    private ShopComponent component;
    private String shopId;

    // this field only used first time for new fragment
    private String keyword;
    private String etalaseId;
    private String sort;
    private String attribution;

    private SearchInputView searchInputView;

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

    @DeepLink(ApplinkConst.SHOP_ETALASE)
    public static Intent getCallingIntentEtalaseSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopProductListActivity.class)
                .setData(uri.build())
                .putExtra(ShopParamConstant.EXTRA_SHOP_ID, extras.getString(ShopParamConstant.KEY_SHOP_ID))
                .putExtra(ShopParamConstant.EXTRA_ATTRIBUTION, extras.getString(ShopPageActivity.APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtra(ShopParamConstant.EXTRA_ETALASE_ID, extras.getString(ShopParamConstant.KEY_ETALASE_ID));
    }

    @DeepLink(ApplinkConst.SHOP_ETALASE_WITH_KEYWORD_AND_SORT)
    public static Intent getCallingIntentEtalaseSelectedWithKeywordAndSort(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopProductListActivity.class)
                .setData(uri.build())
                .putExtra(ShopParamConstant.EXTRA_SHOP_ID, extras.getString(ShopParamConstant.KEY_SHOP_ID))
                .putExtra(ShopParamConstant.EXTRA_ATTRIBUTION, extras.getString(ShopPageActivity.APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                .putExtra(ShopParamConstant.EXTRA_ETALASE_ID, extras.getString(ShopParamConstant.KEY_ETALASE_ID))
                .putExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, extras.getString(ShopParamConstant.KEY_KEYWORD))
                .putExtra(ShopParamConstant.EXTRA_SORT_ID, extras.getString(ShopParamConstant.KEY_SORT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_ID);
        etalaseId = getIntent().getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
        sort = getIntent().getStringExtra(ShopParamConstant.EXTRA_SORT_ID);
        attribution = getIntent().getStringExtra(ShopParamConstant.EXTRA_ATTRIBUTION);
        if (savedInstanceState == null) {
            keyword = getIntent().getStringExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD);
        } else {
            keyword = savedInstanceState.getString(SAVED_KEYWORD);
        }

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        searchInputView = findViewById(R.id.searchInputView);
        searchInputView.getSearchTextView().setText(keyword);

        searchInputView.setListener(new SearchInputView.Listener() {
            @Override
            public void onSearchSubmitted(String text) {
                ShopProductListFragment fragment = (ShopProductListFragment) getFragment();
                if (fragment!= null) {
                    fragment.updateDataByChangingKeyword(text);
                }

                KeyboardHandler.hideSoftKeyboard(ShopProductListActivity.this);
            }

            @Override
            public void onSearchTextChanged(String text) {
                if (TextUtils.isEmpty(text)) {
                    onSearchSubmitted(text);
                }
            }
        });

        findViewById(R.id.mainLayout).requestFocus();
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
    public void updateUIByShopName(String shopName) {
        searchInputView.setSearchHint(getString(R.string.shop_product_search_hint_2,
                MethodChecker.fromHtml(shopName)));
    }


    @Override
    public void updateUIByEtalaseName(String etalaseName) {
        searchInputView.setSearchHint(getString(R.string.shop_product_search_hint_3,
                MethodChecker.fromHtml(etalaseName)));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_product_list;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            String applink = GlobalConfig.isSellerApp()? ApplinkConst.SellerApp.SELLER_APP_HOME: ApplinkConst.HOME;
            ApplinkRouter router = (ApplinkRouter) getApplicationContext();
            if (router.isSupportApplink(applink)) {
                Intent intent = router.getApplinkIntent(this, applink);
                startActivity(intent);
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_KEYWORD, searchInputView.getSearchText());
    }
}