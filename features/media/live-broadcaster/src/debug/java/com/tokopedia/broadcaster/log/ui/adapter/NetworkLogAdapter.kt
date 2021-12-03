package com.tokopedia.broadcaster.log.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.broadcaster.data.uimodel.LoggerUIModel
import com.tokopedia.broadcaster.log.util.timeFormat
import com.tokopedia.broadcaster.databinding.ItemChuckerLogBinding

interface StatsNerdItemListener {
    fun onLogClicked(model: LoggerUIModel)
}

class ChuckerAdapter constructor(
    private var logs: MutableList<LoggerUIModel> = mutableListOf(),
    private val listener: StatsNerdItemListener
) : RecyclerView.Adapter<ChuckerAdapter.ChuckerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChuckerViewHolder {
        return ChuckerViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ChuckerViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            holder.bind(logs[position])
        }
    }

    override fun getItemCount() = logs.size

    fun updateItems(models: MutableList<LoggerUIModel>) {
        val diff = DiffUtil.calculateDiff(
            DiffUtilCallback(logs, models)
        )

        logs.clear()
        logs.addAll(models)
        diff.dispatchUpdatesTo(this)
    }

    class ChuckerViewHolder(
        private val binding: ItemChuckerLogBinding,
        private val listener: StatsNerdItemListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: LoggerUIModel) {
            binding.txtUrl.text = model.url
            binding.txtFps.text = model.fps
            binding.txtStartDate.text = model.startTime.timeFormat()

            itemView.setOnClickListener {
                listener.onLogClicked(model)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: StatsNerdItemListener): ChuckerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val inflate = ItemChuckerLogBinding.inflate(layoutInflater, parent, false)
                return ChuckerViewHolder(inflate, listener)
            }
        }

    }

}