package com.tokopedia.privacycenter.dsar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.dsar.DsarConstants
import com.tokopedia.privacycenter.dsar.DsarConstants.DATE_RANGE_CUSTOM
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_PAYMENT
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_PERSONAL
import com.tokopedia.privacycenter.dsar.DsarConstants.FILTER_TYPE_TRANSACTION
import com.tokopedia.privacycenter.dsar.DsarConstants.HTML_NEW_LINE
import com.tokopedia.privacycenter.dsar.DsarConstants.MAX_SELECTED_ITEM
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
import com.tokopedia.privacycenter.dsar.model.uimodel.CustomDateModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
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

    private val _itemRangeData = MutableLiveData<ArrayList<ItemRangeModel>>()
    val itemRangeData: LiveData<ArrayList<ItemRangeModel>> = _itemRangeData

    private val _filterItems = arrayListOf<String>()

    private val _customDate = MutableLiveData(CustomDateModel())
    val customDate: LiveData<CustomDateModel> = _customDate

    private val _showSummary = MutableLiveData<String>()
    val showSummary: LiveData<String> = _showSummary

    private val _mainButtonLoading = SingleLiveEvent<Boolean>()
    val mainButtonLoading: LiveData<Boolean> = _mainButtonLoading

    private val _mainLoader = SingleLiveEvent<Boolean>()
    val mainLoader: LiveData<Boolean> = _mainLoader

    private val _toasterError = SingleLiveEvent<String>()
    val toasterError: LiveData<String> = _toasterError

    private val _submitRequest = MutableLiveData<CreateRequestResponse>()
    val submitRequest: LiveData<CreateRequestResponse> = _submitRequest

    private val _requestDetails = MutableLiveData<GetRequestDetailResponse>()
    val requestDetails: LiveData<GetRequestDetailResponse> = _requestDetails

    private val _showMainLayout = SingleLiveEvent<Boolean>()
    val showMainLayout: LiveData<Boolean> = _showMainLayout

    fun getSelectedRangeItems(): ItemRangeModel? {
        return this.itemRangeData.value?.first { it.selected }
    }

    fun setSelectedRangeItems(id: Int) {
        this.itemRangeData.value?.forEach {
            it.selected = it.id == id
            if(it.id == DATE_RANGE_CUSTOM) {
                it.transactionDate = calculateTransactionDateForCustomDate(id)
            }
        }
        _itemRangeData.value = this.itemRangeData.value
    }

    fun populateRangeItems() {
        _itemRangeData.value?.clear()
        _itemRangeData.value = getInitialRangeItem()
    }

    fun addFilter(filter: String) {
        if((_filterItems.count()) < MAX_SELECTED_ITEM) {
            _filterItems.add(filter)
        }
    }

    fun removeFilter(filter: String) {
        _filterItems.remove(filter)
    }

    fun submitRequest() {
        _mainButtonLoading.value = true
        launch {
            try {
                val requests = arrayListOf<String>()
                _filterItems.forEach {
                    if(it == FILTER_TYPE_PERSONAL) {
                        requests.addAll(DsarConstants.DSAR_PERSONAL_DATA)
                    }
                    if(it == FILTER_TYPE_PAYMENT) {
                        requests.addAll(DsarConstants.DSAR_PAYMENT_DATA)
                    }
                    if(it == FILTER_TYPE_TRANSACTION) {
                        requests.add(getSelectedRangeItems()?.transactionDate ?: "")
                    }
                }
                val param = submitRequestUseCase.constructParams(requests)
                val result = submitRequestUseCase(param)
                _submitRequest.value = result
            } catch (e: Exception) {
                _toasterError.value = e.message
            } finally {
                _mainButtonLoading.value = false
            }
        }
    }

    fun calculateTransactionDateForCustomDate(selectedId: Int): String {
        val maxDate = GregorianCalendar(Locale.getDefault())
        val minDate = GregorianCalendar(Locale.getDefault())
        if(selectedId == DATE_RANGE_CUSTOM) {
            maxDate.time = _customDate.value?.endDate.toDate(DateUtil.DEFAULT_VIEW_FORMAT)
            minDate.time = _customDate.value?.startDate.toDate(DateUtil.DEFAULT_VIEW_FORMAT)
        }

        val formattedMaxDate = maxDate.time.toString(DateUtil.YYYYMMDD)
        val formattedMinDate = minDate.time.toString(DateUtil.YYYYMMDD)
        return "${TRANSACTION_HISTORY_PREFIX}_${formattedMinDate}_${formattedMaxDate}"
    }

    fun setStartDate(date: Date) {
        _customDate.value = _customDate.value?.copy(
            startDate = date.toString(DateUtil.DEFAULT_VIEW_FORMAT)
        )
    }

    fun setEndDate(date: Date) {
        _customDate.value = _customDate.value?.copy(
            endDate = date.toString(DateUtil.DEFAULT_VIEW_FORMAT)
        )
        setSelectedRangeItems(DATE_RANGE_CUSTOM)
    }

    fun checkRequestStatus() {
        _mainLoader.value = true
        launch {
            try {
                val param = SearchRequestBody(email = userSession.email)
                val result = searchRequestUseCase(param)
                if(result.status.isEmpty()) {
                    _showMainLayout.value = true
                } else if(result.status != STATUS_REJECTED && result.status != STATUS_COMPLETED && result.status != STATUS_CLOSED) {
                    _requestDetails.value = result
                }
            } catch (e: Exception) {
                _showMainLayout.value = true
                _toasterError.value = e.message
            } finally {
                _mainLoader.value = false
            }
        }
    }

    fun showSummary() {
        var text = ""
        if(_filterItems.isNotEmpty()) {
            _showSummary.value = _filterItems.joinToString { "," }
            _filterItems.forEachIndexed { index, s ->
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
                        transText = "${_customDate.value?.startDate} - ${_customDate.value?.endDate}"
                    }
                    text += "$TRANSACTION_LABEL${transText}"
                }
                text += if (index == (_filterItems.count()) - 1) {
                    HTML_NEW_LINE
                } else {
                    // append 2 new line for last item
                    "$HTML_NEW_LINE$HTML_NEW_LINE"
                }
            }
            _showSummary.value = text
        } else {
            _toasterError.value = SUMMARY_ERROR
        }
    }
}
