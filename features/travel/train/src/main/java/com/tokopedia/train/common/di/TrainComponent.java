package com.tokopedia.train.common.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.train.common.domain.TrainRepository;

import dagger.Component;

/**
 * @author by alvarisi on 2/19/18.
 */
@TrainScope
@Component(modules = TrainModule.class, dependencies = BaseAppComponent.class)
public interface TrainComponent {
    TrainRepository trainRepository();
}
