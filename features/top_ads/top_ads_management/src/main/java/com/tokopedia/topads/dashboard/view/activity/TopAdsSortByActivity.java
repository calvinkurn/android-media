package com.tokopedia.topads.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.topads.common.view.activity.TopAdsBaseActivity;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsSortByFragment;

/**
 * Created by nakama on 10/04/18.
 */

public class TopAdsSortByActivity extends TopAdsBaseActivity {
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

}
