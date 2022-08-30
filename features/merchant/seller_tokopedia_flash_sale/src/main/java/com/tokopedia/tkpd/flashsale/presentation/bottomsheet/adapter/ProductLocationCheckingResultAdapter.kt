package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductLocationCheckResultBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ProductCheckingResult

class ProductLocationCheckingResultAdapter: RecyclerView.Adapter<ProductLocationCheckingResultAdapter.CriteriaViewHolder>() {

    private var data: List<ProductCheckingResult.LocationCheckingResult> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemProductLocationCheckResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<ProductCheckingResult.LocationCheckingResult>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class CriteriaViewHolder(private val binding: StfsItemProductLocationCheckResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductCheckingResult.LocationCheckingResult) {
            binding.tfLocationName.text = item.cityName
            binding.tfPrice.text = item.checkingDetailResult.discountedPrice.getCurrencyFormatted()
            binding.tfSlashPrice.text = item.checkingDetailResult.originalPrice.getCurrencyFormatted()
            binding.labelDiscount.text = "${item.checkingDetailResult.discountPercent}%"
            binding.tfSubsidiary.text = "Menerima subsidi sebesar ${item.checkingDetailResult.subsidyAmount.getCurrencyFormatted()} dari Tokopedia"
            binding.tfSubsidiary.isVisible = item.checkingDetailResult.isSubsidy
            binding.tfCampaignStock.text = "Stok Campaign: ${item.checkingDetailResult.stock}"
        }
    }
}