package com.tokopedia.attachproduct.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.attachproduct.R;
import com.tokopedia.attachcommon.data.ResultProduct;
import com.tokopedia.attachproduct.view.fragment.AttachProductFragment;
import com.tokopedia.attachproduct.view.presenter.AttachProductContract;

import java.util.ArrayList;

import static com.tokopedia.applink.ApplinkConst.AttachProduct.*;

/**
 * For navigating to this class
 * [com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.ATTACH_PRODUCT]
 * Please pass com.tokopedia.applink.ApplinkConst.AttachProduct
 */

public class AttachProductActivity extends BaseSimpleActivity implements AttachProductContract
        .Activity {
    public static final int TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK = 324;

    public static final String SOURCE_TALK = "talk";
    public static final Integer MAX_CHECKED_DEFAULT = 3;

    private String shopId;
    private String shopName;
    private boolean isSeller;
    private String source;
    private int maxChecked = MAX_CHECKED_DEFAULT;
    private ArrayList<String> hiddenProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = "";
        if (getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY) != null) {
            shopId = getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY);
        }
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        setTitle(getString(R.string.string_attach_product_activity_title));
        super.setupLayout(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    getResources().getDrawable(R.drawable.bg_attach_product_white_toolbar_drop_shadow));
            getSupportActionBar().setHomeAsUpIndicator(
                    MethodChecker.getDrawable(this, R.drawable.ic_attach_product_close_default));
        }
        if (getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY) != null) {
            shopName = getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY);
        } else {
            shopName = "";
        }
        toolbar.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0));
        toolbar.setSubtitle(shopName);
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getTagFragment());
        if (fragment != null) {
            return fragment;
        } else {
            if (getIntent() != null && getIntent().getExtras() != null) {
                isSeller = getIntent().getBooleanExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY, false);
                source = getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY);
                maxChecked = getIntent().getIntExtra(TOKOPEDIA_ATTACH_PRODUCT_MAX_CHECKED, MAX_CHECKED_DEFAULT);
                hiddenProducts = getIntent().getStringArrayListExtra(TOKOPEDIA_ATTACH_PRODUCT_HIDDEN);
            }
            fragment = AttachProductFragment.newInstance(this, isSeller, source, maxChecked, hiddenProducts);
            return fragment;
        }
    }

    @Override
    public String getShopId() {
        return shopId;
    }

    @Override
    public void finishActivityWithResult(ArrayList<ResultProduct> products) {
        Intent data = new Intent();
        data.putParcelableArrayListExtra(TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY, products);
        setResult(TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK, data);
        finish();
    }

    @Override
    public boolean isSeller() {
        return this.isSeller;
    }

    @Override
    public void setShopName(String shopName) {
        this.shopName = shopName;
        getIntent().putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY, shopName);
        toolbar.setSubtitle(shopName);
    }

    @Override
    public void goToAddProduct(String shopId) {
        if (isSeller) {
            RouteManager.route(this, ApplinkConst.PRODUCT_ADD);
        }
    }
}
