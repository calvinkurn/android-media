package com.tokopedia.train.passenger.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.presentation.fragment.TrainBookingAddPassengerFragment;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingAddPassengerActivity extends TrainBaseActivity implements TrainBookingAddPassengerFragment.ActionListener {

    public static final String PASSENGER_DATA = "passenger_data";

    public static Intent callingIntent(Context context, TrainPassengerViewModel trainPassengerViewModel) {
        Intent intent = new Intent(context, TrainBookingAddPassengerActivity.class);
        intent.putExtra(PASSENGER_DATA, trainPassengerViewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainBookingAddPassengerFragment.newInstance(getIntent().getParcelableExtra(PASSENGER_DATA));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TrainPassengerViewModel trainPassengerViewModel = getIntent().getParcelableExtra(PASSENGER_DATA);
        updateTitle(trainPassengerViewModel.getHeaderTitle());
    }

    @Override
    public void navigateToBookingPassenger(TrainPassengerViewModel trainPassengerViewModel) {
        Intent intent = new Intent();
        intent.putExtra(TrainBookingAddPassengerActivity.PASSENGER_DATA, trainPassengerViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this);
        super.onBackPressed();
    }
}
