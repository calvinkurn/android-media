package com.tokopedia.vouchercreation.create.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.vouchercreation.create.view.fragment.BaseCreateMerchantVoucherFragment

class CreateMerchantVoucherStepsAdapter(fragmentActivity: FragmentActivity,
                                        private val fragmentList: List<BaseCreateMerchantVoucherFragment>) :
        FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int = fragmentList.size

    private fun onNextStep() {

    }
}