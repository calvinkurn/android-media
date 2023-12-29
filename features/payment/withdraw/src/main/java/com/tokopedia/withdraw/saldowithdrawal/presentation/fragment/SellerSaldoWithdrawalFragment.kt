package com.tokopedia.withdraw.saldowithdrawal.presentation.fragment

import android.os.Bundle
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.util.SellerSaldoWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount

class SellerSaldoWithdrawalFragment : BaseWithdrawalFragment() {

    private var sellerSaldoBalance: Long = 0L
    private var showMclBlockTickerFirebaseFlag: Boolean = false
    private var mclLateCount = 0
    private var sellerWithdrawalLocked = 0

    override fun onViewCreated(savedInstanceState: Bundle?) {
        arguments?.let {
            sellerSaldoBalance = it.getLong(
                    SaldoWithdrawalFragment.ARG_SELLER_SALDO_BALANCE_LONG, 0L)
            showMclBlockTickerFirebaseFlag = it.getBoolean(
                    SaldoWithdrawalFragment.ARG_FIREBASE_FLAG_STATUS_BOOLEAN, false)
            mclLateCount = it.getInt(SaldoWithdrawalFragment.ARG_MCL_LATE_COUNT_INT, 0)
            sellerWithdrawalLocked = it.getInt(
                    SaldoWithdrawalFragment.ARG_SELLER_WITHDRAWAL_LOCK_INT, 0)
        }
        setBalanceType(SellerSaldoWithdrawal, sellerSaldoBalance)
        lockWithdrawal(isSellerWithdrawalLocked())
    }

    private fun isSellerWithdrawalLocked(): Boolean {
        return ((sellerWithdrawalLocked == MCL_STATUS_BLOCK3 ||
                sellerWithdrawalLocked == MCL_STATUS_BLOCK1)
                && showMclBlockTickerFirebaseFlag)
    }

    override fun updateWithdrawalHint(bankAccount: BankAccount?, withdrawalAmount: Long) {
        bankAccount?.let {
            when {
                withdrawalAmount == 0L -> {
                    val minStr = getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.minAmount.toString()))
                    changeHint(false, String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                withdrawalAmount !in 1L..bankAccount.maxAmount && bankAccount.isGopayEligible() -> {
                    changeHint(true,
                        context?.getString(R.string.swd_saldo_exceeding_withdraw_balance_gopay) ?: "")
                }
                withdrawalAmount !in 1L..sellerSaldoBalance -> {
                    changeHint(true,
                    context?.getString(R.string.swd_saldo_exceeding_withdraw_balance) ?: "")
                }

                withdrawalAmount in 1L until bankAccount.minAmount -> {
                    val minStr = getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.minAmount.toString()))
                    changeHint(true, String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                withdrawalAmount in bankAccount.minAmount..bankAccount.maxAmount -> {
                    val minStr = getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.minAmount.toString()))
                    changeHint(false,
                            String.format(getString(R.string.swd_min_saldo_withdraw_hint), minStr))
                }
                else -> {
                    val maxStr = getString(R.string.swd_rp,
                            CurrencyFormatHelper.convertToRupiah(bankAccount.maxAmount.toString()))
                    changeHint( true, String.format(getString(R.string.swd_max_saldo_withdraw_hint), maxStr))
                }
            }
        } ?: run {
            changeHint(true, getString(R.string.swd_has_no_bank))
        }
    }

    override fun updateWithdrawalButtonState(bankAccount: BankAccount?, withdrawalAmount: Long) {
        val canWithdraw: Boolean = if (isSellerWithdrawalLocked()) {
            false
        } else {
            bankAccount?.let {
                return@let when (withdrawalAmount) {
                    0L -> false
                    !in 1L..sellerSaldoBalance -> false
                    in 1L until bankAccount.minAmount -> false
                    in bankAccount.minAmount..bankAccount.maxAmount -> true
                    else -> false
                }
            } ?: run {
                return@run false
            }
        }
        updateWithdrawalButton(canWithdraw)
    }

    companion object {
        private const val MCL_STATUS_BLOCK1 = 700
        private const val MCL_STATUS_BLOCK3 = 999
        fun getInstance(bundle: Bundle) = SellerSaldoWithdrawalFragment().apply {
            arguments = bundle
        }
    }

}
