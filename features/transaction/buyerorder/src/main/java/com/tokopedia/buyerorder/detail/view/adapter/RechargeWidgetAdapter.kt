package com.tokopedia.buyerorder.detail.view.adapter

import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
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
import com.tokopedia.buyerorder.detail.data.recommendationPojo.WidgetGridItem
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class RechargeWidgetAdapter(private val recommendationItems: List<WidgetGridItem>) : RecyclerView.Adapter<RechargeWidgetAdapter.RechargeWidgetViewHolder>() {

    private val viewMap = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeWidgetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_widget_itm_list, parent, false)
        return RechargeWidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: RechargeWidgetViewHolder, position: Int) {
        val item = recommendationItems[position]
        holder.renderTitle(item)
        holder.renderImage(item)
        holder.renderProduct(item)
        holder.renderSubtitle(item)
        holder.renderFooter(item)

        if (item.applink?.isNotEmpty() ?: false) {
            holder.itemView.setOnClickListener {
                RouteManager.route(holder.itemView.context, item.applink)
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

        private var title: TextView = itemView.findViewById(R.id.title)
        private var icon: AppCompatImageView = itemView.findViewById(R.id.icon)
        private var productName: TextView = itemView.findViewById(R.id.productName)
        private var subtitle: TextView = itemView.findViewById(R.id.subtitle)
        private var footer: View = itemView.findViewById(R.id.footer)
        private var tagLine: TextView = itemView.findViewById(R.id.tagLine)
        private var pricePrefix: TextView = itemView.findViewById(R.id.pricePrefix)
        private var strikeThroughPrice: TextView = itemView.findViewById(R.id.strikeThroughPrice)
        private var price: TextView = itemView.findViewById(R.id.price)

        fun renderTitle(element: WidgetGridItem) {
            if (element.titleFirst.isNullOrEmpty()) {
                title.hide()
            } else {
                title.show()
                title.text = MethodChecker.fromHtml(element.titleFirst)
            }

            if (element.descFirst.isNullOrEmpty()) {
                if ((hasPrice(element) || hasTagLabel(element))) {
                    title.maxLines = 2
                } else {
                    title.maxLines = 3
                }
            } else {
                title.maxLines = 1
            }
        }

        fun renderImage(element: WidgetGridItem) {
            ImageHandler.loadImageThumbs(itemView.context, icon, element.imageUrl)

        }

        private fun hasPrice(element: WidgetGridItem): Boolean {
            return element.price.isNullOrEmpty().not()
                    || element.pricePrefix.isNullOrEmpty().not()
                    || element.originalPrice.isNullOrEmpty().not()
        }

        private fun hasTagLabel(element: WidgetGridItem): Boolean {
            return !element.tagName.isNullOrEmpty()
        }

        fun renderProduct(element: WidgetGridItem) {
            if (element.name.isNullOrEmpty()) {
                productName.hide()
            } else {
                productName.show()
                productName.text = MethodChecker.fromHtml(element.name)
            }
        }

        fun renderSubtitle(element: WidgetGridItem) {
            if (element.descFirst.isNullOrEmpty()) {
                subtitle.hide()
            } else {
                subtitle.show()
                subtitle.text = MethodChecker.fromHtml(element.descFirst)
            }

            if (element.titleFirst.isNullOrEmpty() &&
                    element.tagName.isNullOrEmpty()) {
                if (hasPrice(element) || hasTagLabel(element)) {
                    subtitle.maxLines = 2
                } else {
                    subtitle.maxLines = 3
                }
            } else {
                subtitle.maxLines = 1
            }
        }

        fun renderFooter(element: WidgetGridItem) {
            if (hasPrice(element) || hasTagLabel(element)) {
                footer.show()
                renderLabel(element)
                renderPrice(element)
            } else {
                footer.hide()
            }
        }


        fun renderLabel(element: WidgetGridItem) {
            if (hasTagLabel(element)) {
                tagLine.show()
                tagLine.text = MethodChecker.fromHtml(element.tagName)
                when (element.tagType) {
                    1 -> {
                        MethodChecker.setBackground(tagLine,findMyDrawable(R.drawable.bg_rounded_pink_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
                    }
                    2 -> {
                        MethodChecker.setBackground(tagLine,findMyDrawable(R.drawable.bg_rounded_green_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    }
                    3 -> {
                        MethodChecker.setBackground(tagLine,findMyDrawable(R.drawable.bg_rounded_blue_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_B500))
                    }
                    4 -> {
                        MethodChecker.setBackground(tagLine,findMyDrawable(R.drawable.bg_rounded_yellow_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Y400))
                    }
                    5 -> {
                        MethodChecker.setBackground(tagLine,findMyDrawable(R.drawable.bg_rounded_grey_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N200))
                    }
                    else -> {
                        tagLine.hide()
                    }
                }
            } else {
                tagLine.hide()
            }

        }

        private fun findMyDrawable(drawable: Int): Drawable {
            return itemView.context.resources.getDrawable(drawable)
        }

        private fun renderPrice(element: WidgetGridItem) {
            if (hasPrice(element)) {

                if (element.pricePrefix.isNullOrEmpty()) {
                    pricePrefix.hide()
                } else {
                    pricePrefix.show()
                    pricePrefix.text = MethodChecker.fromHtml(element.pricePrefix)
                }

                if (element.originalPrice.isNullOrEmpty()) {
                    strikeThroughPrice.hide()
                } else {
                    strikeThroughPrice.show()
                    strikeThroughPrice.text = MethodChecker.fromHtml(element.originalPrice)
                    strikeThroughPrice.paintFlags = strikeThroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }

                if (element.price.isNullOrEmpty()) {
                    price.hide()
                } else {
                    price.show()
                    price.text = MethodChecker.fromHtml(element.price)
                }

            } else {
                price.hide()
                pricePrefix.hide()
                strikeThroughPrice.hide()
            }
        }


    }
}

