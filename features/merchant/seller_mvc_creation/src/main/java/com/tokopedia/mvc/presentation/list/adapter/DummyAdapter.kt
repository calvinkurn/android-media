package com.tokopedia.mvc.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.databinding.SmvcDummyBinding

class DummyAdapter: RecyclerView.Adapter<DummyAdapter.CriteriaViewHolder>() {

    private var data: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = SmvcDummyBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<String>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class CriteriaViewHolder(private val binding: SmvcDummyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            val numberText = "${adapterPosition.inc()}."
            binding.tvText.text = numberText + title
        }
    }
}
