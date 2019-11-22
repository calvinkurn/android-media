package com.tokopedia.digital.home.presentation.customview

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.Toolbar
import android.util.AttributeSet
import com.tokopedia.banner.dynamic.util.ViewHelper
import com.tokopedia.digital.home.R

/**
 * @author by jessica on 2019-08-20
 */

class DigitalHomepageToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
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
        setTitleTextColor(resources.getColor(com.tokopedia.design.R.color.grey_800))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationIcon?.setTint(resources.getColor(com.tokopedia.design.R.color.grey_800))
        } else navigationIcon =  resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24)

    }

    private fun hideShadow() {
        if(shadowApplied){
            shadowApplied = false
            val pL = this.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = ViewHelper.getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = resources.getDimensionPixelSize(R.dimen.dp_32)
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
                pT = ViewHelper.getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = resources.getDimensionPixelSize(TOOLBAR_PADDING)

            this.background = ContextCompat.getDrawable(context, R.drawable.travel_homepage_toolbar_bg_shadow_bottom)
            this.setPadding(pL, pT, pR, pB)
        }
    }

    companion object {
        val TOOLBAR_PADDING = com.tokopedia.design.R.dimen.dp_8
    }

}