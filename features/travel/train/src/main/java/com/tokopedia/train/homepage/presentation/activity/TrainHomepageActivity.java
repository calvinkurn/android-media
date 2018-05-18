package com.tokopedia.train.homepage.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.homepage.di.DaggerTrainHomepageComponent;
import com.tokopedia.train.homepage.di.TrainHomepageComponent;
import com.tokopedia.train.homepage.presentation.fragment.TrainHomepageFragment;

public class TrainHomepageActivity extends TrainBaseActivity implements HasComponent<TrainHomepageComponent> {
    private static TrainHomepageComponent component;

    public static Intent getCallingIntent(Activity activity){
        return new Intent(activity, TrainHomepageActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return new TrainHomepageFragment();
    }

    @Override
    public TrainHomepageComponent getComponent() {
        if (component == null) {
            component = DaggerTrainHomepageComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getApplication()))
                    .build();
        }
        return component;
    }
}
