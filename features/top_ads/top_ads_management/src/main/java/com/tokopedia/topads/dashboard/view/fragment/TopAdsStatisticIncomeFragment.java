package com.tokopedia.topads.dashboard.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.Cell;

/**
 * Created by hadi.putra on 17/05/18.
 */

public class TopAdsStatisticIncomeFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        return new TopAdsStatisticIncomeFragment();
    }

    @Override
    protected float getValueData(Cell cell) {
        return cell.getGrossProfit();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_top_ads_statistic_graph_total_income);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return getString(R.string.top_ads_tooltip_statistic_use, cell.getGrossProfitFmt());
    }
}
