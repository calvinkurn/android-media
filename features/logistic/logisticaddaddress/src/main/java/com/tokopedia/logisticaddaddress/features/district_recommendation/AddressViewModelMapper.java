package com.tokopedia.logisticaddaddress.features.district_recommendation;


import com.tokopedia.logisticaddaddress.domain.model.Address;
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 19/11/18.
 */

public class AddressViewModelMapper {

    @Inject
    public AddressViewModelMapper() {
    }

    public List<AddressViewModel> transformToViewModel(AddressResponse addressResponse) {
        List<AddressViewModel> viewModels = new ArrayList<>();
        for (Address address : addressResponse.getAddresses()) {
            AddressViewModel addressViewModel = new AddressViewModel();
            addressViewModel.setAddress(address);

            viewModels.add(addressViewModel);
        }

        return viewModels;
    }
}
