package com.tokopedia.tkpd.tokocash.datepicker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.tkpd.R;

/**
 * Created by nabillasabbaha on 8/22/17.
 */

public class DatePickerTokoCashActivity extends DatePickerActivity {

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.toko_cash_tab_choose_date_range)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.toko_cash_tab_choose_date)));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.tkpd_main_green));
        tabLayout.setTabTextColors(ContextCompat.getColor(getApplicationContext(), R.color.black_38),
                ContextCompat.getColor(getApplicationContext(), R.color.tkpd_main_green));

        getSupportActionBar().setTitle(getString(R.string.toko_cash_title_page_date_range));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_new_action_back_grey);
        getSupportActionBar().show();
    }

    @Override
    protected void setToolbarColorWhite() {
        super.setToolbarColorWhite();
    }
}
