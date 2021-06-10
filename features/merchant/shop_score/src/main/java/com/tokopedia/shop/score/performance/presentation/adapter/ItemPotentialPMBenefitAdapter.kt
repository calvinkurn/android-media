package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.SectionPotentialPMBenefitUiModel
import kotlinx.android.synthetic.main.item_potential_pm_benefit.view.*

class ItemPotentialPMBenefitAdapter : RecyclerView.Adapter<ItemPotentialPMBenefitAdapter.ItemPotentialPMBenefitViewHolder>() {

    private val potentialPMBenefitList: MutableList<SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel> = mutableListOf()

    fun setPotentialPowerMerchantBenefit(potentialPMBenefitList: List<SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel>) {
        if (potentialPMBenefitList.isNullOrEmpty()) return
        this.potentialPMBenefitList.clear()
        this.potentialPMBenefitList.addAll(potentialPMBenefitList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPotentialPMBenefitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_potential_pm_benefit, parent, false)
        return ItemPotentialPMBenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPotentialPMBenefitViewHolder, position: Int) {
        val data = potentialPMBenefitList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = potentialPMBenefitList.size

    inner class ItemPotentialPMBenefitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel) {
            with(itemView) {
                iv_potential_pm_benefit?.loadImage(data.iconPotentialPMUrl)
                tv_potential_pm_benefit?.text = MethodChecker.fromHtml(data.titlePotentialPM?.let { context.getString(it) })
            }
        }
    }
}