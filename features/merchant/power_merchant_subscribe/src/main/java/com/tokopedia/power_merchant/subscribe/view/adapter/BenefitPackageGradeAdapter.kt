package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemBenefitPackageSectionPmProBinding
import com.tokopedia.power_merchant.subscribe.view.model.BenefitItem
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageGradeUiModel

class BenefitPackageGradeAdapter :
    RecyclerView.Adapter<BenefitPackageGradeAdapter.BenefitPackageGradeViewHolder>() {

    private val benefitPackageDataList = mutableListOf<BenefitPackageGradeUiModel>()

    fun setBenefitPackageList(benefitPackageDataList: List<BenefitPackageGradeUiModel>?) {
        if (benefitPackageDataList.isNullOrEmpty()) return
        this.benefitPackageDataList.clear()
        this.benefitPackageDataList.addAll(benefitPackageDataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BenefitPackageGradeViewHolder {
        val binding = ItemBenefitPackageSectionPmProBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BenefitPackageGradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BenefitPackageGradeViewHolder, position: Int) {
        val data = benefitPackageDataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = benefitPackageDataList.size

    inner class BenefitPackageGradeViewHolder(
        private val binding: ItemBenefitPackageSectionPmProBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var benefitPackageItemAdapter: BenefitPackageItemAdapter? = null

        fun bind(data: BenefitPackageGradeUiModel) {
            with(binding) {
                bgBenefitPackage.loadImage(data.backgroundUrl)
                imgBenefitPackageSection.loadImage(data.iconBenefitUrl)
                tvTitleBenefitPackageSection.text = if (data.isDowngrade) {
                    root.context.getString(
                        R.string.pm_title_benefit_package_downgrade_section,
                        data.gradeName.asCamelCase()
                    )
                } else {
                    root.context.getString(
                        R.string.pm_title_benefit_package_upgrade_section,
                        data.gradeName.asCamelCase()
                    )
                }
                tvDescBenefitPackageSection.text = MethodChecker.fromHtml(data.descBenefit)
            }
            setItemBenefitAdapter(data.benefitItemList)
        }

        private fun setItemBenefitAdapter(benefitItemList: List<BenefitItem>) {
            benefitPackageItemAdapter = BenefitPackageItemAdapter()
            with(binding) {
                rvBenefitPackageItem.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = benefitPackageItemAdapter
                }
                benefitPackageItemAdapter?.setBenefitItemList(benefitItemList)
            }
        }
    }
}