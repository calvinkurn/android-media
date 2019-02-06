package com.tokopedia.logisticaddaddress.domain;

import com.tokopedia.logisticaddaddress.adapter.AddressViewModel;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fajar Ulin Nuha on 16/10/18.
 */
public class AddressViewModelMapper {

    public static List<AddressViewModel> convertToViewModel(List<AddressModel> data) {
        List<AddressViewModel> result = new ArrayList<>();
        for(AddressModel addressModel : data) {
            AddressViewModel temp = new AddressViewModel(
                    addressModel.getAddressId(),
                    addressModel.getReceiverPhone(),
                    addressModel.getAddressStatus(),
                    addressModel.getPostalCode(),
                    addressModel.getLatitude(),
                    addressModel.getAddressStreet(),
                    addressModel.getCityName(),
                    addressModel.getDistrictId(),
                    addressModel.getCityId(),
                    addressModel.getCountryName(),
                    addressModel.getLongitude(),
                    addressModel.getProvinceId(),
                    addressModel.getAddressName(),
                    addressModel.getReceiverName(),
                    addressModel.getProvinceName(),
                    addressModel.getDistrictName()
            );
            result.add(temp);
        }
        return result;
    }

    public static AddressModel convertFromViewModel(AddressViewModel viewModel) {
        return new AddressModel(
                viewModel.getAddressId(),
                viewModel.getReceiverPhone(),
                viewModel.getAddressStatus(),
                viewModel.getPostalCode(),
                viewModel.getLatitude(),
                viewModel.getAddressStreet(),
                viewModel.getCityName(),
                viewModel.getDistrictId(),
                viewModel.getCityId(),
                viewModel.getCountryName(),
                viewModel.getLongitude(),
                viewModel.getProvinceId(),
                viewModel.getAddressName(),
                viewModel.getReceiverName(),
                viewModel.getProvinceName(),
                viewModel.getDistrictName()
        );
    }
}
