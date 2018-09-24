package com.tokopedia.affiliate.feature.explore.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.affiliate.feature.explore.view.fragment.ExploreFragment;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExploreActivity extends BaseSimpleActivity {

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, ExploreActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return ExploreFragment.getInstance(getIntent().getExtras());
    }
}
