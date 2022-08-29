package com.tokopedia.manageaddress.domain.mapper

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.manageaddress.domain.response.shareaddress.GetSharedAddressListResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.SharedAddressData
import rx.functions.Func1
import javax.inject.Inject

class SharedAddressMapper @Inject constructor() : Func1<GetSharedAddressListResponse, AddressListModel> {

    override fun call(it: GetSharedAddressListResponse): AddressListModel {
        return AddressListModel().apply {
            this.listAddress = it.keroGetSharedAddressList?.data?.map(recipientModelMapper) ?: arrayListOf()
        }
    }

    private val recipientModelMapper: (SharedAddressData) -> RecipientAddressModel = {
        RecipientAddressModel().apply {
            this.id = it.senderUserId?.toString()
            this.recipientName = it.receiverName
            this.addressName = it.addrName
            this.street = it.maskedAddress1
            this.recipientPhoneNumber = it.maskedPhone
            this.latitude = it.maskedLatitude
            this.longitude = it.maskedLongitude
        }
    }
}