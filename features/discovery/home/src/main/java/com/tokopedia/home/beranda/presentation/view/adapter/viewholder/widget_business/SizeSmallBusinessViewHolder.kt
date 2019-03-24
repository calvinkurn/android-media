package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.layout_template_footer_business.view.*
import kotlinx.android.synthetic.main.layout_template_icon_business_widget.view.*
import kotlinx.android.synthetic.main.layout_template_small_business.view.*


open class SizeSmallBusinessViewHolder (
        itemView: View?,
        private val listener: BusinessUnitItemView
) : AbstractViewHolder<HomeWidget.ContentItemTab>(itemView) {

    override fun bind(element: HomeWidget.ContentItemTab?) {
        renderImage(element)
        renderProduct(element)
        renderTitle(element)
        renderSubtitle(element)
        renderFooter(element)
        addImpressionListener(element)
    }

    open fun addImpressionListener(element: HomeWidget.ContentItemTab?) {
        itemView.icon.addOnImpressionListener(element!!.impressHolder,
                object: ViewHintListener {
                    override fun onViewHint() {
                        listener.onImpressed(element, adapterPosition)
                    }
                }
        )
    }

    open fun getProductName(): TextView {
        return itemView.productName
    }

    open fun getIcon(): AppCompatImageView {
        return itemView.icon
    }

    open fun getTitle(): TextView {
        return itemView.title
    }

    open fun getSubtitle(): TextView {
        return itemView.subtitle
    }

    open fun renderImage(element: HomeWidget.ContentItemTab?) {
        ImageHandler.loadImageThumbs(itemView.context, itemView.icon, element?.imageUrl)
    }

    open fun renderProduct(element: HomeWidget.ContentItemTab?) {
        if (element?.name.isNullOrEmpty()) {
            getProductName().visibility = View.GONE
        } else {
            getProductName().visibility = View.VISIBLE
            getProductName().text = MethodChecker.fromHtml(element?.name)
        }
    }

    open fun renderTitle(element: HomeWidget.ContentItemTab?) {
        if (element?.title1st.isNullOrEmpty()) {
            getTitle().visibility = View.GONE
        } else {
            getTitle().visibility = View.VISIBLE
            getTitle().text = MethodChecker.fromHtml(element?.title1st)
        }

        if (element?.desc1st.isNullOrEmpty()) {
            if ((hasPrice(element) || hasTagLabel(element))) {
                getTitle().maxLines = 2
            } else {
                getTitle().maxLines = 3
            }
        } else {
            getTitle().maxLines = 1
        }
    }

    open fun renderSubtitle(element: HomeWidget.ContentItemTab?) {
        if (element?.desc1st.isNullOrEmpty()) {
            getSubtitle().visibility = View.GONE
        } else {
            getSubtitle().visibility = View.VISIBLE
            getSubtitle().text = MethodChecker.fromHtml(element?.desc1st)
        }

        if (element?.title1st.isNullOrEmpty() &&
                element?.tagName.isNullOrEmpty()) {
            if (hasPrice(element) || hasTagLabel(element)) {
                getSubtitle().maxLines = 2
            } else {
                getSubtitle().maxLines = 3
            }
        } else {
            getSubtitle().maxLines = 1
        }
    }

    open fun renderFooter(element: HomeWidget.ContentItemTab?) {
        if (hasPrice(element) || hasTagLabel(element)) {
            itemView.footer.visibility = View.VISIBLE
            renderLabel(element)
            renderPrice(element)
        } else {
            itemView.footer.visibility = View.GONE
        }
    }

    open fun hasTagLabel(element: HomeWidget.ContentItemTab?): Boolean {
        return !element?.tagName.isNullOrEmpty()
    }

    open fun hasPrice(element: HomeWidget.ContentItemTab?): Boolean {
        return !(element?.price.isNullOrEmpty() || element?.pricePrefix.isNullOrEmpty() || element?.originalPrice.isNullOrEmpty())
    }

    open fun renderPrice(element: HomeWidget.ContentItemTab?) {
        if (hasPrice(element)) {
            itemView.priceLayout.visibility = View.VISIBLE

            if (element?.pricePrefix.isNullOrEmpty()) {
                itemView.pricePrefix.visibility = View.GONE
            } else {
                itemView.pricePrefix.visibility = View.VISIBLE
                itemView.pricePrefix.text = MethodChecker.fromHtml(element?.pricePrefix)
            }

            if (element?.originalPrice.isNullOrEmpty()) {
                itemView.strikeThroughPrice.visibility = View.GONE
            } else {
                itemView.strikeThroughPrice.visibility = View.VISIBLE
                itemView.strikeThroughPrice.text = MethodChecker.fromHtml(element?.originalPrice)
                itemView.strikeThroughPrice.paintFlags = itemView.strikeThroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            if (element?.price.isNullOrEmpty()) {
                itemView.price.visibility = View.GONE
            } else {
                itemView.price.visibility = View.VISIBLE
                itemView.price.text = MethodChecker.fromHtml(element?.price)
            }

        } else {
            itemView.priceLayout.visibility = View.GONE
        }
    }

    open fun renderLabel(element: HomeWidget.ContentItemTab?) {
        if (hasTagLabel(element)) {
            itemView.tagLine.visibility = View.VISIBLE
            itemView.tagLine.text = MethodChecker.fromHtml(element?.tagName)
            if (element != null) {
                when (element.tagType) {
                    1 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_pink_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_pink))
                    }
                    2 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_green_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_green))
                    }
                    3 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_blue_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_blue))
                    }
                    4 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_yellow_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_yellow))
                    }
                    5 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_grey_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_grey))
                    }
                    else -> {
                        itemView.tagLine.visibility = View.GONE
                    }
                }
            }
        } else {
            itemView.tagLine.visibility = View.GONE
        }
    }

    companion object {
        val LAYOUT: Int = R.layout.layout_template_small_business
    }

}
