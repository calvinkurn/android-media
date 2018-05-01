package com.tokopedia.topads.dashboard.view.fragment;


import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.Summary;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticImprFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsStatisticImprFragment();
        return fragment;
    }

    @Override
    public float getValueData(Cell cell) {
        return (float)cell.getImpressionSum();
    }

    @Override
    protected String getTotalSummary(Summary summary) {
        return summary.getImpressionSumFmt();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.label_top_ads_impression);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getImpressionSumFmt();
    }
}
