package com.tokopedia.analyticsdebugger.sse.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
class SSELogAdapter: RecyclerView.Adapter<SSELogViewHolder>() {

    private val data = mutableListOf<SSELogUiModel>()

    private var clickListener: ((SSELogUiModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SSELogViewHolder =
        SSELogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sse_log_list, parent, false))

    override fun onBindViewHolder(holder: SSELogViewHolder, position: Int) {
        holder.bind(data[position])
        holder.setOnClickListener {
            clickListener?.invoke(it)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newList: List<SSELogUiModel>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (SSELogUiModel) -> Unit) {
        this.clickListener = listener
    }
}