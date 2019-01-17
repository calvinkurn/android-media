package com.tokopedia.flashsale.management.product.widget

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.flashsale.management.R

/**
 * Created by hendry on 08/11/18.
 */
class FlashSaleSearchInputView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : SearchInputView(context, attrs, defStyleAttr) {

    override fun init() {
        super.init()
        val defaultDp = dpToPx(4f)
        val viewToChangePadding: View = view.findViewById<View>(R.id.search_input_view_content)
        viewToChangePadding.setPadding(3 * defaultDp, 2 * defaultDp, 3 * defaultDp, defaultDp)
    }

    protected fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics).toInt()
    }

}