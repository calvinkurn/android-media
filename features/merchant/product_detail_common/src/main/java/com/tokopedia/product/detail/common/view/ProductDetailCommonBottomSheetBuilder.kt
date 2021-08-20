package com.tokopedia.product.detail.common.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.data.model.rates.ErrorBottomSheet
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 13/07/21
 */
object ProductDetailCommonBottomSheetBuilder {
    const val ATC_VAR_SHIPPING_CHOOSE_ADDRESS_TAG = "ATC_VAR_SHIPPING_CHOOSE_ADDRESS_TAG"
    const val TAG_USP_BOTTOM_SHEET = "TAG_USP_BOTTOM_SHEET"

    fun getShippingErrorBottomSheet(context: Context, data: ErrorBottomSheet, errorCode: Int, onButtonClicked: (Int) -> Unit, onHomeClicked: () -> Unit): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_shipping_error, null)

        bottomSheetUnify.apply {
            setTitle(data.title)
            setChild(view)
            val btnError = view.findViewById<UnifyButton>(R.id.shipping_error_btn)
            val imgError = view.findViewById<ImageView>(R.id.shipping_error_img)
            val txtError = view.findViewById<Typography>(R.id.shipping_error_desc)
            val textHome = view.findViewById<Typography>(R.id.text_home)

            imgError.loadImage(data.iconURL)
            btnError.text = data.buttonCopy
            txtError.text = HtmlLinkHelper(context, data.subtitle).spannedString

            btnError.setOnClickListener {
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


    fun getUspBottomSheet(context: Context, freeOngkirUrl: String, uspTokoCabangImgUrl: String): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_usp, null)

        bottomSheetUnify.apply {
            isDragable = true
            isHideable = true
            isSkipCollapseState = true
            setTitle(context.getString(R.string.pdp_usp_static_title))
            setChild(view)
            val imgFreeOngkir = view.findViewById<ImageView>(R.id.pdp_usp_static_free_ongkir)
            val imgUsp = view.findViewById<ImageView>(R.id.usp_pdp_img)

            imgUsp.loadImage(uspTokoCabangImgUrl)
            imgFreeOngkir.loadImage(freeOngkirUrl)
        }

        return bottomSheetUnify
    }

    fun getUspTokoNowBottomSheet(context: Context): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_usp_tokonow, null)
        val wordingDynamic = FirebaseRemoteConfigImpl(context).getString(RemoteConfigKey.REMOTE_CONFIG_APP_WORDING_TOKONOW_USP_PDP) ?: ""
        bottomSheetUnify.apply {
            isDragable = true
            isHideable = true
            isSkipCollapseState = true
            setTitle(context.getString(R.string.pdp_usp_tokonow_static_title))
            setChild(view)

            val subtitle3 = view.findViewById<Typography>(R.id.usp_illustration_3_subtitle)
            subtitle3.text = if (wordingDynamic.isNotEmpty()) HtmlLinkHelper(context, wordingDynamic).spannedString else context.getString(R.string.pdp_usp_tokonow_static_illustration_3_subtitle)
        }

        return bottomSheetUnify
    }
}