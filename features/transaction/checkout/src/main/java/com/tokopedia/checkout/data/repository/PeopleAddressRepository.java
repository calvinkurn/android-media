package com.tokopedia.checkout.data.repository;

import com.tokopedia.checkout.domain.datamodel.addresscorner.AddressCornerResponse;
import com.tokopedia.checkout.domain.datamodel.addressoptions.PeopleAddressModel;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author Aghny A. Putra on 21/02/18
 */

public interface PeopleAddressRepository {

    /**
     * Get an {@link Observable} which will emits a {@link List<RecipientAddressModel>}
     *
     * @param params Parameters used to retrieve address data
     * @return List of all address
     */
    Observable<GetPeopleAddress> getAllAddress(final Map<String, String> params);

    Observable<AddressCornerResponse> getCornerData(final Map<String, String> params);

}