package com.tokopedia.withdraw.saldowithdrawal.presentation.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.BuyerSaldoWithdrawalFragment
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.SellerSaldoWithdrawalFragment
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.BaseWithdrawalFragment

class SaldoWithdrawalPagerAdapter(val context: Context,
                                  fragmentManager: FragmentManager,
                                  var arguments: Bundle)
    : FragmentStatePagerAdapter(fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getPageTitle(position: Int): CharSequence? {
        return when (getItem(position)) {
            is BuyerSaldoWithdrawalFragment -> context.getString(R.string.swd_tab_saldo_refund)
            is SellerSaldoWithdrawalFragment -> context.getString(R.string.swd_tab_buyer_income)
            else -> null
        }
    }

    override fun getItem(position: Int): Fragment {
        return if(position == 0){
            BuyerSaldoWithdrawalFragment.getInstance(arguments)
        } else {
            SellerSaldoWithdrawalFragment.getInstance(arguments)
        }
    }

    override fun getCount(): Int = 2

}