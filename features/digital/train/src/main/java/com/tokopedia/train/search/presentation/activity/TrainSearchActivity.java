package com.tokopedia.train.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.train.search.presentation.fragment.TrainSearchFragment;

/**
 * @author by alvarisi on 3/8/18.
 */

public abstract class TrainSearchActivity extends TrainBaseActivity implements TrainSearchFragment.ActionListener {

    public static final String EXTRA_SEARCH_PASS_DATA = "EXTRA_SEARCH_PASS_DATA";
    protected TrainSearchPassDataViewModel trainSearchPassDataViewModel;
    private String dateString;
    private String adultPassenger;
    private String infantPassenger;
    private int infantTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeDataFromIntent();
        super.onCreate(savedInstanceState);
        setupTrainToolbar();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    protected void initializeDataFromIntent() {
        trainSearchPassDataViewModel = getIntent().getParcelableExtra(EXTRA_SEARCH_PASS_DATA);
        dateString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_SEARCH,
                TrainDateUtil.DEFAULT_VIEW_LOCAL_DETAIL, getDepartureDate());
        adultPassenger = String.format(getString(R.string.train_search_passenger_adult), trainSearchPassDataViewModel.getAdult());
        infantPassenger = String.format(getString(R.string.train_search_passenger_infant), trainSearchPassDataViewModel.getInfant());
        infantTotal = trainSearchPassDataViewModel.getInfant();
    }

    private void setupTrainToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String subtitle = dateString + " | " + adultPassenger + ", " + infantPassenger;
        String subtitleNonInfant = dateString + " | " + adultPassenger;
        updateTitle(getTitleTrainToolbar(), infantTotal == 0 ? subtitleNonInfant : subtitle);
    }

    protected abstract String getTitleTrainToolbar();

    protected abstract String getDepartureDate();

    @Override
    public void navigateToHomepage() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
