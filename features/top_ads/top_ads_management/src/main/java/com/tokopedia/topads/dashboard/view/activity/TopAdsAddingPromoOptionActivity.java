package com.tokopedia.topads.dashboard.view.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAddingPromoOptionFragment;

/**
 * Created by hadi.putra on 26/04/18.
 */

public class TopAdsAddingPromoOptionActivity extends BaseSimpleActivity{
    @Override
    protected Fragment getNewFragment() {
        return new TopAdsAddingPromoOptionFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_top_ads_promo_option;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default));
    }
}
