package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPmBenefitBinding
import com.tokopedia.shop.score.performance.presentation.model.ItemParentBenefitUiModel
import com.tokopedia.utils.view.binding.viewBinding


class ItemPMBenefitAdapter :
    RecyclerView.Adapter<ItemPMBenefitAdapter.ItemPMProBenefitViewHolder>() {

    private val itemBenefitList = mutableListOf<ItemParentBenefitUiModel>()

    fun setBenefits(potentialParentBenefitList: List<ItemParentBenefitUiModel>) {
        if (potentialParentBenefitList.isNullOrEmpty()) return
        this.itemBenefitList.clear()
        this.itemBenefitList.addAll(potentialParentBenefitList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPMProBenefitViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pm_benefit, parent, false)
        return ItemPMProBenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPMProBenefitViewHolder, position: Int) {
        holder.bind(itemBenefitList[position])
    }

    override fun getItemCount(): Int = itemBenefitList.size

    class ItemPMProBenefitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemPmBenefitBinding? by viewBinding()
        fun bind(data: ItemParentBenefitUiModel) {
            binding?.run {
                ivPotentialPmBenefit.loadImage(data.iconUrl)
                tvPotentialPmBenefit.text = MethodChecker.fromHtml(
                    data.titleResources?.let { root.context.getString(it) }
                )
            }
        }
    }
}