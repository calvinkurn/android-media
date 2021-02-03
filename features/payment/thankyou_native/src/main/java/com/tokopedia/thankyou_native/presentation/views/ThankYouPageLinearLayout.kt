package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class ThankYouPageLinearLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    var onViewAddedListener: OnViewAddedListener? = null

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        onViewAddedListener?.onViewAdded(child)
    }

}

interface OnViewAddedListener {
    fun onViewAdded(view: View?)
}