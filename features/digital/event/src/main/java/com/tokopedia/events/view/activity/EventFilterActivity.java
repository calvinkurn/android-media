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
import com.tokopedia.events.view.contractor.EventFilterContract;
import com.tokopedia.events.view.contractor.ICloseFragement;
import com.tokopedia.events.view.fragment.CategoryFilterFragment;
import com.tokopedia.events.view.fragment.TimeFilterFragment;
import com.tokopedia.events.view.presenter.EventFilterPresenterImpl;
import com.tokopedia.events.view.utils.Utils;

import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ARRAY;
import static com.tokopedia.events.view.contractor.EventFilterContract.CATEGORY_ID;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_ID;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_VALUES;

public class EventFilterActivity
        extends EventBaseActivity
        implements EventFilterContract.EventFilterView,
        CategoryFilterFragment.CategorySelectedListener, TimeFilterFragment.OnSelectTimeFilterListener, ICloseFragement, View.OnClickListener {
    ImageView ivCloseFilter;
    TextView tvFilterCalendar;
    TextView itemCalendar;
    View deleteCalendar;
    LinearLayout selectedTime;
    TextView tvFilterCategory;
    TextView itemCategory;
    View deleteCategory;
    LinearLayout selectedCategory;
    TextView resetBtn;
    TextView simpanBtn;
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
    void setupVariables() {
        ivCloseFilter = findViewById(R.id.iv_close_filter);
        tvFilterCalendar = findViewById(R.id.tv_filter_calendar);
        itemCalendar = findViewById(R.id.item_calendar);
        deleteCalendar = findViewById(R.id.delete_calendar);
        selectedTime = findViewById(R.id.selected_filter1);
        tvFilterCategory = findViewById(R.id.tv_filter_category);
        itemCategory = findViewById(R.id.item_category);
        deleteCategory = findViewById(R.id.delete_category);
        selectedCategory = findViewById(R.id.selected_filter2);
        resetBtn = findViewById(R.id.tv_reset);
        simpanBtn = findViewById(R.id.tv_simpan);

        deleteCalendar.setOnClickListener(this);
        deleteCategory.setOnClickListener(this);
        ivCloseFilter.setOnClickListener(this);
        tvFilterCategory.setOnClickListener(this);
        tvFilterCalendar.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        simpanBtn.setOnClickListener(this);
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

    @Override
    public void closeFragmentSelf() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_calendar ||
                v.getId() == R.id.delete_category ||
                v.getId() == R.id.tv_reset ||
                v.getId() == R.id.iv_close_filter) {
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
        } else if (v.getId() == R.id.tv_simpan) {
            filterPresenter.setFilters();
        } else if (v.getId() == R.id.tv_filter_category || v.getId() == R.id.tv_filter_calendar) {
            int id = v.getId();
            if (id == R.id.tv_filter_calendar)
                filterPresenter.onClickCalendar();
            else
                filterPresenter.onClickCategory();
        }
    }
}