package com.tokopedia.minicart.bmgm.presentation.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.tokopedia.minicart.bmgm.presentation.bottomsheet.BmgmMiniCartDetailBottomSheet

/**
 * Created by @ilhamsuaib on 02/08/23.
 */

class BmgmMiniCartDetailActivity : AppCompatActivity() {

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
        val bottomSheet = BmgmMiniCartDetailBottomSheet.getInstance(supportFragmentManager)
        if (!supportFragmentManager.isStateSaved) {
            bottomSheet.setOnDismissListener {
                finish()
            }
            bottomSheet.show(supportFragmentManager, BmgmMiniCartDetailBottomSheet.TAG)
        }
    }
}