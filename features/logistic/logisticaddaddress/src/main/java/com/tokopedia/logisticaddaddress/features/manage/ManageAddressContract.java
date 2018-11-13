package com.tokopedia.logisticaddaddress.features.manage;

import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;

import java.util.List;

import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
public interface ManageAddressContract {

    interface View {

        void openFormAddressView(AddressModel data);

        void showData(List<AddressModel> data, boolean hasNext);

    }

    interface Presenter {

        void setView(ManageAddressContract.View view);

        void detachView();

        Observable<GetPeopleAddress> getAddress(int page, int sortId, String query);

    }
}
