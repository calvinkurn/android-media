package com.tokopedia.buyerorder.detail.view.adapter

import android.graphics.drawable.Drawable
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo.RecommendationsItem
import com.tokopedia.buyerorder.detail.view.OrderListAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class RecommendationMPAdapter(private val recommendationItems: List<RecommendationsItem>) : RecyclerView.Adapter<RecommendationMPAdapter.RecommendationViewHolder>() {

    private val viewMap = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecommendationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_widget_itm_list, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val item = recommendationItems[position]
        holder.renderTitle(item)
        holder.renderImage(item)
        holder.renderProduct(item)
        holder.renderSubtitle(item)
        holder.renderFooter(item)

        if (item.appLink?.isNotEmpty() ?: false) {
            holder.itemView.setOnClickListener {
                RouteManager.route(holder.itemView.context, item.appLink)
                OrderListAnalytics.eventRecommendationClick(item, position)
            }
        }

    }


    override fun getItemCount(): Int {
        return recommendationItems.size
    }

    override fun onViewAttachedToWindow(holder: RecommendationViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        if (!viewMap[position]) {
            viewMap.put(position, true)
            OrderListAnalytics.eventRecommendationListView(recommendationItems[position], position)
        }
    }

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var title: TextView = itemView.findViewById(R.id.title)
        private var icon: AppCompatImageView = itemView.findViewById(R.id.icon)
        private var productName: TextView = itemView.findViewById(R.id.productName)
        private var subtitle: TextView = itemView.findViewById(R.id.subtitle)
        private var footer: View = itemView.findViewById(R.id.footer)
        private var tagLine: TextView = itemView.findViewById(R.id.tagLine)
        private var pricePrefix: TextView = itemView.findViewById(R.id.pricePrefix)
        private var strikeThroughPrice: TextView = itemView.findViewById(R.id.strikeThroughPrice)
        private var price: TextView = itemView.findViewById(R.id.price)

        fun renderTitle(element: RecommendationsItem) {
            if (element.title.isNullOrEmpty()) {
                title.hide()
            } else {
                title.show()
                title.text = MethodChecker.fromHtml(element.title)
            }

            if (element.clientNumber.isNullOrEmpty()) {
                if (hasTagLabel(element)) {
                    title.maxLines = 2
                } else {
                    title.maxLines = 3
                }
            } else {
                title.maxLines = 1
            }
        }

        fun renderImage(element: RecommendationsItem) {
            ImageHandler.loadImageThumbs(itemView.context, icon, element.iconUrl)

        }

        private fun hasPrice(element: RecommendationsItem): Boolean {
            return element.productPrice.toString().isNullOrEmpty().not()
        }

        private fun hasTagLabel(element: RecommendationsItem): Boolean {
            return !element.tag.isNullOrEmpty()
        }

        fun renderProduct(element: RecommendationsItem) {
            if (element.title.isNullOrEmpty()) {
                productName.hide()
            } else {
                productName.show()
                productName.text = MethodChecker.fromHtml(element.title)
            }
        }

        fun renderSubtitle(element: RecommendationsItem) {
            if (element.clientNumber.isNullOrEmpty()) {
                subtitle.hide()
            } else {
                subtitle.show()
                subtitle.text = MethodChecker.fromHtml(element.clientNumber)
            }

            if (element.title.isNullOrEmpty() &&
                    element.tag.isNullOrEmpty()) {
                if (hasTagLabel(element)) {
                    subtitle.maxLines = 2
                } else {
                    subtitle.maxLines = 3
                }
            } else {
                subtitle.maxLines = 1
            }
        }

        fun renderFooter(element: RecommendationsItem) {
            if (hasTagLabel(element)) {
                footer.show()
                renderLabel(element)
                //  renderPrice(element)
            } else {
                footer.hide()
            }
        }


        fun renderLabel(element: RecommendationsItem) {
            if (hasTagLabel(element)) {
                tagLine.show()
                tagLine.text = MethodChecker.fromHtml(element.tag)
                when (element.tagType) {
                    1 -> {
                        MethodChecker.setBackground(tagLine, findMyDrawable(R.drawable.bg_rounded_pink_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
                    }
                    2 -> {
                        MethodChecker.setBackground(tagLine, findMyDrawable(R.drawable.bg_rounded_green_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    }
                    3 -> {
                        MethodChecker.setBackground(tagLine, findMyDrawable(R.drawable.bg_rounded_blue_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_B500))
                    }
                    4 -> {
                        MethodChecker.setBackground(tagLine, findMyDrawable(R.drawable.bg_rounded_yellow_label_buyer))
                        tagLine.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Y400))
                    }
                    5 -> {
                        MethodChecker.setBackground(tagLine, findMyDrawable(R.drawable.bg_rounded_grey_label_buyer))
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

        private fun renderPrice(element: RecommendationsItem) {
            if (hasPrice(element)) {

                if (element.productPrice.toString().isNullOrEmpty()) {
                    price.hide()
                } else {
                    price.show()
                    price.text = MethodChecker.fromHtml(element.productPrice.toString())
                }

            } else {
                price.hide()
            }
        }
    }
}

