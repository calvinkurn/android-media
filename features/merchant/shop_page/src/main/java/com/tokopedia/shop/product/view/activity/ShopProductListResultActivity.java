package com.tokopedia.shop.product.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.shop.R;
import com.tokopedia.shop.ShopComponentHelper;
import com.tokopedia.shop.analytic.OldShopPageTrackingBuyer;
import com.tokopedia.shop.common.constant.ShopParamConstant;
import com.tokopedia.shop.common.di.component.ShopComponent;
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo;
import com.tokopedia.shop.product.view.fragment.ShopPageProductListResultFragment;
import com.tokopedia.shop.product.view.listener.OnShopProductListFragmentListener;
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.SCREEN_SHOP_PAGE;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductListResultActivity extends BaseSimpleActivity
        implements HasComponent<ShopComponent>,
        OnShopProductListFragmentListener,
        ShopPageProductListResultFragment.ShopPageProductListResultFragmentListener {

    public static final String SAVED_KEYWORD = "svd_keyword";
    private static final String QUERY_SHOP_REF = "shop_ref";
    private static final String QUERY_SORT = "sort";
    private static final String QUERY_ATTRIBUTION = "tracker_attribution";
    private static final String QUERY_SEARCH = "search";
    private ShopComponent component;
    private String shopId;
    private String shopRef = "";

    // this field only used first time for new fragment
    private String keyword = "";
    private String etalaseId;
    private String sort;
    private String attribution;
    private Boolean isNeedToReloadData = false;

    private ShopInfo shopInfo;
    private OldShopPageTrackingBuyer shopPageTracking;
    private EditText editTextSearch;
    private AppCompatImageView actionUpBtn;

    public static Intent createIntent(Context context, String shopId, String keyword,
                                      String etalaseId, String attribution, String sortId, String shopRef) {
        Intent intent = createIntent(context, shopId, keyword, etalaseId, attribution, shopRef);
        intent.putExtra(ShopParamConstant.EXTRA_SORT_ID, sortId);
        return intent;
    }

    public static Intent createIntent(Context context, String shopId, String keyword,
                                      String etalaseId, String attribution, String shopRef) {
        Intent intent = new Intent(context, ShopProductListResultActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        intent.putExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword);
        intent.putExtra(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId);
        intent.putExtra(ShopParamConstant.EXTRA_ATTRIBUTION, attribution);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_REF, shopRef);
        return intent;
    }

    public static Intent createIntent(Context context, String shopId, String shopRef) {
        Intent intent = new Intent(context, ShopProductListResultActivity.class);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_ID, shopId);
        intent.putExtra(ShopParamConstant.EXTRA_SHOP_REF, shopRef);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_ID);
        shopRef = getIntent().getStringExtra(ShopParamConstant.EXTRA_SHOP_REF);
        etalaseId = getIntent().getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID);
        sort = getIntent().getStringExtra(ShopParamConstant.EXTRA_SORT_ID) == null ? "" : getIntent().getStringExtra(ShopParamConstant.EXTRA_SORT_ID);
        attribution = getIntent().getStringExtra(ShopParamConstant.EXTRA_ATTRIBUTION);
        isNeedToReloadData = getIntent().getBooleanExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false);
        if (savedInstanceState == null) {
            keyword = getIntent().getStringExtra(ShopParamConstant.EXTRA_PRODUCT_KEYWORD);
            if (null == keyword) {
                keyword = "";
            }
        } else {
            keyword = savedInstanceState.getString(SAVED_KEYWORD, "");
        }
        Uri data = getIntent().getData();
        if (null != data) {
            List<String> pathSegments = data.getPathSegments();
            getShopIdFromUri(data, pathSegments);
            getEtalaseIdFromUri(data, pathSegments);
            shopRef = data.getQueryParameter(QUERY_SHOP_REF) == null ? "" : data.getQueryParameter(QUERY_SHOP_REF);
            sort = data.getQueryParameter(QUERY_SORT) == null ? "" : data.getQueryParameter(QUERY_SORT);
            attribution = data.getQueryParameter(QUERY_ATTRIBUTION) == null ? "" : data.getQueryParameter(QUERY_ATTRIBUTION);
            keyword = data.getQueryParameter(QUERY_SEARCH) == null ? "" : data.getQueryParameter(QUERY_SEARCH);
        }

        if (shopRef == null) {
            shopRef = "";
        }
        shopPageTracking = new OldShopPageTrackingBuyer(new TrackingQueue(this));
        super.onCreate(savedInstanceState);
        initSearchInputView();
        findViewById(R.id.mainLayout).requestFocus();
    }

    private void getShopIdFromUri(Uri data, List<String> pathSegments) {
        if (pathSegments.size() >= 2) {
            shopId = data.getPathSegments().get(1);
        } else {
            shopId = "0";
        }
    }

    private void getEtalaseIdFromUri(Uri data, List<String> pathSegments) {
        if (pathSegments.size() >= 4) {
            etalaseId = data.getPathSegments().get(3);
        } else {
            etalaseId = "0";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        shopPageTracking.sendAllTrackingQueue();
    }

    private void initSearchInputView() {
        editTextSearch = findViewById(R.id.editTextSearchProduct);
        actionUpBtn = findViewById(R.id.actionUpBtn);
        editTextSearch.setText(keyword);
        editTextSearch.setKeyListener(null);
        editTextSearch.setMovementMethod(null);
        editTextSearch.setOnClickListener(view -> {
            if (null != shopPageTracking)
                shopPageTracking.clickSearchBox(SCREEN_SHOP_PAGE);
            if (null != shopInfo) {
                redirectToShopSearchProduct();
            }
        });
        actionUpBtn.setOnClickListener(view -> {
            finish();
        });
    }

    private void redirectToShopSearchProduct() {
        startActivity(ShopSearchProductActivity.createIntent(
                this,
                shopId,
                shopInfo.getShopCore().getName(),
                shopInfo.getGoldOS().isOfficial() == 1,
                shopInfo.getGoldOS().isGold() == 1,
                keyword,
                attribution,
                shopRef
        ));
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopPageProductListResultFragment.createInstance(shopId, shopRef, keyword, etalaseId, sort, attribution, isNeedToReloadData);
    }

    @Override
    public ShopComponent getComponent() {
        if (component == null) {
            component = new ShopComponentHelper().getComponent(getApplication(), this);
        }
        return component;
    }

    @Override
    public void updateUIByShopName(String shopName) {
        if (null != editTextSearch)
            editTextSearch.setHint(getString(R.string.shop_product_search_hint_2,
                    MethodChecker.fromHtml(shopName)));
    }


    @Override
    public void updateUIByEtalaseName(String etalaseName) {
        if (null != editTextSearch) {
            editTextSearch.setHint(getString(R.string.shop_product_search_hint_3,
                    MethodChecker.fromHtml(etalaseName)));
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_new_shop_product_list_result;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = RouteManager.getIntentNoFallback(this, ApplinkConst.HOME);
            if (intent != null) {
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
        outState.putString(SAVED_KEYWORD, keyword);
    }

    @Override
    public void updateShopInfo(@NotNull ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    @Override
    public void onSortValueUpdated(@NotNull String sortValue) {
        this.sort = sortValue;
    }

    @Override
    protected int getParentViewResourceID() {
        return R.id.parent_view;
    }

}