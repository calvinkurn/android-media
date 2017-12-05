package com.tokopedia.core.manage.people.address.presenter;

import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public interface DistrictRecomendationFragmentPresenter {

    void searchAddress(String keyword);

    void searchNextIfAvailable(String keyword);

    ArrayList<Address> getAddresses();

    void clearData();

    void detachView();
}
