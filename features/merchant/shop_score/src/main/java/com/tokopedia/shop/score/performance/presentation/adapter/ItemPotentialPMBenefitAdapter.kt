package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.SectionRMPotentialPMBenefitUiModel
import kotlinx.android.synthetic.main.item_potential_pm_benefit.view.*

class ItemPotentialPMBenefitAdapter(private val itemRegularMerchantListener: ItemRegularMerchantListener) : RecyclerView.Adapter<ItemPotentialPMBenefitAdapter.ItemPotentialPMBenefitViewHolder>() {

    private val itemRMPotentialPMBenefitList: MutableList<SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel> = mutableListOf()

    fun setPotentialPowerMerchantBenefit(itemRMPotentialPMBenefitList: List<SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel>) {
        if (itemRMPotentialPMBenefitList.isNullOrEmpty()) return
        this.itemRMPotentialPMBenefitList.clear()
        this.itemRMPotentialPMBenefitList.addAll(itemRMPotentialPMBenefitList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPotentialPMBenefitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_potential_pm_benefit, parent, false)
        return ItemPotentialPMBenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPotentialPMBenefitViewHolder, position: Int) {
        val data = itemRMPotentialPMBenefitList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemRMPotentialPMBenefitList.size

    inner class ItemPotentialPMBenefitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel) {
            with(itemView) {
                iv_potential_pm_benefit?.loadImage(data.iconPotentialPMUrl)
                tv_potential_pm_benefit?.text = MethodChecker.fromHtml(data.titlePotentialPM?.let { context.getString(it) })

                setOnClickListener {
                    itemRegularMerchantListener.onRMSectionToPMPage()
                }
            }
        }
    }
}