package com.tokopedia.common.travel.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.presentation.fragment.TravelPassengerUpdateFragment;
import com.tokopedia.common.travel.presentation.model.TravelTrip;

public class TravelPassengerUpdateActivity extends BaseSimpleActivity implements TravelPassengerUpdateFragment.ActionListener {

    public static final String TRAVEL_TRIP = "travel_trip";
    public static final String TYPE_PASSENGER_PAGE = "type_passenger_page";
    public static final int ADD_PASSENGER_TYPE = 1;
    public static final int EDIT_PASSENGER_TYPE = 2;

    public static Intent callingIntent(Context context,
                                       TravelTrip travelTrip,
                                       int typePage) {
        Intent intent = new Intent(context, TravelPassengerUpdateActivity.class);
        intent.putExtra(TRAVEL_TRIP, travelTrip);
        intent.putExtra(TYPE_PASSENGER_PAGE, typePage);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TravelPassengerUpdateFragment.newInstance(getIntent().getParcelableExtra(TRAVEL_TRIP),
                getIntent().getIntExtra(TYPE_PASSENGER_PAGE, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitlePage();
    }

    private void setTitlePage() {
        String title = getString(R.string.travel_edit_passenger_title_page);
        if (getIntent().getIntExtra(TYPE_PASSENGER_PAGE, 0) == ADD_PASSENGER_TYPE) {
            title = getString(R.string.travel_add_passenger_title_page);
        }
        updateTitle(title);
    }

    @Override
    public void navigateToPassengerList() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this);
        super.onBackPressed();
    }
}
