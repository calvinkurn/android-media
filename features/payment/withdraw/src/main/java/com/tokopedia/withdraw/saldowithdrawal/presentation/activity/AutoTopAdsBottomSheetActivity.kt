package com.tokopedia.withdraw.saldowithdrawal.presentation.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.AutoTopAdsBottomSheet

class AutoTopAdsBottomSheetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientation()
        val originalAmount = intent.extras?.getLong(ORIGINAL_AMOUNT) ?: 0
        val recommendedAmount = intent.extras?.getLong(RECOMMENDED_AMOUNT) ?: 0
        AutoTopAdsBottomSheet.getInstance(originalAmount, recommendedAmount).apply {
            setOnDismissListener {
                finish()
            }
        }.show(supportFragmentManager, AutoTopAdsBottomSheet.TAG)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    companion object {
        private const val ORIGINAL_AMOUNT = "originalAmount"
        private const val RECOMMENDED_AMOUNT = "recommendedAmount"
        fun getInstance(context: Context, originalAmount: Long, recommendedAmount: Long): Intent {
            val intent = Intent(context, AutoTopAdsBottomSheetActivity::class.java)
            intent.putExtra(ORIGINAL_AMOUNT, originalAmount)
            intent.putExtra(RECOMMENDED_AMOUNT, recommendedAmount)
            return intent
        }
    }
}
