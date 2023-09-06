package com.tokopedia.withdraw.saldowithdrawal.presentation.fragment

import android.os.Bundle
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.util.BuyerSaldoWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount

class BuyerSaldoWithdrawalFragment : BaseWithdrawalFragment() {

    private var buyerSaldoBalance: Long = 0L

    override fun onViewCreated(savedInstanceState: Bundle?) {
        arguments?.let {
            buyerSaldoBalance = it.getLong(SaldoWithdrawalFragment.ARG_BUYER_SALDO_BALANCE_LONG, 0L)
        }
        setBalanceType(BuyerSaldoWithdrawal, buyerSaldoBalance)
    }

    override fun updateWithdrawalHint(bankAccount: BankAccount?, withdrawalAmount: Long) {
        bankAccount?.let {
            when {
                withdrawalAmount == 0L -> {
                    val minStr = getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.minAmount.toString()))
                    changeHint(false,
                            String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                withdrawalAmount !in 1L..bankAccount.maxAmount && bankAccount.isGopayEligible() -> {
                    changeHint(true,
                        context?.getString(R.string.swd_saldo_exceeding_withdraw_balance_gopay) ?: "")
                }
                withdrawalAmount !in 1L..buyerSaldoBalance -> {
                    changeHint(true,
                        context?.getString(R.string.swd_saldo_exceeding_withdraw_balance) ?: "")
                }
                withdrawalAmount in 1L until bankAccount.minAmount -> {
                    val minStr = getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.minAmount.toString()))
                    changeHint(true,
                            String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                withdrawalAmount in bankAccount.minAmount..bankAccount.maxAmount -> {
                    val minStr= getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.minAmount.toString()))
                    changeHint(false,
                            String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                else -> {
                    val maxStr = getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.maxAmount.toString()))
                    changeHint(true,
                            String.format(getString(R.string.swd_max_saldo_withdraw_hint), maxStr))
                }
            }
        } ?: run {
            changeHint(true, getString(R.string.swd_has_no_bank))
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
