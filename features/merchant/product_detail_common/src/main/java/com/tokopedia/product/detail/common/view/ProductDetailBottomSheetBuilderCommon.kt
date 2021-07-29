package com.tokopedia.product.detail.common.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.data.model.rates.ErrorBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 13/07/21
 */
object ProductDetailBottomSheetBuilderCommon {
    const val ATC_VAR_SHIPPING_CHOOSE_ADDRESS_TAG = "ATC_VAR_SHIPPING_CHOOSE_ADDRESS_TAG"

    fun getShippingErrorBottomSheet(context: Context, data: ErrorBottomSheet, errorCode: Int, onButtonClicked: (Int) -> Unit, onHomeClicked: () -> Unit): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_shipping_error, null)

        bottomSheetUnify.apply {
            setTitle(data.title)
            setChild(view)
            val btn_error = view.findViewById<UnifyButton>(R.id.shipping_error_btn)
            val imgError = view.findViewById<ImageView>(R.id.shipping_error_img)
            val txtError = view.findViewById<Typography>(R.id.shipping_error_desc)
            val textHome = view.findViewById<Typography>(R.id.text_home)

            imgError.loadImage(data.iconURL)
            btn_error.text = data.buttonCopy
            txtError.text = HtmlLinkHelper(context, data.subtitle).spannedString

            btn_error.setOnClickListener {
                dismiss()
                onButtonClicked.invoke(errorCode)
            }

            textHome.setOnClickListener {
                dismiss()
                onHomeClicked.invoke()
            }
        }

        return bottomSheetUnify
    }
}