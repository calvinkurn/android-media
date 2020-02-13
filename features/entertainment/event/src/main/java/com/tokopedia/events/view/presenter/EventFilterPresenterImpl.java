package com.tokopedia.events.view.presenter;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.events.R;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventFilterContract;
import com.tokopedia.events.view.fragment.CategoryFilterFragment;
import com.tokopedia.events.view.fragment.TimeFilterFragment;
import com.tokopedia.events.view.utils.Utils;

import javax.inject.Inject;

import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY;
import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ARRAY;
import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ID;
import static com.tokopedia.events.view.contractor.EventFilterContract.START_DATE;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_ID;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_RANGE;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_VALUES;

public class EventFilterPresenterImpl
        extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventFilterContract.EventFilterPresenter {
    private EventFilterContract.EventFilterView mView;

    public EventFilterPresenterImpl() {

    }

    private String filterCategoryId;
    private String filterTimeRange;
    private long filterStartDate;

    @Override
    public boolean onClickOptionMenu(int id) {
        return false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventFilterContract.EventFilterView) view;
        Intent inIntent = mView.getActivity().getIntent();
        String timefiltertext = "";
        String categoryText = "";
        filterTimeRange = inIntent.getStringExtra(TIME_RANGE);
        filterCategoryId = inIntent.getStringExtra(CATEGORY);
        for (int i = 0; i < CATEGORY_ID.length; i++) {
            if (CATEGORY_ID[i].equals(filterCategoryId)) {
                categoryText = CATEGORY_ARRAY[i];
                break;
            }
        }
        filterStartDate = inIntent.getLongExtra(START_DATE, 0);
        if (filterStartDate > 0 && filterTimeRange.isEmpty()) {
            timefiltertext = Utils.getSingletonInstance().convertLongEpoch(filterStartDate);
        } else {
            for (int i = 0; i < TIME_ID.length; i++) {
                if (TIME_ID[i].equals(filterTimeRange)) {
                    timefiltertext = TIME_VALUES[i];
                    break;
                }
            }
        }
        mView.renderFilters(timefiltertext, categoryText);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void onClickCalendar() {
        FragmentManager fragmentManager = mView.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TimeFilterFragment filterFragment = TimeFilterFragment.newInstance(filterTimeRange, filterStartDate);
        fragmentTransaction.add(R.id.main_content, filterFragment);
        fragmentTransaction.addToBackStack("FILTERFRAGMENT");
        fragmentTransaction.commit();
    }

    @Override
    public void onClickCategory() {
        FragmentManager fragmentManager = mView.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        int index = -1;
        for (int i = 0; i < CATEGORY_ID.length; i++) {
            if (CATEGORY_ID[i].equals(filterCategoryId)) {
                index = i;
                break;
            }
        }
        CategoryFilterFragment filterFragment = CategoryFilterFragment.newInstance(index);
        fragmentTransaction.add(R.id.main_content, filterFragment);
        fragmentTransaction.addToBackStack("FILTERFRAGMENT");
        fragmentTransaction.commit();
    }

    @Override
    public void onClickResetFilter() {
        mView.resetFilters();
        filterCategoryId = "";
        filterTimeRange = "";
        filterStartDate = 0;
    }

    @Override
    public void setCategoryFilter(String selectedCategory) {
        filterCategoryId = selectedCategory;
        mView.getSupportFragmentManager().popBackStack();
    }

    @Override
    public void deleteCalendarFilter() {
        filterTimeRange = "";
        filterStartDate = 0;
    }

    @Override
    public void deleteCategoryFilter() {
        filterCategoryId = "";
    }

    @Override
    public void setTimeRangeFilter(String timeRange, long startDate) {
        filterTimeRange = timeRange;
        filterStartDate = startDate;
        mView.getSupportFragmentManager().popBackStack();
    }

    @Override
    public void setFilters() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TIME_RANGE, filterTimeRange);
        resultIntent.putExtra(START_DATE, filterStartDate);
        resultIntent.putExtra(CATEGORY, filterCategoryId);
        mView.getActivity().setResult(Activity.RESULT_OK, resultIntent);
        mView.getActivity().finish();
    }
}
