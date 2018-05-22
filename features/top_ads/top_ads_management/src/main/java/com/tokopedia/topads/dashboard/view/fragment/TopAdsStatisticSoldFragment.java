package com.tokopedia.topads.dashboard.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.data.model.data.Cell;

/**
 * Created by hadi.putra on 17/05/18.
 */

public class TopAdsStatisticSoldFragment extends TopAdsDashboardStatisticFragment {

    public static Fragment createInstance() {
        return new TopAdsStatisticSoldFragment();
    }

    @Override
    protected float getValueData(Cell cell) {
        return cell.getSoldSum();
    }

    @Override
    protected String getTitleGraph() {
        return getString(R.string.title_top_ads_statistic_graph_sold);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getSoldSumFmt();
    }
}
