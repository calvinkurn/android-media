package com.tokopedia.topads.dashboard.view.fragment;


import android.support.v4.app.Fragment;

import com.tokopedia.topads.dashboard.R;
import com.tokopedia.topads.dashboard.data.model.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticAvgFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        return new TopAdsStatisticAvgFragment();
    }

    @Override
    public float getValueData(Cell cell) {
        return (float) cell.getCostAvg();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_top_ads_statistic_graph_avg);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return getString(R.string.top_ads_tooltip_statistic_avg, cell.getCostAvgFmt());
    }
}
