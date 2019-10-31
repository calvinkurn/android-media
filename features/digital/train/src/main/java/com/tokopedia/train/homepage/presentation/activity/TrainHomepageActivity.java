package com.tokopedia.train.homepage.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.Menu;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.homepage.di.DaggerTrainHomepageComponent;
import com.tokopedia.train.homepage.di.TrainHomepageComponent;
import com.tokopedia.train.homepage.presentation.fragment.TrainHomepageFragment;

public class TrainHomepageActivity extends TrainBaseActivity implements HasComponent<TrainHomepageComponent> {

    private static TrainHomepageComponent component;

    @SuppressWarnings("unused")
    @DeepLink({ApplinkConst.TRAIN_HOMEPAGE})
    public static Intent intentForTaskStackBuilderMethods(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, TrainHomepageActivity.class);
        return intent.setData(uri.build()).putExtras(extras);
    }

    public static Intent getCallingIntent(Activity activity) {
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