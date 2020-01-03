package com.tokopedia.saldodetails.presenter

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.contract.SaldoDetailContract
import com.tokopedia.saldodetails.deposit.listener.MerchantFinancialStatusActionListener
import com.tokopedia.saldodetails.response.model.*
import com.tokopedia.saldodetails.subscriber.GetMerchantCreditDetailsSubscriber
import com.tokopedia.saldodetails.subscriber.GetMerchantFinancialStatusSubscriber
import com.tokopedia.saldodetails.subscriber.GetMerchantSaldoDetailsSubscriber
import com.tokopedia.saldodetails.usecase.*
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.Companion.BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.Companion.BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SaldoDetailsPresenter @Inject constructor() :
        BaseDaggerPresenter<SaldoDetailContract.View>(), SaldoDetailContract.Presenter, MerchantFinancialStatusActionListener {

    private val withdrawActivityBundle = Bundle()

    @Inject
    internal lateinit var getDepositSummaryUseCase: GetDepositSummaryUseCase
    @Inject
    internal lateinit var getSaldoBalanceUseCase: GetSaldoBalanceUseCase
    @Inject
    internal lateinit var getTickerWithdrawalMessageUseCase: GetTickerWithdrawalMessageUseCase
    @Inject
    internal lateinit var setMerchantSaldoStatusUseCase: SetMerchantSaldoStatus
    @Inject
    internal lateinit var getMerchantSaldoDetails: GetMerchantSaldoDetails
    @Inject
    internal lateinit var getMerchantCreditDetails: GetMerchantCreditDetails
    @Inject
    internal lateinit var getMerchantFinancialStatus: GetMerchantFinancialStatus
    @Inject
    internal lateinit var getMCLLateCountUseCase: GetMCLLateCountUseCase

    var isSeller: Boolean = false

    override fun detachView() {
        super.detachView()
        try {
            setMerchantSaldoStatusUseCase.unsubscribe()
            getDepositSummaryUseCase.unsubscribe()
            getSaldoBalanceUseCase.unsubscribe()
            getTickerWithdrawalMessageUseCase.unsubscribe()
            getMerchantSaldoDetails.unsubscribe()
            getMerchantCreditDetails.unsubscribe()
            getMerchantFinancialStatus.unsubscribe()
            getMCLLateCountUseCase.unsubscribe()
        } catch (e: NullPointerException) {

        }

    }

    override fun getUserFinancialStatus() {
        val getMerchantFinancialStatusSubscribe = GetMerchantFinancialStatusSubscriber(this)
        getMerchantFinancialStatus.execute(getMerchantFinancialStatusSubscribe)
    }

    override fun getMerchantSaldoDetails() {
        val getMerchantSaldoDetailsSubscriber = GetMerchantSaldoDetailsSubscriber(this)
        getMerchantSaldoDetails.execute(getMerchantSaldoDetailsSubscriber)
    }

    override fun getMerchantCreditLineDetails() {
        val getMerchantCreditDetailsSubscriber = GetMerchantCreditDetailsSubscriber(this)
        getMerchantCreditDetails.execute(getMerchantCreditDetailsSubscriber)
    }

    override fun getSaldoBalance() {

        getSaldoBalanceUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (!isViewAttached) {
                    return
                }
                if (e is UnknownHostException) {
                    view.setRetry()

                } else if (e is SocketTimeoutException) {
                    view.setRetry()
                } else {
                    view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                }
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                if (!isViewAttached) {
                    return
                }

                val gqlSaldoBalanceResponse = graphqlResponse.getData<GqlSaldoBalanceResponse>(GqlSaldoBalanceResponse::class.java)
                        ?: return

                view.setSellerSaldoBalance(gqlSaldoBalanceResponse.saldo!!.sellerUsable,
                        gqlSaldoBalanceResponse.saldo!!.sellerUsableFmt!!)
                view.showSellerSaldoRL()

                view.setBuyerSaldoBalance(gqlSaldoBalanceResponse.saldo!!.buyerUsable,
                        gqlSaldoBalanceResponse.saldo!!.buyerUsableFmt!!)
                view.showBuyerSaldoRL()

                val totalBalance = gqlSaldoBalanceResponse.saldo!!.buyerUsable + gqlSaldoBalanceResponse.saldo!!.sellerUsable

                view.setBalance(totalBalance, CurrencyFormatUtil.convertPriceValueToIdrFormat(totalBalance, false))


                view.setWithdrawButtonState(totalBalance != 0L)

                val holdBalance = (gqlSaldoBalanceResponse.saldo!!.buyerHold + gqlSaldoBalanceResponse.saldo!!.sellerHold).toFloat()

                if (holdBalance > 0) {
                    view.showHoldWarning(CurrencyFormatUtil.convertPriceValueToIdrFormat(holdBalance.toDouble(), false))
                } else {
                    view.hideWarning()
                }
            }
        })

    }


    override fun getMCLLateCount() {

        getMCLLateCountUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

                if (!isViewAttached) {
                    return
                }
                view.hideWithdrawTicker()
            }

            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (graphqlResponse != null) {
                    val gqlMclLateCountResponse = graphqlResponse.getData<GqlMclLateCountResponse>(GqlMclLateCountResponse::class.java)
                    if (gqlMclLateCountResponse != null) {
                        view.setLateCount(gqlMclLateCountResponse.mclGetLatedetails!!.lateCount)
                    } else
                        view.hideWithdrawTicker()
                }
            }
        })
    }

    override fun getTickerWithdrawalMessage() {
        getTickerWithdrawalMessageUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideTickerMessage()
                }
            }

            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (graphqlResponse != null) {
                    val gqlWithdrawalTickerResponse = graphqlResponse.getData<GqlWithdrawalTickerResponse>(GqlWithdrawalTickerResponse::class.java)
                    if (gqlWithdrawalTickerResponse != null && !TextUtils.isEmpty(gqlWithdrawalTickerResponse.withdrawalTicker!!.tickerMessage)) {
                        view.showTickerMessage(gqlWithdrawalTickerResponse.withdrawalTicker!!.tickerMessage!!)
                    } else {
                        view.hideTickerMessage()
                    }
                }
            }
        })
    }

    override fun onDrawClicked(intent: Intent, statusWithDrawLock: Int, mclLateCount: Int, showMclBlockTickerFirebaseFlag: Boolean) {
        if (!isViewAttached) {
            return
        }
        val context = view.getContext()
        val userSession = UserSession(context)
        if (userSession.hasPassword()) {

            val sellerBalance = view.getSellerSaldoBalance()
            val buyerBalance = view.getBuyerSaldoBalance()

            val minSaldoLimit: Long = 10000
            if (sellerBalance < minSaldoLimit && buyerBalance < minSaldoLimit) {
                view.showErrorMessage(view.getString(com.tokopedia.saldodetails.R.string.saldo_min_withdrawal_error))
            } else {
                withdrawActivityBundle.putBoolean(FIREBASE_FLAG_STATUS, showMclBlockTickerFirebaseFlag)
                withdrawActivityBundle.putInt(IS_WITHDRAW_LOCK, statusWithDrawLock)
                withdrawActivityBundle.putInt(MCL_LATE_COUNT, mclLateCount)
                withdrawActivityBundle.putBoolean(IS_SELLER, isSeller)
                withdrawActivityBundle.putLong(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT, view.getBuyerSaldoBalance())
                withdrawActivityBundle.putLong(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT, view.getSellerSaldoBalance())
                launchWithdrawActivity(intent)
            }
        } else {
            view.showWithdrawalNoPassword()
        }
    }


    private fun launchWithdrawActivity(intent: Intent) {
        intent.putExtras(withdrawActivityBundle)
        view.getActivity()?.startActivityForResult(intent, REQUEST_WITHDRAW_CODE)
    }

    override fun hideSaldoPrioritasFragment() {
        if (!isViewAttached) {
            return
        }
        view.hideSaldoPrioritasFragment()
    }

    override fun hideUserFinancialStatusLayout() {
        if (!isViewAttached) {
            return
        }
        view.hideUserFinancialStatusLayout()
    }

    override fun showSaldoPrioritasFragment(sellerDetails: GqlDetailsResponse?) {
        if (!isViewAttached) {
            return
        }
        view.showSaldoPrioritasFragment(sellerDetails)
    }

    override fun hideMerchantCreditLineFragment() {
        if (!isViewAttached) {
            return
        }
        view.hideMerchantCreditLineFragment()
    }

    override fun showMerchantCreditLineFragment(response: GqlMerchantCreditResponse?) {
        if (!isViewAttached) {
            return
        }
        view.showMerchantCreditLineFragment(response)
    }

    companion object {

        @JvmField
        val REQUEST_WITHDRAW_CODE = 1
        private val IS_SELLER = "is_seller"
        private val IS_WITHDRAW_LOCK = "is_lock"
        private val MCL_LATE_COUNT = "late_count"
        private val FIREBASE_FLAG_STATUS = "is_on"
    }
}
