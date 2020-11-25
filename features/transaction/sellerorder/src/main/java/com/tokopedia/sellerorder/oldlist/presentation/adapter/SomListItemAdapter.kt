package com.tokopedia.sellerorder.oldlist.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.util.SomConsts.LABEL_EMPTY
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.oldlist.data.model.SomListOrder
import com.tokopedia.sellerorder.oldlist.presentation.fragment.SomListFragment
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.som_list_item.view.*
import java.lang.StringBuilder

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
            with(holder.itemView) {
                somItemList.getOrNull(position)?.let { orderItem ->
                    label_status_order.text = orderItem.status

                    if (orderItem.tickerInfo.text.isNotBlank()) {
                        orderItem.tickerInfo.let { tickerInfo ->
                            setupTicker(holder.itemView.ticker_buyer_request_cancel, tickerInfo)
                            ticker_buyer_request_cancel?.show()
                        }
                    } else {
                        ticker_buyer_request_cancel?.hide()
                    }

                    if (orderItem.statusColor.isNotEmpty() && !orderItem.statusColor.equals(LABEL_EMPTY, true)) {
                        label_status_order.setBackgroundColor(Color.parseColor(orderItem.statusColor))
                    }
                    label_invoice.text = orderItem.orderResi
                    if (orderItem.listOrderProduct.isNotEmpty()) {
                        ic_product.loadImage(orderItem.listOrderProduct[0].pictureUrl, com.tokopedia.design.R.drawable.ic_loading_image)
                    }
                    label_date_order.text = orderItem.orderDate
                    label_buyer_name.text = orderItem.buyerName

                    if (orderItem.deadlineText.isEmpty() || orderItem.deadlineText.equals(LABEL_EMPTY, true)) {
                        label_due_response.visibility = View.GONE
                        ic_label_due_card.visibility = View.GONE
                    } else {
                        if (orderItem.orderStatusId == STATUS_CODE_ORDER_DELIVERED || orderItem.orderStatusId == STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT) {
                            label_due_response.text = holder.itemView.context.getString(R.string.som_deadline_done)
                        } else {
                            label_due_response.text = holder.itemView.context.getString(R.string.som_deadline)
                        }
                        label_due_response.visibility = View.VISIBLE
                        ic_label_due_card.visibility = View.VISIBLE
                        label_due_response_day_count.text = orderItem.deadlineText
                        ic_time.loadImageDrawable(R.drawable.ic_label_due_time)
                        ic_time.setColorFilter(androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                        if (orderItem.deadlineColor.isNotEmpty() && !orderItem.deadlineColor.equals(LABEL_EMPTY, true)) {
                            holder.itemView.ic_label_due_card.setCardBackgroundColor(Color.parseColor(orderItem.deadlineColor))
                        }
                    }

                    val totalProducts = orderItem.listOrderProduct.size
                    if (totalProducts > 1) {
                        rl_overlay_product?.visibility = View.VISIBLE
                        label_total_product?.text = StringBuilder("$totalProducts\n Produk")
                    } else {
                        rl_overlay_product?.visibility = View.GONE
                    }

                    if (orderItem.listOrderLabel.isNotEmpty()) {
                        ll_label_order?.visibility = View.VISIBLE
                        createOrderLabelList(holder, position)
                    } else {
                        ll_label_order?.visibility = View.GONE
                    }

                    val orderId = orderItem.orderId
                    setOnClickListener {
                        actionListener?.onListItemClicked(orderId)
                    }
                }
            }
        }
    }

    private fun setupTicker(tickerBuyerRequestCancel: Ticker?, tickerInfo: TickerInfo) {
        tickerBuyerRequestCancel?.apply {
            setTextDescription(tickerInfo.text)
            tickerType = Utils.mapStringTickerTypeToUnifyTickerType(tickerInfo.type)
            closeButtonVisibility = View.GONE
        }
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}