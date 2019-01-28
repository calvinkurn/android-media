package com.tokopedia.district_recommendation.domain.mapper;

import com.tokopedia.district_recommendation.domain.model.Address;
import com.tokopedia.logisticdata.data.entity.address.DistrictRecommendationAddress;

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

}
