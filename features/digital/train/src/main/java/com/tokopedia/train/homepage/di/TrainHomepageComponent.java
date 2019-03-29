package com.tokopedia.train.homepage.di;

import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.homepage.presentation.fragment.TrainHomepageFragment;

import dagger.Component;

/**
 * @author by alvarisi on 3/1/18.
 */
@TrainHomepageScope
@Component(modules = TrainHomepageModule.class, dependencies = TrainComponent.class)
public interface TrainHomepageComponent {

    void inject(TrainHomepageFragment trainHomepageFragment);

}
