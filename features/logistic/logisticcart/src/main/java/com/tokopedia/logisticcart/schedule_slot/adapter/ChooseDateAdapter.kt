package com.tokopedia.logisticcart.schedule_slot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.databinding.ItemDateCardBinding
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener

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

    inner class ChooseDateViewHolder(private val binding: ItemDateCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(buttonDateUiModel: ButtonDateUiModel) {
            binding.tvTitleDate.text = buttonDateUiModel.title
            binding.tvDate.text = buttonDateUiModel.date

            if (buttonDateUiModel.isEnabled) {
                binding.tvLabelOutOfDate.visibility = View.GONE

                if (buttonDateUiModel.isSelected) {
                    binding.containerDateItem.background = ContextCompat.getDrawable(
                        binding.root.context,
                        com.tokopedia.logisticcart.R.drawable.bg_card_date_selected
                    )
                } else {
                    binding.containerDateItem.background = ContextCompat.getDrawable(
                        binding.root.context,
                        com.tokopedia.logisticcart.R.drawable.bg_card_date
                    )
                    binding.root.setOnClickListener {
                        listener.onClickDateListener(buttonDateUiModel)
                    }
                }
            } else {
                binding.tvLabelOutOfDate.visibility = View.VISIBLE
                binding.containerDateItem.background = ContextCompat.getDrawable(
                    binding.root.context,
                    com.tokopedia.logisticcart.R.drawable.bg_card_disabled
                )
            }
        }
    }
}
