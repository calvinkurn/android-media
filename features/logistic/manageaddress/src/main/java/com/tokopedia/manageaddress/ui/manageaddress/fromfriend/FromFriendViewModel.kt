package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.request.AddressRequest
import com.tokopedia.manageaddress.domain.usecase.DeleteFromFriendAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.GetAddressSharedListUseCase
import com.tokopedia.manageaddress.domain.usecase.SaveFromFriendAddressUseCase
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressActionState
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressListState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class FromFriendViewModel @Inject constructor(
    private val getFromFriendAdrressListUseCase: GetAddressSharedListUseCase,
    private val saveAddressUseCase: SaveFromFriendAddressUseCase,
    private val deleteAddressUseCase: DeleteFromFriendAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _getFromFriendAddressState =
        MutableLiveData<FromFriendAddressListState<AddressListModel>>()
    val getFromFriendAddressState: LiveData<FromFriendAddressListState<AddressListModel>>
        get() = _getFromFriendAddressState

    private val _saveAddressState =
        MutableLiveData<FromFriendAddressActionState>()
    val saveAddressState: LiveData<FromFriendAddressActionState>
        get() = _saveAddressState

    private val _deleteAddressState =
        MutableLiveData<FromFriendAddressActionState>()
    val deleteAddressState: LiveData<FromFriendAddressActionState>
        get() = _deleteAddressState

    var chosenAddrId = 0L
    var isCancelDelete = false
    val addressList = mutableListOf<RecipientAddressModel>()
    private val temporaryList = arrayListOf<RecipientAddressModel>()
    val isHaveAddressList: Boolean
        get() = addressList.isNotEmpty()
    val isAllSelected: Boolean
        get() = getSelectedAddressList().size == addressList.size
    var isNeedUpdateAllList = true

    fun onSearchAdrress(searchKey: String) {
        getFromFriendAddressList(searchKey)
    }

    private fun getFromFriendAddressList(searchKey: String) {
        launchCatchError(block = {
            showGetShareAddressLoading(true)
            val param = getAddressRequest(searchKey)
            val result = getFromFriendAdrressListUseCase(param)
            updateAddressList(result.listAddress)
            _getFromFriendAddressState.value = FromFriendAddressListState.Success(result)
            showGetShareAddressLoading(false)
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

    private fun getAddressRequest(searchKey: String): AddressRequest {
        return AddressRequest(
            searchKey = searchKey,
            page = FIRST_PAGE,
            showAddress = true,
            showCorner = false,
            limit = GET_FRIEND_ADDRESS_LIMIT,
            previousState = 0,
            localStateChosenAddressId = chosenAddrId,
            whitelistChosenAddress = true
        )
    }

    fun getSelectedAddressList(): List<RecipientAddressModel> {
        return addressList.filter { it.isSelected }
    }

    private fun getSelectedAddressId(): List<String> {
        return getSelectedAddressList().map { it.id }
    }

    fun saveAddress() {
        launchCatchError(block = {
            showSaveAddressLoading(true)
            val param = getSelectedAddressId()
            val result = saveAddressUseCase(param.toString())
            _saveAddressState.value = if (result.shareAddressResponse.isSuccess) {
                FromFriendAddressActionState.Success
            } else {
                FromFriendAddressActionState.Fail(null, result.shareAddressResponse.error)
            }
            showSaveAddressLoading(false)
        }, onError = {
            _saveAddressState.value = FromFriendAddressActionState.Fail(it, it.message.orEmpty())
            showSaveAddressLoading(false)
        })
    }

    private fun showSaveAddressLoading(isShowLoading: Boolean) {
        _saveAddressState.value = FromFriendAddressActionState.Loading(isShowLoading)
    }

    fun deleteAddress() = launch {
        isCancelDelete = false
        val param = getSelectedAddressId()
        updateTemporaryList()
        updateAddressList(getUnSelectAddressList().toTypedArray())
        onDeletingAddress(true)
        delay(TOAST_SHOWING_TIME)

        if (isCancelDelete) {
            return@launch
        }

        launchCatchError(block = {
            val result = deleteAddressUseCase(param.toString())
            _deleteAddressState.value = if (result.shareAddressResponse.isSuccess) {
                temporaryList.clear()
                FromFriendAddressActionState.Success
            } else {
                updateAddressList(temporaryList)
                FromFriendAddressActionState.Fail(null, result.shareAddressResponse.error)
            }
            onDeletingAddress(false)
        }, onError = {
            updateAddressList(temporaryList)
            _deleteAddressState.value =
                FromFriendAddressActionState.Fail(it, it.message.orEmpty())
            onDeletingAddress(false)
        })
    }

    fun onCancelDeleteAddress() {
        isCancelDelete = true
        updateAddressList(temporaryList)
        onDeletingAddress(false)
    }

    private fun updateTemporaryList() {
        temporaryList.clear()
        temporaryList.addAll(addressList.toTypedArray())
    }

    private fun getUnSelectAddressList(): List<RecipientAddressModel> {
        return addressList.filter { it.isSelected.not() }
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
        const val FIRST_PAGE = 1
        private const val GET_FRIEND_ADDRESS_LIMIT = 10
    }
}