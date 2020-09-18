package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.thankyou_native.R

class FeatureListView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val layout = R.layout.thank_feature_listing

    init {
        val v = LayoutInflater.from(context).inflate(layout, null)
        addView(v)
    }

    fun addData(){

    }


}