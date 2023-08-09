package com.tokopedia.minicart.bmgm.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.minicart.bmgm.presentation.bottomsheet.BmgmMiniCartBottomSheet

/**
 * Created by @ilhamsuaib on 02/08/23.
 */

class BmgmMiniCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showBottomSheet()
    }

    private fun showBottomSheet() {
        val bottomSheet = BmgmMiniCartBottomSheet.getInstance(supportFragmentManager)
        if (!supportFragmentManager.isStateSaved) {
            bottomSheet.setOnDismissListener {
                finish()
            }
            bottomSheet.show(supportFragmentManager, BmgmMiniCartBottomSheet.TAG)
        }
    }
}