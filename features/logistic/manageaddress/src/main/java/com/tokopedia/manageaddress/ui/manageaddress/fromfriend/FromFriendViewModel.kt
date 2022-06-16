package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.request.AddressRequest
import com.tokopedia.manageaddress.domain.DeleteFromFriendAddressUseCase
import com.tokopedia.manageaddress.domain.GetAddressSharedListUseCase
import com.tokopedia.manageaddress.domain.SaveFromFriendAddressUseCase
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

    var page = FIRST_PAGE
    var chosenAddrId = 0L
    var isOnLoadingGetAddress = false
    var isCanLoadMore = true
    var isCancelDelete = false

    fun onLoadMore(searchKey: String) {
        if (isOnLoadingGetAddress || isCanLoadMore.not()) {
            return
        } else {
            page++
        }

        getFromFriendAddressList(searchKey)
    }

    fun onSearchAdrress(searchKey: String) {
        page = FIRST_PAGE
        isCanLoadMore = true
        getFromFriendAddressList(searchKey)
    }

    private fun getFromFriendAddressList(searchKey: String) {
        launchCatchError(block = {
            showGetShareAddressLoading(true)
            val param = getAddressRequest(searchKey)
            val result = getFromFriendAdrressListUseCase(param)
            isCanLoadMore = result.listAddress.isNotEmpty()
            _getFromFriendAddressState.value = FromFriendAddressListState.Success(result)
            showGetShareAddressLoading(false)
        }, onError = {
            _getFromFriendAddressState.value = FromFriendAddressListState.Fail(it, it.message.orEmpty())
            showGetShareAddressLoading(false)
        })
    }

    private fun showGetShareAddressLoading(isShowLoading: Boolean) {
        _getFromFriendAddressState.value = FromFriendAddressListState.Loading(isShowLoading)
    }

    private fun getAddressRequest(searchKey: String): AddressRequest {
        return AddressRequest(
            searchKey = searchKey,
            page = page,
            showAddress = true,
            showCorner = false,
            limit = GET_FRIEND_ADDRESS_LIMIT,
            previousState = 0,
            localStateChosenAddressId = chosenAddrId,
            whitelistChosenAddress = true
        )
    }

    fun saveAddress(addressId: String) {
        launchCatchError(block = {
            showSaveAddressLoading(true)
            val result = saveAddressUseCase(addressId)
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

    fun deleteAddress(addressId: String) = launch {
        isCancelDelete = false
        delay(TOAST_SHOWING_TIME)

        if (isCancelDelete) {
            return@launch
        }

        launchCatchError(block = {
            showDeleteAddressLoading(true)
            val result = deleteAddressUseCase(addressId)
            _deleteAddressState.value = if (result.shareAddressResponse.isSuccess) {
                FromFriendAddressActionState.Success
            } else {
                FromFriendAddressActionState.Fail(null, result.shareAddressResponse.error)
            }
            showDeleteAddressLoading(false)
        }, onError = {
            _deleteAddressState.value = FromFriendAddressActionState.Fail(it, it.message.orEmpty())
            showDeleteAddressLoading(false)
        })
    }

    private fun showDeleteAddressLoading(isShowLoading: Boolean) {
        _deleteAddressState.value = FromFriendAddressActionState.Loading(isShowLoading)
    }

    companion object {
        private const val TOAST_SHOWING_TIME = 3000L
        const val FIRST_PAGE = 1
        private const val GET_FRIEND_ADDRESS_LIMIT = 10
    }
}