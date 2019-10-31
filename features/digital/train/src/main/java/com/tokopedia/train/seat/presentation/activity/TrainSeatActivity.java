package com.tokopedia.train.seat.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.seat.di.DaggerTrainSeatComponent;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.fragment.TrainSeatFragment;

public class TrainSeatActivity extends TrainBaseActivity implements HasComponent<TrainSeatComponent>,
        TrainSeatFragment.InteractionListener {

    private static final String EXTRA_SOFTBOOK = "EXTRA_SOFTBOOK";
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final String EXTRA_DEPARTURE_STATE = "EXTRA_DEPARTURE_STATE";
    private TrainSeatComponent trainSeatComponent;
    private TrainSoftbook trainSoftbook;
    private TrainScheduleBookingPassData passData;
    private boolean isDeparture;

    public static Intent getCallingIntent(Activity activity, TrainSoftbook trainSoftbook, TrainScheduleBookingPassData trainScheduleBookingPassData, boolean isDeparture) {
        Intent intent = new Intent(activity, TrainSeatActivity.class);
        intent.putExtra(EXTRA_SOFTBOOK, trainSoftbook);
        intent.putExtra(EXTRA_PASS_DATA, trainScheduleBookingPassData);
        intent.putExtra(EXTRA_DEPARTURE_STATE, isDeparture);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trainSoftbook = getIntent().getParcelableExtra(EXTRA_SOFTBOOK);
        passData = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        isDeparture = getIntent().getBooleanExtra(EXTRA_DEPARTURE_STATE, true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSeatFragment.newInstance(trainSoftbook, passData, isDeparture);
    }

    @Override
    public TrainSeatComponent getComponent() {
        if (trainSeatComponent == null) {
            trainSeatComponent = DaggerTrainSeatComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getApplication()))
                    .build();
        }
        return trainSeatComponent;
    }

    @Override
    public void setToolbar(String subtitle) {
        updateTitle(getString(R.string.train_seat_toolbar_title), subtitle);
    }

    @Override
    public int getToolbarHeight() {
        return getResources().getDimensionPixelSize(R.dimen.dp_85);
    }
}

