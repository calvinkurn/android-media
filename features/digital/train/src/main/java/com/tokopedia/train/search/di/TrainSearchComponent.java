package com.tokopedia.train.search.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.search.presentation.activity.TrainFilterSearchActivity;
import com.tokopedia.train.search.presentation.fragment.TrainSearchFragment;
import com.tokopedia.train.search.presentation.fragment.TrainSearchReturnFragment;

import dagger.Component;

/**
 * Created by nabilla on 3/9/18.
 */
@TrainSearchScope
@Component(modules = TrainSearchModule.class, dependencies = TrainComponent.class)
public interface TrainSearchComponent {
    void inject(TrainSearchReturnFragment trainSearchReturnFragment);

    void inject(TrainSearchFragment trainSearchFragment);

    void inject(TrainFilterSearchActivity trainFilterSearchActivity);
}