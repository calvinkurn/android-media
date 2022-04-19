package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmProUpsaleBenefitBinding
import com.tokopedia.power_merchant.subscribe.view.model.PMProBenefitUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PMProBenefitAdapter(
        private val items: List<PMProBenefitUiModel>
) : RecyclerView.Adapter<PMProBenefitAdapter.PotentialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PotentialViewHolder {
        val binding = ItemPmProUpsaleBenefitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PotentialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PotentialViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class PotentialViewHolder(private val binding: ItemPmProUpsaleBenefitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PMProBenefitUiModel) {
            with(binding) {
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