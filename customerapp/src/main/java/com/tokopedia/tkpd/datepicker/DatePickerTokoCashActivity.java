package com.tokopedia.tkpd.datepicker;

import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.tkpd.R;

/**
 * Created by nabillasabbaha on 8/22/17.
 */

public class DatePickerTokoCashActivity extends DatePickerActivity {

    @Override
    protected void setupLayout() {
        super.setupLayout();
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Pilih Rentang Waktu"));
        tabLayout.addTab(tabLayout.newTab().setText("Pilih Tanggal"));

        getSupportActionBar().setTitle("Rentang Tanggal");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_new_action_back_grey);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_date_picker_tokocash;
    }
}
