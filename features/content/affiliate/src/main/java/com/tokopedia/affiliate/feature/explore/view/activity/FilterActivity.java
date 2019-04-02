package com.tokopedia.affiliate.feature.explore.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.explore.view.fragment.FilterFragment;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 28/12/18.
 */
public class FilterActivity extends BaseSimpleActivity {

    public static final String PARAM_FILTER_LIST = "PARAM_FILTER_LIST";

    public static Intent getIntent(Context context,Bundle bundle) {
        Intent intent = new Intent(context, FilterActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FilterFragment.getInstance(getIntent().getExtras());
    }
}
