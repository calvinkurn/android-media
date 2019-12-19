package com.tokopedia.train.passenger.presentation.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.tokopedia.design.component.Dialog;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.presentation.fragment.TrainBookingPassengerFragment;
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

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this, Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.train_dialog_booking_passanger_title));
        dialog.setDesc(getString(R.string.train_dialog_booking_passanger_desc));
        dialog.setBtnCancel(getString(R.string.train_dialog_booking_passanger_no));
        dialog.setBtnOk(getString(R.string.train_dialog_booking_passanger_yes));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                TrainBookingPassengerActivity.super.onBackPressed();
            }
        });
        dialog.show();
    }
}
