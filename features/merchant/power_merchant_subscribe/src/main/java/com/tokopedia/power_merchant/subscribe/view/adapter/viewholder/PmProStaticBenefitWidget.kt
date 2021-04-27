package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitUiModel
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.GradeBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPmProStaticBenefit

/**
 * Created By @ilhamsuaib on 26/04/21
 */

class PmProStaticBenefitWidget(itemView: View) : AbstractViewHolder<WidgetPmProStaticBenefit>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_pro_static_benefit
    }

    private val benefits by getBenefitList()
    private val benefitAdapter by lazy { GradeBenefitAdapter(benefits) }
    private val rvBenefit by lazy { itemView.findViewById<RecyclerView>(R.id.rvPmProStaticBenefit) }

    override fun bind(element: WidgetPmProStaticBenefit) {
        setupBenefitList()
    }

    private fun setupBenefitList() {
        rvBenefit.layoutManager = object : LinearLayoutManager(itemView.context) {
            override fun canScrollVertically(): Boolean = false
        }
        rvBenefit.adapter = benefitAdapter
    }

    private fun getBenefitList(): Lazy<List<PMGradeBenefitUiModel>> {
        return lazy {
            listOf(
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_free_shipping_description)
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_search_ranking_description)
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_exclusive_badge_pm_pro_description)
                    ),
                    PMGradeBenefitUiModel(
                            benefitName = getString(R.string.pm_growing_benefit_description)
                    )
            )
        }
    }
}