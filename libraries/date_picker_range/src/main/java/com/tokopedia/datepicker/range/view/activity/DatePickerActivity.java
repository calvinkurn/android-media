package com.tokopedia.datepicker.range.view.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.datepicker.range.R;
import com.tokopedia.datepicker.range.view.adapter.DatePickerTabPagerAdapter;
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;
import com.tokopedia.datepicker.range.view.fragment.DatePickerCustomFragment;
import com.tokopedia.datepicker.range.view.fragment.DatePickerPeriodFragment;
import com.tokopedia.datepicker.range.view.listener.DatePickerTabListener;
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 12/7/16.
 */

public class DatePickerActivity extends BaseTabActivity {

    public static final int OFFSCREEN_PAGE_LIMIT = 2;

    protected int selectionPeriod;
    protected int selectionType;
    protected long startDate;
    protected long endDate;
    protected long minStartDate;
    protected long maxStartDate;
    protected int maxDateRange;

    protected ArrayList<PeriodRangeModel> periodRangeModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fetchIntent(getIntent().getExtras());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        DatePickerTabListener tabListener = new DatePickerTabListener(viewPager);
        tabLayout.setOnTabSelectedListener(tabListener);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_period));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.label_date_custom));
        String title = getIntent().getExtras().getString(DatePickerConstant.EXTRA_PAGE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().show();
        }
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        super.setupFragment(savedinstancestate);
        viewPager.setCurrentItem(selectionType);
    }

    protected PagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(getDatePickerPeriodFragment());
        fragmentList.add(getDatePickerCustomFragment());
        return new DatePickerTabPagerAdapter(this, getSupportFragmentManager(), fragmentList);
    }

    protected DatePickerPeriodFragment getDatePickerPeriodFragment() {
        DatePickerPeriodFragment datePickerPeriodFragment = DatePickerPeriodFragment.newInstance(selectionPeriod, periodRangeModelList);
        return datePickerPeriodFragment;
    }

    protected DatePickerCustomFragment getDatePickerCustomFragment() {
        DatePickerCustomFragment datePickerCustomFragment = DatePickerCustomFragment.newInstance(startDate, endDate, minStartDate, maxStartDate, maxDateRange);
        return datePickerCustomFragment;
    }

    @Override
    protected int getPageLimit() {
        return OFFSCREEN_PAGE_LIMIT;
    }

    protected void fetchIntent(Bundle extras) {
        if (extras != null) {
            startDate = extras.getLong(DatePickerConstant.EXTRA_START_DATE, -1);
            endDate = extras.getLong(DatePickerConstant.EXTRA_END_DATE, -1);
            selectionPeriod = extras.getInt(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
            selectionType = extras.getInt(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
            minStartDate = extras.getLong(DatePickerConstant.EXTRA_MIN_START_DATE, -1);
            maxStartDate = extras.getLong(DatePickerConstant.EXTRA_MAX_END_DATE, -1);
            maxDateRange = extras.getInt(DatePickerConstant.EXTRA_MAX_DATE_RANGE, -1);
            periodRangeModelList = extras.getParcelableArrayList(DatePickerConstant.EXTRA_DATE_PERIOD_LIST);
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }
}