package com.tokopedia.common.travel.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.fragment.TravelPassengerEditFragment;
import com.tokopedia.common.travel.presentation.model.TravelTrip;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class TravelPassengerEditActivity extends BaseSimpleActivity {

    public static final String TRAVEL_TRIP = "travel_trip";

    public static Intent callingIntent(Context context, TravelTrip travelTrip) {
        Intent intent = new Intent(context, TravelPassengerEditActivity.class);
        intent.putExtra(TRAVEL_TRIP, travelTrip);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TravelPassengerEditFragment.newInstance(getIntent().getParcelableExtra(TRAVEL_TRIP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateTitle(getString(R.string.travel_title_edit_passenger_list));
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.travel_anim_stay,R.anim.travel_slide_out_up);
    }
}
