package com.tokopedia.entertainment.pdp.listener

import android.view.View

interface OnCoachmarkListener {
    fun getLocalCache():Boolean
    fun showCoachMark(width:Int, height:Int, view: View, sizeMargin:Int=2)
}