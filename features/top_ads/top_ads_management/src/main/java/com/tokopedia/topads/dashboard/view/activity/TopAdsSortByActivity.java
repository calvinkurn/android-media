package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsSortByFragment;

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
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default));
    }
}
