package com.tokopedia.train.search.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.presentation.TrainBaseActivity;
import com.tokopedia.train.search.di.DaggerTrainSearchComponent;
import com.tokopedia.train.search.presentation.contract.BaseTrainFilterListener;
import com.tokopedia.train.search.presentation.contract.FilterSearchActionView;
import com.tokopedia.train.search.presentation.contract.TrainFilterSearchContract;
import com.tokopedia.train.search.presentation.fragment.FilterTrainClassFragment;
import com.tokopedia.train.search.presentation.fragment.FilterTrainDepartureFragment;
import com.tokopedia.train.search.presentation.fragment.FilterTrainNameFragment;
import com.tokopedia.train.search.presentation.fragment.TrainFilterSearchFragment;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.presenter.TrainFilterSearchPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 3/20/18.
 */

public class TrainFilterSearchActivity extends TrainBaseActivity
        implements TrainFilterSearchContract.View, FilterSearchActionView {

    public static final String MODEL_SEARCH_FILTER = "model_filter";
    private static final String ARRIVAL_TIMESTAMP_SELECTED = "arrival_timestamp_selected";
    private static final String SCHEDULE_VARIANT = "schedule_variant";
    private static final String FILTER_SEARCH = "filter_search";

    @Inject
    TrainFilterSearchPresenter presenter;

    private ProgressBar progressBar;
    private LinearLayout containerLayout;
    private FilterSearchData filterSearchData;
    private Button filterButton;
    private boolean showCloseButton;
    private String arrivalTimestampSelected;
    private int scheduleVariant;

    public static Intent getCallingIntent(Activity activity, String arrivalTimestampSelected,
                                          int scheduleVariant, FilterSearchData filterSearchData) {
        Intent intent = new Intent(activity, TrainFilterSearchActivity.class);
        intent.putExtra(ARRIVAL_TIMESTAMP_SELECTED, arrivalTimestampSelected);
        intent.putExtra(SCHEDULE_VARIANT, scheduleVariant);
        intent.putExtra(FILTER_SEARCH, filterSearchData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.progress_bar);
        containerLayout = findViewById(R.id.container_layout);
        filterButton = findViewById(R.id.button_filter);

        resetBackStackFragment();
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilterButtonClick();
            }
        });

        DaggerTrainSearchComponent.builder()
                .trainComponent(TrainComponentUtils.getTrainComponent(getApplication()))
                .build()
                .inject(this);
        presenter.attachView(this);
        arrivalTimestampSelected = getIntent().getStringExtra(ARRIVAL_TIMESTAMP_SELECTED);
        scheduleVariant = getIntent().getIntExtra(SCHEDULE_VARIANT, 0);
        presenter.getFilterSearchData();
    }

    protected void setupFragment(Bundle savedInstance) {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_train_filter_search;
    }

    @Override
    protected Fragment getNewFragment() {
        return filterSearchData == null ? null : TrainFilterSearchFragment.newInstance();
    }

    @Override
    public void showCountSchedule(int totalSchedule) {
        filterButton.setText(String.format(getString(R.string.train_filter_button_info), totalSchedule));
    }

    @Override
    public void renderFilterSearchData(FilterSearchData filterSearchData) {
        this.filterSearchData = filterSearchData;
        presenter.getCountScheduleAvailable(filterSearchData);
        inflateFragment();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        containerLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        containerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public FilterSearchData getFilterSearchDataFromIntent() {
        return getIntent().getParcelableExtra(FILTER_SEARCH);
    }

    @Override
    public String getArrivalTimestampSelected() {
        return arrivalTimestampSelected;
    }

    @Override
    public int getScheduleVariant() {
        return scheduleVariant;
    }

    @Override
    public FilterSearchData getFilterSearchData() {
        return filterSearchData;
    }

    @Override
    public void onChangeFilterSearchData(FilterSearchData filterSearchData) {
        this.filterSearchData = filterSearchData;
        presenter.getCountScheduleAvailable(filterSearchData);
    }

    @Override
    public void setTitleToolbar(String titleToolbar) {
        updateTitle(titleToolbar);
    }

    private void resetBackStackFragment() {
        getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onNameFilterSearchTrainClicked() {
        replaceFragment(FilterTrainNameFragment.newInstance(), FilterTrainNameFragment.TAG);
    }

    @Override
    public void onDepartureFilterSearchTrainClicked() {
        replaceFragment(FilterTrainDepartureFragment.newInstance(), FilterTrainNameFragment.TAG);
    }

    @Override
    public void onClassFilterSearchTrainClicked() {
        replaceFragment(FilterTrainClassFragment.newInstance(), FilterTrainNameFragment.TAG);
    }

    public void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.parent_view, fragment, tag).addToBackStack(tag).commit();
    }

    @Override
    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    @Override
    public void setCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
        if (getSupportActionBar() != null && showCloseButton) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_train_filter_reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_reset) {
            onResetAllFilter();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onResetAllFilter() {
        Fragment fragment = getCurrentFragment();
        if (fragment != null && fragment instanceof BaseTrainFilterListener) {
            ((BaseTrainFilterListener) fragment).resetFilter();
        }
    }

    private void onResetSpecificFilter() {
        Fragment fragment = getCurrentFragment();
        if (fragment != null && fragment instanceof BaseTrainFilterListener) {
            ((BaseTrainFilterListener) fragment).changeFilterToOriginal();
        }
    }

    private void onFilterButtonClick() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            Intent intent = new Intent();
            filterSearchData.setHasFilter(filterSearchData);
            intent.putExtra(MODEL_SEARCH_FILTER, filterSearchData);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        onBackPressed(true);
    }

    @Override
    public void onBackPressed() {
        this.onBackPressed(false);
    }

    private void onBackPressed(boolean submitFilter) {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            if (!submitFilter) {
                onResetSpecificFilter();
            }
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.travel_anim_stay,R.anim.travel_slide_out_up);
        }
    }

    private Fragment getCurrentFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (int i = 0, sizei = fragmentList.size(); i < sizei; i++) {
            Fragment fragment = fragmentList.get(i);
            if (fragment.isAdded() && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}
