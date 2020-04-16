package com.tokopedia.vouchercreation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.vouchercreation.R

class MerchantVoucherTargetFragment(onNextInvoker: () -> Unit = {}) : BaseCreateMerchantVoucherFragment(onNextInvoker) {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit) = MerchantVoucherTargetFragment(onNext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_merchant_voucher_target, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }
}