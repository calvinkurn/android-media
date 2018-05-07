package com.tokopedia.topads.common.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.design.label.DateLabelView;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.presenter.TopAdsBaseListPresenter;
import com.tokopedia.topads.dashboard.constant.SortTopAdsOption;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.utils.TopAdsDatePeriodUtil;
import com.tokopedia.topads.dashboard.view.activity.TopAdsSortByActivity;
import com.tokopedia.topads.dashboard.view.model.TopAdsSortByModel;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by hadi.putra on 04/05/18.
 */

public abstract class TopAdsBaseListFragment<V extends Visitable, F extends AdapterTypeFactory, P extends TopAdsBaseListPresenter>
        extends BaseListFragment<V, F>
        implements SearchInputView.Listener {

    protected static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300);
    protected static final int REQUEST_CODE_AD_FILTER = 3;
    protected static final int REQUEST_CODE_AD_SORT_BY = 5;

    protected Date startDate;
    protected Date endDate;

    @Px
    private int tempTopPaddingRecycleView;
    @Px
    private int tempBottomPaddingRecycleView;
    private int scrollFlags;
    protected TopAdsSortByModel selectedSort;
    protected int status;
    protected String keyword;

    protected SearchInputView searchInputView;
    private AppBarLayout appBarLayout;
    private DateLabelView dateLabelView;
    private BottomActionView buttonActionView;
    private CoordinatorLayout.Behavior appBarBehaviour;
    private RecyclerView recyclerView;


    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_ads_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getRecyclerView(view);
        tempTopPaddingRecycleView = recyclerView.getPaddingTop();
        tempBottomPaddingRecycleView = recyclerView.getPaddingBottom();
        initDateLabelView(view);
    }

    private void initDateLabelView(View view) {
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        dateLabelView = (DateLabelView) view.findViewById(R.id.date_label_view);
        dateLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        AppBarLayout.LayoutParams dateLabelViewLayoutParams = (AppBarLayout.LayoutParams) dateLabelView.getLayoutParams();
        scrollFlags = dateLabelViewLayoutParams.getScrollFlags();
        searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
        searchInputView.setDelayTextChanged(DEFAULT_DELAY_TEXT_CHANGED);
        searchInputView.setListener(this);
        buttonActionView = (BottomActionView) view.findViewById(R.id.bottom_action_view);
        buttonActionView.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSortBy();
            }
        });
        buttonActionView.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilter();
            }
        });
        appBarBehaviour = new AppBarLayout.Behavior();
    }

    public abstract void goToFilter();

    public abstract P getPresenter();

    public void openDatePicker(){
        Intent intent = getDatePickerIntent(getActivity(), startDate, endDate);
        startActivityForResult(intent, DatePickerConstant.REQUEST_CODE_DATE);
    }

    public void goToSortBy() {
        Intent intent = TopAdsSortByActivity.createIntent(getActivity(), selectedSort == null?
                SortTopAdsOption.LATEST : selectedSort.getId());
        startActivityForResult(intent, REQUEST_CODE_AD_SORT_BY);
    }

    @Override
    public void onItemClicked(V o) {

    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected F getAdapterTypeFactory() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getPresenter().isDateUpdated(startDate, endDate)){
            startDate = getPresenter().getStartDate();
            endDate = getPresenter().getEndDate();
            //loadData();
        }
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    private void showDateLabel(boolean show) {
        @Px int topPadding = 0;
        @Px int bottomPadding = 0;
        if (show) {
            topPadding = tempTopPaddingRecycleView;
            bottomPadding = tempBottomPaddingRecycleView;
        }
        recyclerView.setPadding(0, topPadding, 0, bottomPadding);
        if (appBarLayout != null) {
            AppBarLayout.LayoutParams dateLabelLayoutParams = (AppBarLayout.LayoutParams) dateLabelView.getLayoutParams();
            dateLabelLayoutParams.setScrollFlags(show ? scrollFlags : 0);
            dateLabelView.setLayoutParams(dateLabelLayoutParams);

            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(show ? appBarBehaviour : null);
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }
        if (dateLabelView != null) {
            dateLabelView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private Intent getDatePickerIntent(Context context, Date start, Date end){
        Intent intent = new Intent(context, DatePickerActivity.class);
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.YEAR, -1);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, start.getTime());
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, end.getTime());

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, TopAdsConstant.MAX_DATE_RANGE);

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, TopAdsDatePeriodUtil.getPeriodRangeList(getActivity()));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, getPresenter().getLastSelectionDatePickerIndex());
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, getPresenter().getLastSelectionDatePickerType());

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, getActivity().getString(R.string.title_date_picker));
        return intent;
    }
}
