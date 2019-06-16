package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.power_merchant.subscribe.IMG_URL_KYC_TRANSITION
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.fragment_transition_period.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImageHandler.LoadImage(img_kyc_verification,IMG_URL_KYC_TRANSITION)

    }
}