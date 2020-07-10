package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.entertainment.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_event_pdp_detail_lokasi.view.*

class WidgetEventPDPMaps @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr){


    init {
        View.inflate(context, R.layout.widget_event_pdp_detail_lokasi, this)
    }

    fun setLocationTitle(title : String){
        tg_event_pdp_detail_lokasi_map_title.text = title
    }
}