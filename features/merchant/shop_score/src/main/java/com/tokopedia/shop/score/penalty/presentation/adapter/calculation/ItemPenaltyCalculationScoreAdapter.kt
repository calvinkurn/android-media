package com.tokopedia.shop.score.penalty.presentation.adapter.calculation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.databinding.ItemPenaltyCalculationScoreDetailBinding
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationScoreDetailUiModel

class ItemPenaltyCalculationScoreAdapter(
    private val uiModels: List<ItemPenaltyCalculationScoreDetailUiModel>,
    private val listener: Listener
): RecyclerView.Adapter<ItemPenaltyCalculationScoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPenaltyCalculationScoreDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uiModels[position])
    }

    override fun getItemCount(): Int = uiModels.size

    inner class ViewHolder(
        private val binding: ItemPenaltyCalculationScoreDetailBinding,
        private val listener: Listener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: ItemPenaltyCalculationScoreDetailUiModel) {
            binding.tvPenaltyCalculationScoreDetail.text = uiModel.detail
            binding.tvPenaltyCalculationScoreValue.text = uiModel.value.toString()
            binding.icPenaltyCalculationScoreDetail.run {
                showWithCondition(uiModel.shouldShowIcon)
                if (uiModel.shouldShowIcon) {
                    setOnClickListener {
                        listener.onIconClicked()
                    }
                }
            }
        }

    }

    interface Listener {
        fun onIconClicked()
    }

}
