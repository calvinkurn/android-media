package com.tokopedia.logisticaddaddress.features.manage;

import com.tokopedia.logisticaddaddress.di.AddressScope;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
@AddressScope
public class ManageAddressPresenter implements ManageAddressContract.Presenter {

    private ManageAddressContract.View mView;
    private GetAddressUseCase getAddressUseCase;

    @Inject
    public ManageAddressPresenter(GetAddressUseCase getAddressUseCase) {
        this.getAddressUseCase = getAddressUseCase;
    }

    @Override
    public void setView(ManageAddressContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        getAddressUseCase.unsubscribe();
    }

    @Override
    public Observable<GetPeopleAddress> getAddress(int page, int sortId, String query) {
        return getAddressUseCase.getExecuteObservable(getAddressUseCase.getAddressParam(page, sortId, query));
    }
}
