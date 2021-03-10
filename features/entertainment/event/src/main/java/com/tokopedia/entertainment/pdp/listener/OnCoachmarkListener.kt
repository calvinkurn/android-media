package com.tokopedia.entertainment.pdp.listener

import android.view.View

interface OnCoachmarkListener {
    fun getLocalCache(): Boolean
    fun getLocalCacheRecom(): Boolean
    fun showCoachMark(view: View)
    fun showCoachMarkRecom(view: View)
}