package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.item_pm_next_shop_grade_benefit.view.*

/**
 * Created By @ilhamsuaib on 17/03/21
 */

class NextShopGradeBenefitAdapter(
        private val items: List<String>
) : RecyclerView.Adapter<NextShopGradeBenefitAdapter.NextGradeBenefitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextGradeBenefitViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pm_next_shop_grade_benefit, parent, false)
        return NextGradeBenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: NextGradeBenefitViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class NextGradeBenefitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: String) {
            with(itemView) {
                tvPmNextGrade.text = item
                if (items.size.minus(1) == adapterPosition) {
                    horLinePmNextGrade.gone()
                }
            }
        }
    }
}