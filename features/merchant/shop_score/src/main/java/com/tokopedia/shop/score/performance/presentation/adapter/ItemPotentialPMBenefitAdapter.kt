package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPotentialPmBenefitBinding
import com.tokopedia.shop.score.performance.presentation.model.SectionRMPotentialPMBenefitUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPotentialPMBenefitAdapter(private val itemRegularMerchantListener: ItemRegularMerchantListener) :
    RecyclerView.Adapter<ItemPotentialPMBenefitAdapter.ItemPotentialPMBenefitViewHolder>() {

    private val itemRMPotentialPMBenefitList:
            MutableList<SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel> =
        mutableListOf()

    fun setPotentialPowerMerchantBenefit(
        itemRMPotentialPMBenefitList:
        List<SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel>
    ) {
        if (itemRMPotentialPMBenefitList.isNullOrEmpty()) return
        this.itemRMPotentialPMBenefitList.clear()
        this.itemRMPotentialPMBenefitList.addAll(itemRMPotentialPMBenefitList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemPotentialPMBenefitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_potential_pm_benefit, parent, false)
        return ItemPotentialPMBenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPotentialPMBenefitViewHolder, position: Int) {
        val data = itemRMPotentialPMBenefitList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemRMPotentialPMBenefitList.size

    inner class ItemPotentialPMBenefitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemPotentialPmBenefitBinding? by viewBinding()

        fun bind(data: SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel) {
            binding?.run {
                ivPotentialPmBenefit.loadImage(data.iconPotentialPMUrl)
                tvPotentialPmBenefit.text =
                    MethodChecker.fromHtml(data.titlePotentialPM?.let {
                        root.context.getString(it)
                    })

                root.setOnClickListener {
                    itemRegularMerchantListener.onRMSectionToPMPage()
                }
            }
        }
    }
}