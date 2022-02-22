package com.tokopedia.gopayhomewidget.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import com.tokopedia.gopayhomewidget.R
import com.tokopedia.gopayhomewidget.databinding.LayoutGopayHomeWidgetBinding
import com.tokopedia.gopayhomewidget.presentation.domain.data.PayLaterWidgetData
import com.tokopedia.gopayhomewidget.presentation.listener.PayLaterWidgetListener
import com.tokopedia.unifycomponents.BaseCustomView

class PayLaterWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var payLaterWidgetListener: PayLaterWidgetListener? = null
    private lateinit var layoutGopayBinding :LayoutGopayHomeWidgetBinding

    init {
        initView()
    }

    private fun initView() {
        layoutGopayBinding = LayoutGopayHomeWidgetBinding.inflate(LayoutInflater.from(context), this,true)
        this.visibility = INVISIBLE
    }

    fun setPayLaterWidgetListener(payLaterWidgetListener: PayLaterWidgetListener){
        this.payLaterWidgetListener =  payLaterWidgetListener
    }

    fun setData(payLaterWidgetData: PayLaterWidgetData) {
        if(payLaterWidgetData.isShow == true)
        {
            this.visibility = VISIBLE
            layoutGopayBinding.gopayDetail.text = payLaterWidgetData.description
            payLaterWidgetData.imageLight?.let { imageUrl->
                layoutGopayBinding.gatewayIcon.setImageUrl(imageUrl)
            }
            layoutGopayBinding.proccedToGopay.text = payLaterWidgetData.button?.buttonName
            when(payLaterWidgetData.button?.ctaType)
            {

            }
            layoutGopayBinding.crossIcon.setOnClickListener {
                payLaterWidgetListener?.onClosePayLaterWidget()
            }

        }

    }

}