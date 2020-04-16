package com.tokopedia.vouchercreation.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.vouchercreation.view.fragment.MerchantVoucherTargetFragment

class CreateMerchantVoucherStepsAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

    private val createMerchantVoucherFragments by lazy {
        arrayOf(
                MerchantVoucherTargetFragment.createInstance(::onNextStep),
                MerchantVoucherTargetFragment.createInstance(::onNextStep))
    }

    override fun createFragment(position: Int): Fragment {
        return createMerchantVoucherFragments[position]
    }

    override fun getItemCount(): Int = createMerchantVoucherFragments.size

    private fun onNextStep() {

    }
}