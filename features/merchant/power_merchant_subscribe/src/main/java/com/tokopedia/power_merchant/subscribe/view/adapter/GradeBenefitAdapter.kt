package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.GradeBenefitItemUiModel
import kotlinx.android.synthetic.main.item_pm_grade_benefit.view.*

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class GradeBenefitAdapter(
        private val benefits: List<GradeBenefitItemUiModel>
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
        fun bind(benefit: GradeBenefitItemUiModel) {
            with(itemView) {
                icPmBenefitItem.loadImageWithoutPlaceholder(benefit.iconUrl)
                tvPmBenefitItem.text = benefit.description.parseAsHtml()
            }
        }
    }
}