package com.tokopedia.topads.dashboard.view.fragment;


import android.support.v4.app.Fragment;

import com.tokopedia.topads.dashboard.R;
import com.tokopedia.topads.dashboard.data.model.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticCtrFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        return new TopAdsStatisticCtrFragment();
    }

    @Override
    public float getValueData(Cell cell) {
        return (float)cell.getCtrPercentage();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_top_ads_statistic_graph_ctr);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getCtrPercentageFmt();
    }
}
