package com.tokopedia.universal_sharing.view.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.model.BroadcastChannelModel
import com.tokopedia.universal_sharing.model.TickerShareModel

class TickerListAdapter(private val onClick: (data: TickerShareModel) -> Unit) : RecyclerView.Adapter<TickerListAdapter.TickerListAdapterViewHolder>() {

    private val tickerList = mutableListOf<TickerShareModel>()


    fun addItem(data: TickerShareModel) {
        tickerList.add(data)
        notifyItemInserted(tickerList.size - 1)
    }

    inner class TickerListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<Typography>(com.tokopedia.universal_sharing.R.id.tv_title_ticker)
        val description = view.findViewById<Typography>(com.tokopedia.universal_sharing.R.id.tv_description_ticker)
        val image = view.findViewById<ImageView>(com.tokopedia.universal_sharing.R.id.iv_ticker)
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
        }
    }

    override fun getItemCount(): Int {
        return tickerList.size
    }

    companion object {
        private const val IC_BROADCAST = "https://images.tokopedia.net/img/share/icons/ic_broadcast.png"
    }
}
