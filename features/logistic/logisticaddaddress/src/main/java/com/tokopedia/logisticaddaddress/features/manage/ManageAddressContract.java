package com.tokopedia.logisticaddaddress.features.manage;

import com.tokopedia.logisticaddaddress.adapter.AddressViewModel;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.List;

import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
public interface ManageAddressContract {

    interface View {

        void openFormAddressView(AddressModel data);

        void showData(List<AddressViewModel> data, boolean hasNext);

        void showNetworkError();

    }

    interface Presenter {

        void setView(ManageAddressContract.View view);

        void detachView();

        void getAddress(int page, int sortId, String query);

        Token getToken();

    }
}
