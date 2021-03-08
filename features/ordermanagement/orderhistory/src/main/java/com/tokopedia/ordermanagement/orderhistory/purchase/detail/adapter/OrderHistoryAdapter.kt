package com.tokopedia.ordermanagement.orderhistory.purchase.detail.adapter

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.ordermanagement.orderhistory.R
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryListData

/**
 * Created by kris on 11/8/17. Tokopedia
 */
class OrderHistoryAdapter(private val historyListDatas: List<OrderHistoryListData>) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_history_adapter, parent, false)
        return OrderHistoryViewHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.orderHistoryTitle.text = (historyListDatas[position].actionBy
                + " - "
                + historyListDatas[position].orderHistoryDate)
        setTitleColor(holder, position)
        holder.orderHistoryComment.text = historyListDatas[position].orderHistoryComment
        holder.orderHistoryComment.visibility = if (historyListDatas[position]
                        .orderHistoryComment == "") View.GONE else View.VISIBLE
        holder.orderHistoryDescription.text = Html.fromHtml(historyListDatas[position].orderHistoryTitle)
        holder.orderHistoryTime.text = historyListDatas[position].orderHistoryTime
        holder.dot.setColorFilter(Color.parseColor(historyListDatas[position].color))
        if (position == 0) {
            holder.dotTraiTop.visibility = View.GONE
            holder.dotTrailBot.setBackgroundColor(
                    Color.parseColor(historyListDatas[position].color)
            )
        } else if (position == historyListDatas.size - 1) {
            holder.dotTrailBot.visibility = View.GONE
            holder.dotTraiTop.setBackgroundColor(
                    Color.parseColor(historyListDatas[position - 1].color)
            )
        } else {
            holder.dotTrailBot.setBackgroundColor(
                    Color.parseColor(historyListDatas[position].color)
            )
            holder.dotTraiTop.setBackgroundColor(
                    Color.parseColor(historyListDatas[position - 1].color)
            )
        }
    }

    private fun setTitleColor(holder: OrderHistoryViewHolder, position: Int) {
        if (position == 0) {
            holder.orderHistoryTitle.setTextColor(Color.parseColor(
                    historyListDatas[position].color
            ))
        } else holder.orderHistoryTitle.setTextColor(
                holder.context.resources.getColor(R.color.black_70))
    }

    override fun getItemCount(): Int {
        return historyListDatas.size
    }

    inner class OrderHistoryViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderHistoryTitle: TextView
        val orderHistoryDescription: TextView
        val orderHistoryTime: TextView
        val dot: ImageView
        val dotTrailBot: View
        val dotTraiTop: View
        val orderHistoryComment: TextView

        init {
            orderHistoryTitle = itemView.findViewById(R.id.history_title)
            orderHistoryDescription = itemView.findViewById(R.id.history_description)
            orderHistoryTime = itemView.findViewById(R.id.history_date)
            dot = itemView.findViewById(R.id.dot_image)
            dotTraiTop = itemView.findViewById(R.id.dot_trail_top)
            dotTrailBot = itemView.findViewById(R.id.dot_trail_bottom)
            orderHistoryComment = itemView.findViewById(R.id.history_comment)
        }
    }

}