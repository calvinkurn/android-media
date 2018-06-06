package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsSortByFragment;
import android.support.graphics.drawable.VectorDrawableCompat;

/**
 * Created by nakama on 10/04/18.
 */

public class TopAdsSortByActivity extends BaseSimpleActivity {
    public static final String EXTRA_SORT_SELECTED = "EXTRA_SORT_SELECTED";

    public static Intent createIntent(Context context, @SortTopAdsOption String sortTopAdsOption){
        Intent intent = new Intent(context, TopAdsSortByActivity.class);
        intent.putExtra(EXTRA_SORT_SELECTED, sortTopAdsOption);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TopAdsSortByFragment.createInstance(getIntent().getStringExtra(EXTRA_SORT_SELECTED));
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void setToolbarColorWhite() {
        super.setToolbarColorWhite();
        getSupportActionBar().setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_close_default, null));
    }
}
