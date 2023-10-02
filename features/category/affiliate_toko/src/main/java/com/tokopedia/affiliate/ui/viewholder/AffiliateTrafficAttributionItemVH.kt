package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficAttributionModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_BORDER
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class AffiliateTrafficAttributionItemVH(itemView: View) :
    AbstractViewHolder<AffiliateTrafficAttributionModel>(itemView) {

    companion object {
        private const val ZERO = 0
        private const val ZERO_POINT_ZERO = 0.0
        private const val ROTATION_90 = 90f
        private const val ROTATION_270 = 270f

        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_traffic_attribution_item
    }

    override fun bind(element: AffiliateTrafficAttributionModel?) {
        itemView.findViewById<Typography>(R.id.text_title)?.apply {
            text = element?.metrics?.metricTitle
        }
        itemView.findViewById<Typography>(R.id.text)?.apply {
            text = element?.metrics?.metricValueFmt
        }
        itemView.findViewById<Typography>(R.id.text_sub_title)?.apply {
            isVisible = element?.metrics?.tooltip?.description?.isEmpty() == false
            text = element?.metrics?.tooltip?.description
        }
        itemView.findViewById<Typography>(R.id.amount)?.apply {
            text = element?.metrics?.metricDifferenceValueFmt
        }
        itemView.findViewById<CardUnify2>(R.id.extra_card)?.apply {
            isVisible = element?.metrics?.tooltip?.metrics?.isNotEmpty() == true
            cardType = TYPE_BORDER
        }

        element?.metrics?.tooltip?.metrics?.forEach { subSubMetrics ->
            if (subSubMetrics?.metricType == "tokopediaCommission") {
                itemView.findViewById<Typography>(R.id.extra_text_one)?.apply {
                    isVisible = true
                    text = subSubMetrics.metricTitle
                }
                itemView.findViewById<Typography>(R.id.extra_text_amount_one).apply {
                    isVisible = true
                    text = subSubMetrics.metricValueFmt
                }
            }
            if (subSubMetrics?.metricType == "totalAmountSsa") {
                itemView.findViewById<Typography>(R.id.extra_text_two)?.apply {
                    isVisible = true
                    text = subSubMetrics.metricTitle
                }
                itemView.findViewById<Typography>(R.id.extra_text_amount_two).apply {
                    isVisible = true
                    text = subSubMetrics.metricValueFmt
                }
            }
        }
        setDivider(element)
        setTrend(element)
    }

    private fun setDivider(element: AffiliateTrafficAttributionModel?) {
        itemView.findViewById<DividerUnify>(R.id.dividerUnify)?.apply {
            if (element?.metrics?.isLastItem == false) {
                isVisible = true
            } else {
                invisible()
            }
        }
    }

    private fun setTrend(element: AffiliateTrafficAttributionModel?) {
        val metricIntValue = element?.metrics?.metricDifferenceValue?.toDouble()
        if (metricIntValue != null) {
            when {
                metricIntValue == ZERO_POINT_ZERO -> hideTrend()

                metricIntValue > ZERO -> {
                    showTrend()
                    itemView.findViewById<Typography>(R.id.amount).setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            unifyprinciplesR.color.Unify_GN500
                        )
                    )
                    itemView.findViewById<IconUnify>(R.id.trend_icon).apply {
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                unifyprinciplesR.color.Unify_GN500
                            )
                        )
                        rotation = ROTATION_90
                    }
                }

                metricIntValue < ZERO -> {
                    showTrend()
                    itemView.findViewById<Typography>(R.id.amount).setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            unifyprinciplesR.color.Unify_RN500
                        )
                    )
                    itemView.findViewById<IconUnify>(R.id.trend_icon).apply {
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                unifyprinciplesR.color.Unify_RN500
                            )
                        )
                        rotation = ROTATION_270
                    }
                }
            }
        }
    }

    private fun hideTrend() {
        itemView.findViewById<Typography>(R.id.amount).hide()
        itemView.findViewById<IconUnify>(R.id.trend_icon).hide()
    }

    private fun showTrend() {
        itemView.findViewById<Typography>(R.id.amount).show()
        itemView.findViewById<IconUnify>(R.id.trend_icon).show()
    }
}
