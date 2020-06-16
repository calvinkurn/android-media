package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import kotlinx.android.synthetic.main.thank_digital_recommendation_item.view.*


class DigitalRecommendationWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var data: RecommendationsItem? = null
    set(value) {
        field = value
        if (value != null) renderWidget(value)
    }

    init {
        View.inflate(context, getLayout(), this)
    }

    fun renderWidget(element: RecommendationsItem) {
        renderImage(element)
        renderProduct(element)
        renderTitle(element)
      //  renderSubtitle(element)
        //renderFooter(element)
    }

    open fun renderImage(element: RecommendationsItem) {
        ImageHandler.LoadImage(thanks_dg_rec_image, element.iconUrl)
    }

    open fun renderProduct(element: RecommendationsItem) {
        if (element.categoryName.isNullOrBlank()) {
            thanks_dh_rec_name.visibility = View.GONE
        } else {
            thanks_dh_rec_name.visibility = View.VISIBLE
            thanks_dh_rec_name.text = element.categoryName.capitalize()
        }
    }

    open fun renderTitle(element: RecommendationsItem) {
        if (element.title.isNullOrBlank()) {
            thanks_dg_rec_text_sub.visibility = View.GONE
        } else {
            thanks_dg_rec_text_sub.visibility = View.VISIBLE
            thanks_dg_rec_text_sub.text = element.title
        }

//        if (element.description.isNullOrBlank()) {
//            if ((hasPrice(element) || hasTagLabel(element))) {
//                thanks_dg_rec_text_sub.maxLines = 2
//            } else {
//                thanks_dg_rec_text_sub.maxLines = 3
//            }
//        } else {
//            thanks_dg_rec_text_sub.maxLines = 1
//        }
    }

//    open fun renderSubtitle(element: RecommendationsItem) {
//        if (element.desc1st.isEmpty()) {
//            subtitle.visibility = View.GONE
//        } else {
//            subtitle.visibility = View.VISIBLE
//            subtitle.text = element.desc1st
//        }
//
//        if (element.title1st.isEmpty() &&
//                element.tagName.isEmpty()) {
//            if (hasPrice(element) || hasTagLabel(element)) {
//                subtitle.maxLines = 2
//            } else {
//                subtitle.maxLines = 3
//            }
//        } else {
//            subtitle.maxLines = 1
//        }
//    }
//
//    open fun renderFooter(element: RecommendationsItem) {
//        if (hasPrice(element) || hasTagLabel(element)) {
//            footer.visibility = View.VISIBLE
//            renderLabel(element)
//            renderPrice(element)
//        } else {
//            footer.visibility = View.GONE
//        }
//    }

//    open fun hasTagLabel(element: RecommendationsItem): Boolean {
//        return element.tagName.isNotEmpty()
//    }

    open fun hasPrice(element: RecommendationsItem): Boolean {
        return element.productPrice.toZeroIfNull() > 0
//                || element.pricePrefix.isNotEmpty()
//                || element.originalPrice.isNotEmpty()
    }
//
//    open fun renderPrice(element: RecommendationsItem) {
//        if (hasPrice(element)) {
//
//            if (element.pricePrefix.isEmpty()) {
//                pricePrefix.visibility = View.GONE
//            } else {
//                pricePrefix.visibility = View.VISIBLE
//                pricePrefix.text = element.pricePrefix
//            }
//
//            if (element.originalPrice.isEmpty()) {
//                strikeThroughPrice.visibility = View.GONE
//            } else {
//                strikeThroughPrice.visibility = View.VISIBLE
//                strikeThroughPrice.text = element.originalPrice
//                strikeThroughPrice.paintFlags = strikeThroughPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            }
//
//            if (element.price.isEmpty()) {
//                price.visibility = View.GONE
//            } else {
//                price.visibility = View.VISIBLE
//                price.text = element.price
//            }
//
//        } else {
//            price.visibility = View.GONE
//            pricePrefix.visibility = View.GONE
//            strikeThroughPrice.visibility = View.GONE
//        }
//    }
//
//    open fun renderLabel(element: RecommendationsItem) {
//        if (hasTagLabel(element)) {
//            tagLine.visibility = View.VISIBLE
//            tagLine.setLabel(element.tagName)
//            when (element.tagType) {
//                1 -> tagLine.setLabelType(Label.GENERAL_LIGHT_RED)
//                2 -> tagLine.setLabelType(Label.GENERAL_LIGHT_GREEN)
//                3 -> tagLine.setLabelType(Label.GENERAL_LIGHT_BLUE)
//                4 -> tagLine.setLabelType(Label.GENERAL_LIGHT_ORANGE)
//                5 -> tagLine.setLabelType(Label.GENERAL_LIGHT_GREY)
//                else -> {
//                    tagLine.visibility = View.GONE
//                }
//            }
//        } else {
//            tagLine.visibility = View.GONE
//        }
//    }

    open fun getLayout(): Int {
        return R.layout.thank_digital_recommendation_item
    }

}