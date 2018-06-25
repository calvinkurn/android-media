package com.tokopedia.train.passenger.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.fragment.TrainBookingAddPassengerFragment;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingAddPassengerActivity extends TrainBaseActivity {

    public static Intent callingIntent(Context context) {
        return new Intent(context, TrainBookingAddPassengerActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainBookingAddPassengerFragment.newInstance();
    }
}
