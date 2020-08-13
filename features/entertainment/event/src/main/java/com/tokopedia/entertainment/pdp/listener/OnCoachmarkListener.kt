package com.tokopedia.entertainment.pdp.listener

import android.view.View

interface OnCoachmarkListener {
    fun getLocalCache(): Boolean
    fun showCoachMark(view: View, height: Int)
}