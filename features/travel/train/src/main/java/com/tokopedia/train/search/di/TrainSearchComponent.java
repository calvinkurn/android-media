package com.tokopedia.train.search.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.search.presentation.activity.TrainFilterSearchActivity;
import com.tokopedia.train.search.presentation.fragment.TrainReturnSearchFragment;
import com.tokopedia.train.search.presentation.fragment.TrainSearchFragment;

import dagger.Component;

/**
 * Created by nabilla on 3/9/18.
 */
@TrainSearchScope
@Component(modules = TrainSearchModule.class, dependencies = TrainComponent.class)
public interface TrainSearchComponent {
    void inject(TrainReturnSearchFragment trainReturnSearchFragment);

    void inject(TrainSearchFragment trainSearchFragment);

    void inject(TrainFilterSearchActivity trainFilterSearchActivity);
}
