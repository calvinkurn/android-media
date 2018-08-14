package com.tokopedia.common.travel.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

public class TravelPassengerBookingActivity extends BaseSimpleActivity implements TravelPassengerBookingFragment.ActionListener {

    public static final String PASSENGER_DATA = "passenger_data";
    public static final String IS_CHECK_SAME_AS_BUYER = "is_check_same_as_buyer";

    public static Intent callingIntent(Context context, TravelPassenger trainPassengerViewModel, boolean isCheckSameAsBuyer) {
        Intent intent = new Intent(context, TravelPassengerBookingActivity.class);
        intent.putExtra(PASSENGER_DATA, trainPassengerViewModel);
        intent.putExtra(IS_CHECK_SAME_AS_BUYER, isCheckSameAsBuyer);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TravelPassengerBookingFragment.newInstance(getIntent().getParcelableExtra(PASSENGER_DATA),
                getIntent().getBooleanExtra(IS_CHECK_SAME_AS_BUYER, false));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TravelPassenger trainPassengerViewModel = getIntent().getParcelableExtra(PASSENGER_DATA);
        updateTitle(trainPassengerViewModel.getHeaderTitle());
    }

    @Override
    public void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel) {
        Intent intent = new Intent();
        intent.putExtra(PASSENGER_DATA, trainPassengerViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this);
        super.onBackPressed();
    }
}
