package com.tokopedia.train.station.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.station.di.DaggerTrainStationsComponent;
import com.tokopedia.train.station.di.TrainStationsComponent;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationAndCityViewModel;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainStationsActivity extends BaseSimpleActivity implements HasComponent<TrainStationsComponent>, TrainStationsFragment.OnFragmentInteractionListener {
    public static final String EXTRA_SELECTED_STATION = "EXTRA_SELECTED_STATION";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    private TrainStationsComponent trainStationsComponent;

    public static Intent getCallingIntent(Activity activity, String title) {
        Intent intent = new Intent(activity, TrainStationsActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getIntent().getStringExtra(EXTRA_TITLE));
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainStationsFragment.newInstance();
    }

    @Override
    public TrainStationsComponent getComponent() {
        if (trainStationsComponent == null) {
            trainStationsComponent = DaggerTrainStationsComponent.builder().trainComponent(TrainComponentUtils.getTrainComponent(getApplication())).build();
        }
        return trainStationsComponent;
    }

    @Override
    public void onStationClicked(TrainStationAndCityViewModel viewModel) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_SELECTED_STATION, viewModel);
        setResult(RESULT_OK, intent);
        finish();
    }
}
