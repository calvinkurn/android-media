package com.tokopedia.cart.view.compoundview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.cart.R
import com.tokopedia.design.base.BaseCustomView

/**
 * Created by meta on 19/07/18.
 */
class ToolbarRemoveWithBackView : BaseCustomView {

    lateinit var btnBack: ImageView
    lateinit var textView: TextView

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
        val view = View.inflate(context, R.layout.toolbar_delete_with_back, this)
        btnBack = view.findViewById(R.id.btn_back)
        textView = view.findViewById(R.id.textview_title)
    }

    fun setTitle(title: CharSequence) {
        textView.text = title
    }

    fun navigateUp(activity: Activity) {
        btnBack.setOnClickListener { activity.onBackPressed() }
    }

}
