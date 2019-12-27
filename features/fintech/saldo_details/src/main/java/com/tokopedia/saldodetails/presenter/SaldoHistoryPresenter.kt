package com.tokopedia.saldodetails.presenter

import android.content.Context
import android.util.Log

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.date.util.SaldoDatePickerUtil
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.contract.SaldoHistoryContract
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.response.model.SummaryDepositParam
import com.tokopedia.saldodetails.usecase.GetAllTransactionUsecase
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

import javax.inject.Inject

import rx.Subscriber

import android.content.ContentValues.TAG

class SaldoHistoryPresenter @Inject constructor(@ApplicationContext context: Context) :
        BaseDaggerPresenter<SaldoHistoryContract.View>(), SaldoHistoryContract.Presenter {
    @Inject
    internal lateinit var getDepositSummaryUseCase: GetDepositSummaryUseCase
    @Inject
    internal lateinit var getAllTransactionUsecase: GetAllTransactionUsecase

    private val paging = PagingHandler()
    private var paramStartDate: String? = null
    private var paramEndDate: String? = null

    private val summaryDepositParam: Map<String, Any>
        get() {
            val param = SummaryDepositParam()
            val sdf = SimpleDateFormat(DATE_FORMAT_VIEW, Locale.US)
            val sdf_ws = SimpleDateFormat(DATE_FORMAT_WS, Locale.US)
            try {
                val formattedStart = sdf.parse(paramStartDate)
                val formattedEnd = sdf.parse(paramEndDate)
                param.startDate = getDateParam(sdf_ws.format(formattedStart))
                param.endDate = getDateParam(sdf_ws.format(formattedEnd))
            } catch (e: ParseException) {
                view.showErrorMessage(view.getString(com.tokopedia.saldodetails.R.string.sp_error_invalid_date))
            }

            param.page = paging.page
            return param.paramSummaryDeposit
        }

    private val isValid: Boolean
        get() {
            var isValid = true

            val sdf = SimpleDateFormat(DATE_FORMAT_VIEW, Locale.US)
            try {
                val endDate = sdf.parse(paramEndDate)
                val startDate = sdf.parse(paramStartDate)
                if (endDate.time - startDate.time < 0) {
                    isValid = false
                    view.showInvalidDateError(view.getString(com.tokopedia.saldodetails.R.string.sp_error_invalid_date))
                }

                if ((endDate.time - startDate.time) / SEC_TO_DAY_CONVERSION >= MAX_DAYS_DIFFERENCE) {
                    isValid = false
                    view.showInvalidDateError(view.getString(com.tokopedia.saldodetails.R.string.sp_title_max_day))
                }
            } catch (e: ParseException) {
                isValid = false
                view.showInvalidDateError(view.getString(com.tokopedia.saldodetails.R.string.sp_error_invalid_date))
            }

            return isValid
        }


    override fun setFirstDateParameter() {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_VIEW)
        val date = Date()
        val cal = GregorianCalendar()
        cal.time = date
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val startDate = dateFormat.format(cal.time)
        val endDate = dateFormat.format(date)
        view.startDate = startDate
        view.endDate = endDate
        paramStartDate = startDate
        paramEndDate = endDate
    }

    override fun onSearchClicked() {
        paramStartDate = view.startDate
        paramEndDate = view.endDate
        getSummaryDeposit()
    }

    private fun setData(data: GqlAllDepositSummaryResponse?) {
        if (!isViewAttached || view.adapter == null || data == null) {
            return
        }


        view.allHistoryAdapter.addElement(data.allDepositHistory!!.depositHistoryList)
        (view.allSaldoHistoryTabItem.fragment as BaseListFragment<*, *>).updateScrollListenerState(data.allDepositHistory!!.isHaveNextPage)
        if (view.allHistoryAdapter.itemCount == 0) {
            view.allHistoryAdapter.addElement(view.defaultEmptyViewModel)
        }

        view.buyerHistoryAdapter.addElement(data.buyerDepositHistory!!.depositHistoryList)
        (view.buyerSaldoHistoryTabItem.fragment as BaseListFragment<*, *>).updateScrollListenerState(data.buyerDepositHistory!!.isHaveNextPage)
        if (view.buyerHistoryAdapter.itemCount == 0) {
            view.buyerHistoryAdapter.addElement(view.defaultEmptyViewModel)
        }

        view.sellerHistoryAdapter.addElement(data.sellerDepositHistory!!.depositHistoryList)
        (view.sellerSaldoHistoryTabItem.fragment as BaseListFragment<*, *>).updateScrollListenerState(data.sellerDepositHistory!!.isHaveNextPage)
        if (view.sellerHistoryAdapter.itemCount == 0) {
            view.sellerHistoryAdapter.addElement(view.defaultEmptyViewModel)
        }

    }

    private fun setData(data: GqlCompleteTransactionResponse?) {
        if (!isViewAttached || view.allHistoryAdapter == null || data == null) {
            return
        }

        view.allHistoryAdapter.addElement(data.allDepositHistory!!.depositHistoryList)
        (view.allSaldoHistoryTabItem.fragment as BaseListFragment<*, *>).updateScrollListenerState(data.allDepositHistory!!.isHaveNextPage)
        if (view.allHistoryAdapter.itemCount == 0) {
            view.allHistoryAdapter.addElement(view.defaultEmptyViewModel)
        }

    }

    private fun showLoading() {
        if (isViewAttached && view.adapter != null) {
            view.adapter.showLoading()
        }
    }

    private fun hideLoading() {
        if (isViewAttached && view.adapter != null) {
            view.adapter.hideLoading()
            view.finishLoading()
        }
    }

    override fun onEndDateClicked(datePicker: SaldoDatePickerUtil) {
        val date = dateFormatter(view.endDate)
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date))
        datePicker.DatePickerCalendar { year, month, day ->
            val selectedDate = this@SaldoHistoryPresenter.getDate(year, month, day)
            this@SaldoHistoryPresenter.view.endDate = selectedDate
            android.os.Handler().postDelayed({ this@SaldoHistoryPresenter.onSearchClicked() }, SEARCH_DELAY)
        }
    }

    override fun onStartDateClicked(datePicker: SaldoDatePickerUtil) {
        val date = dateFormatter(view.startDate)
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date))
        datePicker.DatePickerCalendar { year, month, day ->
            val selectedDate = getDate(year, month, day)
            view.startDate = selectedDate
            android.os.Handler().postDelayed({ this.onSearchClicked() }, SEARCH_DELAY)

        }
    }

    private fun getDate(year: Int, month: Int, day: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_VIEW, Locale.US)
        val date = Date()
        val cal = GregorianCalendar()
        cal.time = date
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.MONTH, month - 1)
        return dateFormat.format(cal.time)
    }

    private fun getStartYear(date: String): Int {
        val year = date.substring(6, 10)
        return Integer.parseInt(year)
    }

    private fun getStartMonth(date: String): Int {
        val month = date.substring(3, 5)
        return Integer.parseInt(month)
    }

    private fun getDay(date: String): Int {
        val day = date.substring(0, 2)
        return Integer.parseInt(day)
    }

    private fun dateFormatter(date: String): String {

        val sdf = SimpleDateFormat(DATE_FORMAT_VIEW, Locale.US)
        val sdf_ws = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        var formattedStart: Date? = null
        try {
            formattedStart = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return sdf_ws.format(formattedStart)

    }

    override fun loadMore(lastItemPosition: Int, visibleItem: Int) {
        if (paging.CheckNextPage()
                && isOnLastPosition(lastItemPosition, visibleItem)
                && canLoadMore()) {
            paging.nextPage()
            getSummaryDeposit()
        }
    }

    private fun canLoadMore(): Boolean {
        return !getDepositSummaryUseCase.isRequesting
    }

    private fun isOnLastPosition(lastItemPosition: Int, visibleItem: Int): Boolean {
        return lastItemPosition == visibleItem
    }

    override fun getSummaryDeposit() {
        if (!isViewAttached) {
            return
        }
        view.removeError()
        if (isValid) {
            showLoading()
            view.setActionsEnabled(false)

            getDepositSummaryUseCase.isRequesting = true
            getDepositSummaryUseCase.setRequestVariables(summaryDepositParam)

            getDepositSummaryUseCase.execute(object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {
                    getDepositSummaryUseCase.isRequesting = false
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, e.toString())
                    if (isViewNotAttached) {
                        return
                    }
                    hideLoading()
                    ErrorHandler.getErrorMessage(view.context, e)
                    if (e is UnknownHostException || e is SocketTimeoutException) {
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState()
                        } else {
                            view.setRetry()
                        }

                    } else {
                        view.setActionsEnabled(true)
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        } else {
                            view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        }
                    }
                }

                override fun onNext(graphqlResponse: GraphqlResponse) {
                    hideLoading()
                    onDepositSummaryFetched(graphqlResponse)
                }
            })

        } else {
            view.finishLoading()
        }
    }

    private fun onDepositSummaryFetched(graphqlResponse: GraphqlResponse?) {
        if (!isViewAttached) {
            return
        }
        view.setActionsEnabled(true)

        if (graphqlResponse?.getData<Any>(GqlAllDepositSummaryResponse::class.java) != null) {

            val gqlDepositSummaryResponse = graphqlResponse.getData<GqlAllDepositSummaryResponse>(GqlAllDepositSummaryResponse::class.java)

            if (gqlDepositSummaryResponse != null && !gqlDepositSummaryResponse.allDepositHistory!!.isHaveError) {

                if (paging.page == 1) {
                    view.allHistoryAdapter.clearAllElements()
                    view.buyerHistoryAdapter.clearAllElements()
                    view.sellerHistoryAdapter.clearAllElements()
                }
                setData(gqlDepositSummaryResponse)
            } else {
                if (gqlDepositSummaryResponse?.allDepositHistory != null) {
                    if (view.allHistoryAdapter.itemCount == 0) {
                        view.showEmptyState(gqlDepositSummaryResponse.allDepositHistory!!.message!!)
                    } else {
                        view.setRetry(gqlDepositSummaryResponse.allDepositHistory!!.message!!)
                    }
                }

                if (gqlDepositSummaryResponse?.buyerDepositHistory != null) {
                    if (view.buyerHistoryAdapter.itemCount == 0) {
                        view.showEmptyState(gqlDepositSummaryResponse.buyerDepositHistory!!.message!!)
                    } else {
                        view.setRetry(gqlDepositSummaryResponse.buyerDepositHistory!!.message!!)
                    }
                }

                if (gqlDepositSummaryResponse?.sellerDepositHistory != null) {
                    if (view.sellerHistoryAdapter.itemCount == 0) {
                        view.showEmptyState(gqlDepositSummaryResponse.sellerDepositHistory!!.message!!)
                    } else {
                        view.setRetry(gqlDepositSummaryResponse.sellerDepositHistory!!.message!!)
                    }
                }
            }

        } else {
            if (view.adapter != null && view.adapter.itemCount == 0) {
                view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
            } else {
                view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
            }

        }

        finishLoading()
    }

    private fun finishLoading() {
        if (isViewAttached) {
            view.finishLoading()
        }
    }

    override fun onRefresh() {
        paging.resetPage()
        getSummaryDeposit()
    }

    private fun getDateParam(date: String): String {
        return date.replace("/", "-")
    }

    private fun getSummaryDepositParam(page: Int, saldoType: Int): Map<String, Any> {
        val param = SummaryDepositParam()
        val sdf = SimpleDateFormat(DATE_FORMAT_VIEW, Locale.US)
        val sdf_ws = SimpleDateFormat(DATE_FORMAT_WS, Locale.US)
        try {
            val formattedStart = sdf.parse(paramStartDate)
            val formattedEnd = sdf.parse(paramEndDate)
            param.startDate = getDateParam(sdf_ws.format(formattedStart))
            param.endDate = getDateParam(sdf_ws.format(formattedEnd))
        } catch (e: ParseException) {
            view.showErrorMessage(view.getString(com.tokopedia.saldodetails.R.string.sp_error_invalid_date))
        }

        param.page = page
        param.setSaldoType(saldoType)
        return param.paramSummaryDeposit

    }

    override fun detachView() {
        super.detachView()
        try {
            getDepositSummaryUseCase.unsubscribe()
        } catch (e: Exception) {

        }

    }

    fun loadMoreAllTransaction(lastItemPosition: Int, type: Int) {
        if (!isViewAttached) {
            return
        }
        view.removeError()
        if (isValid) {
            showLoading()
            view.setActionsEnabled(false)

            getAllTransactionUsecase.setRequesting(true)
            getAllTransactionUsecase.setRequestVariables(getSummaryDepositParam(lastItemPosition, type))
            getAllTransactionUsecase.execute(object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {
                    getAllTransactionUsecase.setRequesting(false)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, e.toString())
                    if (isViewNotAttached) {
                        return
                    }
                    hideLoading()
                    ErrorHandler.getErrorMessage(view.context, e)
                    if (e is UnknownHostException || e is SocketTimeoutException) {
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState()
                        } else {
                            view.setRetry()
                        }

                    } else {
                        view.setActionsEnabled(true)
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        } else {
                            view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        }
                    }
                }

                override fun onNext(graphqlResponse: GraphqlResponse) {
                    hideLoading()
                    onAllTransactionsFetched(graphqlResponse, lastItemPosition)
                }

            })

        } else {
            finishLoading()
        }
    }

    private fun onAllTransactionsFetched(graphqlResponse: GraphqlResponse?, page: Int) {

        if (!isViewAttached) {
            return
        }
        view.setActionsEnabled(true)

        if (graphqlResponse?.getData<Any>(GqlCompleteTransactionResponse::class.java) != null) {

            val gqlCompleteTransactionResponse = graphqlResponse.getData<GqlCompleteTransactionResponse>(GqlCompleteTransactionResponse::class.java)

            if (gqlCompleteTransactionResponse != null && !gqlCompleteTransactionResponse.allDepositHistory!!.isHaveError) {
                setData(gqlCompleteTransactionResponse)
            } else {
                if (gqlCompleteTransactionResponse?.allDepositHistory != null) {
                    if (view.allHistoryAdapter.itemCount == 0) {
                        view.showEmptyState(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                    } else {
                        view.setRetry(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                    }
                }
            }
        } else {
            if (view.adapter != null && view.adapter.itemCount == 0) {
                view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
            } else {
                view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
            }
        }
        finishLoading()
    }

    fun loadMoreSellerTransaction(lastItemPosition: Int, type: Int) {
        if (!isViewAttached) {
            return
        }
        view.removeError()
        if (isValid) {
            showLoading()
            view.setActionsEnabled(false)

            getAllTransactionUsecase.setRequesting(true)
            getAllTransactionUsecase.setRequestVariables(getSummaryDepositParam(lastItemPosition, type))
            getAllTransactionUsecase.execute(object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {
                    getAllTransactionUsecase.setRequesting(false)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, e.toString())
                    if (isViewNotAttached) {
                        return
                    }
                    hideLoading()
                    ErrorHandler.getErrorMessage(view.context, e)
                    if (e is UnknownHostException || e is SocketTimeoutException) {
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState()
                        } else {
                            view.setRetry()
                        }

                    } else {
                        view.setActionsEnabled(true)
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        } else {
                            view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        }
                    }
                }

                override fun onNext(graphqlResponse: GraphqlResponse?) {
                    hideLoading()
                    if (!isViewAttached) {
                        return
                    }
                    view.setActionsEnabled(true)

                    if (graphqlResponse?.getData<Any>(GqlAllDepositSummaryResponse::class.java) != null) {

                        val gqlCompleteTransactionResponse = graphqlResponse.getData<GqlCompleteTransactionResponse>(GqlCompleteTransactionResponse::class.java)

                        if (gqlCompleteTransactionResponse != null && !gqlCompleteTransactionResponse.allDepositHistory!!.isHaveError) {
                            if (!isViewAttached || view.sellerHistoryAdapter == null) {
                                return
                            }

                            (view.sellerSaldoHistoryTabItem.fragment as BaseListFragment<*, *>).updateScrollListenerState(gqlCompleteTransactionResponse.allDepositHistory!!.isHaveNextPage)

                            view.sellerHistoryAdapter.addElement(
                                    gqlCompleteTransactionResponse.allDepositHistory!!.depositHistoryList)
                            if (view.sellerHistoryAdapter.itemCount == 0) {
                                view.sellerHistoryAdapter.addElement(view.defaultEmptyViewModel)
                            }
                        } else {
                            if (gqlCompleteTransactionResponse?.allDepositHistory != null) {
                                if (view.sellerHistoryAdapter.itemCount == 0) {
                                    view.showEmptyState(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                                } else {
                                    view.setRetry(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                                }
                            }
                        }
                    } else {
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        } else {
                            view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        }
                    }
                    finishLoading()
                }

            })

        } else {
            finishLoading()
        }
    }

    fun loadMoreBuyerTransaction(lastItemPosition: Int, type: Int) {
        if (!isViewAttached) {
            return
        }
        view.removeError()
        if (isValid) {
            showLoading()
            view.setActionsEnabled(false)

            getAllTransactionUsecase.setRequesting(true)
            getAllTransactionUsecase.setRequestVariables(getSummaryDepositParam(lastItemPosition, type))
            getAllTransactionUsecase.execute(object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {
                    getAllTransactionUsecase.setRequesting(false)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, e.toString())
                    if (isViewNotAttached) {
                        return
                    }
                    hideLoading()
                    ErrorHandler.getErrorMessage(view.context, e)
                    if (e is UnknownHostException || e is SocketTimeoutException) {
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState()
                        } else {
                            view.setRetry()
                        }

                    } else {
                        view.setActionsEnabled(true)
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        } else {
                            view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        }
                    }
                }

                override fun onNext(graphqlResponse: GraphqlResponse?) {
                    hideLoading()
                    if (!isViewAttached) {
                        return
                    }
                    view.setActionsEnabled(true)

                    if (graphqlResponse?.getData<Any>(GqlCompleteTransactionResponse::class.java) != null) {

                        val gqlCompleteTransactionResponse = graphqlResponse.getData<GqlCompleteTransactionResponse>(GqlCompleteTransactionResponse::class.java)

                        if (gqlCompleteTransactionResponse != null && !gqlCompleteTransactionResponse.allDepositHistory!!.isHaveError) {
                            if (!isViewAttached || view.buyerHistoryAdapter == null) {
                                return
                            }

                            view.buyerHistoryAdapter.addElement(
                                    gqlCompleteTransactionResponse.allDepositHistory!!.depositHistoryList)

                            (view.buyerSaldoHistoryTabItem.fragment as BaseListFragment<*, *>).updateScrollListenerState(gqlCompleteTransactionResponse.allDepositHistory!!.isHaveNextPage)

                            if (view.buyerHistoryAdapter.itemCount == 0) {
                                view.buyerHistoryAdapter.addElement(view.defaultEmptyViewModel)
                            }
                        } else {
                            if (gqlCompleteTransactionResponse?.allDepositHistory != null) {
                                if (view.buyerHistoryAdapter.itemCount == 0) {
                                    view.showEmptyState(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                                } else {
                                    view.setRetry(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                                }
                            }
                        }
                    } else {
                        if (view.adapter != null && view.adapter.itemCount == 0) {
                            view.showEmptyState(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        } else {
                            view.setRetry(view.getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error))
                        }
                    }
                    finishLoading()
                }

            })

        } else {
            finishLoading()
        }
    }

    companion object {

        private val DEFAULT_PAGE = 1

        private val SEC_TO_DAY_CONVERSION = (24 * 60 * 60 * 1000).toLong()
        private val MAX_DAYS_DIFFERENCE: Long = 31
        private val SEARCH_DELAY: Long = 500
        private val DATE_FORMAT_VIEW = "dd MMM yyyy"
        val REQUEST_WITHDRAW_CODE = 1
        private val DATE_FORMAT_WS = "yyyy/MM/dd"
    }
}
