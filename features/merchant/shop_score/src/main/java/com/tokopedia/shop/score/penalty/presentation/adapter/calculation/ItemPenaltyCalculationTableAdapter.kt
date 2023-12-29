package com.tokopedia.shop.score.penalty.presentation.adapter.calculation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.score.databinding.ItemPenaltyCalculationTableDetailBinding
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationTableDetailUiModel
import com.tokopedia.unifyprinciples.Typography

class ItemPenaltyCalculationTableAdapter(
    private val uiModels: List<ItemPenaltyCalculationTableDetailUiModel>
): RecyclerView.Adapter<ItemPenaltyCalculationTableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPenaltyCalculationTableDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uiModels[position])
    }

    override fun getItemCount(): Int = uiModels.size

    inner class ViewHolder(
        private val binding: ItemPenaltyCalculationTableDetailBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(uiModel: ItemPenaltyCalculationTableDetailUiModel) {
            binding.tvPenaltyCalculationTableDetail.run {
                text = uiModel.text
                setTextColor(uiModel.isRedColor)
                setWeightType(uiModel.isBold)
            }
        }

        private fun Typography.setTextColor(isRedColor: Boolean) {
            val textColorRes =
                if (isRedColor) {
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                } else {
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950
                }
            setTextColor(MethodChecker.getColor(context, textColorRes))
        }

        private fun Typography.setWeightType(isBold: Boolean) {
            val weight =
                if (isBold) {
                    Typography.BOLD
                } else {
                    Typography.REGULAR
                }
            setWeight(weight)
        }

    }

}
