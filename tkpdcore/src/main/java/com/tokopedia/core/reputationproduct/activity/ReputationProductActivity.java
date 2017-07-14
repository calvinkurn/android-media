package com.tokopedia.core.reputationproduct.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.reputationproduct.fragment.ReputationProductFragment;
import com.tokopedia.core.reputationproduct.presenter.ReputationProductViewPresenter;
import com.tokopedia.core.reputationproduct.presenter.ReputationProductViewPresenterImpl;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

/**
 * Created by hangnadi on 7/7/15.
 */
public class ReputationProductActivity extends BasePresenterActivity<ReputationProductViewPresenter> {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new ReputationProductViewPresenterImpl();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reputation_product_view;
    }

    @Override
    protected void initView() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, getFragment())
                .commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_REPUTATION_VIEW_DETAIL;
    }

    private Fragment getFragment() {
        return ReputationProductFragment.createInstance(getIntentProductID(), getIntentShopID(), getIntentModelData());
    }

    private String getIntentProductID() {
        return getIntent().getStringExtra("product_id");
    }

    private String getIntentShopID() {
        return getIntent().getStringExtra("shop_id");
    }

    private ReviewProductModel getIntentModelData() {
        return (ReviewProductModel) getIntent().getParcelableExtra("data_model");
    }

}
