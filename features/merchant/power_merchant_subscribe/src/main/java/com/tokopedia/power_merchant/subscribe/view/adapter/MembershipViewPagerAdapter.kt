package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmMembershipPageBinding
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDataUiModel

/**
 * Created by @ilhamsuaib on 23/05/22.
 */

class MembershipViewPagerAdapter : RecyclerView.Adapter<MembershipViewPagerAdapter.ViewHolder>() {

    private val items = mutableListOf<MembershipDataUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPmMembershipPageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.getOrNull(position))
    }

    override fun getItemCount(): Int = items.size

    fun clearItems() {
        items.clear()
    }

    fun addItem(item: MembershipDataUiModel) {
        items.add(item)
    }

    fun getPmGradeList(): List<PMGradeWithBenefitsUiModel> {
        return items.map { it.gradeBenefit }
    }

    inner class ViewHolder(
        private val binding: ItemPmMembershipPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MembershipDataUiModel?) {
            if (item == null) return
            binding.run {
                val benefitAdapter = GradeBenefitAdapter(item.gradeBenefit.benefitList)
                rvPmGradeBenefit.layoutManager = object : LinearLayoutManager(root.context) {
                    override fun canScrollVertically(): Boolean = false
                }
                rvPmGradeBenefit.adapter = benefitAdapter
            }
        }
    }
}