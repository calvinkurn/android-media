package com.tokopedia.checkout.domain.mapper

import com.tokopedia.checkout.domain.datamodel.newaddresscorner.Data
import com.tokopedia.checkout.domain.datamodel.newaddresscorner.NewAddressCornerResponse
import com.tokopedia.checkout.domain.datamodel.newaddresscorner.AddressListModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by fajarnuha on 2019-05-27.
 */
class AddressCornerMapper @Inject constructor() : Func1<NewAddressCornerResponse, AddressListModel> {

    override fun call(it: NewAddressCornerResponse): AddressListModel {
        val token = Token()
        token.districtRecommendation = it.keroAddressCorner.token.districtRecommendation
        token.ut = it.keroAddressCorner.token.ut.toInt()

        return AddressListModel().apply {
            this.token = token
            this.listAddress = it.keroAddressCorner.data.map(recipientModelMapper)
            this.hasNext = it.keroAddressCorner.hasNext
        }
    }

    private val recipientModelMapper: (Data) -> RecipientAddressModel = {
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
        }
    }
}