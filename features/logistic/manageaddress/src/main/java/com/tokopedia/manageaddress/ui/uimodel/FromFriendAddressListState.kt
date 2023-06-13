package com.tokopedia.manageaddress.ui.uimodel

import com.tokopedia.manageaddress.domain.response.shareaddress.KeroAddrGetSharedAddressList

sealed class FromFriendAddressListState {
    data class Success(val data: KeroAddrGetSharedAddressList?) : FromFriendAddressListState()
    data class Fail(val throwable: Throwable?, val errorMessage: String) : FromFriendAddressListState()
    data class Loading(val isShowLoading: Boolean) : FromFriendAddressListState()
}
