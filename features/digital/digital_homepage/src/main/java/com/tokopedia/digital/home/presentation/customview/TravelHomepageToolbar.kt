package com.tokopedia.digital.home.presentation.customview

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import com.tokopedia.digital.home.R

/**
 * @author by jessica on 2019-08-20
 */

class TravelHomepageToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : Toolbar(context, attrs, defStyleAttr) {

    var shadowApplied: Boolean = true

    fun toInitialMode() {
        hideShadow()
        setTitleTextColor(resources.getColor(com.tokopedia.design.R.color.white))
        setBackgroundColor(resources.getColor(com.tokopedia.design.R.color.transparent))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationIcon?.setTint(resources.getColor(com.tokopedia.design.R.color.white))
        } else navigationIcon = resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_back_white_24)
    }

    fun toOnScrolledMode() {
        showShadow()
        setTitleTextColor(resources.getColor(R.color.grey_800))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationIcon?.setTint(resources.getColor(R.color.grey_800))
        } else navigationIcon =  resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24)

    }

    private fun hideShadow() {
        if(shadowApplied){
            shadowApplied = false
            val pL = this.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = resources.getDimensionPixelSize(com.tokopedia.digital.home.R.dimen.toolbar_padding)
            this.background = ColorDrawable(ContextCompat.getColor(context, com.tokopedia.design.R.color.white))
            this.setPadding(pL, pT, pR, pB)
        }
    }

    private fun showShadow() {
        if(!shadowApplied){
            shadowApplied = true
            val pL = this.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = resources.getDimensionPixelSize(com.tokopedia.digital.home.R.dimen.toolbar_padding)

            this.background = ContextCompat.getDrawable(context, com.tokopedia.digital.home.R.drawable.travel_homepage_toolbar_bg_shadow_bottom)
            this.setPadding(pL, pT, pR, pB)
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height + 32
    }

}