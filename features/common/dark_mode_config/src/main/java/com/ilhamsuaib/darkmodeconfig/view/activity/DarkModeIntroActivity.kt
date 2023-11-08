package com.ilhamsuaib.darkmodeconfig.view.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.ilhamsuaib.darkmodeconfig.view.bottomsheet.DarkModeIntroBottomSheet
import com.tokopedia.abstraction.base.view.activity.BaseActivity

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

class DarkModeIntroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
        setTransparentStatusBar()

        showBottomSheet()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun setTransparentStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun showBottomSheet() {
        val fm = supportFragmentManager
        DarkModeIntroBottomSheet.getInstance(fm).apply {
            setOnDismissListener {
                this@DarkModeIntroActivity.finish()
            }
        }.show(fm)
    }
}