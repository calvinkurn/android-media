package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
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

open class SizeSmallBusinessViewHolder (
        itemView: View,
        private val listener: BusinessUnitItemViewListener
) : RecyclerView.ViewHolder(itemView) {

    private var icon: AppCompatImageView = itemView.findViewById(R.id.icon)
    private var tagLine: TextView? = itemView.findViewById(R.id.tagLine)
    private var strikeThroughPrice: TextView = itemView.findViewById(R.id.strikeThroughPrice)
    private var pricePrefix: TextView? = itemView.findViewById(R.id.pricePrefix)
    private var price: TextView? = itemView.findViewById(R.id.price)
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
        icon?.addOnImpressionListener(element as ImpressHolder,
                object: ViewHintListener {
                    override fun onViewHint() {
                        listener.onImpressed(element, adapterPosition)
                    }
                }
        )
    }

    open fun getProductName(): TextView {
        return itemView.findViewById(R.id.productName)
    }

    open fun getIcon(): AppCompatImageView {
        return icon
    }

    open fun getTitle(): TextView {
        return itemView.findViewById(R.id.title)
    }

    open fun getSubtitle(): TextView {
        return itemView.findViewById(R.id.subtitle)
    }

    open fun renderImage(element: HomeWidget.ContentItemTab?) {
        element?.imageUrl?.let { icon.loadImage(it) }
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
            itemView.findViewById<ConstraintLayout>(R.id.footer).visibility = View.VISIBLE
            renderLabel(element)
            renderPrice(element)
        } else {
            itemView.findViewById<ConstraintLayout>(R.id.footer).visibility = View.GONE
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
                pricePrefix?.visibility = View.GONE
            } else {
                pricePrefix?.visibility = View.VISIBLE
                pricePrefix?.text = MethodChecker.fromHtml(element?.pricePrefix)
            }

            if (element?.originalPrice.isNullOrEmpty()) {
                strikeThroughPrice?.visibility = View.GONE
            } else {
                strikeThroughPrice?.visibility = View.VISIBLE
                strikeThroughPrice?.text = MethodChecker.fromHtml(element?.originalPrice)
                strikeThroughPrice?.paintFlags = strikeThroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            if (element?.price.isNullOrEmpty()) {
                price?.visibility = View.GONE
            } else {
                price?.visibility = View.VISIBLE
                price?.text = MethodChecker.fromHtml(element?.price)
            }

        } else {
            price?.visibility = View.GONE
            pricePrefix?.visibility = View.GONE
            strikeThroughPrice?.visibility = View.GONE
        }
    }

    open fun renderLabel(element: HomeWidget.ContentItemTab?) {
        if (hasTagLabel(element)) {
            tagLine?.visibility = View.VISIBLE
            tagLine?.text = MethodChecker.fromHtml(element?.tagName)
            if (element != null) {
                when (element.tagType) {
                    1 -> {
                        tagLine?.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_pink_label)
                        tagLine?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
                    }
                    2 -> {
                        tagLine?.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_green_label)
                        tagLine?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    }
                    3 -> {
                        tagLine?.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_blue_label)
                        tagLine?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_B500))
                    }
                    4 -> {
                        tagLine?.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_yellow_label)
                        tagLine?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Y400))
                    }
                    5 -> {
                        tagLine?.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_grey_label)
                        tagLine?.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    }
                    else -> {
                        tagLine?.visibility = View.GONE
                    }
                }
            }
        } else {
            tagLine?.visibility = View.GONE
        }
    }

    companion object {
        val LAYOUT: Int = R.layout.layout_template_small_business
    }

}
