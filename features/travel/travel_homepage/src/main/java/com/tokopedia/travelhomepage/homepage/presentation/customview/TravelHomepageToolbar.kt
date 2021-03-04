package com.tokopedia.travelhomepage.homepage.presentation.customview

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.travelhomepage.R

/**
 * @author by jessica on 2019-08-20
 */

class TravelHomepageToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : Toolbar(context, attrs, defStyleAttr) {

    var shadowApplied: Boolean = true

    fun toInitialMode() {
        hideShadow()
        title = context.getString(R.string.travel_title_subhomepage)
        setTitleTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
        setBackgroundColor(resources.getColor(R.color.travel_subhome_transparent))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationIcon?.setTint(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
        } else navigationIcon = resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_back_white_24)
    }

    fun toOnScrolledMode() {
        showShadow()
        title = context.getString(R.string.travel_title_subhomepage)
        setTitleTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N600))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationIcon?.setTint(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N600))
        } else navigationIcon = resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_back_grayscale_24)

    }

    private fun hideShadow() {
        if (shadowApplied) {
            shadowApplied = false
            val pL = this.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = resources.getDimensionPixelSize(R.dimen.destination_toolbar_padding)
            this.background = ColorDrawable(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            this.setPadding(pL, pT, pR, pB)
        }
    }

    private fun showShadow() {
        if (!shadowApplied) {
            shadowApplied = true
            val pL = this.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = getStatusBarHeight(context)
            }
            val pR = this.paddingRight
            val pB = resources.getDimensionPixelSize(R.dimen.destination_toolbar_padding)

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
        return height + 32
    }

}