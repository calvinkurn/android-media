package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingStepperStatusBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.diffutil.StepperStatusDiffUtilCallback
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StepperStatusUiModel

class StepperStatusAdapter : RecyclerView.Adapter<StepperStatusAdapter.StepperStatusViewHolder>() {

    private val stepperStatusList = mutableListOf<StepperStatusUiModel>()

    fun setStepperList(newStepperStatusList: List<StepperStatusUiModel>) {
        if (stepperStatusList.isNullOrEmpty()) return
        val callBack = StepperStatusDiffUtilCallback(stepperStatusList, newStepperStatusList)
        val diffResult = DiffUtil.calculateDiff(callBack)
        stepperStatusList.clear()
        stepperStatusList.addAll(newStepperStatusList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepperStatusViewHolder {
        val binding = ItemTokofoodOrderTrackingStepperStatusBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StepperStatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepperStatusViewHolder, position: Int) {
        holder.bind(stepperStatusList[position])
    }

    override fun onBindViewHolder(
        holder: StepperStatusViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads.getOrNull(Int.ZERO) as? Bundle
            bundle?.keySet()?.forEach { key ->
                if (key == KEY_IS_ICON_ACTIVE) {
                    holder.updateStepperIcon(bundle.getBoolean(KEY_IS_ICON_ACTIVE))
                }
                if (key == KEY_IS_LINE_ACTIVE) {
                    holder.updateStepperLine(bundle.getBoolean(KEY_IS_LINE_ACTIVE))
                }
            }
        }
    }

    override fun getItemCount(): Int = stepperStatusList.size

    inner class StepperStatusViewHolder(private val binding: ItemTokofoodOrderTrackingStepperStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val stepperColor = getStepperColor()

        fun updateStepperIcon(isActive: Boolean) {
            setupStepperIcon(isActive, stepperColor)
        }

        fun updateStepperLine(isActive: Boolean) {
            setupStepperIcon(isActive, stepperColor)
        }

        fun bind(item: StepperStatusUiModel) {
            if (stepperStatusList.size == adapterPosition + Int.ONE) {
                hideStepperLine()
            } else {
                setStepperIcon(item.iconName)
                setupStepperIcon(item.isIconActive, stepperColor)
                setupStepperLine(item.isLineActive, stepperColor)
            }
        }

        private fun setStepperIcon(iconName: Int) {
            binding.icOrderTrackingStatus.setImage(iconName)
        }

        private fun setupStepperIcon(isActive: Boolean, stepperColor: Pair<Int, Int>) {
            with(binding) {
                icOrderTrackingStatus.setBackgroundColor(
                    if (isActive) stepperColor.first else stepperColor.second
                )
            }
        }

        private fun setupStepperLine(isActive: Boolean, stepperColor: Pair<Int, Int>) {
            with(binding) {
                ivOrderTrackingStatusLine.setBackgroundColor(
                    if (isActive) stepperColor.first else stepperColor.second
                )
            }
        }

        private fun hideStepperLine() {
            binding.ivOrderTrackingStatusLine.hide()
        }

        private fun getStepperColor(): Pair<Int, Int> {
            val gn400Color = ContextCompat.getColor(
                binding.root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN400
            )
            val nn300Color = ContextCompat.getColor(
                binding.root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN300
            )
            return Pair(gn400Color, nn300Color)
        }
    }

    companion object {
        const val KEY_IS_ICON_ACTIVE = "isIconActive"
        const val KEY_IS_LINE_ACTIVE = "isLineActive"
    }
}