package com.tokopedia.attachproduct.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.attachproduct.AttachProductRouter;
import com.tokopedia.attachproduct.R;
import com.tokopedia.attachproduct.resultmodel.ResultProduct;
import com.tokopedia.attachproduct.view.fragment.AttachProductFragment;
import com.tokopedia.attachproduct.view.presenter.AttachProductContract;

import java.util.ArrayList;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductActivity extends BaseSimpleActivity implements AttachProductContract
        .Activity {
    public static final int TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE = 113;
    public static final int TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK = 324;
    public static final String TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY = "TKPD_ATTACH_PRODUCT_RESULTS";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY = "TKPD_ATTACH_PRODUCT_SHOP_ID";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY = "TKPD_ATTACH_PRODUCT_SHOP_NAME";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY = "TKPD_ATTACH_PRODUCT_IS_SELLER";
    public static final String TOKOPEDIA_ATTACH_PRODUCT_MAX_CHECKED = "TKPD_ATTACH_PRODUCT_MAX_CHECKED";
    private static final String TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY = "TKPD_ATTACH_PRODUCT_SOURCE";
    private static final String TOKOPEDIA_ATTACH_PRODUCT_HIDDEN = "TKPD_ATTACH_PRODUCT_HIDDEN";

    public static final String SOURCE_TOPCHAT = "topchat";
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
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        setTitle(getString(R.string.string_attach_product_activity_title));
        super.setupLayout(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    getResources().getDrawable(R.drawable.bg_white_toolbar_drop_shadow));
            getSupportActionBar().setHomeAsUpIndicator(
                    getResources().getDrawable(R.drawable.ic_close_default));
        }
        if (getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY) != null) {
            shopName = getIntent().getStringExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY);
        } else {
            shopName = "";
        }
        toolbar.setSubtitleTextAppearance(this, R.style.AttachProductToolbarSubTitle_SansSerif);
        toolbar.setTitleTextAppearance(this, R.style.AttachProductToolbarTitle_SansSerif);
        toolbar.setSubtitle(shopName);
    }

    /**
     * @param context   Context
     * @param shopId    Target shop id
     * @param shopName  Target shop name
     * @param isSeller  is user the owner of the shop
     * @param maxChecked the maximal number of product that can be attached
     * @param source    use Key Source from this activity
     * @return
     */
    public static Intent createInstance(Context context, String shopId, String shopName,
                                        boolean isSeller, String source, int maxChecked,
                                        ArrayList<String> hiddenProducts) {
        Intent intent = new Intent(context, AttachProductActivity.class);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY, shopId);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY, isSeller);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_NAME_KEY, shopName);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, source);
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_MAX_CHECKED, maxChecked);
        intent.putStringArrayListExtra(TOKOPEDIA_ATTACH_PRODUCT_HIDDEN, hiddenProducts);
        return intent;
    }

    /**
     * @param context   Context
     * @param shopId    Target shop id
     * @param shopName  Target shop name
     * @param isSeller  is user the owner of the shop
     * @param source    use Key Source from this activity
     * @return
     */
    public static Intent createInstance(Context context, String shopId, String shopName,
                                        boolean isSeller, String source) {
        return createInstance(context, shopId, shopName, isSeller, source, MAX_CHECKED_DEFAULT, null);
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
        if (isSeller && getApplicationContext() instanceof AttachProductRouter) {
            ((AttachProductRouter) getApplicationContext()).goToAddProduct(this);
        }
    }
}
