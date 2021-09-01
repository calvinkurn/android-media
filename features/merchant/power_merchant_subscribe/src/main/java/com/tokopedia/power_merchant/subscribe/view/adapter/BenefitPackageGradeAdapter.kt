package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.BenefitItem
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageDataUiModel
import kotlinx.android.synthetic.main.item_benefit_package_section_pm_pro.view.*

class BenefitPackageGradeAdapter :
    RecyclerView.Adapter<BenefitPackageGradeAdapter.BenefitPackageGradeViewHolder>() {

    private val benefitPackageDataList = mutableListOf<BenefitPackageDataUiModel>()

    private fun setBenefitPackageList(benefitPackageDataList: List<BenefitPackageDataUiModel>) {
        if (benefitPackageDataList.isNullOrEmpty()) return
        this.benefitPackageDataList.clear()
        this.benefitPackageDataList.addAll(benefitPackageDataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BenefitPackageGradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_benefit_package_section_pm_pro, parent, false)
        return BenefitPackageGradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BenefitPackageGradeViewHolder, position: Int) {
        val data = benefitPackageDataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = benefitPackageDataList.size

    inner class BenefitPackageGradeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var benefitPackageItemAdapter: BenefitPackageItemAdapter? = null

        fun bind(data: BenefitPackageDataUiModel) {
            with(itemView) {
                imgBenefitPackageSection?.loadImage(data.iconBenefitUrl)
                tvTitleBenefitPackageSection?.text = context.getString(
                    R.string.pm_title_benefit_package_section,
                    data.gradeName.asCamelCase()
                )
                tvDescBenefitPackageSection?.text = MethodChecker.fromHtml(data.descBenefit)
            }
            setItemBenefitAdapter(data.benefitItemList)
        }

        private fun setItemBenefitAdapter(benefitItemList: List<BenefitItem>) {
            benefitPackageItemAdapter = BenefitPackageItemAdapter()
            with(itemView) {
                rvBenefitPackageItem?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = benefitPackageItemAdapter
                }
                benefitPackageItemAdapter?.setBenefitItemList(benefitItemList)
            }
        }
    }
}