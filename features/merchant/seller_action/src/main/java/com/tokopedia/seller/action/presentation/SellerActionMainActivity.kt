package com.tokopedia.seller.action.presentation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

class SellerActionMainActivity: AppCompatActivity() {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let {
            handleDeeplink(it)
        }
    }

    private fun handleDeeplink(data: Uri?) {

    }

}