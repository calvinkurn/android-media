package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCampaignCriteriaResultBinding
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCampaignCriteriaResultContentBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.common.customview.TitleValueView
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CampaignCriteriaCheckingResultViewHolder(
    private val binding: StfsItemCampaignCriteriaResultBinding,
    private val onTickerClick: (locationResult: List<CriteriaCheckingResult.LocationCheckingResult>) -> Unit,
    isVariant: Boolean,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        if (isVariant) initIconExpandListener()
        else setupNonVariant()
        adjustLayoutItemWidth(binding.layoutContent)
    }

    private fun initIconExpandListener() {
        binding.iconExpand.setOnClickListener {
            binding.layoutContent.root.apply {
                isVisible = !isVisible
                setExpandIcon(isVisible)
            }
        }
    }

    private fun setupNonVariant() {
        binding.iconExpand.gone()
        binding.tfVariantName.gone()
        binding.layoutContent.root.visible()
    }

    private fun setExpandIcon(isContentVisible: Boolean) {
        binding.iconExpand.setImage(if (isContentVisible) {
            IconUnify.CHEVRON_UP
        } else {
            IconUnify.CHEVRON_DOWN
        })
    }

    private fun setupOtherCriteria(
        bindingContent: StfsItemCampaignCriteriaResultContentBinding,
        item: CriteriaCheckingResult,
    ) {
        bindingContent.apply {
            tfOtherCriteriaFreeOngkir.isVisible = item.includeFreeOngkirCheckingResult.isActive
            tfOtherCriteriaFreeOngkir.setStatusPassed(item.includeFreeOngkirCheckingResult.isEligible)

            tfOtherCriteriaReadyStock.isVisible = item.includePreOrderCheckingResult.isActive
            tfOtherCriteriaReadyStock.setStatusPassed(item.includePreOrderCheckingResult.isEligible)

            tfOtherCriteriaWholesale.isVisible = item.includeWholesaleCheckingResult.isActive
            tfOtherCriteriaWholesale.setStatusPassed(item.includeWholesaleCheckingResult.isEligible)

            tfOtherCriteriaCondition.isVisible = item.includeSecondHandCheckingResult.isActive
            tfOtherCriteriaCondition.setStatusPassed(item.includeSecondHandCheckingResult.isEligible)
        }
    }

    private fun setupMultiloc(
        bindingContent: StfsItemCampaignCriteriaResultContentBinding,
        item: CriteriaCheckingResult
    ) {
        bindingContent.apply {
            val htmlDecText = this.root.context.getString(R.string.commonbs_criteria_location_desc)
            tickerMultiloc.setHtmlDescription(htmlDecText)
            tickerMultiloc.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    onTickerClick(item.locationResult)
                }
                override fun onDismiss() {}
            })
            tfOriginalPrice.isVisible = !item.isMultiloc
            tfCampaignStock.isVisible = !item.isMultiloc
        }
    }

    private fun TitleValueView.setStatusAndValue(fieldIsEligible: Boolean, fieldValue: Long) {
        setStatusAndValue(fieldIsEligible, fieldValue.toString())
    }

    private fun TitleValueView.setStatusAndValue(fieldIsEligible: Boolean, fieldValue: String) {
        value = fieldValue
        setStatusPassed(fieldIsEligible)
    }

    private fun adjustLayoutItemWidth(layout: StfsItemCampaignCriteriaResultContentBinding) {
        (0 until layout.root.childCount).forEach {
            val view = layout.root.getChildAt(it)
            if (view is TitleValueView) view.layoutParams.width = (getScreenWidth() * 0.45F).toInt()
        }
    }

    fun bind(item: CriteriaCheckingResult) {
        val context = binding.root.context
        binding.tfVariantName.text = item.name
        binding.layoutContent.apply {
            val priceMinFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.priceCheckingResult.min, false)
            val priceMaxFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.priceCheckingResult.max, false)
            val soldMax = item.countSoldResult.max
            val soldMin = item.countSoldResult.min
            val maxShown = item.maxAppearanceCheckingResult.max
            val periodShown = item.maxAppearanceCheckingResult.dayPeriod
            val maxShownText = context.getString(R.string.commonbs_max_shown_format, maxShown, periodShown)

            tfOriginalPrice.setStatusAndValue(item.priceCheckingResult.isEligible, "$priceMinFormatted - $priceMaxFormatted")
            tfCampaignStock.setStatusAndValue(item.stockCheckingResult.isEligible, item.stockCheckingResult.min)
            tfMinRating.setStatusAndValue(item.ratingResult.isEligible, item.ratingResult.min.toString())
            tfMinProductScore.setStatusAndValue(item.scoreCheckingResult.isEligible, item.scoreCheckingResult.min)
            tfSoldProduct.setStatusAndValue(item.countSoldResult.isEligible, "$soldMin - $soldMax")
            tfMinBuy.setStatusAndValue(item.minOrderCheckingResult.isEligible, item.minOrderCheckingResult.min)
            tfMaxShown.setStatusAndValue(item.maxAppearanceCheckingResult.isEligible, maxShownText)

            setupMultiloc(this, item)
            setupOtherCriteria(this, item)
        }
    }
}
