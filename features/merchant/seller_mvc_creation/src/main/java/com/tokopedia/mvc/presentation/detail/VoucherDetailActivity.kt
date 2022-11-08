package com.tokopedia.mvc.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.seller_mvc_creation.R

class VoucherDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_voucher_detail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, VoucherDetailFragment.newInstance(0))
                .commitNow()
        }
    }
}
