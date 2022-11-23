package com.tokopedia.privacycenter.dsar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.common.utils.getSimpleDateFormat
import com.tokopedia.privacycenter.common.utils.toHumanReadableFormat
import com.tokopedia.privacycenter.dsar.DsarConstants.DATE_RANGE_CUSTOM
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_PAYMENT
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_PERSONAL
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_TRANSACTION
import com.tokopedia.privacycenter.dsar.DsarConstants.HTML_NEW_LINE
import com.tokopedia.privacycenter.dsar.DsarConstants.ONE_TRUST_FORMAT_1
import com.tokopedia.privacycenter.dsar.DsarConstants.PAYMENT_LABEL
import com.tokopedia.privacycenter.dsar.DsarConstants.PERSONAL_LABEL
import com.tokopedia.privacycenter.dsar.DsarConstants.STATUS_CLOSED
import com.tokopedia.privacycenter.dsar.DsarConstants.STATUS_COMPLETED
import com.tokopedia.privacycenter.dsar.DsarConstants.STATUS_REJECTED
import com.tokopedia.privacycenter.dsar.DsarConstants.SUMMARY_ERROR
import com.tokopedia.privacycenter.dsar.DsarConstants.TRANSACTION_HISTORY_PREFIX
import com.tokopedia.privacycenter.dsar.DsarConstants.TRANSACTION_LABEL
import com.tokopedia.privacycenter.dsar.DsarUtils.getInitialRangeItem
import com.tokopedia.privacycenter.dsar.domain.SearchRequestUseCase
import com.tokopedia.privacycenter.dsar.domain.SubmitRequestUseCase
import com.tokopedia.privacycenter.dsar.model.CreateRequestResponse
import com.tokopedia.privacycenter.dsar.model.GetRequestDetailResponse
import com.tokopedia.privacycenter.dsar.model.ItemRangeModel
import com.tokopedia.privacycenter.dsar.model.SearchRequestBody
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DsarViewModel @Inject constructor(
    val submitRequestUseCase: SubmitRequestUseCase,
    val searchRequestUseCase: SearchRequestUseCase,
    val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val itemRangeData = MutableLiveData<ArrayList<ItemRangeModel>>()
    val _itemRangeData: LiveData<ArrayList<ItemRangeModel>> = itemRangeData

    private val filterItems = MutableLiveData<ArrayList<String>>(arrayListOf())
    val _filterItems: LiveData<ArrayList<String>> = filterItems

    private val startDate = MutableLiveData<String>()
    val _startDate: LiveData<String> = startDate

    private val showSummary = MutableLiveData<String>()
    val _showSummary: LiveData<String> = showSummary

    private val endDate = MutableLiveData<String>()
    val _endDate: LiveData<String> = endDate

    private val mainButtonLoading = SingleLiveEvent<Boolean>()
    val _mainButtonLoading: LiveData<Boolean> = mainButtonLoading

    private val mainLoader = SingleLiveEvent<Boolean>()
    val _mainLoader: LiveData<Boolean> = mainLoader

    private val toasterError = SingleLiveEvent<String>()
    val _toasterError: LiveData<String> = toasterError

    private val submitRequest = MutableLiveData<CreateRequestResponse>()
    val _submitRequest: LiveData<CreateRequestResponse> = submitRequest

    private val requestDetails = MutableLiveData<GetRequestDetailResponse>()
    val _requestDetails: LiveData<GetRequestDetailResponse> = requestDetails

    private val showMainLayout = SingleLiveEvent<Boolean>()
    val _showMainLayout: LiveData<Boolean> = showMainLayout

    fun getSelectedRangeItems(): ItemRangeModel? {
        return itemRangeData.value?.first { it.selected }
    }

    fun setSelectedRangeItems(id: Int) {
        itemRangeData.value?.forEach {
            it.selected = it.id == id
            if(it.id == DATE_RANGE_CUSTOM) {
                it.transactionDate = calculateTransactionDateForCustomDate(id)
            }
        }
        itemRangeData.value = itemRangeData.value
    }

    fun populateRangeItems() {
        itemRangeData.value?.clear()
        itemRangeData.value = getInitialRangeItem()
    }

    fun addFilter(filter: String) {
        if((filterItems.value?.count() ?: 0) < 3) {
            val newVal = filterItems.value?.apply {
                add(filter)
            }
            filterItems.value = newVal
        }
    }

    fun removeFilter(filter: String) {
        val newVal = filterItems.value?.apply {
            remove(filter)
        }
        filterItems.value = newVal
    }

    fun submitRequest() {
        mainButtonLoading.value = true
        launch {
            try {
                val requests = arrayListOf<String>()
                filterItems.value?.forEach {
                    if(it == FILTER_TYPE_PERSONAL) {
                        requests.add("full_name")
                        requests.add("mailing_address")
                        requests.add("phone_number")
                        requests.add("email")
                        requests.add("dob")
                        requests.add("gender")
                    }
                    if(it == FILTER_TYPE_PAYMENT) {
                        requests.add("bank_account")
                        requests.add("payment")
                    }
                    if(it == FILTER_TYPE_TRANSACTION) {
                        requests.add(getSelectedRangeItems()?.transactionDate ?: "")
                    }
                }
                val param = submitRequestUseCase.constructParams(requests)
                val result = submitRequestUseCase(param)
                if(result.email.isNotEmpty() && result.deadline.isNotEmpty()) {
                    submitRequest.value = result
                } else {
                    toasterError.value = "Terjadi Kesalahan"
                }
            } catch (e: Exception) {
                toasterError.value = e.message
            } finally {
                mainButtonLoading.value = false
            }
        }
    }

    fun calculateTransactionDateForCustomDate(selectedId: Int): String {
        val maxDate = GregorianCalendar(Locale.getDefault())
        val minDate = GregorianCalendar(Locale.getDefault())
        if(selectedId == DATE_RANGE_CUSTOM) {
            endDate.value?.let {
                maxDate.time = it.toHumanReadableFormat()
            }
            startDate.value?.let {
                minDate.time = it.toHumanReadableFormat()
            }
        }
        val formattedMaxDate = getSimpleDateFormat(ONE_TRUST_FORMAT_1).format(maxDate.time)
        val formattedMinDate = getSimpleDateFormat(ONE_TRUST_FORMAT_1).format(minDate.time)
        return "${TRANSACTION_HISTORY_PREFIX}_${formattedMinDate}_${formattedMaxDate}"
    }

    fun setStartDate(date: Date) {
        startDate.value = date.toHumanReadableFormat()
    }

    fun setEndDate(date: Date) {
        endDate.value = date.toHumanReadableFormat()
        setSelectedRangeItems(DATE_RANGE_CUSTOM)
    }

    fun checkRequestStatus() {
        mainLoader.value = true
        launch {
            try {
                val param = SearchRequestBody(email = userSession.email)
                val result = searchRequestUseCase(param)
                if(result.status.isEmpty()) {
                    showMainLayout.value = true
                } else if(result.status != STATUS_REJECTED && result.status != STATUS_COMPLETED && result.status != STATUS_CLOSED) {
                    requestDetails.value = result
                }
            } catch (e: Exception) {
                showMainLayout.value = true
                toasterError.value = e.message
            } finally {
                mainLoader.value = false
            }
        }
    }

    fun showSummary() {
        var text = ""
        if(filterItems.value?.isNotEmpty() == true) {
            showSummary.value = filterItems.value?.joinToString { "," }
            filterItems.value?.forEachIndexed { index, s ->
                if (s == FILTER_TYPE_PERSONAL) {
                    text += PERSONAL_LABEL
                }
                if (s == FILTER_TYPE_PAYMENT) {
                    text += PAYMENT_LABEL
                }
                if (s == FILTER_TYPE_TRANSACTION) {
                    val transData = getSelectedRangeItems()
                    var transText = transData?.title
                    if (transData?.id == DATE_RANGE_CUSTOM) {
                        transText = "${startDate.value} - ${endDate.value}"
                    }
                    text += "$TRANSACTION_LABEL${transText}"
                }
                text += if (index == (filterItems.value?.count() ?: 0) - 1) {
                    HTML_NEW_LINE
                } else {
                    // append 2 new line for last item
                    "$HTML_NEW_LINE$HTML_NEW_LINE"
                }
            }
            showSummary.value = text
        } else {
            toasterError.value = SUMMARY_ERROR
        }
    }
}
