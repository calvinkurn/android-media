package com.tokopedia.home_wishlist.common

import android.annotation.TargetApi
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat.setElevation
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.home_wishlist.R


class ToolbarElevationOffsetListener(private val mActivity: AppCompatActivity, private val mToolbar: Toolbar) : AppBarLayout.OnOffsetChangedListener {
    private var mTargetElevation: Float = 0.toFloat()

    init {
        mTargetElevation = mActivity.resources.getDimension(R.dimen.dp_4)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        var offset = offset
        offset = Math.abs(offset)
        mTargetElevation = Math.max(mTargetElevation, appBarLayout.targetElevation)
        if (offset >= appBarLayout.totalScrollRange - mToolbar!!.getHeight()) {
            val flexibleSpace = (appBarLayout.totalScrollRange - offset).toFloat()
            val ratio = 1 - flexibleSpace / mToolbar!!.getHeight()
            val elevation = ratio * mTargetElevation
            setToolbarElevation(elevation)
        } else {
            setToolbarElevation(0f)
        }

    }

    private fun setToolbarElevation(targetElevation: Float) {
        val supportActionBar = mActivity.supportActionBar
        if (supportActionBar != null)
            supportActionBar.elevation = targetElevation
        else setElevation(mToolbar, targetElevation)
    }
}