package com.tokopedia.logisticcart.scheduledelivery.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.databinding.ItemDateCardBinding
import com.tokopedia.logisticcart.scheduledelivery.utils.ScheduleSlotListener
import com.tokopedia.logisticcart.scheduledelivery.view.uimodel.ButtonDateUiModel
import com.tokopedia.unifycomponents.CardUnify2

class ChooseDateAdapter(
    private val items: List<ButtonDateUiModel>,
    private val listener: ScheduleSlotListener
) : RecyclerView.Adapter<ChooseDateAdapter.ChooseDateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChooseDateAdapter.ChooseDateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDateCardBinding.inflate(inflater, parent, false)
        return ChooseDateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseDateAdapter.ChooseDateViewHolder, position: Int) {
        val data = items[position]
        holder.bindData(data)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun onDateSelected(dateUiModel: ButtonDateUiModel) {
        // find previous selected date, set selected to false
        val previousSelectedDateIndex = items.indexOfFirst { it.isSelected }
        if (previousSelectedDateIndex != -1) {
            items.getOrNull(previousSelectedDateIndex)?.isSelected = false
            // notify item changed
            notifyItemChanged(previousSelectedDateIndex)
        }
        // find current selected date, set selected to true
        val dateUiModelIndex = items.indexOf(dateUiModel)
        if (dateUiModelIndex != -1) {
            items.getOrNull(dateUiModelIndex)?.let {
                it.isSelected = !it.isSelected
            }
            notifyItemChanged(dateUiModelIndex)
        }
        // notify item changed
        listener.onClickDateListener(dateUiModel)
    }

    fun selectedDatePosition(): Int {
        return items.indexOfFirst { it.isSelected }
    }

    inner class ChooseDateViewHolder(private val binding: ItemDateCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(buttonDateUiModel: ButtonDateUiModel) {
            binding.tvTitleDate.text = buttonDateUiModel.title
            binding.tvDate.text = buttonDateUiModel.date
            setCardState(buttonDateUiModel)
            setListener(buttonDateUiModel)
            setLabelOufOfSlot(buttonDateUiModel)
        }

        private fun setLabelOufOfSlot(buttonDateUiModel: ButtonDateUiModel) {
            if (buttonDateUiModel.isEnabled) {
                binding.tvLabelOutOfDate.gone()
            } else {
                binding.tvLabelOutOfDate.visible()
            }
        }

        private fun setListener(buttonDateUiModel: ButtonDateUiModel) {
            if (buttonDateUiModel.isEnabled) {
                binding.root.setOnClickListener {
                    onDateSelected(buttonDateUiModel)
                }
            } else {
                binding.root.setOnClickListener(null)
            }
        }

        private fun setCardState(buttonDateUiModel: ButtonDateUiModel) {
            binding.containerDateItem.cardType = when {
                !buttonDateUiModel.isEnabled -> CardUnify2.TYPE_BORDER_DISABLED
                buttonDateUiModel.isSelected -> CardUnify2.TYPE_BORDER_ACTIVE
                else -> CardUnify2.TYPE_BORDER
            }
        }
    }
}
