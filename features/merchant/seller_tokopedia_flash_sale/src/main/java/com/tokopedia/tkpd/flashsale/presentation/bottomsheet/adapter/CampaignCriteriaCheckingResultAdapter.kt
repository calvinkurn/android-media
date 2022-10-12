package com.tokopedia.tkpd.flashsale.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemCampaignCriteriaResultBinding
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult

class CampaignCriteriaCheckingResultAdapter: RecyclerView.Adapter<CampaignCriteriaCheckingResultViewHolder>() {

    companion object {
        private const val SINGLE_PRODUCT_COUNT = 1
    }

    private var data: List<CriteriaCheckingResult> = emptyList()
    private var onClickListener: (locationResult: List<CriteriaCheckingResult.LocationCheckingResult>) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignCriteriaCheckingResultViewHolder {
        val binding = StfsItemCampaignCriteriaResultBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CampaignCriteriaCheckingResultViewHolder(binding, onClickListener, isVariant = data.size > SINGLE_PRODUCT_COUNT)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CampaignCriteriaCheckingResultViewHolder, position: Int) {
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
}
