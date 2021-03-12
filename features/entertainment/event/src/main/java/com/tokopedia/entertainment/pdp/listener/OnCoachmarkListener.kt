package com.tokopedia.entertainment.pdp.listener

import android.view.View

interface OnCoachmarkListener {
    fun getLocalCache(): Boolean
    fun addCoachmark(view: View, isRecommendationPackage: Boolean)
    fun showCoachMark()
}