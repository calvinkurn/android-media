package com.tokopedia.product.share.ekstensions

import android.content.Context
import android.view.LayoutInflater

val Context.layoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater