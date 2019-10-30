package com.tokopedia.train.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.presentation.fragment.TrainSearchReturnFragment;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainSearchReturnActivity extends TrainSearchActivity {

    public static final String EXTRA_SEARCH_ID_SCHEDULE = "id_schedule";
    public static final String EXTRA_SCHEDULE_BOOKING = "departure_schedule";
    public static final String EXTRA_ARRIVAL_TIME_SCHEDULE_SELECTED = "arrival_time_schedule_selected";

    public static Intent getCallingIntent(Activity activity, TrainSearchPassDataViewModel viewModel,
                                          TrainScheduleBookingPassData scheduleBookingPassData,
                                          String idSchedule, String arrivalScheduleSelected) {
        Intent intent = new Intent(activity, TrainSearchReturnActivity.class);
        intent.putExtra(EXTRA_SEARCH_PASS_DATA, viewModel);
        intent.putExtra(EXTRA_SEARCH_ID_SCHEDULE, idSchedule);
        intent.putExtra(EXTRA_SCHEDULE_BOOKING, scheduleBookingPassData);
        intent.putExtra(EXTRA_ARRIVAL_TIME_SCHEDULE_SELECTED, arrivalScheduleSelected);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainSearchReturnFragment.newInstance(trainSearchPassDataViewModel,
                getIntent().getStringExtra(EXTRA_SEARCH_ID_SCHEDULE),
                getIntent().getParcelableExtra(EXTRA_SCHEDULE_BOOKING),
                getIntent().getStringExtra(EXTRA_ARRIVAL_TIME_SCHEDULE_SELECTED));
    }

    @Override
    protected String getTitleTrainToolbar() {
        return trainSearchPassDataViewModel.getDestinationCityName() + " ➝ " + trainSearchPassDataViewModel.getOriginCityName();
    }

    @Override
    protected String getDepartureDate() {
        return trainSearchPassDataViewModel.getReturnDate();
    }
}
