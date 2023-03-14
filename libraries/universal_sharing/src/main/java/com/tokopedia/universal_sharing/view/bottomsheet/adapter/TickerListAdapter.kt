package com.tokopedia.universal_sharing.view.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.universal_sharing.model.TickerShareModel
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.TickerListAdapterViewHolder

class TickerListAdapter(private val onClick: (data: TickerShareModel) -> Unit) : RecyclerView.Adapter<TickerListAdapterViewHolder>() {

    private val tickerList = mutableListOf<TickerShareModel>()

    fun addItem(data: TickerShareModel) {
        tickerList.add(data)
        notifyItemInserted(tickerList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerListAdapterViewHolder {
        return TickerListAdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(com.tokopedia.universal_sharing.R.layout.item_ticker, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TickerListAdapterViewHolder, position: Int) {
        val data = tickerList[position]
        holder.title.text = data.title
        holder.description.text = data.description
        holder.image.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, data.imageResDrawable))

        holder.itemView.setOnClickListener {
            onClick.invoke(data)
            data.callback.invoke()
        }
    }

    override fun getItemCount(): Int {
        return tickerList.size
    }
}
