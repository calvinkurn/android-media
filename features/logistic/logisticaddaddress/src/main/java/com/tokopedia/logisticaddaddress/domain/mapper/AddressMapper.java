package com.tokopedia.logisticaddaddress.domain.mapper;

import com.tokopedia.localizationchooseaddress.domain.model.DistrictRecommendationAddressModel;
import com.tokopedia.logisticCommon.data.entity.response.Data;
import com.tokopedia.logisticaddaddress.domain.model.Address;
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress;

import java.util.ArrayList;
import javax.inject.Inject;

public class AddressMapper {

    @Inject
    public AddressMapper() {

    }

    public DistrictRecommendationAddress convertAddress(Address address) {
        DistrictRecommendationAddress districtAddress = new DistrictRecommendationAddress();

        districtAddress.setCityId(address.getCityId());
        districtAddress.setCityName(address.getCityName());
        districtAddress.setDistrictId(address.getDistrictId());
        districtAddress.setDistrictName(address.getDistrictName());
        districtAddress.setProvinceId(address.getProvinceId());
        districtAddress.setProvinceName(address.getProvinceName());
        districtAddress.setZipCodes(address.getZipCodes());

        return districtAddress;
    }

    public DistrictRecommendationAddress convertAutofillResponse(Data data) {
        DistrictRecommendationAddress districtAddress = new DistrictRecommendationAddress();

        ArrayList<String> arrayPostalCode = new ArrayList<>();
        arrayPostalCode.add(data.getPostalCode());

        districtAddress.setCityId(data.getCityId());

        // note : city name is not provided by backend
        districtAddress.setCityName("");
        districtAddress.setDistrictId(data.getDistrictId());
        districtAddress.setDistrictName(data.getDistrictName());
        districtAddress.setProvinceId(data.getProvinceId());

        // note : province name is not provided by backend
        districtAddress.setProvinceName("");
        districtAddress.setZipCodes(arrayPostalCode);

        return districtAddress;
    }

    public DistrictRecommendationAddressModel convertToAddressLocalizationModel(Address address) {
        DistrictRecommendationAddressModel districtAddressModel = new DistrictRecommendationAddressModel();

        districtAddressModel.setCityId(address.getCityId());
        districtAddressModel.setCityName(address.getCityName());
        districtAddressModel.setDistrictId(address.getDistrictId());
        districtAddressModel.setDistrictName(address.getDistrictName());
        districtAddressModel.setProvinceId(address.getProvinceId());
        districtAddressModel.setProvinceName(address.getProvinceName());
        districtAddressModel.setProvinceCode(address.getZipCodes());

        return districtAddressModel;
    }

}
