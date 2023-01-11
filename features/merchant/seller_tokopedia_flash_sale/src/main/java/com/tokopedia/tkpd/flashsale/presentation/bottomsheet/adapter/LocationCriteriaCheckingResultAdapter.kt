package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemLocationCriteriaResultBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.utils.currency.CurrencyFormatUtil

class LocationCriteriaCheckingResultAdapter: RecyclerView.Adapter<LocationCriteriaCheckingResultAdapter.CriteriaViewHolder>() {

    private var data: List<CriteriaCheckingResult.LocationCheckingResult> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemLocationCriteriaResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<CriteriaCheckingResult.LocationCheckingResult>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class CriteriaViewHolder(private val binding: StfsItemLocationCriteriaResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconExpand.setOnClickListener {
                binding.layoutContent.root.apply {
                    isVisible = !isVisible
                    setExpandIcon(isVisible)
                }
            }
        }

        private fun setExpandIcon(isContentVisible: Boolean) {
            binding.iconExpand.setImage(if (isContentVisible) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            })
        }

        fun bind(item: CriteriaCheckingResult.LocationCheckingResult) {
            if(absoluteAdapterPosition.isZero()) binding.divider.gone()
            binding.tfCityName.text = item.cityName
            binding.iconLocation.isVisible = item.isDilayaniTokopedia
            binding.layoutContent.apply {
                val priceMinFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.priceCheckingResult.min, false)
                val priceMaxFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.priceCheckingResult.max, false)
                tfOriginalPrice.value = "$priceMinFormatted - $priceMaxFormatted"
                tfOriginalPrice.setStatusPassed(item.priceCheckingResult.isEligible)
                val stockMin = item.stockCheckingResult.min
                val stockMax = item.stockCheckingResult.max
                tfCampaignStock.value ="$stockMin ${if(stockMax.isMoreThanZero()) " - $stockMax" else ""}"
                tfCampaignStock.setStatusPassed(item.stockCheckingResult.isEligible)
            }
        }
    }
}
