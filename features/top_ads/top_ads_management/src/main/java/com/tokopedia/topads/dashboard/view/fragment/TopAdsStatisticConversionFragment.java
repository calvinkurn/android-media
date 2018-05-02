package com.tokopedia.topads.dashboard.view.fragment;


import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.Summary;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticConversionFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsStatisticConversionFragment();
        return fragment;
    }

    @Override
    public float getValueData(Cell cell) {
        return (float)cell.getConversionSum();
    }

    @Override
    protected String getTotalSummary(Summary summary) {
        return summary.getConversionSumFmt();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.label_top_ads_conversion);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getConversionSumFmt();
    }
}
