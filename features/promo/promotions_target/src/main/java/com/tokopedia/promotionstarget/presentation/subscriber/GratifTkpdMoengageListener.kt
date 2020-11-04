package com.tokopedia.promotionstarget.presentation.subscriber

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import java.lang.ref.WeakReference

class GratifTkpdMoengageListener : Function2<Int, Boolean, Int> {

    val hashMap = HashMap<Int, Boolean>()
    val graitfCallback: GraitfCallback? = null

    override fun invoke(p1: Int, isShow: Boolean): Int {
        if (isShow) {
            hashMap[p1] = isShow
        } else {
            hashMap.remove(p1)
        }
        val isGratifPopupVisible = graitfCallback?.isGratifPopUpVisible(p1)
        if (isGratifPopupVisible != null && isGratifPopupVisible) {
            hideMoengagePopup()
        }
        return 0
    }

    @SuppressLint("ResourceType")
    fun hideMoengagePopup(){
        graitfCallback?.getActivity()?.get()?.let {activity->
            val view = activity.window.decorView.findViewById<View>(12345)
            if (view != null && view.parent is RelativeLayout) {
                val rl = view.parent as RelativeLayout
                rl.visibility = View.GONE
                rl.postDelayed({
                    rl.visibility = View.VISIBLE
                },2000L)
            }
        }
    }
}

interface GraitfCallback {
    fun isGratifPopUpVisible(entityHashCode: Int): Boolean
    fun getActivity(): WeakReference<Activity?>?
}