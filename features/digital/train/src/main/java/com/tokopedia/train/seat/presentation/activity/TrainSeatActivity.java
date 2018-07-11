package com.tokopedia.train.seat.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.seat.di.DaggerTrainSeatComponent;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.fragment.TrainSeatFragment;

public class TrainSeatActivity extends TrainBaseActivity implements HasComponent<TrainSeatComponent> {

    private static final String EXTRA_SOFTBOOK = "EXTRA_SOFTBOOK";
    private TrainSeatComponent trainSeatComponent;
    private TrainSoftbook trainSoftbook;

    public static Intent getCallingIntent(Activity activity, TrainSoftbook trainSoftbook) {
        Intent intent = new Intent(activity, TrainSeatActivity.class);
        intent.putExtra(EXTRA_SOFTBOOK, trainSoftbook);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trainSoftbook = getIntent().getParcelableExtra(EXTRA_SOFTBOOK);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSeatFragment.newInstance(trainSoftbook);
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

}
