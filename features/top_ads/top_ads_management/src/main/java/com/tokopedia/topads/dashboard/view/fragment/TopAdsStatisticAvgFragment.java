package com.tokopedia.topads.dashboard.view.fragment;


import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.Summary;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticAvgFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsStatisticAvgFragment();
        return fragment;
    }

    @Override
    public float getValueData(Cell cell) {
        return (float) cell.getCostAvg();
    }

    @Override
    protected String getTotalSummary(Summary summary) {
        return summary.getCostAvgFmt();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.label_top_ads_avg_cost);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return getString(R.string.top_ads_tooltip_statistic_avg, cell.getCostAvgFmt());
    }
}
