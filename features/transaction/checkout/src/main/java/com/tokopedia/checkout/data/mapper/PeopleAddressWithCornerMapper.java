package com.tokopedia.checkout.data.mapper;

import com.tokopedia.checkout.domain.datamodel.addresscorner.AddressCornerResponse;
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;

import rx.functions.Func2;

/**
 * Created by fajarnuha on 10/02/19.
 * A class to map result of zip to ui model
 * NOTE : Can be improved later... (Better from backend)
 */
public class PeopleAddressWithCornerMapper implements Func2<GetPeopleAddress, AddressCornerResponse, PeopleAddressModel> {

    @Override
    public PeopleAddressModel call(GetPeopleAddress getPeopleAddress, AddressCornerResponse cornerAddressModel) {
        PeopleAddressModel result = new PeopleAddressModel();
        AddressModelMapper oldMapper = new AddressModelMapper();
        AddressCornerMapper withCornerMapper = new AddressCornerMapper();

        PeopleAddressModel withMapper = withCornerMapper.call(cornerAddressModel);

        result = oldMapper.transform(getPeopleAddress);
        result.setCornerAddressModelsList(withMapper.getCornerAddressModelsList());
        return result;
    }
}
