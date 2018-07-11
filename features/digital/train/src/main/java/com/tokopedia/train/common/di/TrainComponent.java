package com.tokopedia.train.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.scheduledetail.domain.GetScheduleDetailUseCase;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;

import dagger.Component;

/**
 * @author by alvarisi on 2/19/18.
 */
@TrainScope
@Component(modules = TrainModule.class, dependencies = BaseAppComponent.class)
public interface TrainComponent {

    @ApplicationContext
    Context getContext();

    TrainRepository trainRepository();

    GetDetailScheduleUseCase getDetailScheduleUseCase();

    GetScheduleDetailUseCase getScheduleDetailUseCase();

}