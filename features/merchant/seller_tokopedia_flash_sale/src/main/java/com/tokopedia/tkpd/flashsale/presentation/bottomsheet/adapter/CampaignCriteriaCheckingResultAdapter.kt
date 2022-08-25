package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCampaignCriteriaResultBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
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
            val htmlDecText = binding.root.context.getString(R.string.commonbs_multiloc_desc)
            binding.layoutContent.tickerMultiloc.setHtmlDescription(htmlDecText)
        }

        fun bind(item: CriteriaCheckingResult) {
            val context = binding.root.context
            binding.tfVariantName.text = item.name
            binding.layoutContent.apply {
                val priceMinFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.priceCheckingResult.min, false)
                val priceMaxFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.priceCheckingResult.max, false)
                tfOriginalPrice.value = "$priceMinFormatted - $priceMaxFormatted"
                tfOriginalPrice.setStatusPassed(item.priceCheckingResult.isEligible)

                tfCampaignStock.value = item.stockCheckingResult.min.toString()
                tfCampaignStock.setStatusPassed(item.stockCheckingResult.isEligible)

                tfMinRating.value = item.ratingResult.min.toString()
                tfMinRating.setStatusPassed(item.ratingResult.isEligible)

                tfMinProductScore.value = item.scoreCheckingResult.min.toString()
                tfMinProductScore.setStatusPassed(item.scoreCheckingResult.isEligible)

                val soldMax = item.countSoldResult.max
                val soldMin = item.countSoldResult.min
                tfSoldProduct.value = "$soldMin - $soldMax"
                tfSoldProduct.setStatusPassed(item.countSoldResult.isEligible)

                tfMinBuy.value = item.minOrderCheckingResult.min.toString()
                tfMinBuy.setStatusPassed(item.minOrderCheckingResult.isEligible)

                val maxShown = item.maxAppearanceCheckingResult.max
                val periodShown = item.maxAppearanceCheckingResult.dayPeriod
                tfMaxShown.value = context.getString(R.string.commonbs_max_shown_format, maxShown, periodShown)
                tfMaxShown.setStatusPassed(item.maxAppearanceCheckingResult.isEligible)

                tickerMultiloc.isVisible = item.isMultiloc
                tickerMultiloc.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        onTickerClick(item.locationResult)
                    }

                    override fun onDismiss() {}
                })
            }
        }
    }
}