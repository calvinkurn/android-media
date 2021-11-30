package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitUiModel
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmGradeBenefitBinding

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class GradeBenefitAdapter(
    private val benefits: List<PMGradeBenefitUiModel>
) : RecyclerView.Adapter<GradeBenefitAdapter.GradeBenefitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeBenefitViewHolder {
        val binding = ItemPmGradeBenefitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return GradeBenefitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeBenefitViewHolder, position: Int) {
        val benefit = benefits[position]
        holder.bind(benefit)
    }

    override fun getItemCount(): Int = benefits.size

    inner class GradeBenefitViewHolder(private val binding: ItemPmGradeBenefitBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(benefit: PMGradeBenefitUiModel) {
            with(binding) {
                val drawableResIcon = benefit.drawableResIcon
                if (drawableResIcon != null) {
                    icPmBenefitItem.loadImage(drawableResIcon)
                } else {
                    benefit.iconUrl?.let {
                        icPmBenefitItem.loadImage(it)
                    }
                }
                tvPmBenefitItem.text = benefit.benefitName.parseAsHtml()
            }
        }
    }
}