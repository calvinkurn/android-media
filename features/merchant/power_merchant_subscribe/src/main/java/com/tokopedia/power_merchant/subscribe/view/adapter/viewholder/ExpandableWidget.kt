package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.analytics.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmExpandableBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactory
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import com.tokopedia.utils.view.binding.viewBinding

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

    private val binding: WidgetPmExpandableBinding? by viewBinding()

    override fun bind(element: WidgetExpandableUiModel) {
        binding?.run {
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

    private fun setupPmSection() = binding?.run {
        viewPmBenefitSection.setOnClickListener {
            handleExpandableView()
        }
    }

    private fun setupPmProSection(element: WidgetExpandableUiModel) = binding?.run {
        viewPmProBenefitSection.show(element)
        viewPmProBenefitSection.setOnUpdateInfoCtaClickedListener {
            listener.showUpdateInfoBottomSheet(element.grade?.gradeName.orEmpty())
        }
    }

    private fun handleExpandableView() {
        binding?.run {
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

        binding?.rvPmExpandableItem?.run {
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