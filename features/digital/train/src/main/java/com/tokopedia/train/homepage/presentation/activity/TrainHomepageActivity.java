package com.tokopedia.train.homepage.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.homepage.di.DaggerTrainHomepageComponent;
import com.tokopedia.train.homepage.di.TrainHomepageComponent;
import com.tokopedia.train.homepage.presentation.fragment.TrainHomepageFragment;

public class TrainHomepageActivity extends TrainBaseActivity implements HasComponent<TrainHomepageComponent>,
        TrainHomepageFragment.OnCloseBottomMenusListener {

    private static TrainHomepageComponent component;

    @SuppressWarnings("unused")
    @DeepLink({ApplinkConst.TRAIN_HOMEPAGE})
    public static TaskStackBuilder intentForTaskStackBuilderMethods(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = TrainHomepageActivity.getCallingIntent(context);
        taskStackBuilder.addNextIntent(intent);
        return taskStackBuilder;
    }

    public static Intent getCallingIntent(Context context){
        return new Intent(context, TrainHomepageActivity.class);
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

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        ((TrainHomepageFragment) getFragment()).showBottomMenus();

        return false;
//        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        ((TrainHomepageFragment) getFragment()).closeBottomMenus();

        super.onPanelClosed(featureId, menu);
    }

    @Override
    public void onCloseBottomMenus() {
        closeOptionsMenu();
    }

}
