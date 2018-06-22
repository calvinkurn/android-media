package com.tokopedia.train.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.presentation.fragment.TrainSearchDepartureFragment;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainSearchDepartureActivity extends TrainSearchActivity {

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel) {
        Intent intent = new Intent(activity, TrainSearchDepartureActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSearchDepartureFragment.newInstance(trainSearchPassDataViewModel);
    }

    @Override
    protected String getTitleTrainToolbar() {
        return getString(R.string.train_search_departure_title);
    }

    @Override
    protected String getDepartureDate() {
        return trainSearchPassDataViewModel.getDepartureDate();
    }
}
