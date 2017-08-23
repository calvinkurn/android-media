package com.tokopedia.digital.tokocash.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.router.digitalmodule.sellermodule.PeriodRangeModelCore;
import com.tokopedia.core.router.digitalmodule.sellermodule.SellerModuleRouter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.base.BaseDigitalPresenterActivity;
import com.tokopedia.digital.tokocash.adapter.FilterTokoCashAdapter;
import com.tokopedia.digital.tokocash.model.HeaderHistory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * Created by nabillasabbaha on 8/21/17.
 */

public class HistoryTokocashActivity extends BaseDigitalPresenterActivity {

    private static final int SELECTION_TYPE_PERIOD_DATE = 0;
    private static final int EXTRA_INTENT_DATE_PICKER = 50;
    private static final String EXTRA_START_DATE = "EXTRA_START_DATE";
    private static final String EXTRA_END_DATE = "EXTRA_END_DATE";
    private static final String EXTRA_SELECTION_PERIOD = "EXTRA_SELECTION_PERIOD";
    private static final String EXTRA_SELECTION_TYPE = "EXTRA_SELECTION_TYPE";

    @BindView(R2.id.date_label_view)
    LinearLayout layoutDate;
    @BindView(R2.id.text_view_date)
    TextView tvDate;
    @BindView(R2.id.filter_history_tokocash)
    RecyclerView filterHistoryRecyclerView;

    private int datePickerSelection = 2;
    private int datePickerType = 0;
    private long startDate;
    private long endDate;
    private FilterTokoCashAdapter adapter;

    public static Intent newInstance(Context context) {
        return new Intent(context, HistoryTokocashActivity.class);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_tokocash;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(getString(R.string.title_menu_history_tokocash));
        initialRangeDateFilter();
        initialRecyclerView();
    }

    private void initialRangeDateFilter() {
        Application application = HistoryTokocashActivity.this.getApplication();
        if (application != null && application instanceof SellerModuleRouter) {
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
            startCalendar.add(Calendar.DATE, -29);
            startDate = startCalendar.getTimeInMillis();
            endDate = endCalendar.getTimeInMillis();
            String getFormattedDatePicker =
                    ((SellerModuleRouter) application)
                            .getRangeDateFormatted(HistoryTokocashActivity.this, startDate, endDate);
            tvDate.setText(getFormattedDatePicker);
        }
    }

    private void initialRecyclerView() {
        filterHistoryRecyclerView.setHasFixedSize(true);
        filterHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        adapter = new FilterTokoCashAdapter(new ArrayList<HeaderHistory>());
        adapter.addFilterTokoCashList(mockDataFilter());
        adapter.setListener(getFilterTokoCashListener());
        filterHistoryRecyclerView.setAdapter(adapter);
    }

    //TODO : delete this if data from server has been ready
    private List<HeaderHistory> mockDataFilter() {
        List<HeaderHistory> headerList = new ArrayList<>();
        String[] listName = {"Semua", "Pembayaran", "Top Up", "Penerimaan", "Pengembalian"};
        for (int i = 0; i < 5; i++) {
            HeaderHistory headerHistory = new HeaderHistory();
            headerHistory.setName(listName[i]);
            headerHistory.setSelected(false);
            headerList.add(headerHistory);
        }
        return headerList;
    }

    private FilterTokoCashAdapter.FilterTokoCashListener getFilterTokoCashListener() {
        return new FilterTokoCashAdapter.FilterTokoCashListener() {
            @Override
            public void clearFilter() {
                //TODO : change with method clear filter
                Toast.makeText(getApplicationContext(), "clear", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void selectFilter(String typeFilter) {
                //TODO : change with method selected filter
                Toast.makeText(getApplicationContext(), "select" + typeFilter, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Application application = HistoryTokocashActivity.this.getApplication();
                if (application != null && application instanceof SellerModuleRouter) {
                    Intent intent = ((SellerModuleRouter) application)
                            .goToDatePicker(HistoryTokocashActivity.this, getPeriodRangeModel(),
                                    startDate, endDate, datePickerSelection, datePickerType);
                    startActivityForResult(intent, EXTRA_INTENT_DATE_PICKER);
                }
            }
        });
    }

    private List<PeriodRangeModelCore> getPeriodRangeModel() {
        List<PeriodRangeModelCore> periodRangeModelCoreList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DATE, 0);
        startCalendar.add(Calendar.DATE, 0);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_today)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -6);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -29);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_thirty_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_this_month)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -59);
        endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        endCalendar.add(Calendar.DATE, -29);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), getString(R.string.range_date_last_month)));
        return periodRangeModelCoreList;
    }

    public PeriodRangeModelCore convert(long startDate, long endDate, String label) {
        return new PeriodRangeModelCore(startDate, endDate, label);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == EXTRA_INTENT_DATE_PICKER && intent != null) {
            Application application = HistoryTokocashActivity.this.getApplication();
            if (application != null && application instanceof SellerModuleRouter) {
                startDate = intent.getLongExtra(EXTRA_START_DATE, -1);
                endDate = intent.getLongExtra(EXTRA_END_DATE, -1);
                datePickerSelection = intent.getIntExtra(EXTRA_SELECTION_PERIOD, 1);
                datePickerType = intent.getIntExtra(EXTRA_SELECTION_TYPE, SELECTION_TYPE_PERIOD_DATE);
                String getFormattedDatePicker =
                        ((SellerModuleRouter) application)
                                .getRangeDateFormatted(HistoryTokocashActivity.this,
                                        startDate, endDate);
                tvDate.setText(getFormattedDatePicker);
            }
        }
    }
}