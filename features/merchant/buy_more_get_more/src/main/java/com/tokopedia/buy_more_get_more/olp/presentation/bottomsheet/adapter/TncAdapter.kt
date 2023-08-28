package com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.databinding.ItemTncBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

class TncAdapter : RecyclerView.Adapter<TncAdapter.ViewHolder>() {
    private var data: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTncBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it, position)
        }
    }

    fun setDataList(newData: List<String>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class ViewHolder(
        private val binding: ItemTncBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, position: Int) {
            with(binding) {
                tpgNumber.text = MethodChecker.fromHtml((position+Int.ONE).toString())
                tpgTnc.text = item
            }
        }
    }
}

