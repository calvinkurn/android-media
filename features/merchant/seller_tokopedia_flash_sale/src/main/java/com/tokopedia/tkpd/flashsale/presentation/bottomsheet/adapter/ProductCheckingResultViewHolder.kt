package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
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
            val percentText: String

            if (!item.isMultiloc) {
                priceText = item.checkingDetailResult.discountedPrice.getCurrencyFormatted()
                slashPriceText = item.checkingDetailResult.originalPrice.getCurrencyFormatted()
                percentText = item.checkingDetailResult.discountPercent.getPercentFormatted()
            } else {
                val discountedPriceMax = item.locationCheckingResult.maxOfOrNull { it.checkingDetailResult.discountedPrice }.orZero()
                val discountedPriceMin = item.locationCheckingResult.minOfOrNull { it.checkingDetailResult.discountedPrice }.orZero()
                val originalPriceMax = item.locationCheckingResult.maxOfOrNull { it.checkingDetailResult.originalPrice }.orZero()
                val originalPriceMin = item.locationCheckingResult.minOfOrNull { it.checkingDetailResult.originalPrice }.orZero()
                val discountMax = item.locationCheckingResult.maxOfOrNull { it.checkingDetailResult.discountPercent }.orZero()
                val discountMin = item.locationCheckingResult.minOfOrNull { it.checkingDetailResult.discountPercent }.orZero()

                priceText = getPriceRangeText(discountedPriceMin, discountedPriceMax)
                slashPriceText = getPriceRangeText(originalPriceMin, originalPriceMax)
                percentText = getPercentRangeText(discountMin, discountMax)
            }

            tfPrice.text = priceText
            tfSlashPrice.text = slashPriceText
            labelDiscount.text = percentText
            tfSlashPrice.strikethrough()
        }
    }

    private fun getPriceRangeText(min: Long, max: Long): String {
        return if (min == max) {
            min.getCurrencyFormatted()
        } else {
            "${min.getCurrencyFormatted()} - ${max.getCurrencyFormatted()}"
        }
    }

    private fun getPercentRangeText(min: Int, max: Int): String {
        return if (min == max) {
            min.getPercentFormatted()
        } else {
            "$min - $max%"
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
        val statusText = item.checkingDetailResult.statusText
        val statusType = item.checkingDetailResult.statusLabelType
        val subsidyText = context.getString(R.string.stfs_subsidy_value_placeholder,
            item.checkingDetailResult.subsidyAmount.getCurrencyFormatted())
        val soldCountText = context.getString(R.string.commonbs_product_sold_count_format,
            item.soldCount)
        val refusedText = context.getString(R.string.stfs_rejection_reason_placeholder,
            item.checkingDetailResult.rejectionReason)

        binding.apply {
            imgProduct.loadImage(item.imageUrl)
            tfProductName.text = item.name
            tfProductName.isVisible = item.name.isNotEmpty()
            labelStatus.text = statusText
            labelStatus.setLabelType(statusType)
            labelStatus.isVisible = !item.isMultiloc && statusText.isNotEmpty()
            tfSubsidy.text = MethodChecker.fromHtml(subsidyText)
            tfSubsidy.isVisible = !item.isMultiloc && item.checkingDetailResult.isSubsidy
            tfSoldCount.text = MethodChecker.fromHtml(soldCountText)
            tfSoldCount.isVisible = !item.isMultiloc && item.soldCount != null
            tfRefused.text = MethodChecker.fromHtml(refusedText)
            tfRefused.isVisible = !item.isMultiloc && item.checkingDetailResult.rejectionReason.isNotEmpty()
        }
        setupPrice(binding, item)
        setupStock(binding, item)
        setupMultiloc(binding.layoutLocationContent, item)
    }

    private fun setupStock(
        binding: StfsItemProductCheckResultBinding,
        item: ProductCheckingResult,
    ) {
        val stock = item.locationCheckingResult.sumOf { it.checkingDetailResult.stock }
        val stockText = context.getString(R.string.commonbs_product_check_stock_format, stock)
        binding.tfCampaignStock.text = MethodChecker.fromHtml(stockText)
        if (item.isMultiloc) binding.tfCampaignStock.apply {
            text = context.getString(R.string.commonbs_product_check_location_count_suffix_format, text, item.locationCheckingResult.size)
        }
    }
}
