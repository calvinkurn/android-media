package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.databinding.ItemOrderTrackingSecondaryActionButtonBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ActionButtonsUiModel

class SecondaryActionButtonAdapter(private val listener: ActionButtonListener) :
    RecyclerView.Adapter<SecondaryActionButtonAdapter.ViewHolder>() {

    private val actionButtonList = mutableListOf<ActionButtonsUiModel.ActionButton>()

    fun setActionButtonList(newActionButtonList: List<ActionButtonsUiModel.ActionButton>) {
        if (newActionButtonList.isNullOrEmpty()) return
        actionButtonList.clear()
        actionButtonList.addAll(newActionButtonList)
        notifyItemRangeInserted(Int.ONE, newActionButtonList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderTrackingSecondaryActionButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(actionButtonList[position])
    }

    override fun getItemCount(): Int = actionButtonList.size

    inner class ViewHolder(private val binding: ItemOrderTrackingSecondaryActionButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ActionButtonsUiModel.ActionButton) {
            with(binding) {
                tvOrderTrackingSecondaryActionButton.run {
                    text = item.label
                    setOnClickListener {
                        listener.onActionButtonClicked(item)
                    }
                }
            }
        }
    }

    interface ActionButtonListener {
        fun onActionButtonClicked(button: ActionButtonsUiModel.ActionButton)
    }
}