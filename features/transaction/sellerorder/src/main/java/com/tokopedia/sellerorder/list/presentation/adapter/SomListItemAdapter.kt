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
            holder.itemView.label_status_order.text = somItemList[position].status

            if (somItemList[position].cancelRequest == 1) {
                setupTicker(holder.itemView.ticker_buyer_request_cancel, somItemList.getOrNull(position)?.tickerInfo)
                holder.itemView.ticker_buyer_request_cancel?.show()
            } else {
                holder.itemView.ticker_buyer_request_cancel?.hide()
            }

            if (somItemList[position].statusColor.isNotEmpty() && !somItemList[position].statusColor.equals(LABEL_EMPTY, true)) {
                holder.itemView.label_status_order.setBackgroundColor(Color.parseColor(somItemList[position].statusColor))
            }
            holder.itemView.label_invoice.text = somItemList[position].orderResi
            if (somItemList[position].listOrderProduct.isNotEmpty()) {
                holder.itemView.ic_product.loadImage(somItemList[position].listOrderProduct[0].pictureUrl, com.tokopedia.design.R.drawable.ic_loading_image)
            }
            holder.itemView.label_date_order.text = somItemList[position].orderDate
            holder.itemView.label_buyer_name.text = somItemList[position].buyerName

            if (somItemList[position].deadlineText.isEmpty() || somItemList[position].deadlineText.equals(LABEL_EMPTY, true)) {
                holder.itemView.label_due_response.visibility = View.GONE
                holder.itemView.ic_label_due_card.visibility = View.GONE
            } else {
                if (somItemList[position].orderStatusId == STATUS_ORDER_DELIVERED || somItemList[position].orderStatusId == STATUS_ORDER_DELIVERED_DUE_LIMIT) {
                    holder.itemView.label_due_response.text = holder.itemView.context.getString(R.string.som_deadline_done)
                } else {
                    holder.itemView.label_due_response.text = holder.itemView.context.getString(R.string.som_deadline)
                }
                holder.itemView.label_due_response.visibility = View.VISIBLE
                holder.itemView.ic_label_due_card.visibility = View.VISIBLE
                holder.itemView.label_due_response_day_count.text = somItemList[position].deadlineText
                holder.itemView.ic_time.loadImageDrawable(R.drawable.ic_label_due_time)
                holder.itemView.ic_time.setColorFilter(Color.WHITE)
                if (somItemList[position].deadlineColor.isNotEmpty() && !somItemList[position].deadlineColor.equals(LABEL_EMPTY, true)) {
                    holder.itemView.ic_label_due_card.setCardBackgroundColor(Color.parseColor(somItemList[position].deadlineColor))
                }
            }

            val totalProducts = somItemList[position].listOrderProduct.size
            if (totalProducts > 1) {
                holder.itemView.rl_overlay_product?.visibility = View.VISIBLE
                holder.itemView.label_total_product?.text = "$totalProducts\n Produk"
            } else {
                holder.itemView.rl_overlay_product?.visibility = View.GONE
            }

            if (somItemList[position].listOrderLabel.isNotEmpty()) {
                holder.itemView.ll_label_order?.visibility = View.VISIBLE
                createOrderLabelList(holder, position)
            } else {
                holder.itemView.ll_label_order?.visibility = View.GONE
            }

            val orderId = somItemList[position].orderId
            holder.itemView.setOnClickListener {
                actionListener?.onListItemClicked(orderId)
            }
        }
    }

    private fun setupTicker(tickerBuyerRequestCancel: Ticker?, tickerInfo: SomListOrder.Data.OrderList.Order.TickerInfo?) {
        tickerBuyerRequestCancel?.apply {
            val tickerDescription = makeTickerDescription(context, tickerInfo)
            setTextDescription(tickerDescription)
            setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.route(context, String.format("%s?=url", ApplinkConst.WEBVIEW, tickerInfo?.linkUrl))
                }
                override fun onDismiss() {}
            })
            tickerType = Utils.mapBackgroundColorToUnifyTickerType(tickerInfo?.backgroundColor.orEmpty())
            closeButtonVisibility = View.GONE
        }
    }

    private fun makeTickerDescription(context: Context, tickerInfo: SomListOrder.Data.OrderList.Order.TickerInfo?): String {
        val message = tickerInfo?.message.orEmpty()
        val messageLink = tickerInfo?.linkText.orEmpty()
        val spannedMessage = SpannableStringBuilder()
                .append(message)
                .append(". $messageLink")

        if (message.isNotBlank() && tickerInfo?.textColor.orEmpty().length > 1) {
            spannedMessage.setSpan(
                    ForegroundColorSpan(Color.parseColor(tickerInfo?.textColor)),
                    0,
                    message.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        if (messageLink.isNotBlank() && tickerInfo?.textColor.orEmpty().length > 1) {
            spannedMessage.setSpan(
                    UrlSpanNoUnderline(messageLink),
                    message.length + 2,
                    message.length + messageLink.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannedMessage.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500)),
                    message.length + 2,
                    message.length + messageLink.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannedMessage.toString()
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
        somItemList[position].listOrderLabel.forEach {
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}