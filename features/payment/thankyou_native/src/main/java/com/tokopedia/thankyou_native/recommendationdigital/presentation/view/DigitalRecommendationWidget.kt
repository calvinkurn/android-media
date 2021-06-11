package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationItem
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationsItem
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.thank_digital_recommendation_item.view.*


class DigitalRecommendationWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    val TYPE_PRODUCT_RECOMMENDATION = "product"
    val TYPE_CATEGORY = "category"

    var data: RecommendationItem? = null
        set(value) {
            field = value
            if (value != null) renderWidget(value)
        }

    init {
        View.inflate(context, getLayout(), this)
    }

    private fun renderWidget(element: RecommendationItem) {
        renderImage(element)
        renderCategoryName(element)
        renderProductName(element)
        renderClientNumber(element)
    }

    private fun renderCategoryName(element: RecommendationItem) {
        val categoryName = when (element.trackingData?.itemLabel) {
            TYPE_PRODUCT_RECOMMENDATION -> element.title
            TYPE_CATEGORY -> element.trackingData.categoryName
            else -> element.title
        }
        if (categoryName.isNullOrEmpty()) {
            thanks_dg_rec_category_name.hide()
        } else {
            thanks_dg_rec_category_name.show()
            thanks_dg_rec_category_name.text = categoryName.capitalize()
        }
    }

    private fun renderProductName(element: RecommendationItem) {
        if (element.subtitle.isNullOrEmpty()) {
            thanks_dg_rec_product_name.hide()
        } else {
            thanks_dg_rec_product_name.show()
            thanks_dg_rec_product_name.text = element.subtitle
        }
    }

    private fun renderImage(element: RecommendationItem) {
        ImageHandler.LoadImage(thanks_dg_rec_image, element.mediaURL)
    }

    private fun renderClientNumber(element: RecommendationItem) {
        if (element.label1.isNullOrEmpty()) {
            thanks_dg_rec_client_number.hide()
            thanks_dg_rec_product_name.maxLines = 3
        } else {
            thanks_dg_rec_client_number.show()
            thanks_dg_rec_client_number.text = element.label1
        }
    }

    private fun getLayout(): Int {
        return R.layout.thank_digital_recommendation_item
    }

}