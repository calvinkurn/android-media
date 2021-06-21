package com.tokopedia.shop.score.penalty.presentation.adapter.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_penalty_stepper.view.*

class PenaltyDetailStepperAdapter : RecyclerView.Adapter<PenaltyDetailStepperAdapter.DetailPenaltyStepperViewHolder>() {

    private val stepperPenaltyDetailList = mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>()

    fun setStepperPenaltyDetail(stepperList: List<ShopPenaltyDetailUiModel.StepperPenaltyDetail>) {
        if (stepperList.isNullOrEmpty()) return
        stepperPenaltyDetailList.clear()
        stepperPenaltyDetailList.addAll(stepperList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPenaltyStepperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_penalty_stepper, parent, false)
        return DetailPenaltyStepperViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailPenaltyStepperViewHolder, position: Int) {
        holder.bind(stepperPenaltyDetailList[position])
    }

    override fun getItemCount(): Int = stepperPenaltyDetailList.size

    inner class DetailPenaltyStepperViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: ShopPenaltyDetailUiModel.StepperPenaltyDetail) {
            with(itemView) {
                setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                if (data.isDividerShow) {
                    divider_stepper_penalty?.show()
                    data.colorLineStepper?.let { divider_stepper_penalty?.setBackgroundColor(ContextCompat.getColor(context, it)) }
                } else {
                    divider_stepper_penalty?.hide()
                }
                 data.titleStepper?.let { titleStepper ->
                     tv_title_status_stepper?.text = MethodChecker.fromHtml(context?.getString(titleStepper) ?: "")
                }
                data.colorStatusTitle?.let { tv_title_status_stepper?.setTextColor(ContextCompat.getColor(context, it)) }
                if (data.isBold) {
                    tv_title_status_stepper?.setWeight(Typography.BOLD)
                } else {
                    tv_title_status_stepper?.setWeight(Typography.REGULAR)
                }
                data.colorDotStepper?.let { ic_dot_stepper?.setColorFilter(ContextCompat.getColor(context, it)) }
            }
        }
    }
}