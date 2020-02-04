package com.tokopedia.purchase_platform.features.cart.view.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.purchase_platform.R

/**
 * Created by meta on 19/07/18.
 */
class ToolbarRemoveView : BaseCustomView {

    lateinit var textView: TextView

    interface ToolbarCartListener {
        fun onToolbarRemoveAllCart()
        fun onGoToChuck()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.toolbar_delete, this)
        textView = view.findViewById(R.id.textview_title)
    }

    fun setTitle(title: CharSequence) {
        textView.text = title
    }

}
