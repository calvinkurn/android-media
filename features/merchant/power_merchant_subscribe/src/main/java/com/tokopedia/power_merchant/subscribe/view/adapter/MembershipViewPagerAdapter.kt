package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmMembershipPageBinding
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDataUiModel
import com.tokopedia.power_merchant.subscribe.view.viewcomponent.MembershipPmCheckListView
import com.tokopedia.power_merchant.subscribe.view.viewcomponent.MembershipPmProCheckListView

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
                        membershipChecklistPmView.show(getPmChecklistData(item))
                    } else { //PM Pro
                        tvPmMembershipPassingGrade.gone()
                        membershipChecklistPmView.gone()
                        membershipChecklistPmProView.visible()
                        membershipChecklistPmProView.show(getPmProChecklistData(item))
                    }
                } else {
                    /*if (page is PMGradeWithBenefitsUiModel.PMProAdvance && data.currentShopLevel <= PMConstant.ShopLevel.TWO) {
                        showTargetAchievement()
                    } else {
                        showTickerBasedOnLevel(page)
                    }*/
                }
            }
        }

        private fun getPmProChecklistData(item: MembershipDataUiModel): MembershipPmProCheckListView.Data {
            return MembershipPmProCheckListView.Data(
                orderThreshold = item.orderThreshold,
                netIncomeThreshold = item.netIncomeThreshold,
                totalOrder = item.totalOrder,
                netIncome = item.netIncome
            )
        }

        private fun getPmChecklistData(item: MembershipDataUiModel): MembershipPmCheckListView.Data {
            return MembershipPmCheckListView.Data(
                shopScoreThreshold = item.shopScoreThreshold,
                orderThreshold = item.orderThreshold,
                netIncomeThreshold = item.netIncomeThreshold,
                shopScore = item.shopScore,
                totalOrder = item.totalOrder,
                netIncome = item.netIncome
            )
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