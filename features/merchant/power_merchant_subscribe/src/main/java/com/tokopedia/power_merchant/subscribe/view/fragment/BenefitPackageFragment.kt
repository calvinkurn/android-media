package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.viewmodel.PowerMerchantSharedViewModel
import javax.inject.Inject

class BenefitPackageFragment : BaseDaggerFragment() {

    @Inject
    lateinit var powerMerchantSharedViewModel: PowerMerchantSharedViewModel

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_benefit_package_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(): BenefitPackageFragment {
            return BenefitPackageFragment()
        }
    }
}