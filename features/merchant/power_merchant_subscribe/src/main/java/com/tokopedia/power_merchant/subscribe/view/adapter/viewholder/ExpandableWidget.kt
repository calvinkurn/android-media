package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactory
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import kotlinx.android.synthetic.main.widget_pm_expandable.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableWidget(
        itemView: View,
        private val listener: Listener
) : AbstractViewHolder<WidgetExpandableUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_expandable
    }

    override fun bind(element: WidgetExpandableUiModel) {
        with(itemView) {
            if (element.isPmPro()) {
                viewPmProBenefitSection.visible()
                viewPmBenefitSection.gone()
            } else {
                viewPmProBenefitSection.gone()
                viewPmBenefitSection.visible()
            }

            setupExpandableItem(element)
            setupPmSection(element)
            setupPmProSection(element)
        }
    }

    private fun setupPmSection(element: WidgetExpandableUiModel) = with(itemView) {
        viewPmBenefitSection.setOnClickListener {
            handleExpandableView()
        }
    }

    private fun setupPmProSection(element: WidgetExpandableUiModel) = with(itemView) {
        viewPmProBenefitSection.show(element)
        viewPmProBenefitSection.setOnClickListener {
            handleExpandableView()
        }
        viewPmProBenefitSection.setOnUpdateInfoCtaClickedListener {
            listener.showUpdateInfoBottomSheet()
        }
    }

    private fun handleExpandableView() {
        with(itemView) {
            val shouldExpanded = rvPmExpandableItem.visibility != View.VISIBLE
            viewPmBenefitSection.setOnExpandedChanged(shouldExpanded)
            viewPmProBenefitSection.setOnExpandedChanged(shouldExpanded)
            if (shouldExpanded) {
                rvPmExpandableItem.visible()
            } else {
                rvPmExpandableItem.gone()
            }
        }
    }

    private fun setupExpandableItem(element: WidgetExpandableUiModel) {
        val expandableAdapter = BaseListAdapter<Visitable<ExpandableAdapterFactory>, ExpandableAdapterFactoryImpl>(ExpandableAdapterFactoryImpl())

        with(itemView.rvPmExpandableItem) {
            isVisible = element.items.isNotEmpty()
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = expandableAdapter
        }

        expandableAdapter.data.clear()
        expandableAdapter.data.addAll(element.items)
        expandableAdapter.notifyDataSetChanged()
    }

    interface Listener {
        fun showUpdateInfoBottomSheet()
    }
}