package com.tokopedia.withdraw.saldowithdrawal.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.AutoTopAdsBottomSheet

class AutoTopAdsBottomSheetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AutoTopAdsBottomSheet().apply {
            setOnDismissListener {
                finish()
            }
        }.show(supportFragmentManager, AutoTopAdsBottomSheet.TAG)
    }
}
