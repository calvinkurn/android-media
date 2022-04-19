package com.tokopedia.shop.score.penalty.presentation.adapter.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyStepperBinding
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding


class PenaltyDetailStepperAdapter :
    RecyclerView.Adapter<PenaltyDetailStepperAdapter.DetailPenaltyStepperViewHolder>() {

    private val stepperPenaltyDetailList =
        mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>()

    fun setStepperPenaltyDetail(stepperList: List<ShopPenaltyDetailUiModel.StepperPenaltyDetail>) {
        if (stepperList.isNullOrEmpty()) return
        stepperPenaltyDetailList.clear()
        stepperPenaltyDetailList.addAll(stepperList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailPenaltyStepperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_penalty_stepper, parent, false)
        return DetailPenaltyStepperViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailPenaltyStepperViewHolder, position: Int) {
        holder.bind(stepperPenaltyDetailList[position])
    }

    override fun getItemCount(): Int = stepperPenaltyDetailList.size

    inner class DetailPenaltyStepperViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemPenaltyStepperBinding? by viewBinding()

        fun bind(data: ShopPenaltyDetailUiModel.StepperPenaltyDetail) {
            binding?.run {
                root.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
                if (data.isDividerShow) {
                    dividerStepperPenalty.show()
                    data.colorLineStepper?.let {
                        dividerStepperPenalty.setBackgroundColor(
                            ContextCompat.getColor(root.context, it)
                        )
                    }
                } else {
                    dividerStepperPenalty.hide()
                }
                data.titleStepper?.let { titleStepper ->
                    tvTitleStatusStepper.text =
                        MethodChecker.fromHtml(root.context?.getString(titleStepper) ?: "")
                }
                data.colorStatusTitle?.let {
                    tvTitleStatusStepper.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            it
                        )
                    )
                }
                if (data.isBold) {
                    tvTitleStatusStepper.setWeight(Typography.BOLD)
                } else {
                    tvTitleStatusStepper.setWeight(Typography.REGULAR)
                }
                data.colorDotStepper?.let {
                    icDotStepper.setColorFilter(
                        ContextCompat.getColor(
                            root.context,
                            it
                        )
                    )
                }
            }
        }
    }
}