package com.tokopedia.train.scheduledetail.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.scheduledetail.presentation.activity.TrainScheduleDetailActivity;

import dagger.Component;

/**
 * Created by Rizky on 07/06/18.
 */
@TrainScheduleDetailScope
@Component(modules = TrainScheduleDetailModule.class, dependencies = TrainComponent.class)
public interface TrainScheduleDetailComponent {

    void inject(TrainScheduleDetailActivity trainScheduleDetailActivity);

}