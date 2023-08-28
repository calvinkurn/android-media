package com.tokopedia.minicart.bmgm.presentation.activity

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

        setTransparentStatusBar()
        showBottomSheet()
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