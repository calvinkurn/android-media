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

    private fun hideShadow() {
        if(shadowApplied){
            shadowApplied = false
            this.background = ColorDrawable(ContextCompat.getColor(context, R.color.white))
        }
    }

    private fun showShadow() {
        if(!shadowApplied){
            shadowApplied = true
            this.background = ContextCompat.getDrawable(context, R.drawable.travel_homepage_toolbar_bg_shadow_bottom)
        }
    }

}