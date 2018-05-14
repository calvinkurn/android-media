package com.tokopedia.train.scheduledetail.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by Rizky on 12/05/18.
 */
public class TrainScheduleDetailActivity extends BaseTabActivity {

    public static final String EXTRA_TRAIN_SCHEDULE_VIEW_MODEL = "EXTRA_TRAIN_SCHEDULE_VIEW_MODEL";

    private TrainScheduleViewModel trainScheduleViewModel;

    private Button buttonSubmit;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView departureStationCode;
    private TextView departureStationName;
    private TextView arrivalStationCode;
    private TextView arrivalStationName;

    public static Intent createIntent(Context context, TrainScheduleViewModel trainScheduleViewModel) {
        Intent intent = new Intent(context, TrainScheduleDetailActivity.class);
        intent.putExtra(EXTRA_TRAIN_SCHEDULE_VIEW_MODEL, trainScheduleViewModel);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_train_schedule_detail;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        trainScheduleViewModel = getIntent().getParcelableExtra(EXTRA_TRAIN_SCHEDULE_VIEW_MODEL);

        super.setupLayout(savedInstanceState);

        buttonSubmit = findViewById(R.id.button_submit);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout = findViewById(R.id.app_bar_layout);
        departureStationCode = findViewById(R.id.departure_station_code);
        departureStationName = findViewById(R.id.departure_station_name);
        arrivalStationCode = findViewById(R.id.arrival_station_code);
        arrivalStationName = findViewById(R.id.arrival_station_name);

//        departureStationCode.setText(trainScheduleViewModel.getDepartureStation());
//        departureStationName.setText(trainScheduleViewModel.getDepartureStationCity());
//        arrivalStationCode.setText(trainScheduleViewModel.getArrivalStation());
//        arrivalStationName.setText(trainScheduleViewModel.getArrivalStationCity());
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0 : return "Perjalanan";
                    case 1 : return "Harga";
                }
                return null;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return TrainScheduleDetailFragment.createInstance(trainScheduleViewModel);
                    case 1 : return TrainSchedulePriceDetailFragment.createInstance();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    protected int getPageLimit() {
        return 2;
    }
}
