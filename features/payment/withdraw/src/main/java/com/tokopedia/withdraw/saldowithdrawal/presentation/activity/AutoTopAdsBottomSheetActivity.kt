package com.tokopedia.withdraw.saldowithdrawal.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.presentation.dialog.AutoTopAdsBottomSheet
import com.tokopedia.withdraw.saldowithdrawal.presentation.listener.AutoTopAdsBottomSheetListener

class AutoTopAdsBottomSheetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val originalAmount = intent.extras?.getLong(ORIGINAL_AMOUNT) ?: 0
        val recommendedAmount = intent.extras?.getLong(RECOMMENDED_AMOUNT) ?: 0
        AutoTopAdsBottomSheet.getInstance(originalAmount, recommendedAmount).apply {
            setOnDismissListener {
                finish()
            }
        }.show(supportFragmentManager, AutoTopAdsBottomSheet.TAG)
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
