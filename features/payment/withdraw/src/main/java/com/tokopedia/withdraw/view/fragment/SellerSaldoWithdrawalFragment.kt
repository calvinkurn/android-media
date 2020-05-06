package com.tokopedia.withdraw.view.fragment

import android.os.Bundle
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.domain.model.BankAccount

class SellerSaldoWithdrawalFragment : WithdrawalBaseFragment() {

    private var sellerSaldoBalance: Long = 0L
    var showMclBlockTickerFirebaseFlag: Boolean = false
    var mclLateCount = 0
    var sellerWithdrawalLocked = 0

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
            when (withdrawalAmount) {
                0L -> {
                    val minStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.minAmount, false)
                    changeHintTextColor(R.color.grey_button_compat, R.color.tkpd_main_green,
                            String.format(getString(R.string.min_saldo_withdraw_hint), minStr))
                }
                !in 1L..sellerSaldoBalance -> {
                    changeHintTextColor(R.color.hint_red, R.color.hint_red,
                            getString(R.string.saldo_exceeding_withdraw_balance))
                }

                in 1L until bankAccount.minAmount -> {
                    val minStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.minAmount, false)
                    changeHintTextColor(R.color.hint_red, R.color.hint_red,
                            String.format(getString(R.string.min_saldo_withdraw_hint), minStr))
                }
                in bankAccount.minAmount..bankAccount.maxAmount -> {
                    val minStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.minAmount, false)
                    changeHintTextColor(R.color.grey_button_compat, R.color.tkpd_main_green,
                            String.format(getString(R.string.min_saldo_withdraw_hint), minStr))
                }
                else -> {
                    val maxStr = CurrencyFormatUtil
                            .convertPriceValueToIdrFormat(bankAccount.maxAmount, false)
                    changeHintTextColor(R.color.hint_red, R.color.hint_red,
                            String.format(getString(R.string.max_saldo_withdraw_hint), maxStr))
                }
            }
        } ?: run {
            changeHintTextColor(R.color.grey_button_compat,
                    R.color.hint_red, getString(R.string.has_no_bank))
        }
    }

    override fun updateWithdrawalButtonState(bankAccount: BankAccount?, withdrawalAmount: Long) {
        val canWithdraw: Boolean = if (isSellerWithdrawalLocked()) {
            true
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