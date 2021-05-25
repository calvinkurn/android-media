package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.GradeBenefitPagerUiModel
import kotlinx.android.synthetic.main.item_pm_grade_benefit_pager.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class GradeBenefitPagerAdapter(
        private val pages: List<PMGradeWithBenefitsUiModel>
) : RecyclerView.Adapter<GradeBenefitPagerAdapter.GradeBenefitPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeBenefitPagerViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pm_grade_benefit_pager, parent, false)
        return GradeBenefitPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeBenefitPagerViewHolder, position: Int) {
        val page = pages[position]
        holder.bind(page)
    }

    override fun getItemCount(): Int = pages.size

    inner class GradeBenefitPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(page: PMGradeWithBenefitsUiModel) {
            setupBenefitListView(page)
        }

        private fun setupBenefitListView(page: PMGradeWithBenefitsUiModel) {
            with(itemView.rvPmGradeBenefitItem) {
                val benefitAdapter = GradeBenefitAdapter(page.benefits.orEmpty())
                layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
                adapter = benefitAdapter
            }
        }
    }
}