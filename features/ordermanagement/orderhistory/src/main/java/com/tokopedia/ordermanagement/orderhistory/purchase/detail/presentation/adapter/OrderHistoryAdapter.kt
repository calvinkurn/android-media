package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.ordermanagement.orderhistory.R
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryListData
import com.tokopedia.unifyprinciples.Typography

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
        val color = historyListDatas.getOrNull(position)?.color ?: ""
        val previousColor = historyListDatas.getOrNull(position - 1)?.color ?: ""
        holder.dot.setColorFilter(Color.parseColor(color))
        when (position) {
            0 -> {
                holder.dotTraiTop.visibility = View.GONE
                holder.dotTrailBot.setBackgroundColor(
                        Color.parseColor(color)
                )
            }
            historyListDatas.size - 1 -> {
                holder.dotTrailBot.visibility = View.GONE
                holder.dotTraiTop.setBackgroundColor(
                        Color.parseColor(previousColor)
                )
            }
            else -> {
                holder.dotTrailBot.setBackgroundColor(
                        Color.parseColor(color)
                )
                holder.dotTraiTop.setBackgroundColor(
                        Color.parseColor(previousColor)
                )
            }
        }
    }

    private fun setTitleColor(holder: OrderHistoryViewHolder, position: Int) {
        if (position == 0) {
            historyListDatas.getOrNull(position)?.color?.let {
                holder.orderHistoryTitle.setTextColor(Color.parseColor(it))
            }
        } else holder.orderHistoryTitle.setTextColor(
                holder.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
    }

    override fun getItemCount(): Int {
        return historyListDatas.size
    }

    inner class OrderHistoryViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderHistoryTitle: Typography = itemView.findViewById(R.id.history_title)
        val orderHistoryDescription: Typography = itemView.findViewById(R.id.history_description)
        val orderHistoryTime: Typography = itemView.findViewById(R.id.history_date)
        val dot: ImageView = itemView.findViewById(R.id.dot_image)
        val dotTrailBot: View = itemView.findViewById(R.id.dot_trail_bottom)
        val dotTraiTop: View = itemView.findViewById(R.id.dot_trail_top)
        val orderHistoryComment: Typography = itemView.findViewById(R.id.history_comment)
    }

}