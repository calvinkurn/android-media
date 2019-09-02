package com.tokopedia.train.reviewdetail.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.presentation.fragment.TrainReviewDetailFragment;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;

/**
 * Created by Rizky on 01/07/18.
 */
public class TrainReviewDetailActivity extends TrainBaseActivity {

    public static String EXTRA_TRAIN_SOFTBOOK = "EXTRA_TRAIN_SOFTBOOK";
    public static String EXTRA_TRAIN_SCHEDULE_BOOKING_PASS_DATA = "EXTRA_TRAIN_SCHEDULE_BOOKING_PASS_DATA";

    private TrainSoftbook trainSoftbookPassData;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;

    public static Intent createIntent(Context context,
                                      TrainSoftbook trainSoftbookPassData,
                                      TrainScheduleBookingPassData trainScheduleBookingPassData) {
        Intent intent = new Intent(context, TrainReviewDetailActivity.class);
        intent.putExtra(EXTRA_TRAIN_SOFTBOOK, trainSoftbookPassData);
        intent.putExtra(EXTRA_TRAIN_SCHEDULE_BOOKING_PASS_DATA, trainScheduleBookingPassData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        trainSoftbookPassData = getIntent().getParcelableExtra(EXTRA_TRAIN_SOFTBOOK);
        trainScheduleBookingPassData = getIntent().getParcelableExtra(EXTRA_TRAIN_SCHEDULE_BOOKING_PASS_DATA);

        return TrainReviewDetailFragment.newInstance(trainSoftbookPassData, trainScheduleBookingPassData);
    }

}