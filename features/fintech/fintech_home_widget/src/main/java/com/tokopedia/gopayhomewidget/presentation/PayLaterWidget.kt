package com.tokopedia.gopayhomewidget.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import com.tokopedia.gopayhomewidget.R
import com.tokopedia.gopayhomewidget.presentation.domain.data.PayLaterWidgetData
import com.tokopedia.gopayhomewidget.presentation.listener.PayLaterWidgetListener
import com.tokopedia.unifycomponents.BaseCustomView

class PayLaterWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var payLaterWidgetListener: PayLaterWidgetListener? = null
    private var baseView: View? =null

    init {
        initView()
    }

    private fun initView() {
        baseView = inflate(context, R.layout.layout_gopay_home_widget, this)
    }

    fun setPayLaterWidgetListener(payLaterWidgetListener: PayLaterWidgetListener){
        this.payLaterWidgetListener =  payLaterWidgetListener
    }

    fun setData(payLaterWidgetData: PayLaterWidgetData) {

    }


}