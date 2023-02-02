package com.tokopedia.privacycenter.ui.dsar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.data.GetRequestDetailResponse
import com.tokopedia.privacycenter.data.SearchRequestBody
import com.tokopedia.privacycenter.data.TransactionHistoryModel
import com.tokopedia.privacycenter.domain.SearchRequestUseCase
import com.tokopedia.privacycenter.domain.SubmitRequestUseCase
import com.tokopedia.privacycenter.ui.dsar.uimodel.CustomDateModel
import com.tokopedia.privacycenter.ui.dsar.uimodel.SubmitRequestUiModel
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
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
    val getProfileUseCase: GetProfileUseCase,
    val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _submitRequestState = MutableLiveData<SubmitRequestUiModel>()
    val submitRequestState: LiveData<SubmitRequestUiModel>
        get() = _submitRequestState

    private val _transactionHistoryModel = MutableLiveData(TransactionHistoryModel())
    val transactionHistoryModel: LiveData<TransactionHistoryModel> = _transactionHistoryModel

    val _filterItems = arrayListOf<String>()

    private val _showSummary = MutableLiveData<String>()
    val showSummary: LiveData<String> = _showSummary

    private val _mainLoader = SingleLiveEvent<Boolean>()
    val mainLoader: LiveData<Boolean> = _mainLoader

    private val _globalError = SingleLiveEvent<Boolean>()
    val globalError: LiveData<Boolean> = _globalError

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
        if (startDate != null && endDate != null) {
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
        addFilter(DsarConstants.FILTER_TYPE_TRANSACTION)
    }

    fun onTransactionHistoryDeselected() {
        _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(showBottomSheet = false, isChecked = false)
        removeFilter(DsarConstants.FILTER_TYPE_TRANSACTION)
    }

    fun addFilter(filter: String) {
        if ((_filterItems.count()) < DsarConstants.MAX_SELECTED_ITEM) {
            _filterItems.add(filter)
        }
    }

    fun backToFormPage() {
        _showSummary.value = ""
    }

    fun removeFilter(filter: String) {
        _filterItems.remove(filter)
    }

    fun formatRequest(): ArrayList<String> {
        val requests = arrayListOf<String>()
        _filterItems.forEach {
            if (it == DsarConstants.FILTER_TYPE_PERSONAL) {
                requests.addAll(DsarConstants.DSAR_PERSONAL_DATA)
            }
            if (it == DsarConstants.FILTER_TYPE_PAYMENT) {
                requests.addAll(DsarConstants.DSAR_PAYMENT_DATA)
            }
            if (it == DsarConstants.FILTER_TYPE_TRANSACTION) {
                requests.add(DsarUtils.formatTransactionDateToParam(getSelectedRangeItems()))
            }
        }
        return requests
    }

    fun submitRequest() {
        _mainLoader.value = true
        launch {
            try {
                val requests = formatRequest()
                val param = submitRequestUseCase.constructParams(requests)
                val result = submitRequestUseCase(param)
                _submitRequestState.value =
                    SubmitRequestUiModel(email = result.email, deadline = result.deadline)
            } catch (e: Exception) {
                _toasterError.value = DsarConstants.LABEL_ERROR_REQUEST
            } finally {
                _showMainLayout.value = true
                _mainLoader.value = false
            }
        }
    }

    fun fetchInitialData() {
        getProfileUseCase.execute(GetProfileSubscriber(userSession,
            { checkRequestStatus() },
            {
                _globalError.value = true
                _mainLoader.value = false
            },
            showLocationAdminPopUp = {},
            onLocationAdminRedirection = {},
            showErrorGetAdminType = {}
        ))
    }

    fun checkRequestStatus() {
        _mainLoader.value = true
        launch {
            try {
                val param = SearchRequestBody(email = userSession.email)
                val result = searchRequestUseCase(param)

                if (result.status.isNotEmpty()) {
                    if (result.status != DsarConstants.STATUS_REJECTED &&
                        result.status != DsarConstants.STATUS_COMPLETED &&
                        result.status != DsarConstants.STATUS_CLOSED
                    ) {
                        _requestDetails.value = result
                    } else {
                        _showMainLayout.value = true
                    }
                } else {
                    _showMainLayout.value = true
                }
            } catch (e: Exception) {
                _globalError.value = true
            } finally {
                _mainLoader.value = false
            }
        }
    }

    fun showSummary() {
        var text = ""
        if (_filterItems.isNotEmpty()) {
            _showSummary.value = _filterItems.joinToString { "," }
            _filterItems.forEachIndexed { index, s ->
                if (s == DsarConstants.FILTER_TYPE_PERSONAL) {
                    text += DsarConstants.PERSONAL_LABEL
                }
                if (s == DsarConstants.FILTER_TYPE_PAYMENT) {
                    text += DsarConstants.PAYMENT_LABEL
                }
                if (s == DsarConstants.FILTER_TYPE_TRANSACTION) {
                    val transData = getSelectedRangeItems()
                    transData?.run {
                        val startDate = DateUtil.formatDate(
                            DateUtil.YYYYMMDD,
                            DateUtil.DEFAULT_VIEW_FORMAT,
                            startDate
                        )
                        val endDate = DateUtil.formatDate(
                            DateUtil.YYYYMMDD,
                            DateUtil.DEFAULT_VIEW_FORMAT,
                            endDate
                        )
                        val transText = "$startDate - $endDate"
                        text += "${DsarConstants.TRANSACTION_LABEL}$transText"
                    }
                }
                text += if (index == (_filterItems.count()) - 1) {
                    DsarConstants.HTML_NEW_LINE
                } else {
                    // append 2 new line for last item
                    "${DsarConstants.HTML_NEW_LINE}${DsarConstants.HTML_NEW_LINE}"
                }
            }
            _showSummary.value = text
        } else {
            _toasterError.value = DsarConstants.SUMMARY_ERROR
        }
    }
}
