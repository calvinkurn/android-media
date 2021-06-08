package com.tokopedia.buyerorder.detail.view.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationItem
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class RechargeWidgetAdapter(private val recommendationItems: List<RecommendationItem>) : RecyclerView.Adapter<RechargeWidgetAdapter.RechargeWidgetViewHolder>() {

    private val viewMap = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeWidgetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_order_detail_widget, parent, false)
        return RechargeWidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: RechargeWidgetViewHolder, position: Int) {
        val item = recommendationItems[position]
        holder.renderCategoryName(item)
        holder.renderProductName(item)
        holder.renderImage(item)
        holder.renderClientNumber(item)

        if (!item.appLink.isNullOrEmpty()) {
            holder.itemView.setOnClickListener {
                RouteManager.route(holder.itemView.context, item.appLink)
                OrderListAnalytics.eventWidgetClick(item, position)
            }
        }

    }


    override fun getItemCount(): Int {
        return recommendationItems.size
    }

    override fun onViewAttachedToWindow(holder: RechargeWidgetViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap[position]) {
            viewMap.put(position, true)
            OrderListAnalytics.eventWidgetListView(recommendationItems[position], position)
        }
    }

    class RechargeWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var categoryName: TextView = itemView.findViewById(R.id.categoryName)
        private var productName: TextView = itemView.findViewById(R.id.productName)
        private var icon: AppCompatImageView = itemView.findViewById(R.id.icon)
        private var clientNumber: TextView = itemView.findViewById(R.id.clientNumber)

        fun renderCategoryName(element: RecommendationItem) {
            if (element.title.isNullOrEmpty()) {
                categoryName.hide()
            } else {
                categoryName.show()
                categoryName.text = MethodChecker.fromHtml(element.title)
            }
        }

        fun renderProductName(element: RecommendationItem) {
            if (element.subtitle.isNullOrEmpty()) {
                productName.hide()
            } else {
                productName.show()
                productName.text = MethodChecker.fromHtml(element.subtitle)
            }
        }

        fun renderImage(element: RecommendationItem) {
            ImageHandler.loadImageThumbs(itemView.context, icon, element.mediaURL)
        }

        fun renderClientNumber(element: RecommendationItem) {
            if (element.label1.isNullOrEmpty()) {
                clientNumber.hide()
            } else {
                clientNumber.show()
                clientNumber.text = MethodChecker.fromHtml(element.label1) // TODO: [Misael] check ini masi butuh fromhtml atau ngga
            }
        }
    }
}

