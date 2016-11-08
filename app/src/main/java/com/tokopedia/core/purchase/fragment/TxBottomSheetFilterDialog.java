package com.tokopedia.core.purchase.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.purchase.model.AllTxFilter;
import com.tokopedia.core.purchase.model.TxFilterItem;
import com.tokopedia.core.purchase.utils.FilterUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Custom dialog for filter
 * Created by Kulomady on 6/27/16.
 */
public class TxBottomSheetFilterDialog implements AdapterView.OnItemSelectedListener,
        DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    private final Activity activity;
    private final BottomSheetDialog dialog;
    private AllTxFilter allTxFilter;
    private List<TxFilterItem> txFilterItemList = new ArrayList<>();
    private ArrayAdapter<TxFilterItem> adapter;

    @Bind(R2.id.filter)
    Spinner spnFilter;
    @Bind(R2.id.search)
    EditText searchField;
    @Bind(R2.id.start_date)
    TextView tvStartDate;
    @Bind(R2.id.end_date)
    TextView tvEndDate;
    @Bind(R2.id.search_button)
    TextView tvSearchSubmit;


    public interface OnFilterListener {
        void onFilterSearchButtonClicked(AllTxFilter allTxFilter);
    }

    private OnFilterListener onFilterListener;

    @SuppressLint("InflateParams")
    public TxBottomSheetFilterDialog(
            Activity activity,
            AllTxFilter allTxFilter,
            OnFilterListener onFilterListener) {

        this.activity = activity;
        this.allTxFilter = allTxFilter;
        this.onFilterListener = onFilterListener;
        this.dialog = new BottomSheetDialog(activity);
        this.dialog.setContentView(R.layout.layout_filter_payment_transaction);
        ButterKnife.bind(this, this.dialog);
        initViewContent(allTxFilter);
        this.dialog.setOnDismissListener(this);
        this.dialog.setOnCancelListener(this);
    }

    private void initViewContent(AllTxFilter allTxFilter) {
        spnFilter.setOnItemSelectedListener(this);
        tvStartDate.setText(allTxFilter.getDateStart());
        tvEndDate.setText(allTxFilter.getDateEnd());
        txFilterItemList = FilterUtils.filterTxAllItems(activity);
        adapter = new ArrayAdapter<>(activity, R.layout.simple_spinner_tv_res, txFilterItemList);
        spnFilter.setAdapter(adapter);
        setStateFilterSelection(allTxFilter.getFilter());
        searchField.clearFocus();
        hideKeyboard(activity.getCurrentFocus());
    }

    @OnClick(R2.id.start_date)
    void btnStartDateClicked() {
        DatePickerUtil datePicker = new DatePickerUtil(this.activity);
        datePicker.SetDate(
                allTxFilter.getDayStart(),
                allTxFilter.getMonthStart(),
                allTxFilter.getYearStart()
        );
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                allTxFilter.setYearStart(year);
                allTxFilter.setDayStart(dayOfMonth);
                allTxFilter.setMonthStart(month);
                tvStartDate.setText(allTxFilter.getDateStart());
            }
        });
    }

    @OnClick(R2.id.end_date)
    void btnEndDateClicked() {
        DatePickerUtil datePicker = new DatePickerUtil(this.activity);
        datePicker.SetDate(allTxFilter.getDayEnd(), allTxFilter.getMonthEnd(),
                allTxFilter.getYearEnd());
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {

            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                allTxFilter.setYearEnd(year);
                allTxFilter.setDayEnd(dayOfMonth);
                allTxFilter.setMonthEnd(month);
                tvEndDate.setText(allTxFilter.getDateEnd());
            }
        });
    }

    @OnClick(R2.id.search_button)
    void btnSearchClicked() {
        allTxFilter.setQuery(searchField.getText().toString());
        onFilterListener.onFilterSearchButtonClicked(allTxFilter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedFilter = ((TxFilterItem) parent.getAdapter().getItem(position)).getId();
        allTxFilter.setFilter(selectedFilter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        View view = activity.getCurrentFocus();
//        setDefaultSpinnerSelected();
        hideKeyboard(view);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        setStateFilterSelection(allTxFilter.getFilter());
        spnFilter.clearFocus();
        View view = activity.getCurrentFocus();
        hideKeyboard(view);
    }


    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
        ButterKnife.unbind(dialog);
    }

    public void setStateFilterSelection(String txFilterID) {
        int index = 0;
        for (int i = 0; i < txFilterItemList.size(); i++) {
            if (txFilterItemList.get(i).getId().equalsIgnoreCase(txFilterID)) index = i;
        }
        spnFilter.setSelection(index);
    }
}
