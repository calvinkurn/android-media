package com.tokopedia.saldodetails.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.saldodetails.utils.SaldoDatePickerUtil
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.contract.SaldoHistoryContract
import com.tokopedia.saldodetails.response.model.DepositActivityResponse
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.response.model.SummaryDepositParam
import com.tokopedia.saldodetails.usecase.GetAllTransactionUsecase
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase
import com.tokopedia.saldodetails.utils.*
import kotlinx.coroutines.Dispatchers
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SaldoHistoryViewModel @Inject constructor(val getDepositSummaryUseCase: GetDepositSummaryUseCase, val getAllTransactionUsecase: GetAllTransactionUsecase) : BaseViewModel(Dispatchers.Main), SaldoHistoryContract.Presenter {

    val buyerResponseLiveData = MutableLiveData<Resources<DepositActivityResponse>>()
    val sellerResponseLiveData = MutableLiveData<Resources<DepositActivityResponse>>()
    val allDepositResponseLiveData = MutableLiveData<Resources<DepositActivityResponse>>()

    val errors = MutableLiveData<Errors>()

    private val paging = PagingHandler()
    private var paramStartDate: String? = null
    private var paramEndDate: String? = null

    private val summaryDepositParam: Map<String, Any>
        get() {
            val param = SummaryDepositParam()
            val sdf = SimpleDateFormat(DATE_FORMAT_VIEW)
            val sdf_ws = SimpleDateFormat(DATE_FORMAT_WS, Locale.US)
            try {
                val formattedStart = sdf.parse(paramStartDate)
                val formattedEnd = sdf.parse(paramEndDate)
                param.startDate = getDateParam(sdf_ws.format(formattedStart))
                param.endDate = getDateParam(sdf_ws.format(formattedEnd))
            } catch (e: ParseException) {
                errors.value = ErrorType(com.tokopedia.saldodetails.R.string.sp_error_invalid_date)
            }

            param.page = paging.page
            return param.paramSummaryDeposit
        }

    private val isValid: Boolean
        get() {
            var isValid = true

            val sdf = SimpleDateFormat(DATE_FORMAT_VIEW)
            try {
                val endDate = sdf.parse(paramEndDate)
                val startDate = sdf.parse(paramStartDate)
                if (endDate.time - startDate.time < 0) {
                    isValid = false
                    errors.value = ErrorType(com.tokopedia.saldodetails.R.string.sp_error_invalid_date, IN_VALID_DATE_ERROR)
                }

                if ((endDate.time - startDate.time) / SEC_TO_DAY_CONVERSION >= MAX_DAYS_DIFFERENCE) {
                    isValid = false
                    errors.value = ErrorType(com.tokopedia.saldodetails.R.string.sp_title_max_day, IN_VALID_DATE_ERROR)
                }
            } catch (e: ParseException) {
                isValid = false
                errors.value = ErrorType(com.tokopedia.saldodetails.R.string.sp_error_invalid_date, IN_VALID_DATE_ERROR)
            }

            return isValid
        }


    override fun setFirstDateParameter(view: SaldoHistoryContract.View) {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_VIEW)
        val date = Date()
        val cal = GregorianCalendar()
        cal.time = date
        cal.add(Calendar.DAY_OF_MONTH, -1)
        val startDate = dateFormat.format(cal.time)
        val endDate = dateFormat.format(date)
        view.setStartDate(startDate)
        view.setEndDate(endDate)
        paramStartDate = startDate
        paramEndDate = endDate
    }


    override fun onSearchClicked(startDate: String, endDate: String) {
        setDates(startDate, endDate)
        getSummaryDeposit()
    }

    fun setDates(startDate: String, endDate: String) {
        paramEndDate = endDate
        paramStartDate = startDate
    }

    private fun setData(data: GqlAllDepositSummaryResponse) {
        allDepositResponseLiveData.value = Success(data.allDepositHistory!!)
        buyerResponseLiveData.value = Success(data.buyerDepositHistory!!)
        sellerResponseLiveData.value = Success(data.sellerDepositHistory!!)
    }

    private fun setData(data: GqlCompleteTransactionResponse) {
        allDepositResponseLiveData.value = AddElements(data.allDepositHistory!!)
    }

    private fun showLoading() {
        allDepositResponseLiveData.value = Loading()
        sellerResponseLiveData.value = Loading()
        buyerResponseLiveData.value = Loading()
    }


    override fun onEndDateClicked(datePicker: SaldoDatePickerUtil, view: SaldoHistoryContract.View) {
        val date = dateFormatter(view.getEndDate())
        date?.let {
            datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date))
            datePicker.datePickerCalendar(object : SaldoDatePickerUtil.OnDateSelectedListener {
                override fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
                    val selectedDate = this@SaldoHistoryViewModel.getDate(year, month, dayOfMonth)
                    view.setEndDate(selectedDate)
                    val startDate = view.getStartDate()
                    android.os.Handler().postDelayed({ this@SaldoHistoryViewModel.onSearchClicked(startDate, selectedDate) }, SEARCH_DELAY)
                }
            })
        }
    }

    override fun onStartDateClicked(datePicker: SaldoDatePickerUtil, view: SaldoHistoryContract.View) {
        val date = dateFormatter(view.getStartDate())
        date?.let {
            datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date))
            datePicker.datePickerCalendar(object : SaldoDatePickerUtil.OnDateSelectedListener {
                override fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) {
                    val selectedDate = getDate(year, month, dayOfMonth)
                    view.setStartDate(selectedDate)
                    val endDate = view.getEndDate()
                    android.os.Handler().postDelayed({ this@SaldoHistoryViewModel.onSearchClicked(selectedDate, endDate) }, SEARCH_DELAY)
                }
            })
        }
    }

    private fun getDate(year: Int, month: Int, day: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_VIEW)
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

    private fun dateFormatter(date: String): String? {

        val sdf = SimpleDateFormat(DATE_FORMAT_VIEW)
        val sdf_ws = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        var formattedStart: Date? = null
        try {
            formattedStart = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

        return sdf_ws.format(formattedStart)

    }

    override fun getSummaryDeposit() {
        launchCatchError(block = {
            if (isValid) {
                showLoading()
                onDepositSummaryFetched(getDepositSummaryUseCase.execute(summaryDepositParam))
            }
        }) {
            Log.e(TAG, it.toString())
            this@SaldoHistoryViewModel.onError(it)
        }

    }

    private fun onError(e: Throwable) {
        if (e is UnknownHostException || e is SocketTimeoutException) {
            allDepositResponseLiveData.value = ErrorMessage(null)
            buyerResponseLiveData.value = ErrorMessage(null)
            sellerResponseLiveData.value = ErrorMessage(null)
        } else {
            allDepositResponseLiveData.value = ErrorMessage(R.string.sp_empty_state_error)
            buyerResponseLiveData.value = ErrorMessage(R.string.sp_empty_state_error)
            sellerResponseLiveData.value = ErrorMessage(R.string.sp_empty_state_error)
        }
    }

    private fun onDepositSummaryFetched(gqlDepositSummaryResponse: GqlAllDepositSummaryResponse) {
        if (!gqlDepositSummaryResponse.allDepositHistory!!.isHaveError) {
            setData(gqlDepositSummaryResponse)
        } else {
            if (gqlDepositSummaryResponse.allDepositHistory != null) {
                allDepositResponseLiveData.value = ErrorMessage(gqlDepositSummaryResponse.allDepositHistory!!.message!!)

            }

            if (gqlDepositSummaryResponse.buyerDepositHistory != null) {
                buyerResponseLiveData.value = ErrorMessage(gqlDepositSummaryResponse.buyerDepositHistory!!.message!!)
            }

            if (gqlDepositSummaryResponse.sellerDepositHistory != null) {
                sellerResponseLiveData.value = ErrorMessage(gqlDepositSummaryResponse.sellerDepositHistory!!.message!!)
            }
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
            errors.value = ErrorType(R.string.sp_error_invalid_date)
        }

        param.page = page
        param.setSaldoType(saldoType)
        return param.paramSummaryDeposit

    }

    fun loadMoreAllTransaction(lastItemPosition: Int) {
        launchCatchError(block = {
            if (isValid) {
                allDepositResponseLiveData.value = Loading()
                onAllTransactionsFetched(getAllTransactionUsecase.execute(getSummaryDepositParam(lastItemPosition, ALL_SALDO)))
            }

        }) {
            Log.e(TAG, it.toString())
            if (it is UnknownHostException || it is SocketTimeoutException) {
                allDepositResponseLiveData.value = ErrorMessage(null)
            } else {
                allDepositResponseLiveData.value = ErrorMessage(R.string.sp_empty_state_error)
            }
        }

    }

    private fun onAllTransactionsFetched(gqlCompleteTransactionResponse: GqlCompleteTransactionResponse) {
        if (!gqlCompleteTransactionResponse.allDepositHistory!!.isHaveError) {
            setData(gqlCompleteTransactionResponse)
        } else {
            if (gqlCompleteTransactionResponse.allDepositHistory != null) {
                allDepositResponseLiveData.value = ErrorMessage(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
            }
        }
    }

    fun loadMoreSellerTransaction(lastItemPosition: Int) {
        launchCatchError(block = {
            if (isValid) {
                sellerResponseLiveData.value = Loading()
                val gqlCompleteTransactionResponse = getAllTransactionUsecase.execute(getSummaryDepositParam(lastItemPosition, SELLER_SALDO))

                if (!gqlCompleteTransactionResponse.allDepositHistory!!.isHaveError) {
                    sellerResponseLiveData.value = AddElements(gqlCompleteTransactionResponse.allDepositHistory!!)

                } else {
                    if (gqlCompleteTransactionResponse.allDepositHistory != null) {
                        sellerResponseLiveData.value = ErrorMessage(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                    }
                }
            }

        }) {
            Log.e(TAG, it.toString())
            if (it is UnknownHostException || it is SocketTimeoutException) {
                sellerResponseLiveData.value = ErrorMessage(null)
            } else {
                sellerResponseLiveData.value = ErrorMessage(R.string.sp_empty_state_error)
            }
        }
    }

    fun loadMoreBuyerTransaction(lastItemPosition: Int) {
        launchCatchError(block = {
            if (isValid) {
                buyerResponseLiveData.value = Loading()
                val gqlCompleteTransactionResponse = getAllTransactionUsecase.execute(getSummaryDepositParam(lastItemPosition, BUYER_SALDO))

                if (!gqlCompleteTransactionResponse.allDepositHistory!!.isHaveError) {
                    buyerResponseLiveData.value = AddElements(gqlCompleteTransactionResponse.allDepositHistory!!)

                } else {
                    if (gqlCompleteTransactionResponse.allDepositHistory != null) {
                        buyerResponseLiveData.value = ErrorMessage(gqlCompleteTransactionResponse.allDepositHistory!!.message!!)
                    }
                }
            }

        }) {
            Log.e(TAG, it.toString())
            if (it is UnknownHostException || it is SocketTimeoutException) {
                buyerResponseLiveData.value = ErrorMessage(null)
            } else {
                buyerResponseLiveData.value = ErrorMessage(R.string.sp_empty_state_error)
            }
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
        private val SELLER_SALDO = 1
        private val BUYER_SALDO = 0
        private val ALL_SALDO = 2
    }
}
