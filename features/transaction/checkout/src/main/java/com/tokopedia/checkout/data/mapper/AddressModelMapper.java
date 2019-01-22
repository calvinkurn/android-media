package com.tokopedia.checkout.data.mapper;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.addressoptions.Paging;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 23/02/18
 */

public class AddressModelMapper {

    public AddressModelMapper() {

    }

    public PeopleAddressModel transform(GetPeopleAddress peopleAddress) {
        PeopleAddressModel peopleAddressModel = new PeopleAddressModel();
        if (peopleAddress != null) {
            peopleAddressModel.setRecipientAddressModelList(transform(peopleAddress.getList()));
            peopleAddressModel.setToken(peopleAddress.getToken());
            Paging paging = new Paging();
            paging.setUriNext(peopleAddress.getPaging().getUriNext());
            peopleAddressModel.setPaging(paging);
        }
        return peopleAddressModel;
    }

    public RecipientAddressModel transform(AddressModel addressModel) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        recipientAddress.setId(addressModel.getAddressId());
        recipientAddress.setAddressStatus(addressModel.getAddressStatus());
        recipientAddress.setAddressName(addressModel.getAddressName());
        recipientAddress.setStreet(addressModel.getAddressStreet());
        recipientAddress.setDestinationDistrictId(addressModel.getDistrictId());
        recipientAddress.setDestinationDistrictName(addressModel.getDistrictName());
        recipientAddress.setCityId(addressModel.getCityId());
        recipientAddress.setProvinceId(addressModel.getProvinceId());
        recipientAddress.setCityName(addressModel.getCityName());
        recipientAddress.setProvinceName(addressModel.getProvinceName());
        recipientAddress.setCountryName(addressModel.getCountryName());
        recipientAddress.setPostalCode(addressModel.getPostalCode());
        recipientAddress.setRecipientName(addressModel.getReceiverName());
        recipientAddress.setRecipientPhoneNumber(addressModel.getReceiverPhone());
        recipientAddress.setLatitude(!TextUtils.isEmpty(addressModel.getLatitude()) ?
                addressModel.getLatitude() : null);
        recipientAddress.setLongitude(!TextUtils.isEmpty(addressModel.getLongitude()) ?
                addressModel.getLongitude() : null);

        return recipientAddress;
    }

    public List<RecipientAddressModel> transform(List<AddressModel> addressModels) {
        List<RecipientAddressModel> recipientAddressModels = new ArrayList<>();
        if (addressModels != null) {
            for (AddressModel addressModel : addressModels) {
                recipientAddressModels.add(transform(addressModel));
            }
        }

        return recipientAddressModels;
    }

    public AddressModel transform(RecipientAddressModel recipientAddress) {
        AddressModel addressModel = new AddressModel();

        addressModel.setAddressId(recipientAddress.getId());
        addressModel.setAddressStatus(recipientAddress.getAddressStatus());
        addressModel.setAddressName(recipientAddress.getAddressName());
        addressModel.setAddressStreet(recipientAddress.getStreet());
        addressModel.setDistrictId(recipientAddress.getDestinationDistrictId());
        addressModel.setDistrictName(recipientAddress.getDestinationDistrictName());
        addressModel.setCityId(recipientAddress.getCityId());
        addressModel.setCityName(recipientAddress.getCityName());
        addressModel.setProvinceId(recipientAddress.getProvinceId());
        addressModel.setProvinceName(recipientAddress.getProvinceName());
        addressModel.setCountryName(recipientAddress.getCountryName());
        addressModel.setPostalCode(recipientAddress.getPostalCode());
        addressModel.setReceiverName(recipientAddress.getRecipientName());
        addressModel.setReceiverPhone(recipientAddress.getRecipientPhoneNumber());
        addressModel.setLatitude(recipientAddress.getLatitude() != null ? String.valueOf(recipientAddress.getLatitude()) : "");
        addressModel.setLongitude(recipientAddress.getLongitude() != null ? String.valueOf(recipientAddress.getLongitude()) : "");

        return addressModel;
    }

}