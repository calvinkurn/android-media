package com.tokopedia.csat_rating.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating;

public class BaseProvideRatingActivity extends BaseSimpleActivity {
    public static final String CLICKED_EMOJI = "clicked_emoji";
    public static final String PARAM_OPTIONS_CSAT = "options_csat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseFragmentProvideRating.newInstance(getIntent().getExtras());
    }

}
