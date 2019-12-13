package com.tokopedia.purchase_platform.features.cart.view.compoundview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.purchase_platform.R

/**
 * Created by meta on 19/07/18.
 */
class ToolbarRemoveWithBackView : BaseCustomView {

    lateinit var btnBack: ImageView
    lateinit var textView: TextView
    lateinit var btnOpenChuck: Button

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
        btnOpenChuck = view.findViewById(R.id.btn_chuck)
        if (GlobalConfig.isAllowDebuggingTools()) {
            btnOpenChuck.visibility = View.VISIBLE
        } else {
            btnOpenChuck.visibility = View.GONE
        }
        // Todo : Revert to GONE
        btnOpenChuck.visibility = View.VISIBLE
    }

    fun setTitle(title: CharSequence) {
        textView.text = title
    }

    fun navigateUp(context: Activity) {
        btnBack.setOnClickListener { context.finish() }
    }

    fun setOnClickGoToChuck(listener: ToolbarRemoveView.ToolbarCartListener) {
        btnOpenChuck.setOnClickListener { listener.onGoToChuck() }
    }

}
