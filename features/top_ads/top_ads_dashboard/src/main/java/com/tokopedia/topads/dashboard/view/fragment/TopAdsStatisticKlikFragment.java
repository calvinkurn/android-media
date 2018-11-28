package com.tokopedia.topads.dashboard.view.fragment;


import android.support.v4.app.Fragment;

import com.tokopedia.topads.dashboard.R;
import com.tokopedia.topads.dashboard.data.model.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticKlikFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        return new TopAdsStatisticKlikFragment();
    }

    @Override
    public float getValueData(Cell cell) {
        return (float) cell.getClickSum();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_top_ads_statistic_graph_click);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getClickSumFmt();
    }
}
