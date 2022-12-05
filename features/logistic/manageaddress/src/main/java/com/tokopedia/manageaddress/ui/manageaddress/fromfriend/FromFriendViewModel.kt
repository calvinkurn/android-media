package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.manageaddress.domain.mapper.SharedAddressMapper
import com.tokopedia.manageaddress.domain.request.shareaddress.SenderShareAddressParam
import com.tokopedia.manageaddress.domain.usecase.shareaddress.DeleteFromFriendAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.GetSharedAddressListUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SaveFromFriendAddressUseCase
import com.tokopedia.manageaddress.ui.uimodel.FromFriendAddressActionState
import com.tokopedia.manageaddress.ui.uimodel.FromFriendAddressListState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class FromFriendViewModel @Inject constructor(
    private val getSharedAddressListUseCase: GetSharedAddressListUseCase,
    private val sharedAddressMapper: SharedAddressMapper,
    private val saveAddressUseCase: SaveFromFriendAddressUseCase,
    private val deleteAddressUseCase: DeleteFromFriendAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _getFromFriendAddressState =
        MutableLiveData<FromFriendAddressListState>()
    val getFromFriendAddressState: LiveData<FromFriendAddressListState>
        get() = _getFromFriendAddressState

    private val _saveAddressState =
        MutableLiveData<FromFriendAddressActionState>()
    val saveAddressState: LiveData<FromFriendAddressActionState>
        get() = _saveAddressState

    private val _deleteAddressState =
        MutableLiveData<FromFriendAddressActionState>()
    val deleteAddressState: LiveData<FromFriendAddressActionState>
        get() = _deleteAddressState

    private var isCancelDelete = false
    val addressList = mutableListOf<RecipientAddressModel>()
    private val temporaryList = arrayListOf<RecipientAddressModel>()

    val selectedAddressList: List<RecipientAddressModel>
        get() = addressList.filter { it.isSelected }

    private val unSelectAddressList: List<RecipientAddressModel>
        get() = addressList.filter { it.isSelected.not() }

    private val senderUserIds: List<String>
        get() = selectedAddressList.map { it.id }

    val isHaveAddressList: Boolean
        get() = addressList.isNotEmpty()
    val isAllSelected: Boolean
        get() = selectedAddressList.size == addressList.size
    var isNeedUpdateAllList = true
    var isShareAddressFromNotif = false
    var source = ""

    fun getFromFriendAddressList() {
        launchCatchError(block = {
            showGetShareAddressLoading(true)
            val response = getSharedAddressListUseCase(source)
            val result = sharedAddressMapper.call(response)
            updateAddressList(result.listAddress)
            showGetShareAddressLoading(false)
            _getFromFriendAddressState.value =
                FromFriendAddressListState.Success(response.keroGetSharedAddressList)
        }, onError = {
            _getFromFriendAddressState.value =
                FromFriendAddressListState.Fail(it, it.message.orEmpty())
            showGetShareAddressLoading(false)
        })
    }

    private fun updateAddressList(resultList: List<RecipientAddressModel>) {
        addressList.clear()
        addressList.addAll(resultList)
    }

    private fun showGetShareAddressLoading(isShowLoading: Boolean) {
        _getFromFriendAddressState.value = FromFriendAddressListState.Loading(isShowLoading)
    }

    fun saveAddress() {
        launchCatchError(block = {
            showSaveAddressLoading(true)
            val param = SenderShareAddressParam(
                SenderShareAddressParam.SenderShareAddressData(
                    senderUserIds = senderUserIds,
                    source = source
                )
            )
            val result = saveAddressUseCase(param)
            _saveAddressState.value = if (result.isSuccess) {
                FromFriendAddressActionState.Success(result.data?.message.orEmpty())
            } else {
                FromFriendAddressActionState.Fail(result.errorMessage)
            }
            showSaveAddressLoading(false)
        }, onError = {
            _saveAddressState.value = FromFriendAddressActionState.Fail(it.message.orEmpty())
            showSaveAddressLoading(false)
        })
    }

    private fun showSaveAddressLoading(isShowLoading: Boolean) {
        _saveAddressState.value = FromFriendAddressActionState.Loading(isShowLoading)
    }

    fun deleteAddress() = launch {
        isCancelDelete = false
        val param = SenderShareAddressParam(
            SenderShareAddressParam.SenderShareAddressData(
                senderUserIds = senderUserIds,
                source = source
            )
        )
        temporaryList.clear()
        temporaryList.addAll(addressList.toTypedArray())
        updateAddressList(unSelectAddressList.toTypedArray())
        onDeletingAddress(true)
        delay(TOAST_SHOWING_TIME)

        if (isCancelDelete) {
            return@launch
        }

        launchCatchError(block = {
            val result = deleteAddressUseCase(param)
            _deleteAddressState.value = if (result.isSuccess) {
                temporaryList.clear()
                FromFriendAddressActionState.Success(result.data?.message.orEmpty())
            } else {
                updateAddressList(temporaryList)
                FromFriendAddressActionState.Fail(result.errorMessage)
            }
            onDeletingAddress(false)
        }, onError = {
            updateAddressList(temporaryList)
            _deleteAddressState.value =
                FromFriendAddressActionState.Fail(it.message.orEmpty())
            onDeletingAddress(false)
        })
    }

    fun onCancelDeleteAddress() {
        isCancelDelete = true
        updateAddressList(temporaryList)
        onDeletingAddress(false)
    }

    private fun updateAddressList(currentList: Array<RecipientAddressModel>) {
        addressList.clear()
        addressList.addAll(currentList)
    }

    private fun onDeletingAddress(isShowLoading: Boolean) {
        _deleteAddressState.value = FromFriendAddressActionState.Loading(isShowLoading)
    }

    fun onCheckedAddress(index: Int, isChecked: Boolean) {
        addressList.getOrNull(index)?.isSelected = isChecked
    }

    fun setAllListSelected(isSelected: Boolean) {
        addressList.forEach {
            it.isSelected = isSelected
        }
    }

    companion object {
        private const val TOAST_SHOWING_TIME = 3000L
    }
}
