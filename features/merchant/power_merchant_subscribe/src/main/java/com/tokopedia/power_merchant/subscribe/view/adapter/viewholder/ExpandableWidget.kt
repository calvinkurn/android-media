package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactory
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import kotlinx.android.synthetic.main.widget_pm_expandable.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableWidget(
    itemView: View,
    private val listener: Listener,
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetExpandableUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_expandable
    }

    override fun bind(element: WidgetExpandableUiModel) {
        with(itemView) {
            if (element.isPmPro()) {
                viewPmProBenefitSection.visible()
            } else {
                viewPmProBenefitSection.gone()
            }
            viewPmBenefitSection.setOnExpandedChanged(true)

            setupExpandableItem(element)
            setupPmSection()
            setupPmProSection(element)
        }
    }

    private fun setupPmSection() = with(itemView) {
        viewPmBenefitSection.setOnClickListener {
            handleExpandableView()
        }
    }

    private fun setupPmProSection(element: WidgetExpandableUiModel) = with(itemView) {
        viewPmProBenefitSection.show(element)
        viewPmProBenefitSection.setOnUpdateInfoCtaClickedListener {
            listener.showUpdateInfoBottomSheet(element.grade?.gradeName.orEmpty())
        }
    }

    private fun handleExpandableView() {
        with(itemView) {
            val shouldExpanded = rvPmExpandableItem.visibility != View.VISIBLE
            viewPmBenefitSection.setOnExpandedChanged(shouldExpanded)
            if (shouldExpanded) {
                rvPmExpandableItem.visible()
            } else {
                rvPmExpandableItem.gone()
            }
        }
    }

    private fun setupExpandableItem(element: WidgetExpandableUiModel) {
        val expandableAdapter =
            BaseListAdapter<Visitable<ExpandableAdapterFactory>, ExpandableAdapterFactoryImpl>(
                ExpandableAdapterFactoryImpl(powerMerchantTracking)
            )

        with(itemView.rvPmExpandableItem) {
            isVisible = element.items.isNotEmpty()
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = expandableAdapter
        }

        if (expandableAdapter.data.isEmpty()) {
            expandableAdapter.data.addAll(element.items)
            expandableAdapter.notifyDataSetChanged()
        }
    }

    interface Listener {
        fun showUpdateInfoBottomSheet(gradeName: String)
        fun onMembershipStatusPmProClickListener()
    }
}