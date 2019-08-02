package com.tokopedia.power_merchant.subscribe.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.power_merchant.subscribe.R

class PowerMerchantCancellationQuestionnaireFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance() = PowerMerchantCancellationQuestionnaireFragment()
    }

    override fun getScreenName() : String = ""

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_power_merchant_cancellation_questionnaire, container, false)
    }


}
