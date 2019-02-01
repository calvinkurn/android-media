package com.tokopedia.train.scheduledetail.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainAnalytics;
import com.tokopedia.train.scheduledetail.di.DaggerTrainScheduleDetailComponent;
import com.tokopedia.train.scheduledetail.di.TrainScheduleDetailComponent;
import com.tokopedia.train.scheduledetail.presentation.contract.TrainScheduleContract;
import com.tokopedia.train.scheduledetail.presentation.fragment.TrainScheduleDetailFragment;
import com.tokopedia.train.scheduledetail.presentation.fragment.TrainSchedulePriceDetailFragment;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.scheduledetail.presentation.presenter.TrainSchedulePresenter;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import javax.inject.Inject;

/**
 * Created by Rizky on 12/05/18.
 */
public class TrainScheduleDetailActivity extends BaseTabActivity implements TrainScheduleContract.View {

    public static final String EXTRA_TRAIN_SCHEDULE_ID = "EXTRA_TRAIN_SCHEDULE_ID";
    public static final String EXTRA_NUMBER_OF_ADULT_PASSENGER = "EXTRA_NUMBER_OF_ADULT_PASSENGER";
    public static final String EXTRA_NUMBER_OF_INFANT_PASSENGER = "EXTRA_NUMBER_OF_INFANT_PASSENGER";
    public static final String EXTRA_SHOW_SUBMIT_BUTTON = "EXTRA_SHOW_SUBMIT_BUTTON";

    public static final String EXTRA_TRAIN_SELECTED = "EXTRA_TRAIN_SELECTED";

    private final int PAGE_COUNT = 2;
    private final int OFFSCREEN_PAGE_LIMIT = 2;
    private final int VIEWPAGER_INDEX_ZERO = 0;
    private final int VIEWPAGER_INDEX_ONE = 1;

    @Inject
    TrainSchedulePresenter trainSchedulePresenter;

    @Inject
    TrainAnalytics trainAnalytics;

    private TrainScheduleDetailComponent trainScheduleDetailComponent;

    private Button buttonSubmit;
    private TextView textHeaderOriginStationCode;
    private TextView textHeaderOriginCityName;
    private TextView textHeaderDestinationStationCode;
    private TextView textHeaderDestinationCityName;
    private RelativeLayout containerScheduleDetail;
    private ProgressBar progressBar;

    private TrainScheduleViewModel trainScheduleViewModel;

    private boolean showSubmitButton;

    public static Intent createIntent(Context context, String scheduleId, int numOfAdultPassenger,
                                      int numOfInfantPassenger, boolean showSubmitButton) {
        Intent intent = new Intent(context, TrainScheduleDetailActivity.class);
        intent.putExtra(EXTRA_TRAIN_SCHEDULE_ID, scheduleId);
        intent.putExtra(EXTRA_NUMBER_OF_ADULT_PASSENGER, numOfAdultPassenger);
        intent.putExtra(EXTRA_NUMBER_OF_INFANT_PASSENGER, numOfInfantPassenger);
        intent.putExtra(EXTRA_SHOW_SUBMIT_BUTTON, showSubmitButton);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_train_schedule_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showSubmitButton = getIntent().getBooleanExtra(EXTRA_SHOW_SUBMIT_BUTTON, false);

        super.onCreate(savedInstanceState);

        initInjector();

        String scheduleId = getIntent().getStringExtra(EXTRA_TRAIN_SCHEDULE_ID);
        int numOfAdultPassenger = getIntent().getIntExtra(EXTRA_NUMBER_OF_ADULT_PASSENGER, 0);
        int numOfInfantPassenger = getIntent().getIntExtra(EXTRA_NUMBER_OF_INFANT_PASSENGER, 0);

        trainSchedulePresenter.attachView(this);
        trainSchedulePresenter.getScheduleDetail(scheduleId, numOfAdultPassenger, numOfInfantPassenger);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        buttonSubmit = findViewById(R.id.button_submit);
        textHeaderOriginStationCode = findViewById(R.id.header_origin_station_code);
        textHeaderOriginCityName = findViewById(R.id.header_origin_city_name);
        textHeaderDestinationStationCode = findViewById(R.id.header_destination_station_code);
        textHeaderDestinationCityName = findViewById(R.id.header_destination_city_name);
        containerScheduleDetail = findViewById(R.id.container_schedule_detail);
        progressBar = findViewById(R.id.progress_bar);

        tabLayout.setupWithViewPager(viewPager);

        if (!showSubmitButton) {
            buttonSubmit.setVisibility(View.GONE);
        }

        buttonSubmit.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TRAIN_SELECTED, trainScheduleViewModel);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return null;
    }

    @Override
    protected int getPageLimit() {
        return OFFSCREEN_PAGE_LIMIT;
    }

    @Override
    public void showScheduleDetail(TrainScheduleViewModel trainScheduleViewModel, TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        this.trainScheduleViewModel = trainScheduleViewModel;
        viewPager.setAdapter(getFragmentsPagerAdapter(trainScheduleDetailViewModel));
        containerScheduleDetail.setVisibility(View.VISIBLE);

        textHeaderOriginStationCode.setText(trainScheduleDetailViewModel.getOriginStationCode());
        textHeaderOriginCityName.setText(trainScheduleDetailViewModel.getOriginCityName());
        textHeaderDestinationStationCode.setText(trainScheduleDetailViewModel.getDestinationStationCode());
        textHeaderDestinationCityName.setText(trainScheduleDetailViewModel.getDestinationCityName());

        trainAnalytics.eventProductDetailImpressions(
                trainScheduleViewModel.getIdSchedule(),
                trainScheduleDetailViewModel.getOriginStationCode(),
                trainScheduleDetailViewModel.getDestinationStationCode(),
                trainScheduleDetailViewModel.getTrainClass(),
                trainScheduleDetailViewModel.getTrainName(),
                trainScheduleDetailViewModel.getTotalPrice());
    }

    private FragmentPagerAdapter getFragmentsPagerAdapter(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        return new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.train_schedule_detail_title_trip);
                    case 1:
                        return getString(R.string.train_schedule_detail_title_price);
                }
                return null;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return TrainScheduleDetailFragment.createInstance(trainScheduleDetailViewModel);
                    case 1:
                        return TrainSchedulePriceDetailFragment.createInstance(trainScheduleDetailViewModel);
                }
                return null;
            }

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }
        };
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        containerScheduleDetail.setVisibility(View.INVISIBLE);
    }

    @Override
    public void stopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    protected void initInjector() {
        if (trainScheduleDetailComponent == null) {
            trainScheduleDetailComponent = DaggerTrainScheduleDetailComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getApplication())).build();
        }
        trainScheduleDetailComponent.inject(this);
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trainSchedulePresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.travel_anim_stay, R.anim.travel_slide_out_up);
    }
}