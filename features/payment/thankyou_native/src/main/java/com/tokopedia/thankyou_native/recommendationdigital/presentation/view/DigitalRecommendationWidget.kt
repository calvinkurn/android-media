package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.thank_digital_recommendation_item.view.*


class DigitalRecommendationWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    val HISTORY = "history"
    val RECOMMENDATION = "recommendation"

    var data: RecommendationsItem? = null
        set(value) {
            field = value
            if (value != null) renderWidget(value)
        }

    init {
        View.inflate(context, getLayout(), this)
    }

    private fun renderWidget(element: RecommendationsItem) {
        renderImage(element)
        renderProduct(element)

        when {
            HISTORY.equals(element.type, ignoreCase = true) -> {
                renderTitle(element.productName)
            }
            RECOMMENDATION.equals(element.type, ignoreCase = true) -> {
                renderTitle(element.type)
            }
            else -> {
                renderTitle(element)
            }
        }
        renderTitle(element)
        renderSubtitle(element)
    }

    private fun renderImage(element: RecommendationsItem) {
        ImageHandler.LoadImage(thanks_dg_rec_image, element.iconUrl)
    }

    private fun renderProduct(element: RecommendationsItem) {
        if (element.categoryName.isNullOrBlank()) {
            thanks_dh_rec_name.gone()
        } else {
            thanks_dh_rec_name.visible()
            thanks_dh_rec_name.text = element.categoryName.capitalize().trim()
        }
    }

    private fun renderTitle(element: RecommendationsItem) {
        if (element.productName.isNullOrBlank()) {
            if (element.description.isNullOrBlank()) {
                thanks_dg_rec_text_sub.gone()
            } else {
                thanks_dg_rec_text_sub.visible()
                thanks_dg_rec_text_sub.text = element.description.trim()
            }
        } else {
            thanks_dg_rec_text_sub.visible()
            thanks_dg_rec_text_sub.text = (element.title ?: "").trim()
        }

    }

    private fun renderTitle(productName: String?) {
        if (productName.isNullOrBlank()) {
            thanks_dg_rec_text_sub.gone()
        } else {
            thanks_dg_rec_text_sub.visible()
            thanks_dg_rec_text_sub.text = productName.trim()
        }

    }

    private fun renderSubtitle(element: RecommendationsItem) {
        if (element.clientNumber.isNullOrBlank()) {
            thanks_dg_rec_text_desc.gone()
        } else {
            thanks_dg_rec_text_desc.visible()
            thanks_dg_rec_text_desc.text = element.clientNumber.trim()
        }

    }

    private fun getLayout(): Int {
        return R.layout.thank_digital_recommendation_item
    }

}