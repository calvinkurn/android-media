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
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class AffiliateTrafficAttributionItemVH(itemView: View) :
    AbstractViewHolder<AffiliateTrafficAttributionModel>(itemView) {

    companion object {
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
        setTrend(element)
    }

    private fun setTrend(element: AffiliateTrafficAttributionModel?) {
        val metricIntValue = element?.metrics?.metricDifferenceValue?.toDouble()
        if (metricIntValue != null) {
            when{
                metricIntValue == 0.0 ->{
                    hideTrend()
                }
                metricIntValue > 0 ->{
                    showTrend()
                    itemView.findViewById<Typography>(R.id.amount).setTextColor(
                        ContextCompat.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                    itemView.findViewById<IconUnify>(R.id.trend_icon).apply {
                        setImage(
                            newLightEnable = MethodChecker.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                        )
                        rotation = 90f
                    }
                }
                metricIntValue < 0 ->{
                    showTrend()
                    itemView.findViewById<Typography>(R.id.amount).setTextColor(
                        MethodChecker.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_RN500))
                    itemView.findViewById<IconUnify>(R.id.trend_icon).apply {
                        setImage(
                            newLightEnable = MethodChecker.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                        )
                        rotation = 270f
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
