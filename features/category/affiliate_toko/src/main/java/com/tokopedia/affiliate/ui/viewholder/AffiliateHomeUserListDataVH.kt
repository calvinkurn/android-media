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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        const val CUMULATIVE_TOTAL_CLICK = "cumulativeTotalClick"
    }

    private val cardUserPerformance = itemView.findViewById<CardUnify2>(R.id.card_user_performance)
    private val tvValue = itemView.findViewById<Typography>(R.id.value)
    private val tvChangedValue = itemView.findViewById<Typography>(R.id.value_change_value)
    private val tvPerformanceType = itemView.findViewById<Typography>(R.id.performa_type)
    private val valueIcon = itemView.findViewById<IconUnify>(R.id.increase_value_icon)

    override fun bind(element: AffiliateUserPerformanceListModel?) {
        setData(element)
        setTrend(element)
        initClickListener(element)
    }

    private fun initClickListener(element: AffiliateUserPerformanceListModel?) {
        cardUserPerformance.setOnClickListener {
            onPerformaGridClick?.onInfoClick(
                element?.data?.metricTitle,
                element?.data?.tooltip?.description,
                element?.data?.tooltip?.metrics,
                element?.data?.metricType,
                element?.data?.tooltip?.ticker
            )
        }
        if (element?.data?.metricType == CUMULATIVE_TOTAL_CLICK) {
            element.totalClick
                ?.onEach {
                    tvValue.apply {
                        post {
                            text = it?.data?.metricValue?.toString() ?: element.data.metricValueFmt
                        }
                    }
                }
                ?.launchIn(CoroutineScope(Dispatchers.IO))

            element.totalClickItem
                ?.onEach {
                    tvChangedValue.apply {
                        post {
                            text =
                                it?.data?.metricDifferenceValue?.toString()
                                    ?: element.data.metricDifferenceValueFmt
                        }
                    }
                }
                ?.launchIn(CoroutineScope(Dispatchers.IO))
        }
    }

    private fun setData(element: AffiliateUserPerformanceListModel?) {
        tvPerformanceType.text = element?.data?.metricTitle
        tvValue.text = element?.data?.metricValueFmt
        tvChangedValue.text = element?.data?.metricDifferenceValueFmt
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
                    tvChangedValue.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    )
                    valueIcon.apply {
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
                    tvChangedValue.setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_RN500
                        )
                    )
                    valueIcon.apply {
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
        tvChangedValue.hide()
        valueIcon.hide()
    }

    private fun showTrend() {
        tvChangedValue.show()
        valueIcon.show()
    }
}
