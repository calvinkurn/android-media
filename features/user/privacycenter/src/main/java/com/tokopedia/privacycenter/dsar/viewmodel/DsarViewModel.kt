package com.tokopedia.privacycenter.dsar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.dsar.DsarConstants
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
import com.tokopedia.privacycenter.dsar.DsarConstants.TRANSACTION_LABEL
import com.tokopedia.privacycenter.dsar.DsarUtils
import com.tokopedia.privacycenter.dsar.domain.SearchRequestUseCase
import com.tokopedia.privacycenter.dsar.domain.SubmitRequestUseCase
import com.tokopedia.privacycenter.dsar.model.GetRequestDetailResponse
import com.tokopedia.privacycenter.dsar.model.SearchRequestBody
import com.tokopedia.privacycenter.dsar.model.TransactionHistoryModel
import com.tokopedia.privacycenter.dsar.model.uimodel.CustomDateModel
import com.tokopedia.privacycenter.dsar.model.uimodel.GlobalErrorCustomUiModel
import com.tokopedia.privacycenter.dsar.model.uimodel.SubmitRequestUiModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
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

    private val _submitRequestState = MutableLiveData<SubmitRequestUiModel>()
    val submitRequestState: LiveData<SubmitRequestUiModel>
        get() = _submitRequestState

    private val _transactionHistoryModel = MutableLiveData(TransactionHistoryModel())
    val transactionHistoryModel: LiveData<TransactionHistoryModel> = _transactionHistoryModel

    private val _filterItems = arrayListOf<String>()

    private val _showSummary = MutableLiveData<String>()
    val showSummary: LiveData<String> = _showSummary

    private val _mainLoader = SingleLiveEvent<Boolean>()
    val mainLoader: LiveData<Boolean> = _mainLoader

    private val _globalError = SingleLiveEvent<GlobalErrorCustomUiModel>()
    val globalError: LiveData<GlobalErrorCustomUiModel> = _globalError

    private val _toasterError = SingleLiveEvent<String>()
    val toasterError: LiveData<String> = _toasterError

    private val _requestDetails = MutableLiveData<GetRequestDetailResponse>()
    val requestDetails: LiveData<GetRequestDetailResponse> = _requestDetails

    private val _showMainLayout = SingleLiveEvent<Boolean>()
    val showMainLayout: LiveData<Boolean> = _showMainLayout

    fun getSelectedRangeItems(): CustomDateModel? {
        return transactionHistoryModel.value?.selectedDate
    }

    fun setSelectedDate(selectedItem: String, startDate: Date? = null, endDate: Date? = null) {
        if(startDate != null && endDate != null) {
            _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(
                selectedDate = CustomDateModel(
                    startDate = startDate.toString(DateUtil.YYYYMMDD),
                    endDate = endDate.toString(DateUtil.YYYYMMDD)
                )
            )
        } else {
            _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(
                selectedDate = DsarUtils.getDateFromSelectedId(selectedItem)
            )
        }
    }

    fun onTransactionHistorySelected() {
        _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(showBottomSheet = true, isChecked = true)
        addFilter(FILTER_TYPE_TRANSACTION)
    }

    fun onTransactionHistoryDeselected() {
        _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(showBottomSheet = false, isChecked = false)
        removeFilter(FILTER_TYPE_TRANSACTION)
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
        _mainLoader.value = true
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
                        requests.add(DsarUtils.formatTransactionDateToParam(getSelectedRangeItems()))
                    }
                }
                val param = submitRequestUseCase.constructParams(requests)
                val result = submitRequestUseCase(param)
                _submitRequestState.value = SubmitRequestUiModel(email = result.email, deadline = result.deadline)
            } catch (e: Exception) {
                _globalError.value = GlobalErrorCustomUiModel(true) {
                    submitRequest()
                }
            } finally {
                _mainLoader.value = false
            }
        }
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
                _globalError.value = GlobalErrorCustomUiModel(true) {
                    checkRequestStatus()
                }
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
                    transData?.run {
                        val startDate = DateUtil.formatDate(DateUtil.YYYYMMDD, DateUtil.DEFAULT_VIEW_FORMAT, startDate)
                        val endDate = DateUtil.formatDate(DateUtil.YYYYMMDD, DateUtil.DEFAULT_VIEW_FORMAT, endDate)
                        val transText = "$startDate - $endDate"
                        text += "$TRANSACTION_LABEL${transText}"
                    }
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
