package com.tokopedia.digital.home.old.presentation.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutQuickBuyWidgetBinding
import com.tokopedia.digital.home.old.model.DigitalQuickBuyItem
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Label

/**
 * @author by resakemal on 21/11/19
 */

class DigitalQuickBuyWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var binding : LayoutQuickBuyWidgetBinding
    
    var data: DigitalQuickBuyItem? = null
    set(value) {
        field = value
        if (value != null) renderWidget(value)
    }

    init {
        binding = LayoutQuickBuyWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun renderWidget(element: DigitalQuickBuyItem) {
        renderImage(element)
        renderProduct(element)
        renderTitle(element)
        renderSubtitle(element)
        renderFooter(element)
    }

    open fun renderImage(element: DigitalQuickBuyItem) {
        ImageHandler.LoadImage(binding.icon, element.imageUrl)
    }

    open fun renderProduct(element: DigitalQuickBuyItem) {
        if (element.name.isEmpty()) {
            
            binding.productName.visibility = View.GONE
        } else {
            binding.productName.visibility = View.VISIBLE
            binding.productName.text = element.name.capitalize()
        }
    }

    open fun renderTitle(element: DigitalQuickBuyItem) {
        if (element.title1st.isEmpty()) {
           binding.title.visibility = View.GONE
        } else {
           binding.title.visibility = View.VISIBLE
           binding.title.text = element.title1st
        }

        if (element.desc1st.isEmpty()) {
            if ((hasPrice(element) || hasTagLabel(element))) {
               binding.title.maxLines = MAX_LINES_2
            } else {
               binding.title.maxLines = MAX_LINES_3
            }
        } else {
           binding.title.maxLines = MAX_LINES_1
        }
    }

    open fun renderSubtitle(element: DigitalQuickBuyItem) {
        if (element.desc1st.isEmpty()) {
            binding.subtitle.visibility = View.GONE
        } else {
            binding.subtitle.visibility = View.VISIBLE
            binding.subtitle.text = element.desc1st
        }

        if (element.title1st.isEmpty() &&
                element.tagName.isEmpty()) {
            if (hasPrice(element) || hasTagLabel(element)) {
                binding.subtitle.maxLines = MAX_LINES_2
            } else {
                binding.subtitle.maxLines = MAX_LINES_3
            }
        } else {
            binding.subtitle.maxLines = MAX_LINES_1
        }
    }

    open fun renderFooter(element: DigitalQuickBuyItem) {
        if (hasPrice(element) || hasTagLabel(element)) {
            binding.footer.root.show()
            renderLabel(element)
            renderPrice(element)
        } else {
            binding.footer.root.hide()
        }
    }

    open fun hasTagLabel(element: DigitalQuickBuyItem): Boolean {
        return element.tagName.isNotEmpty()
    }

    open fun hasPrice(element: DigitalQuickBuyItem): Boolean {
        return element.price.toIntSafely() > 0
                || element.pricePrefix.isNotEmpty()
                || element.originalPrice.isNotEmpty()
    }

    open fun renderPrice(element: DigitalQuickBuyItem) {
        if (hasPrice(element)) {

            if (element.pricePrefix.isEmpty()) {
                binding.footer.pricePrefix.visibility = View.GONE
            } else {
                binding.footer.pricePrefix.visibility = View.VISIBLE
                binding.footer.pricePrefix.text = element.pricePrefix
            }

            if (element.originalPrice.isEmpty()) {
                binding.footer.strikeThroughPrice.visibility = View.GONE
            } else {
                binding.footer.strikeThroughPrice.visibility = View.VISIBLE
                binding.footer.strikeThroughPrice.text = element.originalPrice
                binding.footer.strikeThroughPrice.paintFlags = binding.footer.strikeThroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            if (element.price.isEmpty()) {
                binding.footer.price.visibility = View.GONE
            } else {
                binding.footer.price.visibility = View.VISIBLE
                binding.footer.price.text = element.price
            }

        } else {
            binding.footer.price.visibility = View.GONE
            binding.footer.pricePrefix.visibility = View.GONE
            binding.footer.strikeThroughPrice.visibility = View.GONE
        }
    }

    open fun renderLabel(element: DigitalQuickBuyItem) {
        if (hasTagLabel(element)) {
            binding.footer.tagLine.visibility = View.VISIBLE
            binding.footer.tagLine.setLabel(element.tagName)
            when (element.tagType) {
                TAG_TYPE_1 -> binding.footer.tagLine.setLabelType(Label.GENERAL_LIGHT_RED)
                TAG_TYPE_2 -> binding.footer.tagLine.setLabelType(Label.GENERAL_LIGHT_GREEN)
                TAG_TYPE_3 -> binding.footer.tagLine.setLabelType(Label.GENERAL_LIGHT_BLUE)
                TAG_TYPE_4 -> binding.footer.tagLine.setLabelType(Label.GENERAL_LIGHT_ORANGE)
                TAG_TYPE_5 -> binding.footer.tagLine.setLabelType(Label.GENERAL_LIGHT_GREY)
                else -> {
                    binding.footer.tagLine.visibility = View.GONE
                }
            }
        } else {
            binding.footer.tagLine.visibility = View.GONE
        }
    }

    open fun getLayout(): Int {
        return R.layout.layout_quick_buy_widget
    }

    companion object {
        private const val MAX_LINES_3 = 3
        private const val MAX_LINES_2 = 2
        private const val MAX_LINES_1 = 1

        private const val TAG_TYPE_1 = 1
        private const val TAG_TYPE_2 = 2
        private const val TAG_TYPE_3 = 3
        private const val TAG_TYPE_4 = 4
        private const val TAG_TYPE_5 = 5
    }
}