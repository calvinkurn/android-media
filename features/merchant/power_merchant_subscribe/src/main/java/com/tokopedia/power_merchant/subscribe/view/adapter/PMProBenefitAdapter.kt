package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PMProBenefitUiModel
import kotlinx.android.synthetic.main.item_pm_pro_upsale_benefit.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PMProBenefitAdapter(
        private val items: List<PMProBenefitUiModel>
) : RecyclerView.Adapter<PMProBenefitAdapter.PotentialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PotentialViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pm_pro_upsale_benefit, parent, false)
        return PotentialViewHolder(view)
    }

    override fun onBindViewHolder(holder: PotentialViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class PotentialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: PMProBenefitUiModel) {
            with(itemView) {
                if (item.imgUrl.isBlank()) {
                    icPmPotentialItem.setImage(item.icon)
                } else {
                    icPmPotentialItem.loadImage(item.imgUrl)
                }
                icPmPotentialItem.setBackgroundResource(R.drawable.bg_pm_oval_green)
                tvPmPotentialItemDescription.text = item.description.parseAsHtml()
            }
        }
    }
}