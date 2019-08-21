package com.tokopedia.checkout.data.mapper;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.addresscorner.AddressCornerResponse;
import com.tokopedia.checkout.domain.datamodel.addresscorner.Datum;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class AddressCornerMapper implements Func1<AddressCornerResponse, PeopleAddressModel> {

    @Override
    public PeopleAddressModel call(AddressCornerResponse addressCornerResponse) {
        PeopleAddressModel result = new PeopleAddressModel();

        // when developing, once get null
        if (addressCornerResponse != null && addressCornerResponse.getData() != null) {
            List<RecipientAddressModel> recipientAddressModelList = new ArrayList<>();
            for (Datum addressModel : addressCornerResponse.getData()) {
                RecipientAddressModel recipientAddress = new RecipientAddressModel();
                recipientAddress.setId(String.valueOf(addressModel.getAddrId()));
                recipientAddress.setAddressStatus(addressModel.getStatus());
                recipientAddress.setAddressName(addressModel.getAddrName());
                recipientAddress.setStreet(addressModel.getAddress1());
                recipientAddress.setDestinationDistrictId(String.valueOf(addressModel.getDistrict()));
                recipientAddress.setDestinationDistrictName(addressModel.getDistrictName());
                recipientAddress.setCityId(String.valueOf(addressModel.getCity()));
                recipientAddress.setProvinceId(String.valueOf(addressModel.getProvince()));
                recipientAddress.setCityName(addressModel.getCityName());
                recipientAddress.setProvinceName(addressModel.getProvinceName());
                recipientAddress.setCountryName(addressModel.getCountry());
                recipientAddress.setPostalCode(addressModel.getPostalCode());
                recipientAddress.setRecipientName(addressModel.getReceiverName());
                recipientAddress.setRecipientPhoneNumber(addressModel.getPhone());
                recipientAddress.setLatitude(!TextUtils.isEmpty(addressModel.getLatitude()) ?
                        addressModel.getLatitude() : null);
                recipientAddress.setLongitude(!TextUtils.isEmpty(addressModel.getLongitude()) ?
                        addressModel.getLongitude() : null);
                recipientAddressModelList.add(recipientAddress);
            }

            /* Retrieving Corner Data from here is deprecated see GetCornerList usecase */

            result.setRecipientAddressModelList(recipientAddressModelList);
        }

        return result;
    }

}
