package com.tokopedia.logisticCommon.domain.mapper

import com.tokopedia.logisticCommon.data.entity.address.PageInfoDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.response.GetPeopleAddressResponse
import com.tokopedia.logisticCommon.domain.response.ManageAddressData
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by fajarnuha on 2019-05-27.
 */
class AddressCornerMapper @Inject constructor() : Func1<GetPeopleAddressResponse, AddressListModel> {

    override fun call(it: GetPeopleAddressResponse): AddressListModel {
        val token = Token()
        token.districtRecommendation = it.keroAddressCorner.token.districtReccomendation
        token.ut = it.keroAddressCorner.token.ut

        val pageInfo = PageInfoDataModel()
        pageInfo.ticker = it.keroAddressCorner.pageInfo.ticker
        pageInfo.buttonLabel = it.keroAddressCorner.pageInfo.buttonLabel

        return AddressListModel().apply {
            this.token = token
            this.listAddress = it.keroAddressCorner.data.map(recipientModelMapper)
            this.hasNext = it.keroAddressCorner.hasNext
            this.pageInfo = pageInfo
        }
    }

    private val recipientModelMapper: (ManageAddressData) -> RecipientAddressModel = {
        RecipientAddressModel().apply {
            this.id = it.addrId.toString()
            this.recipientName = it.receiverName
            this.addressName = it.addrName
            this.street = it.address1
            this.postalCode = it.postalCode
            this.provinceId = it.province.toString()
            this.cityId = it.city.toString()
            this.destinationDistrictId = it.district.toString()
            this.recipientPhoneNumber = it.phone
            this.countryName = it.country
            this.provinceName = it.provinceName
            this.cityName = it.cityName
            this.destinationDistrictName = it.districtName
            this.latitude = it.latitude
            this.longitude = it.longitude
            this.addressStatus = it.status
            this.isCornerAddress = it.isCorner
            this.partnerId = it.partnerId
            this.partnerName = it.partnerName
            this.isStateChosenAddress = it.isStateChosenAddress
            this.isRadioButtonChecked = it.radioButonChecked
        }
    }
}