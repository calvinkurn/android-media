package com.tokopedia.attachinvoice.view.widget

import android.content.Context
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.attachinvoice.R

class AttachInvoiceItemDecoration(context: Context?) : DividerItemDecoration(context) {

    override fun getDimenPaddingLeft(): Int {
        return R.dimen.dp_51_attachinvoice
    }

    override fun shouldDrawOnLastItem(): Boolean {
        return true
    }
}