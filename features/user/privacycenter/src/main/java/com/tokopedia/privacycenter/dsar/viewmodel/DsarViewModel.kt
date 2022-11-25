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
import com.tokopedia.privacycenter.dsar.DsarConstants.TRANSACTION_LABEL
import com.tokopedia.privacycenter.dsar.DsarUtils
import com.tokopedia.privacycenter.dsar.DsarUtils.getInitialRangeItem
import com.tokopedia.privacycenter.dsar.domain.SearchRequestUseCase
import com.tokopedia.privacycenter.dsar.domain.SubmitRequestUseCase
import com.tokopedia.privacycenter.dsar.model.GetRequestDetailResponse
import com.tokopedia.privacycenter.dsar.model.ItemRangeModel
import com.tokopedia.privacycenter.dsar.model.SearchRequestBody
import com.tokopedia.privacycenter.dsar.model.TransactionHistoryModel
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

    private val _itemRangeData = MutableLiveData<ArrayList<ItemRangeModel>>()
    val itemRangeData: LiveData<ArrayList<ItemRangeModel>> = _itemRangeData

    private val _submitRequestState = MutableLiveData<SubmitRequestUiModel>()
    val submitRequestState: LiveData<SubmitRequestUiModel>
        get() = _submitRequestState

    private val _transactionHistoryModel = MutableLiveData(TransactionHistoryModel())
    val transactionHistoryModel: LiveData<TransactionHistoryModel> = _transactionHistoryModel

    private val _filterItems = arrayListOf<String>()

    private val _showSummary = MutableLiveData<String>()
    val showSummary: LiveData<String> = _showSummary

    private val _mainButtonLoading = SingleLiveEvent<Boolean>()
    val mainButtonLoading: LiveData<Boolean> = _mainButtonLoading

    private val _mainLoader = SingleLiveEvent<Boolean>()
    val mainLoader: LiveData<Boolean> = _mainLoader

    private val _toasterError = SingleLiveEvent<String>()
    val toasterError: LiveData<String> = _toasterError

    private val _requestDetails = MutableLiveData<GetRequestDetailResponse>()
    val requestDetails: LiveData<GetRequestDetailResponse> = _requestDetails

    private val _showMainLayout = SingleLiveEvent<Boolean>()
    val showMainLayout: LiveData<Boolean> = _showMainLayout

    fun getSelectedRangeItems(): ItemRangeModel? {
        return transactionHistoryModel.value?.itemRange?.first { it.selected }
    }

    fun setSelectedRangeItems(id: Int) {
        transactionHistoryModel.value?.itemRange?.forEach {
            it.selected = it.id == id
            it.transactionDate = DsarUtils.getDateFromSelectedId(id)
        }
        _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(itemRange = _transactionHistoryModel.value?.itemRange ?: arrayListOf())
    }

    fun setSelectedCustomDate(startDate: Date? = null, endDate: Date? = null) {
        transactionHistoryModel.value?.itemRange?.forEach {
            it.selected = it.id == DATE_RANGE_CUSTOM
            if(startDate != null) {
                it.transactionDate.startDate = startDate.toString(DateUtil.YYYYMMDD)
            }
            if(endDate != null) {
                it.transactionDate.endDate = endDate.toString(DateUtil.YYYYMMDD)
            }
        }
        _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(itemRange = _transactionHistoryModel.value?.itemRange ?: arrayListOf())
    }

    fun onTransactionHistorySelected() {
        val items = if(_transactionHistoryModel.value?.itemRange?.isEmpty() == true) getInitialRangeItem() else _transactionHistoryModel.value?.itemRange
        _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(showBottomSheet = true, isChecked = true, itemRange = items ?: arrayListOf())
        addFilter(FILTER_TYPE_TRANSACTION)
    }

    fun addFilter(filter: String) {
        if((_filterItems.count()) < MAX_SELECTED_ITEM) {
            _filterItems.add(filter)
        }
    }

    fun removeFilter(filter: String) {
        if(filter == FILTER_TYPE_TRANSACTION) {
            _transactionHistoryModel.value = _transactionHistoryModel.value?.copy(itemRange = getInitialRangeItem(), isChecked = false, showBottomSheet = false)
        }
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
                        requests.add(DsarUtils.formatTransactionDateToParam(getSelectedRangeItems()?.transactionDate))
                    }
                }
                val param = submitRequestUseCase.constructParams(requests)
                val result = submitRequestUseCase(param)
                _submitRequestState.value = SubmitRequestUiModel(email = result.email, deadline = result.deadline)
            } catch (e: Exception) {
                _toasterError.value = e.message
            } finally {
                _mainButtonLoading.value = false
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
                _showMainLayout.value = true
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
                        val startDate = DateUtil.formatDate(DateUtil.YYYYMMDD, DateUtil.DEFAULT_VIEW_FORMAT, transData.transactionDate.startDate)
                        val endDate = DateUtil.formatDate(DateUtil.YYYYMMDD, DateUtil.DEFAULT_VIEW_FORMAT, transData.transactionDate.endDate)
                        transText = "$startDate - $endDate"
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
