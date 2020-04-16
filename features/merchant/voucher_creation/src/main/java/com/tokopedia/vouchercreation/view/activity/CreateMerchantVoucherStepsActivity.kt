package com.tokopedia.vouchercreation.view.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.view.adapter.CreateMerchantVoucherStepsAdapter
import kotlinx.android.synthetic.main.activity_create_merchant_voucher_steps.*

class CreateMerchantVoucherStepsActivity : FragmentActivity() {

    private val viewPagerAdapter by lazy {
        CreateMerchantVoucherStepsAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_merchant_voucher_steps)
        setupAdapter()
        observeLiveData()
    }

    private fun setupAdapter() {
        createMerchantVoucherViewPager.adapter = viewPagerAdapter
    }

    private fun observeLiveData() {

    }

}