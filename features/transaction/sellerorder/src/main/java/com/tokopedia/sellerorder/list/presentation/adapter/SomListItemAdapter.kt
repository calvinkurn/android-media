package com.tokopedia.sellerorder.list.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.util.SomConsts.LABEL_EMPTY
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ORDER_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ORDER_DELIVERED_DUE_LIMIT
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.presentation.fragment.SomListFragment
import com.tokopedia.unifycomponents.UrlSpanNoUnderline
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.som_list_item.view.*

/**
 * Created by fwidjaja on 2019-08-26.
 */
class SomListItemAdapter : RecyclerView.Adapter<SomListItemAdapter.ViewHolder>() {

    private var actionListener: ActionListener? = null

    private var somItemList = mutableListOf<SomListOrder.Data.OrderList.Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.som_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return somItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (somItemList.isNotEmpty()) {
            somItemList.getOrNull(position)?.let { orderItem ->
                holder.itemView.label_status_order.text = orderItem.status

                if (orderItem.cancelRequest == 1 && orderItem.tickerInfo.text.isNotBlank()) {
                    orderItem.tickerInfo.let { tickerInfo ->
                        holder.setTickerDescriptionClickListener(tickerInfo.actionUrl)
                        setupTicker(holder.tickerDescriptionClickListener, holder.itemView.ticker_buyer_request_cancel, tickerInfo)
                        holder.itemView.ticker_buyer_request_cancel?.show()
                    }
                } else {
                    holder.itemView.ticker_buyer_request_cancel?.hide()
                }

                if (orderItem.statusColor.isNotEmpty() && !orderItem.statusColor.equals(LABEL_EMPTY, true)) {
                    holder.itemView.label_status_order.setBackgroundColor(Color.parseColor(orderItem.statusColor))
                }
                holder.itemView.label_invoice.text = orderItem.orderResi
                if (orderItem.listOrderProduct.isNotEmpty()) {
                    holder.itemView.ic_product.loadImage(orderItem.listOrderProduct[0].pictureUrl, com.tokopedia.design.R.drawable.ic_loading_image)
                }
                holder.itemView.label_date_order.text = orderItem.orderDate
                holder.itemView.label_buyer_name.text = orderItem.buyerName

                if (orderItem.deadlineText.isEmpty() || orderItem.deadlineText.equals(LABEL_EMPTY, true)) {
                    holder.itemView.label_due_response.visibility = View.GONE
                    holder.itemView.ic_label_due_card.visibility = View.GONE
                } else {
                    if (orderItem.orderStatusId == STATUS_ORDER_DELIVERED || orderItem.orderStatusId == STATUS_ORDER_DELIVERED_DUE_LIMIT) {
                        holder.itemView.label_due_response.text = holder.itemView.context.getString(R.string.som_deadline_done)
                    } else {
                        holder.itemView.label_due_response.text = holder.itemView.context.getString(R.string.som_deadline)
                    }
                    holder.itemView.label_due_response.visibility = View.VISIBLE
                    holder.itemView.ic_label_due_card.visibility = View.VISIBLE
                    holder.itemView.label_due_response_day_count.text = orderItem.deadlineText
                    holder.itemView.ic_time.loadImageDrawable(R.drawable.ic_label_due_time)
                    holder.itemView.ic_time.setColorFilter(Color.WHITE)
                    if (orderItem.deadlineColor.isNotEmpty() && !orderItem.deadlineColor.equals(LABEL_EMPTY, true)) {
                        holder.itemView.ic_label_due_card.setCardBackgroundColor(Color.parseColor(orderItem.deadlineColor))
                    }
                }

                val totalProducts = orderItem.listOrderProduct.size
                if (totalProducts > 1) {
                    holder.itemView.rl_overlay_product?.visibility = View.VISIBLE
                    holder.itemView.label_total_product?.text = "$totalProducts\n Produk"
                } else {
                    holder.itemView.rl_overlay_product?.visibility = View.GONE
                }

                if (orderItem.listOrderLabel.isNotEmpty()) {
                    holder.itemView.ll_label_order?.visibility = View.VISIBLE
                    createOrderLabelList(holder, position)
                } else {
                    holder.itemView.ll_label_order?.visibility = View.GONE
                }

                val orderId = orderItem.orderId
                holder.itemView.setOnClickListener {
                    actionListener?.onListItemClicked(orderId)
                }
            }
        }
    }

    private fun setupTicker(listener: TickerCallback?, tickerBuyerRequestCancel: Ticker?, tickerInfo: TickerInfo) {
        tickerBuyerRequestCancel?.apply {
            setOnClickListener { listener?.onDescriptionViewClick(tickerInfo.actionUrl) }
            val tickerDescription = makeTickerDescription(context, tickerInfo)
            setTextDescription(tickerDescription)
            listener?.let { setDescriptionClickEvent(it) }
            tickerType = Utils.mapBackgroundColorToUnifyTickerType(tickerInfo.type)
            closeButtonVisibility = View.GONE
        }
    }

    private fun makeTickerDescription(context: Context, tickerInfo: TickerInfo): SpannableStringBuilder {
        val message = tickerInfo.text
        val messageLink = tickerInfo.actionText
        val spannedMessage = SpannableStringBuilder()
                .append(message)
                .append(" $messageLink")

        if (messageLink.isNotBlank()) {
            spannedMessage.setSpan(
                    UrlSpanNoUnderline(messageLink),
                    message.length,
                    spannedMessage.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannedMessage.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500)),
                    message.length,
                    spannedMessage.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannedMessage
    }

    fun setActionListener(fragment: SomListFragment) {
        this.actionListener = fragment
    }

    fun addList(list: List<SomListOrder.Data.OrderList.Order>) {
        somItemList.clear()
        somItemList.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list: List<SomListOrder.Data.OrderList.Order>) {
        somItemList.addAll(list)
        notifyDataSetChanged()
    }

    private fun createOrderLabelList(holder: ViewHolder, position: Int) {
        holder.itemView.ll_label_order?.removeAllViews()

        // soon will be change with unify label (for now, label unify cannot set color background & text
        somItemList.getOrNull(position)?.listOrderLabel?.forEach {
            val cardView = CardView(holder.itemView.context)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            cardView.layoutParams = layoutParams
            cardView.radius = 3F
            cardView.cardElevation = 0F
            if (it.flagBackground.isNotEmpty() && !it.flagBackground.equals(LABEL_EMPTY, true)) {
                cardView.setCardBackgroundColor(Color.parseColor(it.flagBackground))
            }

            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val marginRightLeft = 4.dpToPx(displayMetrics)
            val marginTopBottom = 2.dpToPx(displayMetrics)
            val text = Typography(holder.itemView.context)
            text.layoutParams = layoutParams
            text.setType(Typography.SMALL)
            text.setWeight(Typography.BOLD)
            text.text = it.flagName
            if (it.flagColor.isNotEmpty() && !it.flagColor.equals(LABEL_EMPTY, true)) {
                text.setTextColor(Color.parseColor(it.flagColor))
            }
            text.setMargin(marginRightLeft, marginTopBottom, marginRightLeft, marginTopBottom)
            cardView.addView(text)
            holder.itemView.ll_label_order.addView(cardView)
        }
    }

    interface ActionListener {
        fun onListItemClicked(orderId: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tickerDescriptionClickListener: TickerCallback? = null

        fun setTickerDescriptionClickListener(link: String) {
            tickerDescriptionClickListener = object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    if (link.isNotBlank()) {
                        RouteManager.route(itemView.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, link))
                    } else {
                        itemView.performClick()
                    }
                }
                override fun onDismiss() {}
            }
        }
    }
}