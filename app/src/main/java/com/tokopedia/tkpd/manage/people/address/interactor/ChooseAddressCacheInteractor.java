package com.tokopedia.tkpd.manage.people.address.interactor;

import com.tokopedia.tkpd.manage.people.address.model.ChooseAddress.ChooseAddressResponse;

/**
 * Created by Alifa on 10/11/2016.
 */

public interface ChooseAddressCacheInteractor {
    void getAddressesCache(GetAddressesCacheListener listener);

    void setAddressesCache(ChooseAddressResponse result);

    interface GetAddressesCacheListener {
        void onSuccess(ChooseAddressResponse result);

        void onError(Throwable e);
    }
}
