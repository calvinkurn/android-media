package com.tokopedia.topads.dashboard.view.fragment


import android.support.annotation.StringRes
import android.support.v4.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.Cell
import kotlinx.android.synthetic.main.partial_statistics_graph_fragment.*

/**
 * Created by zulfikarrahman on 1/6/17.
 */

class TopAdsStatisticConversionFragment : TopAdsDashboardStatisticFragment() {
    @StringRes
    private var title = R.string.title_top_ads_statistic_graph_convertion_all_ads

    override val titleGraph: String
        get() = getString(title)

    override fun getValueData(cell: Cell): Float = cell.conversionSum.toFloat()

    override fun getValueDisplay(cell: Cell): String = cell.conversionSumFmt

    fun updateTitle(@TopAdsStatisticsType selectedStatisticType: Int) {
        title = when (selectedStatisticType) {
            TopAdsStatisticsType.SHOP_ADS -> R.string.title_top_ads_statistic_graph_convertion_store
            TopAdsStatisticsType.PRODUCT_ADS -> R.string.title_top_ads_statistic_graph_convertion_product
            TopAdsStatisticsType.ALL_ADS, TopAdsStatisticsType.HEADLINE_ADS -> R.string.title_top_ads_statistic_graph_convertion_all_ads
            else -> R.string.title_top_ads_statistic_graph_convertion_all_ads
        }
        if (isAdded) {
            content_title_graph.setText(titleGraph)
        }
    }

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticConversionFragment()
    }
}
