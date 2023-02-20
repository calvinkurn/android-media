package com.tokopedia.product_ar.view.bottomsheet

import android.content.Context
import android.view.View
import com.tokopedia.product_ar.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

object ProductArBottomSheetBuilder {
    fun getArInfoBottomSheet(context: Context): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(context, R.layout.bs_info_product_ar, null)

        bottomSheetUnify.apply {
            isDragable = true
            isHideable = true
            isSkipCollapseState = true
            setTitle(context.getString(R.string.txt_bs_info_title))
            setChild(view)
            val txtInfo = view.findViewById<Typography>(R.id.txt_bs_ar_info)
            val btnInfo = view.findViewById<UnifyButton>(R.id.btn_bs_ar_info)
            txtInfo.text = context.getString(R.string.txt_bs_info_desc)
            btnInfo.setOnClickListener {
                dismiss()
            }
        }

        return bottomSheetUnify
    }
}