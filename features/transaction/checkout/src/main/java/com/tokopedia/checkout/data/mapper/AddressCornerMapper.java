package com.tokopedia.checkout.data.mapper;

import android.text.TextUtils;

import com.tokopedia.checkout.domain.datamodel.addresscorner.AddressCornerResponse;
import com.tokopedia.checkout.domain.datamodel.addresscorner.CornerBranch;
import com.tokopedia.checkout.domain.datamodel.addresscorner.Datum;
import com.tokopedia.checkout.domain.datamodel.addresscorner.TokopediaCornerDatum;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class AddressCornerMapper implements Func1<AddressCornerResponse, PeopleAddressModel> {

    private static final String ADDRESS_NAME_SAMPAI = "Tokopedia Corner";

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

            if (addressCornerResponse.getTokopediaCornerData() != null &&
                    !addressCornerResponse.getTokopediaCornerData().isEmpty()) {
                TokopediaCornerDatum cornerDatum = addressCornerResponse.getTokopediaCornerData().get(0);
                List<CornerAddressModel> cornerAddressModels = new ArrayList<>();
                for (CornerBranch cornerBranch : cornerDatum.getCornerBranch()) {
                    CornerAddressModel cornerAddressModel = new CornerAddressModel();
                    cornerAddressModel.setCornerId(cornerBranch.getCornerId());
                    cornerAddressModel.setCornerName(cornerDatum.getCornerName());
                    cornerAddressModel.setCornerBranchName(cornerBranch.getCornerBranchName());
                    cornerAddressModel.setCornerBranchDesc(cornerBranch.getDistrictName() + ", " + cornerBranch.getCityName());
                    cornerAddressModel.setDistrictName(cornerBranch.getDistrictName());
                    cornerAddressModel.setDistrictId(String.valueOf(cornerBranch.getDistrictId()));
                    cornerAddressModel.setPostalCode(cornerBranch.getPostcode());
                    cornerAddressModel.setCityName(cornerBranch.getCityName());
                    cornerAddressModel.setRecipientFullName(cornerDatum.getUserFullname());
                    cornerAddressModel.setUserCornerId(cornerDatum.getUserCornerId());
                    cornerAddressModel.setCityId(String.valueOf(cornerBranch.getCityId()));
                    cornerAddressModel.setProvinceId(String.valueOf(cornerBranch.getProvinceId()));

                    String[] latlong = cornerBranch.getGeoloc().split(",");
                    cornerAddressModel.setLatitude(latlong[0]);
                    cornerAddressModel.setLongitude(latlong[1]);

                    cornerAddressModels.add(cornerAddressModel);
                }
                result.setCornerAddressModelsList(cornerAddressModels);
            }

            result.setRecipientAddressModelList(recipientAddressModelList);
        }

        return result;
    }

}
