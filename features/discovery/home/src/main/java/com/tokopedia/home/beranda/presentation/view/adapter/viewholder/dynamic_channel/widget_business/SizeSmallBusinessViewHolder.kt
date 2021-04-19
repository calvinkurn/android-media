package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import kotlinx.android.synthetic.main.layout_template_footer_business.view.*
import kotlinx.android.synthetic.main.layout_template_icon_business_widget.view.*
import kotlinx.android.synthetic.main.layout_template_small_business.view.*

open class SizeSmallBusinessViewHolder (
        itemView: View,
        private val listener: BusinessUnitItemViewListener
) : RecyclerView.ViewHolder(itemView) {
    fun bind(element: BusinessUnitItemDataModel) {
        element.content.let {
            renderImage(it)
            renderProduct(it)
            renderTitle(it)
            renderSubtitle(it)
            renderFooter(it)
            addImpressionListener(element)
            addClickListener(element)
        }
    }

    open fun addClickListener(element: BusinessUnitItemDataModel){
        itemView.setOnClickListener {
            listener.onClicked(adapterPosition)
            RouteManager.route(itemView.context, element.content.applink)
        }
    }

    open fun addImpressionListener(element: BusinessUnitItemDataModel) {
        itemView.icon.addOnImpressionListener(element as ImpressHolder,
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
        element?.imageUrl?.let { itemView.icon.loadImage(it) }
    }

    open fun renderProduct(element: HomeWidget.ContentItemTab?) {
        if (element?.contentName.isNullOrEmpty()) {
            getProductName().visibility = View.GONE
        } else {
            getProductName().visibility = View.VISIBLE
            getProductName().text = MethodChecker.fromHtml(element?.contentName)
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
        return element?.price.isNullOrEmpty().not()
                || element?.pricePrefix.isNullOrEmpty().not()
                || element?.originalPrice.isNullOrEmpty().not()
    }

    open fun renderPrice(element: HomeWidget.ContentItemTab?) {
        if (hasPrice(element)) {

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
            itemView.price.visibility = View.GONE
            itemView.pricePrefix.visibility = View.GONE
            itemView.strikeThroughPrice.visibility = View.GONE
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
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
                    }
                    2 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_green_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    }
                    3 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_blue_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_B500))
                    }
                    4 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_yellow_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Y400))
                    }
                    5 -> {
                        itemView.tagLine.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_grey_label)
                        itemView.tagLine.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
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
