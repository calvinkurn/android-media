package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.PMProStatusStepperUiModel
import kotlinx.android.synthetic.main.item_pm_pro_status_stepper.view.*

class PMProStatusStepperAdapter :
    RecyclerView.Adapter<PMProStatusStepperAdapter.PMProStatusViewHolder>() {

    private val pmProStatusStepperList =
        mutableListOf<PMProStatusStepperUiModel>()

    fun setPMProStatusList(data: List<PMProStatusStepperUiModel>) {
        if (data.isNullOrEmpty()) return
        pmProStatusStepperList.clear()
        pmProStatusStepperList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PMProStatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_pm_pro_status_stepper,
            parent, false
        )
        return PMProStatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: PMProStatusViewHolder, position: Int) {
        val data = pmProStatusStepperList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = pmProStatusStepperList.size

    inner class PMProStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: PMProStatusStepperUiModel) {
            with(itemView) {
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                )
                tv_title_status_stepper?.text = data.titleStepper

                data.colorStatusTitle?.let {
                    tv_title_status_stepper?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            it
                        )
                    )
                }

                if (data.isStepperShow) {
                    divider_stepper_pm_pro_status?.show()
                    data.colorDivider?.let {
                        divider_stepper_pm_pro_status?.setBackgroundColor(
                            ContextCompat.getColor(
                                context, it
                            )
                        )
                    }
                } else {
                    divider_stepper_pm_pro_status?.hide()
                }

                if (data.isPassedActive) {
                    ic_dot_stepper?.setImageResource(R.drawable.ic_pass_active_pm_pro_status)
                }

                if (data.isCurrentActive) {
                    ic_dot_stepper?.setImageResource(R.drawable.ic_current_active_pm_pro_status)
                }
            }
        }
    }
}