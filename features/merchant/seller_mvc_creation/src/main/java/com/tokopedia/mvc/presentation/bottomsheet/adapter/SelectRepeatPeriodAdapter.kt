package com.tokopedia.mvc.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemSelectRepeatPeriodBinding
import com.tokopedia.mvc.domain.entity.RepeatPeriodData

class SelectRepeatPeriodAdapter(
    private val onPeriodClickListener: OnPeriodClickedListener
) : RecyclerView.Adapter<SelectRepeatPeriodAdapter.ViewHolder>() {

    interface OnPeriodClickedListener {
        fun onPeriodClicked(period: Int)
    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<RepeatPeriodData>() {
        override fun areItemsTheSame(
            oldItem: RepeatPeriodData,
            newItem: RepeatPeriodData
        ): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(
            oldItem: RepeatPeriodData,
            newItem: RepeatPeriodData
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)
    private var listRepeatPeriod: List<RepeatPeriodData> = listOf(
        RepeatPeriodData(1, false),
        RepeatPeriodData(2, false),
        RepeatPeriodData(3, false),
        RepeatPeriodData(4, false),
        RepeatPeriodData(5, false),
        RepeatPeriodData(6, false),
        RepeatPeriodData(7, false),
        RepeatPeriodData(8, false),
        RepeatPeriodData(9, false),
        RepeatPeriodData(10, false),
        RepeatPeriodData(11, false)
    )

    init {
        differ.submitList(listRepeatPeriod)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemSelectRepeatPeriodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding, onPeriodClickListener)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        differ.currentList.getOrNull(position)?.let { repeatPeriodData ->
            holder.bind(repeatPeriodData)
        }
    }

    fun setSelectedRepeatPeriod(selectedRepeatPeriod: Int) {
        val newList = listRepeatPeriod.map {
            if (it.period == selectedRepeatPeriod) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
        differ.submitList(newList)
    }

    inner class ViewHolder(
        private val binding: SmvcItemSelectRepeatPeriodBinding,
        private val onPeriodClickListener: OnPeriodClickedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repeatPeriodData: RepeatPeriodData) {
            with(binding) {
                textRepeatPeriod.text = root.context.getString(
                    R.string.smvc_bottomsheet_select_repeat_period_month_format,
                    repeatPeriodData.period
                )
                root.setOnClickListener { onPeriodClickListener.onPeriodClicked(repeatPeriodData.period) }
                iconCheck.isVisible = repeatPeriodData.isSelected
            }
        }
    }
}
