package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmGradeBenefitPagerBinding

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class GradeBenefitPagerAdapter(
    private val pages: List<PMGradeWithBenefitsUiModel>
) : RecyclerView.Adapter<GradeBenefitPagerAdapter.GradeBenefitPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeBenefitPagerViewHolder {
        val binding = ItemPmGradeBenefitPagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GradeBenefitPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeBenefitPagerViewHolder, position: Int) {
        val page = pages[position]
        holder.bind(page)
    }

    override fun getItemCount(): Int = pages.size

    inner class GradeBenefitPagerViewHolder(private val binding: ItemPmGradeBenefitPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(page: PMGradeWithBenefitsUiModel) {
            setupBenefitListView(page)
        }

        private fun setupBenefitListView(page: PMGradeWithBenefitsUiModel) {
            with(binding.rvPmGradeBenefitItem) {
                val benefitAdapter = GradeBenefitAdapter(page.benefits.orEmpty())
                layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
                adapter = benefitAdapter
            }
        }
    }
}