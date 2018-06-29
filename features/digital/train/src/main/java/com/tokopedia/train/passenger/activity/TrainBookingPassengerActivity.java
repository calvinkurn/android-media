package com.tokopedia.train.passenger.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.fragment.TrainBookingPassengerFragment;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;

/**
 * Created by nabillasabbaha on 21/06/18.
 */
public class TrainBookingPassengerActivity extends TrainBaseActivity {

    public static final String TRAIN_SCHEDULE_BOOKING = "train_schedule_booking";

    public static Intent callingIntent(Context context, TrainScheduleBookingPassData trainScheduleBookingPassData) {
        Intent intent = new Intent(context, TrainBookingPassengerActivity.class);
        intent.putExtra(TRAIN_SCHEDULE_BOOKING, trainScheduleBookingPassData);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainBookingPassengerFragment.newInstance(getIntent().getParcelableExtra(TRAIN_SCHEDULE_BOOKING));
    }
}
