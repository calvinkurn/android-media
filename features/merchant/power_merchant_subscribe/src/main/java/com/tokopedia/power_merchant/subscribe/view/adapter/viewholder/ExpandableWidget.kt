package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
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
    private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetExpandableUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_expandable
    }

    private val binding: WidgetPmExpandableBinding? by viewBinding()

    override fun bind(element: WidgetExpandableUiModel) {
        binding?.run {
            viewPmBenefitSection.setOnExpandedChanged(true)
            setupExpandableItem(element)
            setupPmSection()
            val gradeName = element.grade?.gradeName ?: PMConstant.ShopGrade.PM
            setupInfoIncreaseBenefit(gradeName, element.isPmActive())
        }
    }

    private fun setupInfoIncreaseBenefit(grade: String, isPmActive: Boolean) = binding?.run {
        if (grade != PMConstant.ShopGrade.PM && isPmActive) {
            if (grade == PMConstant.ShopGrade.PRO_ULTIMATE) {
                val textColor = PMCommonUtils.getHexColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                textTitle.text = getString(R.string.pm_info_max_benefit_title)
                textDescription.text = getString(
                    R.string.pm_info_max_benefit_desc, textColor
                ).parseAsHtml()
            } else {
                val textColor = PMCommonUtils.getHexColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                textTitle.text = getString(R.string.pm_info_benefit_increase_title)
                textDescription.text = getString(
                    R.string.pm_info_benefit_increase_desc, textColor
                ).parseAsHtml()
            }
            cardInfoBenefit.show()
        } else {
            cardInfoBenefit.hide()
        }
    }

    private fun setupPmSection() = binding?.run {
        viewPmBenefitSection.setOnClickListener {
            handleExpandableView()
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

    @SuppressLint("NotifyDataSetChanged")
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
}