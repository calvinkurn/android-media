package com.tokopedia.home.beranda.presentation.view.helper

import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

fun TextView.setValue(value: String){
    this.text = value
    if(value.isEmpty()){
        this.hide()
    }else{
        this.show()
    }
}