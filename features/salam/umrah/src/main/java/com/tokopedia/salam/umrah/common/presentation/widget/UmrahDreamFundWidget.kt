package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.salam.umrah.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_dream_fund.view.*

class UmrahDreamFundWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr){

    init {
        View.inflate(context, R.layout.widget_umrah_dream_fund, this)
        loadUmrahDreamFundContainerBg()
    }

    private fun loadUmrahDreamFundContainerBg() {
        val containerBg = context?.let { AppCompatResources.getDrawable(it, R.drawable.umrah_bg_dream_fund_widget) }
        container_widget_umrah_dream_fund.background = containerBg
    }
}