package com.tokopedia.chooseaccount.view.ocl

import android.content.Context
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.chooseaccount.R

class OclItemDivider(context: Context?) : DividerItemDecoration(context) {
    override fun getDimenPaddingLeft(): Int {
        return R.dimen.loginphone_dp_5
    }

    override fun shouldDrawOnLastItem(): Boolean {
        return true
    }
}
