package com.tokopedia.kotlin.extensions.view

/**
 * Created by jegul on 31/01/20
 */
fun Double?.orZero(): Double = this ?: 0f.toDouble()