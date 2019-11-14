package com.tokopedia.sellerorder.list.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.presentation.fragment.SomListFragment
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.som_list_item.view.*

/**
 * Created by fwidjaja on 2019-08-26.
 */
class SomListItemAdapter : RecyclerView.Adapter<SomListItemAdapter.ViewHolder>() {

    private lateinit var actionListener: ActionListener

    interface ActionListener {
        fun onListItemClicked(orderId: String)
    }

    var somItemList = mutableListOf<SomListOrder.Data.OrderList.Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.som_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return somItemList.size
    }

    @SuppressLint("Range", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.label_status_order.text = somItemList[position].status
        holder.itemView.label_status_order.setBackgroundColor(Color.parseColor(somItemList[position].statusColor))
        holder.itemView.label_invoice.text = somItemList[position].orderResi
        holder.itemView.ic_product.loadImage(somItemList[position].listOrderProduct[0].pictureUrl, com.tokopedia.design.R.drawable.ic_loading_image)
        holder.itemView.label_date_order.text = somItemList[position].orderDate
        holder.itemView.label_buyer_name.text = somItemList[position].buyerName
        holder.itemView.label_due_response_day_count.text = somItemList[position].deadlineText
        holder.itemView.ic_time.loadImageDrawable(R.drawable.ic_label_due_time)
        holder.itemView.ic_time.setColorFilter(Color.WHITE)
        holder.itemView.ic_label_due_card.setCardBackgroundColor(Color.parseColor(somItemList[position].deadlineColor))

        val totalProducts = somItemList[position].listOrderProduct.size
        if (totalProducts > 1) {
            holder.itemView.rl_overlay_product?.visibility = View.VISIBLE
            holder.itemView.label_total_product?.text = "$totalProducts\n Produk"
        } else {
            holder.itemView.rl_overlay_product?.visibility = View.GONE
        }

        if (somItemList[position].listOrderLabel.isNotEmpty()) {
            createOrderLabelList(holder, position)
        } else {
            holder.itemView.ll_label_order?.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            actionListener.onListItemClicked(somItemList[position].orderId)
        }
    }

    @SuppressLint("Range", "InflateParams")
    private fun createOrderLabelList(holder: ViewHolder, position: Int) {
        holder.itemView.ll_label_order?.removeAllViews()

        // soon will be change with unify label (for now, label unify cannot set color background & text
        somItemList[position].listOrderLabel.forEach {
            val cardView = CardView(holder.itemView.context)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            cardView.layoutParams = layoutParams
            cardView.radius = 3F
            cardView.cardElevation = 0F
            cardView.setCardBackgroundColor(Color.parseColor(it.flagBackground))

            val displayMetrics = holder.itemView.context.resources.displayMetrics
            val marginRightLeft = 4.dpToPx(displayMetrics)
            val marginTopBottom = 2.dpToPx(displayMetrics)
            val text = Typography(holder.itemView.context)
            text.layoutParams = layoutParams
            text.setType(Typography.SMALL)
            text.setWeight(Typography.BOLD)
            text.text = it.flagName
            text.setTextColor(Color.parseColor(it.flagColor))
            text.setMargin(marginRightLeft, marginTopBottom, marginRightLeft, marginTopBottom)
            cardView.addView(text)

            holder.itemView.ll_label_order.addView(cardView)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setActionListener(fragment: SomListFragment) {
        this.actionListener = fragment
    }
}