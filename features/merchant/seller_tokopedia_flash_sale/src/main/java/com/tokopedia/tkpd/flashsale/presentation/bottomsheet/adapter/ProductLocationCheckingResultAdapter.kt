package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
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
        private val context = binding.root.context

        fun bind(item: ProductCheckingResult.LocationCheckingResult) {
            binding.apply {
                tfLocationName.text = item.cityName
                tfPrice.text = item.checkingDetailResult.discountedPrice.getCurrencyFormatted()
                tfSlashPrice.text = item.checkingDetailResult.originalPrice.getCurrencyFormatted()
                labelDiscount.text = "${item.checkingDetailResult.discountPercent}%"
                tfSubsidiary.text = context.getString(R.string.commonbs_product_check_subsidy_format,
                    item.checkingDetailResult.subsidyAmount.getCurrencyFormatted())
                tfSubsidiary.isVisible = item.checkingDetailResult.isSubsidy
                tfCampaignStock.text = context.getString(R.string.commonbs_product_check_stock_format,
                    item.checkingDetailResult.stock)
            }
        }
    }
}