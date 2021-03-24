package com.tokopedia.home.beranda.presentation.view.helper

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.HexValidator
import com.tokopedia.home.beranda.presentation.view.listener.SafeClickListener
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

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun String.isHexColor(): Boolean {
    return HexValidator.validate(this)
}