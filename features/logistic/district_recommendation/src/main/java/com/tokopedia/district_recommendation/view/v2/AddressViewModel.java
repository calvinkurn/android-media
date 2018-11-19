package com.tokopedia.district_recommendation.view.v2;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.district_recommendation.domain.model.Address;
import com.tokopedia.district_recommendation.view.v2.DistrictRecommendationTypeFactory;

/**
 * Created by Irfan Khoirul on 16/11/18.
 */

public class AddressViewModel implements Visitable<DistrictRecommendationTypeFactory> {

    private Address address;

    public AddressViewModel() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int type(DistrictRecommendationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
