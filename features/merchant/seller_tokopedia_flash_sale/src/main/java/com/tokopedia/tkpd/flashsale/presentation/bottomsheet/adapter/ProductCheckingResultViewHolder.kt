package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCheckResultBinding
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemProductCheckResultLocationContentBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ProductCheckingResult

class ProductCheckingResultViewHolder(private val binding: StfsItemProductCheckResultBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

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
            if (item.isMultiloc) {
                tfLocationCount.text = context.getString(R.string.commonbs_product_check_location_count_format,
                    item.locationCheckingResult.size)
                rvLocationResult.layoutManager = LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false)
                rvLocationResult.adapter = ProductLocationCheckingResultAdapter().apply {
                    setDataList(item.locationCheckingResult)
                }
            }
        }
    }

    fun bind(item: ProductCheckingResult) {
        binding.apply {
            imgProduct.loadImage(item.imageUrl)
            tfProductName.text = item.name
            tfProductName.isVisible = item.name.isNotEmpty()
            tfCampaignStock.text = context.getString(R.string.commonbs_product_check_stock_format,
                item.checkingDetailResult.stock)
            tfSubsidy.text = context.getString(R.string.commonbs_product_check_subsidy_format,
                item.checkingDetailResult.subsidyAmount.getCurrencyFormatted())
            tfSubsidy.isVisible = item.checkingDetailResult.isSubsidy
        }
        setupPrice(binding, item)
        setupMultiloc(binding.layoutLocationContent, item)
    }
}