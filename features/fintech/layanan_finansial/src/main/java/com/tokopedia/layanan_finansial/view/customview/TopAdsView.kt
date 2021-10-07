package com.tokopedia.layanan_finansial.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.layanan_finansial.R

class TopAdsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {


    init {
        LayoutInflater.from(context).inflate(R.layout.layout_topads_view,this,true)
    }

    fun addAdsDetail(topAdsDetail : Class<*>)
    {
        val recyclerDisplayer = findViewById<RecyclerView>(R.id.topAdsImagesRecycler)
    }
}