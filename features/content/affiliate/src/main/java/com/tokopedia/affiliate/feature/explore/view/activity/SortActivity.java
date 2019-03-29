package com.tokopedia.affiliate.feature.explore.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.explore.view.fragment.SortFragment;

/**
 * @author by yfsx on 16/1/19.
 */
public class SortActivity extends BaseSimpleActivity {

    public static final String PARAM_SORT_LIST = "PARAM_SORT_LIST";
    public static final String PARAM_SORT_SELECTED = "PARAM_SORT_SELECTED";

    public static Intent getIntent(Context context,Bundle bundle) {
        Intent intent = new Intent(context, SortActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return SortFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }
}
