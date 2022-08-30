package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCheckResultBinding
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCheckResultLocationContentBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ProductCheckingResult

class ProductCheckingResultAdapter: RecyclerView.Adapter<ProductCheckingResultAdapter.CriteriaViewHolder>() {

    private var data: List<ProductCheckingResult> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemProductCheckResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<ProductCheckingResult>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class CriteriaViewHolder(private val binding: StfsItemProductCheckResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.layoutLocationContent.iconExpand.setOnClickListener {
                binding.layoutLocationContent.rvLocationResult.apply {
                    isVisible = !isVisible
                    setExpandIcon(isVisible)
                }
            }
        }

        private fun setExpandIcon(isContentVisible: Boolean) {
            binding.layoutLocationContent.iconExpand.setImage(if (isContentVisible) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            })
        }

        fun bind(item: ProductCheckingResult) {
            binding.apply {
                imgProduct.loadImage(item.imageUrl)
                tfProductName.text = item.name
                tfProductName.isVisible = item.name.isNotEmpty()
                tfCampaignStock.text = "Stok Campaign: ${item.checkingDetailResult.stock}"
                tfSubsidy.text = "Menerima subsidi sebesar ${item.checkingDetailResult.subsidyAmount.getCurrencyFormatted()} dari Tokopedia"
                tfSubsidy.isVisible = item.checkingDetailResult.isSubsidy
            }
            setupPrice(binding, item)
            setupMultiloc(binding.layoutLocationContent, item)
        }

        private fun setupPrice(
            binding: StfsItemProductCheckResultBinding,
            item: ProductCheckingResult,
        ) {
            binding.apply {
                val priceText: String
                val slashPriceText: String

                if (!item.isMultiloc) {
                    priceText = item.checkingDetailResult.discountedPrice.getCurrencyFormatted()
                    slashPriceText = item.checkingDetailResult.originalPrice.getCurrencyFormatted()
                } else {
                    val discountedPriceMax = item.locationCheckingResult.maxOf { it.checkingDetailResult.discountedPrice }.getCurrencyFormatted()
                    val discountedPriceMin = item.locationCheckingResult.minOf { it.checkingDetailResult.discountedPrice }.getCurrencyFormatted()
                    val originalPriceMax = item.locationCheckingResult.maxOf { it.checkingDetailResult.originalPrice }.getCurrencyFormatted()
                    val originalPriceMin = item.locationCheckingResult.minOf { it.checkingDetailResult.originalPrice }.getCurrencyFormatted()

                    priceText = "$discountedPriceMin - $discountedPriceMax"
                    slashPriceText =  "$originalPriceMin - $originalPriceMax"
                }

                tfPrice.text = priceText
                tfSlashPrice.text = slashPriceText
                labelDiscount.text = "${item.checkingDetailResult.discountPercent}%"
            }
        }

        private fun setupMultiloc(
            bindingLocation: StfsItemProductCheckResultLocationContentBinding,
            item: ProductCheckingResult,
        ) {
            bindingLocation.apply {
                root.isVisible = item.isMultiloc
                tfLocationCount.text = "Detail di ${item.locationCheckingResult.size} Lokasi"
                rvLocationResult.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
                rvLocationResult.adapter = ProductLocationCheckingResultAdapter().apply {
                    setDataList(item.locationCheckingResult)
                }
            }
        }
    }
}