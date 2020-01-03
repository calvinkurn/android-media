package com.tokopedia.saldodetails.contract

import android.app.Activity
import android.content.Context
import android.content.Intent

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse

interface SaldoDetailContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun getActivity(): Activity?

        fun getSellerSaldoBalance(): Long

        fun getBuyerSaldoBalance(): Long

        fun isUserSeller(): Boolean

        fun showErrorMessage(s: String)

        fun getString(resId: Int): String

        fun hideWarning()

        fun refresh()

        fun showEmptyState()

        fun setRetry()

        fun setRetry(error: String)

        fun showWithdrawalNoPassword()

        fun setBalance(totalBalance: Long, summaryUsableDepositIdr: String)

        fun setWithdrawButtonState(state: Boolean)

        fun showHoldWarning(warningText: String)

        fun setBuyerSaldoBalance(amount: Long, text: String)

        fun showSaldoPrioritasFragment(sellerDetails: GqlDetailsResponse?)

        fun hideSaldoPrioritasFragment()

        fun hideUserFinancialStatusLayout()

        fun hideMerchantCreditLineFragment()

        fun showMerchantCreditLineFragment(response: GqlMerchantCreditResponse?)

        fun showTickerMessage(withdrawalTicker: String)

        fun hideTickerMessage()

        fun setLateCount(count: Int)

        fun hideWithdrawTicker()

        fun showSellerSaldoRL()

        fun setSellerSaldoBalance(amount: Long, formattedAmount: String)

        fun showBuyerSaldoRL()

    }

    interface Presenter : CustomerPresenter<SaldoDetailContract.View> {
        fun getSaldoBalance()

        fun getTickerWithdrawalMessage()

        fun getMerchantSaldoDetails()

        fun getUserFinancialStatus()

        fun getMerchantCreditLineDetails()

        fun onDrawClicked(intent: Intent, statusWithDrawLock: Int, mclLateCount: Int, showMclBlockTickerFirebaseFlag: Boolean)

        fun getMCLLateCount()
    }
}
