package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateProductCardMetricsModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit

class AffiliateProductMetricVH(itemView: View) :
    AbstractViewHolder<AffiliateProductCardMetricsModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_product_metrics_layout_item
        private const val ZERO = 0
        private const val R_90 = 90f
        private const val R_270 = 90f
        private const val PRODUCT_ACTIVE = 1
        private const val TOTAL_CLICK = "totalClickPerItem"
        private const val DELAY_TIME: Long = 500
    }

    private val valueChangeShimmer = itemView.findViewById<LoaderUnify>(R.id.value_change_shimmer)
    private val metricValue = itemView.findViewById<Typography>(R.id.metric_value)
    private val metricTitle = itemView.findViewById<Typography>(R.id.metric_title)
    private val trendIcon = itemView.findViewById<IconUnify>(R.id.pendapatan_icon)

    override fun bind(element: AffiliateProductCardMetricsModel?) {
        setCommisionData(element)
    }

    private fun setCommisionData(
        element: AffiliateProductCardMetricsModel?
    ) {
        val isActive = element?.product?.status == PRODUCT_ACTIVE
        val valueColor = MethodChecker.getColor(
            itemView.context,
            if (isActive) {
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            } else {
                com.tokopedia.unifyprinciples.R.color.Unify_NN400
            }
        )
        metricValue?.setTextColor(valueColor)
        metricTitle?.text = element?.metrics?.metricTitle ?: ""
        metricValue?.text = element?.metrics?.metricValueFmt ?: "0"
        val metricIntValue: Double? = element?.metrics?.metricDifferenceValue?.toDouble()
        if (element?.metrics?.metricType == TOTAL_CLICK) {
            element.affiliateSSEAdpTotalClickItem.onEach {
                if (element.product.itemID == it?.data?.itemId?.toString()) {
                    valueChangeShimmer.post {
                        toggleShimmer(true)
                    }
                    metricValue.apply {
                        postDelayed({
                            text =
                                it?.data?.metricValue?.toString() ?: element.metrics.metricValueFmt
                                    ?: "0"
                            setTrend(it?.data?.metricValue?.toDouble(), isActive)
                            toggleShimmer(false)
                        }, TimeUnit.MILLISECONDS.toMillis(DELAY_TIME))
                    }
                }
            }.launchIn(CoroutineScope(Dispatchers.IO))
        }
        setTrend(metricIntValue, isActive)
    }

    private fun setTrend(metricIntValue: Double?, isActive: Boolean) {
        val disabledColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400
        )
        val greenColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
        val redColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_RN500
        )
        metricIntValue?.let {
            when {
                it > ZERO -> {
                    trendIcon?.apply {
                        show()
                        setImage(
                            newLightEnable = if (isActive) greenColor else disabledColor
                        )
                        rotation = R_90
                    }
                }
                it < ZERO -> {
                    trendIcon?.apply {
                        show()
                        setImage(
                            newLightEnable = if (isActive) redColor else disabledColor
                        )
                        rotation = R_270
                    }
                }
                else -> {
                    trendIcon?.hide()
                }
            }
        }
    }

    private fun toggleShimmer(isVisible: Boolean) {
        valueChangeShimmer.isVisible = isVisible
        metricValue.isVisible = !isVisible
        trendIcon.isVisible = !isVisible
    }
}
