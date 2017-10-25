package com.tokopedia.core.reputationproduct;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.reputationproduct.fragment.FragmentReputationProductView;
import com.tokopedia.core.review.model.product_review.ReviewProductModel;

/**
 * Created by hangnadi on 7/7/15.
 */
public class ReputationProductView extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_reputation_product_view);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, getFragment())
                .commit();
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_PRODUCT_REPUTATION_VIEW_DETAIL;
    }

    private Fragment getFragment() {
        return FragmentReputationProductView.createInstance(getIntentProductID(), getIntentShopID(), getIntentModelData());
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

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
