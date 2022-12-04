package com.tokopedia.mvc.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemSelectRepeatPeriodBinding

class SelectRepeatPeriodAdapter(
    private val onPeriodClickListener: OnPeriodClickedListener
) : RecyclerView.Adapter<SelectRepeatPeriodAdapter.ViewHolder>() {

    interface OnPeriodClickedListener {
        fun onPeriodClicked(period: Int)
    }

    private val diffUtilCallback = object : DiffUtil.ItemCallback<Pair<Int, Boolean>>() {
        override fun areItemsTheSame(
            oldItem: Pair<Int, Boolean>,
            newItem: Pair<Int, Boolean>
        ): Boolean {
            return oldItem.second == newItem.second
        }

        override fun areContentsTheSame(
            oldItem: Pair<Int, Boolean>,
            newItem: Pair<Int, Boolean>
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffUtilCallback)
    private var listRepeatPeriod: List<Pair<Int, Boolean>> = listOf(
        1 to false,
        2 to false,
        3 to false,
        4 to false,
        5 to false,
        6 to false,
        7 to false,
        8 to false,
        9 to false,
        10 to false,
        11 to false
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
            if (it.first == selectedRepeatPeriod) {
                it.copy(second = true)
            } else {
                it
            }
        }
        differ.submitList(newList)
    }

    inner class ViewHolder(
        private val binding: SmvcItemSelectRepeatPeriodBinding,
        private val onPeriodClickListener: OnPeriodClickedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repeatPeriodData: Pair<Int, Boolean>) {
            with(binding) {
                textRepeatPeriod.text = root.context.getString(
                    R.string.smvc_bottomsheet_select_repeat_period_month_format,
                    repeatPeriodData.first
                )
                root.setOnClickListener { onPeriodClickListener.onPeriodClicked(repeatPeriodData.first) }
                iconCheck.isVisible = repeatPeriodData.second
            }
        }
    }
}
