package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateProductCardMetricsModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class AffiliateProductMetricVH(itemView: View)
    : AbstractViewHolder<AffiliateProductCardMetricsModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_product_metrics_layout_item
        private const val ZERO = 0
        private const val R_90 = 90f
        private const val R_270 = 90f
    }

    override fun bind(element: AffiliateProductCardMetricsModel?) {
        setCommisionData(element?.metrics)
    }
    private fun setCommisionData(metrics: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item.Metric?) {
        itemView.findViewById<Typography>(R.id.metric_title)?.text = metrics?.metricTitle ?: ""
        itemView.findViewById<Typography>(R.id.metric_value)?.text = metrics?.metricValueFmt ?: "0"
        val metricIntValue: Double? = metrics?.metricDifferenceValue?.toDouble()
        setTrend(itemView.findViewById(R.id.pendapatan_icon), metricIntValue)
    }

    private fun setTrend(view: IconUnify?, metricIntValue: Double?) {
        metricIntValue?.let {
            when {
                it > ZERO -> {
                    view?.apply {
                        show()
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_GN500
                            )
                        )
                        rotation = R_90
                    }
                }
                it < ZERO -> {
                    view?.apply {
                        show()
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_RN500
                            )
                        )
                        rotation = R_270
                    }
                }
                else -> {
                    view?.hide()
                }
            }
        }
    }
}
