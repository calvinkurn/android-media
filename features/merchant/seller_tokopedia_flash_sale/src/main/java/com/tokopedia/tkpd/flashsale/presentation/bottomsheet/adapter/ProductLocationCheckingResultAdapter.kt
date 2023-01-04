package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
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
            val soldCountText = context.getString(R.string.commonbs_product_sold_count_format,
                item.soldCount)
            val textSubsidy = context.getString(R.string.stfs_subsidy_value_placeholder,
                item.checkingDetailResult.subsidyAmount.getCurrencyFormatted())
            val stockText = context.getString(R.string.commonbs_product_check_stock_format,
                item.checkingDetailResult.stock)
            val refusedText = context.getString(R.string.stfs_rejection_reason_placeholder,
                item.checkingDetailResult.rejectionReason)
            binding.apply {
                tfLocationName.text = item.cityName
                tfPrice.text = item.checkingDetailResult.discountedPrice.getCurrencyFormatted()
                tfSlashPrice.text = item.checkingDetailResult.originalPrice.getCurrencyFormatted()
                tfSlashPrice.strikethrough()
                labelDiscount.text = "${item.checkingDetailResult.discountPercent}%"
                tfSubsidiary.text = MethodChecker.fromHtml(textSubsidy)
                tfSubsidiary.isVisible = item.checkingDetailResult.isSubsidy
                binding.tfCampaignStock.text = MethodChecker.fromHtml(stockText)
                labelStatus.setTextAndCheckShow(item.checkingDetailResult.statusText)
                labelStatus.setLabelType(item.checkingDetailResult.statusLabelType)
                tfSoldCount.text = MethodChecker.fromHtml(soldCountText)
                tfSoldCount.isVisible = item.soldCount != null
                tfRefused.text = MethodChecker.fromHtml(refusedText)
                tfRefused.isVisible = item.checkingDetailResult.rejectionReason.isNotEmpty()
            }
        }
    }
}
