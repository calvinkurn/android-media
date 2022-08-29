package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
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

            setupChecklist(item)
            setupBenefitList(item)
        }

        private fun setupChecklist(item: MembershipDataUiModel) {
            binding.run {
                val page = item.gradeBenefit
                if (page.isTabActive) {
                    if (page is PMGradeWithBenefitsUiModel.PM) { //PM
                        tvPmMembershipPassingGrade.gone()
                        membershipChecklistPmProView.gone()
                        membershipChecklistPmView.visible()
                        membershipChecklistPmView.show(item)
                    } else { //PM Pro
                        tvPmMembershipPassingGrade.gone()
                        membershipChecklistPmView.gone()
                        membershipChecklistPmProView.visible()
                        membershipChecklistPmProView.show(item)
                    }
                } else {
                    if (page is PMGradeWithBenefitsUiModel.PM) {
                        tvPmMembershipPassingGrade.visible()
                        tvPmMembershipPassingGrade.text = getPassingGradeInfo()
                        membershipChecklistPmProView.gone()
                        membershipChecklistPmView.gone()
                    } else {
                        val currentShopLevel = items.firstOrNull { it.gradeBenefit.isTabActive }
                            ?.gradeBenefit?.getShopLevel().orZero()
                        if (page.getShopLevel() < currentShopLevel) {
                            tvPmMembershipPassingGrade.visible()
                            tvPmMembershipPassingGrade.text = getPassingGradeInfo()
                            membershipChecklistPmView.gone()
                            membershipChecklistPmProView.gone()
                        } else {
                            tvPmMembershipPassingGrade.gone()
                            membershipChecklistPmView.gone()
                            membershipChecklistPmProView.visible()
                            membershipChecklistPmProView.show(item)
                        }
                    }
                }
            }
        }

        private fun getPassingGradeInfo(): String {
            val gradeName = items.firstOrNull { it.gradeBenefit.isTabActive }
                ?.gradeBenefit?.tabLabel.orEmpty()
            return itemView.context.getString(R.string.pm_membership_passing_grade_info, gradeName)
        }

        private fun setupBenefitList(item: MembershipDataUiModel) {
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