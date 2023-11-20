package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.entertainment.databinding.WidgetEventPdpDetailLokasiBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetEventPDPMaps @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO) :
        BaseCustomView(context, attrs, defStyleAttr){

    private val binding = WidgetEventPdpDetailLokasiBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    fun setLocationTitle(title : String){
        binding.tgEventPdpDetailLokasiMapTitle.text = title
    }
}
