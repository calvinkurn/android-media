package com.tokopedia.flight.dashboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.dashboard.view.fragment.FlightDatePickerFragment;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightDatePickerActivity extends BaseSimpleActivity {
    public static final String EXTRA_DATE = "EXTRA_DATE";

    public static Intent getCallingIntent(Activity activity, String date) {
        Intent intent = new Intent(activity, FlightDatePickerActivity.class);
        intent.putExtra(EXTRA_DATE, date);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightDatePickerFragment.newInstance(getIntent().getStringExtra(EXTRA_DATE));
    }
}
