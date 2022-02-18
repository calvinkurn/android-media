package com.tokopedia.gopayhomewidget.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import com.tokopedia.gopayhomewidget.R
import com.tokopedia.unifycomponents.BaseCustomView

class PayLaterWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var baseView: View

    init {
        initView()
    }

    private fun initView() {
        baseView = inflate(context, R.layout.layout_gopay_home_widget, this)
    }


}