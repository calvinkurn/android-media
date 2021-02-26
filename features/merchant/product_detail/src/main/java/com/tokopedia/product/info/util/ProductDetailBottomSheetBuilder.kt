package com.tokopedia.product.info.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.util.toDateId
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 21/10/20
 */
object ProductDetailBottomSheetBuilder {
    private const val PDP_TIME_PUKUL = "pukul"

    fun getShopNotesBottomSheet(context: Context, dateValue: String, descValue: String, titleValue: String): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_product_shop_notes_info, null)

        bottomSheetUnify.apply {
            setTitle(titleValue)
            setChild(view)
            val date = view.findViewById<Typography>(R.id.product_shop_notes_date)
            val desc = view.findViewById<Typography>(R.id.product_shop_notes_desc)

            date.text = dateValue toDateId ("dd MMM yyyy, '${PDP_TIME_PUKUL}' HH:mm")
            desc.text = HtmlLinkHelper(context, descValue.replace("(\r\n|\n)".toRegex(), "<br />")).spannedString
        }

        return bottomSheetUnify
    }

    fun getUspBottomSheet(context: Context, freeOngkirUrl: String): BottomSheetUnify {
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

            imgUsp.loadImage("https://images.tokopedia.net/img/android/test/Group_1234042.png")
            imgFreeOngkir.loadImage(freeOngkirUrl)
        }

        return bottomSheetUnify
    }
}