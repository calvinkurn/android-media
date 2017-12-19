package com.tokopedia.core.manage.general.districtrecommendation.data;

import com.tokopedia.core.manage.general.districtrecommendation.data.entity.AddressEntity;
import com.tokopedia.core.manage.general.districtrecommendation.data.entity.AddressResponseEntity;
import com.tokopedia.core.manage.general.districtrecommendation.data.entity.TokenEntity;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.AddressResponse;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class DistrictRecommendationEntityMapper {

    public AddressResponse transform(AddressResponseEntity entity) {
        AddressResponse addressResponse = null;
        if (entity != null) {
            addressResponse = new AddressResponse();
            addressResponse.setNextAvailable(entity.isNextAvailable());
            ArrayList<Address> addresses = new ArrayList<>();
            for (AddressEntity addressEntity : entity.getAddresses()) {
                addresses.add(transform(addressEntity));
            }
            addressResponse.setAddresses(addresses);
        }
        return addressResponse;
    }

    public Address transform(AddressEntity entity) {
        Address address = null;
        if (entity != null) {
            address = new Address();
            address.setDistrictId(entity.getDistrictId());
            address.setDistrictName(entity.getDistrictName());
            address.setCityId(entity.getDistrictId());
            address.setCityName(entity.getCityName());
            address.setProvinceId(entity.getProvinceId());
            address.setProvinceName(entity.getProvinceName());
            address.setZipCodes(entity.getZipCodes());
        }
        return address;
    }

    public Token transform(TokenEntity entity) {
        Token token = null;
        if (entity != null) {
            token = new Token();
            token.setDistrictRecommendation(entity.getDistrictRecommendation());
            token.setUnixTime(entity.getUnixTime());
        }
        return token;
    }
}
