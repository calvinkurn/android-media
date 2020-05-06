package com.tokopedia.withdraw.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.view.fragment.BuyerSaldoWithdrawalFragment
import com.tokopedia.withdraw.view.fragment.SellerSaldoWithdrawalFragment
import com.tokopedia.withdraw.view.fragment.WithdrawalBaseFragment

class SaldoWithdrawalPagerAdapter(val context: Context,
                                  fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragmentList: ArrayList<WithdrawalBaseFragment> = arrayListOf()

    override fun getPageTitle(position: Int): CharSequence? {
        return when (getItem(position)) {
            is BuyerSaldoWithdrawalFragment -> context.getString(R.string.tab_saldo_refund)
            is SellerSaldoWithdrawalFragment -> context.getString(R.string.swd_tab_buyer_income)
            else -> null
        }
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int = fragmentList.size

}