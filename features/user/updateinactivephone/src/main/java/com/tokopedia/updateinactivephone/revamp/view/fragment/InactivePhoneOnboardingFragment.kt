package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*

class InactivePhoneOnboardingFragment : BaseDaggerFragment() {

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentTransactionInterface = activity as FragmentTransactionInterface

        btnNext?.setOnClickListener {
            fragmentTransactionInterface.replace(InactivePhoneOnboardingIdCardFragment())
        }
    }
}