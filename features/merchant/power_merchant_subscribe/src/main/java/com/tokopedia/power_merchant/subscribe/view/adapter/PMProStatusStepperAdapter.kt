package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmProStatusStepperBinding
import com.tokopedia.power_merchant.subscribe.view.model.PMProStatusStepperUiModel

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
        val binding = ItemPmProStatusStepperBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PMProStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PMProStatusViewHolder, position: Int) {
        val data = pmProStatusStepperList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = pmProStatusStepperList.size

    inner class PMProStatusViewHolder(private val binding: ItemPmProStatusStepperBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PMProStatusStepperUiModel) {
            with(binding) {
                root.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                )
                tvTitleStatusStepper.text = data.titleStepper

                data.colorStatusTitle?.let {
                    tvTitleStatusStepper.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            it
                        )
                    )
                }

                if (data.isStepperShow) {
                    dividerStepperPmProStatus.show()
                    data.colorDivider?.let {
                        dividerStepperPmProStatus.setBackgroundColor(
                            ContextCompat.getColor(
                                root.context, it
                            )
                        )
                    }
                } else {
                    dividerStepperPmProStatus.hide()
                }

                when {
                    data.isCurrentActive -> {
                        icDotStepper.setImageResource(R.drawable.ic_current_active_pm_pro_status)
                    }
                    data.isPassedActive -> {
                        icDotStepper.setImageResource(R.drawable.ic_pass_active_pm_pro_status)
                    }
                    else -> {
                        icDotStepper.setImageResource(R.drawable.ic_current_inactive_pm_pro_status)
                    }
                }
            }
        }
    }
}