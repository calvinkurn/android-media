package com.tokopedia.travel.homepage.presentation.customview

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import com.tokopedia.travel.homepage.R

/**
 * @author by jessica on 2019-08-20
 */

class TravelHomepageToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : Toolbar(context, attrs, defStyleAttr) {

    var shadowApplied: Boolean = false

    init {
        toInitialMode()
    }

    fun toInitialMode() {
        setTitleTextColor(resources.getColor(R.color.white))
        setBackgroundColor(resources.getColor(R.color.transparent))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationIcon?.setTint(resources.getColor(R.color.white))
        } else navigationIcon = resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_back_white_24)
        hideShadow()
    }

    fun toOnScrolledMode() {
        setTitleTextColor(resources.getColor(R.color.grey_800))
        setBackgroundColor(resources.getColor(R.color.white))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationIcon?.setTint(resources.getColor(R.color.grey_800))
        } else navigationIcon =  resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24)
        showShadow()
    }

    fun hideShadow() {
        if(shadowApplied){
            shadowApplied = false
            val pL = this.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = 0
            this.background = ColorDrawable(ContextCompat.getColor(context, R.color.white))
            this.setPadding(pL, pT, pR, pB)
        }
    }

    fun showShadow() {
        if(!shadowApplied){
            shadowApplied = true
            val pL = this.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = resources.getDimensionPixelSize(R.dimen.dp_8)

            this.background = ContextCompat.getDrawable(context, R.drawable.travel_homepage_toolbar_bg_shadow_bottom)
            this.setPadding(pL, pT, pR, pB)
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }

}