package com.tokopedia.shop.score.penalty.presentation.adapter.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.model.ShopPenaltyDetailUiModel
import kotlinx.android.synthetic.main.item_penalty_stepper.view.*

class PenaltyDetailStepperAdapter: RecyclerView.Adapter<PenaltyDetailStepperAdapter.DetailPenaltyStepperViewHolder>() {

    private var stepperPenaltyDetailList = mutableListOf<ShopPenaltyDetailUiModel.StepperPenaltyDetail>()

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

    inner class DetailPenaltyStepperViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(data: ShopPenaltyDetailUiModel.StepperPenaltyDetail) {
            with(itemView) {
                if (adapterPosition + 1 == MAX_STATUS_PENALTY) {
                    divider_stepper_penalty?.hide()
                } else {
                    divider_stepper_penalty?.show()
                    data.colorLineStepper?.let { divider_stepper_penalty?.setBackgroundColor(ContextCompat.getColor(context, it)) }
                }
                tv_title_status_stepper?.text = data.titleStepper
                data.colorDotStepper?.let { ic_dot_stepper?.setColorFilter(ContextCompat.getColor(context, it) ) }
            }
        }
    }

    companion object {
        const val MAX_STATUS_PENALTY = 3
    }
}