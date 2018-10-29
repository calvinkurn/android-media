package com.tokopedia.events.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.contractor.EventFilterContract;
import com.tokopedia.events.view.contractor.ICloseFragement;
import com.tokopedia.events.view.fragment.CategoryFilterFragment;
import com.tokopedia.events.view.fragment.TimeFilterFragment;
import com.tokopedia.events.view.presenter.EventFilterPresenterImpl;
import com.tokopedia.events.view.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ARRAY;
import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ID;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_ID;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_VALUES;

public class EventFilterActivity
        extends EventBaseActivity
        implements EventFilterContract.EventFilterView,
        CategoryFilterFragment.CategorySelectedListener, TimeFilterFragment.OnSelectTimeFilterListener, ICloseFragement {
    @BindView(R2.id.iv_close_filter)
    ImageView ivCloseFilter;
    @BindView(R2.id.tv_filter_calendar)
    TextView tvFilterCalendar;
    @BindView(R2.id.item_calendar)
    TextView itemCalendar;
    @BindView(R2.id.delete_calendar)
    View deleteCalendar;
    @BindView(R2.id.selected_filter1)
    LinearLayout selectedTime;
    @BindView(R2.id.tv_filter_category)
    TextView tvFilterCategory;
    @BindView(R2.id.item_category)
    TextView itemCategory;
    @BindView(R2.id.delete_category)
    View deleteCategory;
    @BindView(R2.id.selected_filter2)
    LinearLayout selectedCategory;
    private EventFilterPresenterImpl filterPresenter;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, EventFilterActivity.class);
    }

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getEventFilterPresenter();
        filterPresenter = (EventFilterPresenterImpl) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void resetFilters() {
        selectedTime.setVisibility(View.GONE);
        selectedCategory.setVisibility(View.GONE);
    }

    @Override
    public void renderFilters(String timeRange, String category) {
        itemCalendar.setText(timeRange);
        itemCategory.setText(category);
        if (!timeRange.isEmpty())
            selectedTime.setVisibility(View.VISIBLE);
        if (!category.isEmpty())
            selectedCategory.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            getWindow().setStatusBarColor(ContextCompat.getColor(this, com.tokopedia.abstraction.R.color.white));
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_fragment_filter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setCategory(String category) {
        filterPresenter.setCategoryFilter(category);
        for (int i = 0; i < CATEGORY_ID.length; i++) {
            if (category.equals(CATEGORY_ID[i])) {
                itemCategory.setText(CATEGORY_ARRAY[i]);
                break;
            }
        }
        if (category.isEmpty())
            selectedCategory.setVisibility(View.GONE);
        else
            selectedCategory.setVisibility(View.VISIBLE);
    }

    @OnClick({R2.id.delete_calendar,
            R2.id.delete_category,
            R2.id.tv_reset,
            R2.id.iv_close_filter})
    void onClickListener(View v) {
        int id = v.getId();
        if (id == R.id.delete_calendar) {
            filterPresenter.deleteCalendarFilter();
            itemCalendar.setText("");
            selectedTime.setVisibility(View.GONE);
        } else if (id == R.id.delete_category) {
            filterPresenter.deleteCategoryFilter();
            itemCategory.setText("");
            selectedCategory.setVisibility(View.GONE);
        } else if (id == R.id.tv_reset)
            filterPresenter.onClickResetFilter();
        else if (id == R.id.iv_close_filter)
            onBackPressed();
    }

    @Override
    public void onFragmentInteraction(String timerange, long startdate) {
        if (startdate > 0) {
            itemCalendar.setText(Utils.getSingletonInstance().convertLongEpoch(startdate));
            selectedTime.setVisibility(View.VISIBLE);
        } else {
            int index = -1;
            for (int i = 0; i < TIME_ID.length; i++) {
                if (timerange.equals(TIME_ID[i])) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                itemCalendar.setText(TIME_VALUES[index]);
                selectedTime.setVisibility(View.VISIBLE);
            } else {
                selectedTime.setVisibility(View.GONE);
            }
        }
        filterPresenter.setTimeRangeFilter(timerange, startdate);
    }

    @OnClick(R2.id.tv_simpan)
    void onSubmitFilters() {
        filterPresenter.setFilters();
    }

    @OnClick({R2.id.tv_filter_category,
            R2.id.tv_filter_calendar})
    void onClickFilterType(View v) {
        int id = v.getId();
        if (id == R.id.tv_filter_calendar)
            filterPresenter.onClickCalendar();
        else
            filterPresenter.onClickCategory();
    }

    @Override
    public void closeFragmentSelf() {
        getSupportFragmentManager().popBackStack();
    }
}