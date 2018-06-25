package com.tokopedia.train.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.presentation.fragment.TrainSearchReturnFragment;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainSearchReturnActivity extends TrainSearchActivity {

    public static final String EXTRA_SEARCH_ID_SCHEDULE = "id_schedule";
    public static final String EXTRA_SCHEDULE_BOOKING = "departure_schedule";

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel,
                                          TrainScheduleBookingPassData scheduleBookingPassData, String idSchedule) {
        Intent intent = new Intent(activity, TrainSearchReturnActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        intent.putExtra(EXTRA_SEARCH_ID_SCHEDULE, idSchedule);
        intent.putExtra(EXTRA_SCHEDULE_BOOKING, scheduleBookingPassData);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSearchReturnFragment.newInstance(trainSearchPassDataViewModel,
                getIntent().getStringExtra(EXTRA_SEARCH_ID_SCHEDULE), getIntent().getParcelableExtra(EXTRA_SCHEDULE_BOOKING));
    }

    @Override
    protected String getTitleTrainToolbar() {
        return getString(R.string.train_search_return_title);
    }

    @Override
    protected String getDepartureDate() {
        return trainSearchPassDataViewModel.getReturnDate();
    }
}
