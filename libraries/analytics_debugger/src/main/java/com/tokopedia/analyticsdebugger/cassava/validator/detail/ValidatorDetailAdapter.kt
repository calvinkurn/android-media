package com.tokopedia.analyticsdebugger.cassava.validator.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.GtmLogUi

class ValidatorDetailAdapter(actual: List<GtmLogUi>) : RecyclerView.Adapter<ValidatorDetailAdapter.DetailViewHolder>() {

    private val mData: MutableList<ExpectedItem> = actual.map { ExpectedItem(it) }.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_found_log, parent, false)
        return DetailViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val item = mData[position]

        holder.view.setOnClickListener {
            item.expanded = !item.expanded
            notifyItemChanged(position)
        }
        holder.bind(item)
    }

    inner class DetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        internal fun bind(item: ExpectedItem) {
            view.findViewById<TextView>(R.id.tv_timestamp).text = "Timestamp: ${Utils.getTimeStampFormat(item.data.timestamp)}"
            with(view.findViewById<TextView>(R.id.tv_actual)) {
                text = item.data.data ?: ""
                visibility = if (item.expanded) View.VISIBLE else View.GONE
            }
            val expandStateId = if (item.expanded) R.drawable.ic_expand_less_black_24dp else R.drawable.ic_expand_more_black_24dp
            view.findViewById<ImageView>(R.id.iv_expand).setImageResource(expandStateId)
        }
    }

}