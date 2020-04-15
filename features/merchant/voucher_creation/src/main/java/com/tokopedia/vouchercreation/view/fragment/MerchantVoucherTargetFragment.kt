package com.tokopedia.vouchercreation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.vouchercreation.R

class MerchantVoucherTargetFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun createInstance() = MerchantVoucherTargetFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_merchant_voucher_target, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }
}