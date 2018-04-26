package com.tokopedia.topads.dashboard.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default));
    }
}
