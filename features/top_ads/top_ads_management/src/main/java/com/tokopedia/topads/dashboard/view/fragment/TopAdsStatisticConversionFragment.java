package com.tokopedia.topads.dashboard.view.fragment;


import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsStatisticsType;
import com.tokopedia.topads.dashboard.data.model.data.Cell;

/**
 * Created by zulfikarrahman on 1/6/17.
 */

public class TopAdsStatisticConversionFragment extends TopAdsDashboardStatisticFragment {
    private @StringRes int title = R.string.title_top_ads_statistic_graph_convertion_all_ads;

    public static Fragment createInstance() {
        return new TopAdsStatisticConversionFragment();
    }

    @Override
    public float getValueData(Cell cell) {
        return (float)cell.getConversionSum();
    }

    @Override
    protected String getTitleGraph() {
        return getString(title);
    }

    @Override
    protected String getValueDisplay(Cell cell) {
        return cell.getConversionSumFmt();
    }

    public void updateTitle(@TopAdsStatisticsType int selectedStatisticType) {
        switch (selectedStatisticType){
            case TopAdsStatisticsType.SHOP_ADS:
                title = R.string.title_top_ads_statistic_graph_convertion_store;
                break;
            case TopAdsStatisticsType.PRODUCT_ADS:
                title = R.string.title_top_ads_statistic_graph_convertion_product;
                break;
            case TopAdsStatisticsType.ALL_ADS:
            case TopAdsStatisticsType.HEADLINE_ADS:
            default:
                title = R.string.title_top_ads_statistic_graph_convertion_all_ads;
                break;
        }
        if (contentTitleGraph != null) {
            contentTitleGraph.setText(getTitleGraph());
        }
    }
}
