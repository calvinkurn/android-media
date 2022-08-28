package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCampaignCriteriaResultBinding
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCampaignCriteriaResultContentBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.presentation.common.customview.TitleValueView
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CampaignCriteriaCheckingResultAdapter: RecyclerView.Adapter<CampaignCriteriaCheckingResultAdapter.CriteriaViewHolder>() {

    private var data: List<CriteriaCheckingResult> = emptyList()
    private var onClickListener: (locationResult: List<CriteriaCheckingResult.LocationCheckingResult>) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemCampaignCriteriaResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding, onClickListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<CriteriaCheckingResult>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun setOnTickerClick(
        listener: (locationResult: List<CriteriaCheckingResult.LocationCheckingResult>) -> Unit
    ) {
        onClickListener = listener
    }

    inner class CriteriaViewHolder(private val binding: StfsItemCampaignCriteriaResultBinding,
                                   private val onTickerClick: (locationResult: List<CriteriaCheckingResult.LocationCheckingResult>) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private fun setExpandIcon(isContentVisible: Boolean) {
            binding.iconExpand.setImage(if (isContentVisible) {
                IconUnify.CHEVRON_UP
            } else {
                IconUnify.CHEVRON_DOWN
            })
        }

        init {
            binding.iconExpand.setOnClickListener {
                binding.layoutContent.root.apply {
                    isVisible = !isVisible
                    setExpandIcon(isVisible)
                }
            }
            adjustLayoutItemWidth(binding.layoutContent)
            val htmlDecText = binding.root.context.getString(R.string.commonbs_multiloc_desc)
            binding.layoutContent.tickerMultiloc.setHtmlDescription(htmlDecText)
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
                tickerMultiloc.isVisible = item.isMultiloc
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
    }
}