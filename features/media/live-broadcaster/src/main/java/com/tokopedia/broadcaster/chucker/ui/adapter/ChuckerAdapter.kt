package com.tokopedia.broadcaster.chucker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.broadcaster.chucker.ui.adapter.diffutil.ChuckerLogDiffUtilCallback
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel
import com.tokopedia.broadcaster.chucker.util.timeFormat
import com.tokopedia.broadcaster.databinding.ItemChuckerLogBinding

interface ChuckerItemListener {
    fun onLogClicked(model: ChuckerLogUIModel)
}

class ChuckerAdapter constructor(
    private var logs: MutableList<ChuckerLogUIModel> = mutableListOf(),
    private val listener: ChuckerItemListener
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

    fun updateItems(models: MutableList<ChuckerLogUIModel>) {
        val diff = DiffUtil.calculateDiff(
            ChuckerLogDiffUtilCallback(logs, models)
        )

        logs.clear()
        logs.addAll(models)
        diff.dispatchUpdatesTo(this)
    }

    class ChuckerViewHolder(
        private val binding: ItemChuckerLogBinding,
        private val listener: ChuckerItemListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChuckerLogUIModel) {
            binding.txtUrl.text = model.url
            binding.txtFps.text = model.fps
            binding.txtStartDate.text = model.startTime.timeFormat()

            itemView.setOnClickListener {
                listener.onLogClicked(model)
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: ChuckerItemListener): ChuckerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val inflate = ItemChuckerLogBinding.inflate(layoutInflater, parent, false)
                return ChuckerViewHolder(inflate, listener)
            }
        }

    }

}