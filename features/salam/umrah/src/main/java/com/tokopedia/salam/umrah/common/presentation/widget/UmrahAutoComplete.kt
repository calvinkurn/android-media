package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.salam.umrah.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_autocomplete_edit_text.view.*

class UmrahAutoComplete @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.widget_umrah_autocomplete_edit_text, this)
    }

    fun setLabel(label : String){
        tg_umrah_autocomplete_label.text = label
    }

    fun setHint(hint : String){
        ac_umrah_autocomplete.hint = hint
    }

} 