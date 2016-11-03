package com.tokopedia.tkpd.reputationproduct;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.reputationproduct.fragment.FragmentReputationProductView;
import com.tokopedia.tkpd.review.model.product_review.ReviewProductModel;

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

}
