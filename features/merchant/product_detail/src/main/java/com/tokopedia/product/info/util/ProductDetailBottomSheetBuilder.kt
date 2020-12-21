package com.tokopedia.product.info.util

import android.content.Context
import android.view.View
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
}