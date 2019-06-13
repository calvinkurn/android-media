package com.tokopedia.power_merchant.subscribe.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.power_merchant.subscribe.R

class TransitionPeriodPmFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = TransitionPeriodPmFragment()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transition_period, container, false)
    }


}