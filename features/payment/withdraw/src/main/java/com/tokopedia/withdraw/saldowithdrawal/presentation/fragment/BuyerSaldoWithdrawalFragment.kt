package com.tokopedia.withdraw.saldowithdrawal.presentation.fragment

import android.os.Bundle
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.constant.BuyerSaldoWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount

class BuyerSaldoWithdrawalFragment : WithdrawalBaseFragment() {

    private var buyerSaldoBalance: Long = 0L

    override fun onViewCreated(savedInstanceState : Bundle?) {
        arguments?.let {
            buyerSaldoBalance = it.getLong(SaldoWithdrawalFragment.ARG_BUYER_SALDO_BALANCE_LONG, 0L)
        }
        setBalanceType(BuyerSaldoWithdrawal, buyerSaldoBalance)
    }

    override fun updateWithdrawalHint(bankAccount: BankAccount?, withdrawalAmount: Long) {
        bankAccount?.let {
            when (withdrawalAmount) {
                0L -> {
                    val minStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.minAmount, false)
                    changeHintTextColor(R.color.grey_button_compat, R.color.tkpd_main_green,
                            String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                !in 1L..buyerSaldoBalance -> {
                    changeHintTextColor(R.color.swd_hint_red, R.color.swd_hint_red,
                            getString(R.string.saldo_exceeding_withdraw_balance))
                }

                in 1L until bankAccount.minAmount -> {
                    val minStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.minAmount, false)
                    changeHintTextColor(R.color.swd_hint_red, R.color.swd_hint_red,
                            String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                in bankAccount.minAmount..bankAccount.maxAmount -> {
                    val minStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.minAmount, false)
                    changeHintTextColor(R.color.grey_button_compat, R.color.tkpd_main_green,
                            String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                else -> {
                    val maxStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.maxAmount, false)
                    changeHintTextColor(R.color.swd_hint_red, R.color.swd_hint_red,
                            String.format(getString(R.string.swd_max_saldo_withdraw_hint), maxStr))
                }
            }
        } ?: run {
            changeHintTextColor(R.color.grey_button_compat,
                    R.color.swd_hint_red, getString(R.string.swd_has_no_bank))
        }
    }

    override fun updateWithdrawalButtonState(bankAccount: BankAccount?, withdrawalAmount: Long) {
        val canWithdraw: Boolean = bankAccount?.let {
            return@let when (withdrawalAmount) {
                0L -> false
                !in 1L..buyerSaldoBalance -> false
                in 1L until bankAccount.minAmount -> false
                in bankAccount.minAmount..bankAccount.maxAmount -> true
                else -> false
            }
        } ?: run {
            return@run false
        }
        updateWithdrawalButton(canWithdraw)
    }

    companion object {
        fun getInstance(bundle: Bundle) = BuyerSaldoWithdrawalFragment().apply {
            arguments = bundle
        }
    }

}