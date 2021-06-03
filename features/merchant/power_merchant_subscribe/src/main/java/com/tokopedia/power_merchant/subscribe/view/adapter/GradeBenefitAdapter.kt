package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitUiModel
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.item_pm_grade_benefit.view.*

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class GradeBenefitAdapter(
        private val benefits: List<PMGradeBenefitUiModel>
) : RecyclerView.Adapter<GradeBenefitAdapter.GradeBenefitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeBenefitViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pm_grade_benefit, parent, false)
        return GradeBenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeBenefitViewHolder, position: Int) {
        val benefit = benefits[position]
        holder.bind(benefit)
    }

    override fun getItemCount(): Int = benefits.size

    inner class GradeBenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(benefit: PMGradeBenefitUiModel) {
            with(itemView) {
                val drawableResIcon = benefit.drawableResIcon
                if (drawableResIcon != null) {
                    icPmBenefitItem.loadImageWithoutPlaceholder(drawableResIcon)
                } else {
                    benefit.iconUrl?.let {
                        icPmBenefitItem.loadImageWithoutPlaceholder(it)
                    }
                }
                tvPmBenefitItem.text = benefit.benefitName.parseAsHtml()
            }
        }
    }
}