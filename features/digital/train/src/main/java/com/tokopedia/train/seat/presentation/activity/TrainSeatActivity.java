package com.tokopedia.train.seat.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.seat.di.DaggerTrainSeatComponent;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.fragment.TrainSeatFragment;

public class TrainSeatActivity extends TrainBaseActivity implements HasComponent<TrainSeatComponent> {
    private TrainSeatComponent trainSeatComponent;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, TrainSeatActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSeatFragment.newInstance();
    }

    @Override
    public TrainSeatComponent getComponent() {
        trainSeatComponent = DaggerTrainSeatComponent.builder()
                .trainComponent(TrainComponentUtils.getTrainComponent(getApplication()))
                .build();
        return trainSeatComponent;
    }
}
