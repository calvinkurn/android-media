package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateUserPerformanceListModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography

class AffiliateHomeUserListDataVH(
    itemView: View,
    private val onPerformaGridClick: AffiliatePerformaClickInterfaces?
) : AbstractViewHolder<AffiliateUserPerformanceListModel>(itemView) {


    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_performa_item
        const val ROTATION_90 = 90f
        const val ROTATION_270 = 270f
    }

    override fun bind(element: AffiliateUserPerformanceListModel?) {
        setData(element)
        setTrend(element)
        initClickListener(element)
    }

    private fun initClickListener(element: AffiliateUserPerformanceListModel?) {
        itemView.findViewById<CardUnify2>(R.id.card_user_performance).setOnClickListener {
            onPerformaGridClick?.onInfoClick(
                element?.data?.metricTitle,
                element?.data?.tooltip?.description,
                element?.data?.tooltip?.metrics,
                element?.data?.metricType,
                element?.data?.tooltip?.ticker
            )
        }
    }

    private fun setData(element: AffiliateUserPerformanceListModel?) {
        itemView.findViewById<Typography>(R.id.performa_type).text = element?.data?.metricTitle
        itemView.findViewById<Typography>(R.id.value).text = element?.data?.metricValueFmt
        itemView.findViewById<Typography>(R.id.value_change_value).text =
            element?.data?.metricDifferenceValueFmt
    }

    private fun setTrend(element: AffiliateUserPerformanceListModel?) {
        val metricIntValue = element?.data?.metricDifferenceValue?.toDouble()
        if (metricIntValue != null) {
            when {
                metricIntValue == 0.0 -> {
                    hideTrend()
                }
                metricIntValue > 0 -> {
                    showTrend()
                    itemView.findViewById<Typography>(R.id.value_change_value).setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    )
                    itemView.findViewById<IconUnify>(R.id.increase_value_icon).apply {
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_GN500
                            )
                        )
                        rotation = ROTATION_90
                    }
                }
                metricIntValue < 0 -> {
                    showTrend()
                    itemView.findViewById<Typography>(R.id.value_change_value).setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_RN500
                        )
                    )
                    itemView.findViewById<IconUnify>(R.id.increase_value_icon).apply {
                        setImage(
                            newLightEnable = MethodChecker.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_RN500
                            )
                        )
                        rotation = ROTATION_270
                    }
                }
            }
        }
    }

    private fun hideTrend() {
        itemView.findViewById<Typography>(R.id.value_change_value).hide()
        itemView.findViewById<IconUnify>(R.id.increase_value_icon).hide()
    }

    private fun showTrend() {
        itemView.findViewById<Typography>(R.id.value_change_value).show()
        itemView.findViewById<IconUnify>(R.id.increase_value_icon).show()
    }
}
