package com.tokopedia.train.scheduledetail.presentation.activity;

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
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.scheduledetail.di.DaggerTrainScheduleDetailComponent;
import com.tokopedia.train.scheduledetail.di.TrainScheduleDetailComponent;
import com.tokopedia.train.scheduledetail.presentation.contract.TrainScheduleContract;
import com.tokopedia.train.scheduledetail.presentation.fragment.TrainScheduleDetailFragment;
import com.tokopedia.train.scheduledetail.presentation.fragment.TrainSchedulePriceDetailFragment;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.scheduledetail.presentation.presenter.TrainSchedulePresenter;

import javax.inject.Inject;

/**
 * Created by Rizky on 12/05/18.
 */
public class TrainScheduleDetailActivity extends BaseTabActivity implements TrainScheduleContract.View {

    public static final String EXTRA_TRAIN_SCHEDULE_ID = "EXTRA_TRAIN_SCHEDULE_ID";
    public static final String EXTRA_NUMBER_OF_ADULT_PASSENGER = "EXTRA_NUMBER_OF_ADULT_PASSENGER";

    @Inject
    TrainSchedulePresenter trainSchedulePresenter;

    private TrainScheduleDetailComponent trainScheduleDetailComponent;

    private Button buttonSubmit;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView originStationCode;
    private TextView originStationName;
    private TextView destinationStationCode;
    private TextView destinationStationName;

    private TrainScheduleDetailViewModel trainScheduleDetailViewModel;

    public static Intent createIntent(Context context, String scheduleId, int numOfAdultPassenger) {
        Intent intent = new Intent(context, TrainScheduleDetailActivity.class);
        intent.putExtra(EXTRA_TRAIN_SCHEDULE_ID, scheduleId);
        intent.putExtra(EXTRA_NUMBER_OF_ADULT_PASSENGER, numOfAdultPassenger);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_train_schedule_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInjector();

        String scheduleId = getIntent().getStringExtra(EXTRA_TRAIN_SCHEDULE_ID);
        int numOfAdultPassenger = getIntent().getIntExtra(EXTRA_NUMBER_OF_ADULT_PASSENGER, 0);

        trainSchedulePresenter.attachView(this);
        trainSchedulePresenter.getScheduleDetail(scheduleId, numOfAdultPassenger);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        buttonSubmit = findViewById(R.id.button_submit);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        appBarLayout = findViewById(R.id.app_bar_layout);
        originStationCode = findViewById(R.id.departure_station_code);
        originStationName = findViewById(R.id.departure_station_name);
        destinationStationCode = findViewById(R.id.arrival_station_code);
        destinationStationName = findViewById(R.id.arrival_station_name);

        appBarLayout.addOnOffsetChangedListener(onAppbarOffsetChange());
        tabLayout.setupWithViewPager(viewPager);
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
                    case 0 : return TrainScheduleDetailFragment.createInstance();
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

    @Override
    public void showScheduleDetail(TrainScheduleDetailViewModel trainScheduleDetailViewModel) {
        this.trainScheduleDetailViewModel = trainScheduleDetailViewModel;
        originStationCode.setText(trainScheduleDetailViewModel.getOriginStationCode());
        originStationName.setText(trainScheduleDetailViewModel.getOriginStationName());
        destinationStationCode.setText(trainScheduleDetailViewModel.getDestinationStationCode());
        destinationStationName.setText(trainScheduleDetailViewModel.getDestinationStationName());

//        if (viewPager.getAdapter() instanceof ShopInfoPagerAdapter) {
//            ShopInfoPagerAdapter adapter = (ShopInfoPagerAdapter) viewPager.getAdapter();
        Fragment fragmentTrip = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
        if (fragmentTrip instanceof TrainScheduleDetailFragment) {
            ((TrainScheduleDetailFragment) fragmentTrip).showScheduleDetail(trainScheduleDetailViewModel);
        }
        Fragment fragmentPrice = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
        if (fragmentPrice instanceof TrainSchedulePriceDetailFragment) {
            ((TrainSchedulePriceDetailFragment) fragmentPrice).showPrice(trainScheduleDetailViewModel);
        }
//        }
    }

    protected void initInjector() {
        if (trainScheduleDetailComponent == null) {
            trainScheduleDetailComponent = DaggerTrainScheduleDetailComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getApplication())).build();
        }
        trainScheduleDetailComponent.inject(this);
    }

    private AppBarLayout.OnOffsetChangedListener onAppbarOffsetChange() {
        return new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    setUpToolbarTitle();
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        };
    }

    private void setUpToolbarTitle() {
        String title = trainScheduleDetailViewModel.getOriginCityName() + " ➝ " +
                trainScheduleDetailViewModel.getDestinationCityName();
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

}