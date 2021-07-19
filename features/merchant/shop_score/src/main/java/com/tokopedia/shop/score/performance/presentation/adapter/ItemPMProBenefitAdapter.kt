package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.SectionRMPotentialPMProUiModel
import kotlinx.android.synthetic.main.item_pm_pro_benefit.view.*

class ItemPMProBenefitAdapter: RecyclerView.Adapter<ItemPMProBenefitAdapter.ItemPMProBenefitViewHolder>() {

    private val potentialPMProBenefitList = mutableListOf<SectionRMPotentialPMProUiModel.ItemPMProBenefitUIModel>()

    fun setPotentialPMProBenefit(RMPotentialPMProBenefitList: List<SectionRMPotentialPMProUiModel.ItemPMProBenefitUIModel>) {
        if (RMPotentialPMProBenefitList.isNullOrEmpty()) return
        this.potentialPMProBenefitList.clear()
        this.potentialPMProBenefitList.addAll(RMPotentialPMProBenefitList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPMProBenefitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pm_pro_benefit, parent, false)
        return ItemPMProBenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPMProBenefitViewHolder, position: Int) {
        holder.bind(potentialPMProBenefitList[position])
    }

    override fun getItemCount(): Int = potentialPMProBenefitList.size

    class ItemPMProBenefitViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(data: SectionRMPotentialPMProUiModel.ItemPMProBenefitUIModel) {
            with(itemView) {
                iv_potential_pm_pro_benefit?.loadImage(data.iconPotentialPMProUrl)
                tv_potential_pm_pro_benefit?.text = MethodChecker.fromHtml(data.titlePotentialPMPro?.let { context.getString(it) })
            }
        }
    }
}