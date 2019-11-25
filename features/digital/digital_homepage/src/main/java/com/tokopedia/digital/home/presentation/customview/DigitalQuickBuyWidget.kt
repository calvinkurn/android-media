package com.tokopedia.digital.home.presentation.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalQuickBuyItem
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.layout_quick_buy_widget.view.*
import kotlinx.android.synthetic.main.layout_quick_buy_widget_footer.view.*

/**
 * @author by resakemal on 21/11/19
 */

class DigitalQuickBuyWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var data: DigitalQuickBuyItem? = null
    set(value) {
        field = value
        if (value != null) renderWidget(value)
    }

    init {
        View.inflate(context, getLayout(), this)
    }

    fun renderWidget(element: DigitalQuickBuyItem) {
        renderImage(element)
        renderProduct(element)
        renderTitle(element)
        renderSubtitle(element)
        renderFooter(element)
    }

    open fun renderImage(element: DigitalQuickBuyItem) {
        ImageHandler.loadImageFit2(context, icon, element.imageUrl)
    }

    open fun renderProduct(element: DigitalQuickBuyItem) {
        if (element.name.isNullOrEmpty()) {
            product_name.visibility = View.GONE
        } else {
            product_name.visibility = View.VISIBLE
            product_name.text = element.name
        }
    }

    open fun renderTitle(element: DigitalQuickBuyItem) {
        if (element.title1st.isNullOrEmpty()) {
            title.visibility = View.GONE
        } else {
            title.visibility = View.VISIBLE
            title.text = element.title1st
        }

        if (element.desc1st.isNullOrEmpty()) {
            if ((hasPrice(element) || hasTagLabel(element))) {
                title.maxLines = 2
            } else {
                title.maxLines = 3
            }
        } else {
            title.maxLines = 1
        }
    }

    open fun renderSubtitle(element: DigitalQuickBuyItem) {
        if (element.desc1st.isNullOrEmpty()) {
            subtitle.visibility = View.GONE
        } else {
            subtitle.visibility = View.VISIBLE
            subtitle.text = element.desc1st
        }

        if (element.title1st.isNullOrEmpty() &&
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

    open fun renderFooter(element: DigitalQuickBuyItem) {
        if (hasPrice(element) || hasTagLabel(element)) {
            footer.visibility = View.VISIBLE
            renderLabel(element)
            renderPrice(element)
        } else {
            footer.visibility = View.GONE
        }
    }

    open fun hasTagLabel(element: DigitalQuickBuyItem): Boolean {
        return !element.tagName.isNullOrEmpty()
    }

    open fun hasPrice(element: DigitalQuickBuyItem): Boolean {
        return element.price.isNullOrEmpty().not()
                || element.pricePrefix.isNullOrEmpty().not()
                || element.originalPrice.isNullOrEmpty().not()
    }

    open fun renderPrice(element: DigitalQuickBuyItem) {
        if (hasPrice(element)) {

            if (element.pricePrefix.isNullOrEmpty()) {
                pricePrefix.visibility = View.GONE
            } else {
                pricePrefix.visibility = View.VISIBLE
                pricePrefix.text = element.pricePrefix
            }

            if (element.originalPrice.isNullOrEmpty()) {
                strikeThroughPrice.visibility = View.GONE
            } else {
                strikeThroughPrice.visibility = View.VISIBLE
                strikeThroughPrice.text = element.originalPrice
                strikeThroughPrice.paintFlags = strikeThroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            if (element.price.isNullOrEmpty()) {
                price.visibility = View.GONE
            } else {
                price.visibility = View.VISIBLE
                price.text = element.price
            }

        } else {
            price.visibility = View.GONE
            pricePrefix.visibility = View.GONE
            strikeThroughPrice.visibility = View.GONE
        }
    }

    open fun renderLabel(element: DigitalQuickBuyItem) {
        if (hasTagLabel(element)) {
            tagLine.visibility = View.VISIBLE
            tagLine.setLabel(element.tagName ?: "")
            when (element.tagType) {
                1 -> tagLine.setLabelType(Label.GENERAL_LIGHT_RED)
                2 -> tagLine.setLabelType(Label.GENERAL_LIGHT_GREEN)
                3 -> tagLine.setLabelType(Label.GENERAL_LIGHT_BLUE)
                4 -> tagLine.setLabelType(Label.GENERAL_LIGHT_ORANGE)
                5 -> tagLine.setLabelType(Label.GENERAL_LIGHT_GREY)
                else -> {
                    tagLine.visibility = View.GONE
                }
            }
        } else {
            tagLine.visibility = View.GONE
        }
    }

    open fun getLayout(): Int {
        return R.layout.layout_quick_buy_widget
    }

}