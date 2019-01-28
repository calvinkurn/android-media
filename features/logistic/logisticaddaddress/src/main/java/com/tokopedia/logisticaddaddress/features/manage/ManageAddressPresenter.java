package com.tokopedia.logisticaddaddress.features.manage;

import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.logisticaddaddress.adapter.AddressViewModel;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;
import com.tokopedia.logisticaddaddress.domain.AddressViewModelMapper;
import com.tokopedia.logisticaddaddress.domain.usecase.GetAddressUseCase;
import com.tokopedia.logisticdata.data.entity.address.GetPeopleAddress;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Fajar Ulin Nuha on 13/11/18.
 */
@AddressScope
public class ManageAddressPresenter implements ManageAddressContract.Presenter {

    private Token mToken;
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
    public void getAddress(int page, int sortId, String query) {
        mView.onFirstPageStartLoad();
        getAddressUseCase
                .execute(getAddressUseCase.getAddressParam(page, sortId, query),
                        getPeopleAddressSubscriber(page, query));
    }

    @Override
    public void deleteAddress(String id) {
        mView.toggleFilterFab(false);
        mView.showLoadingView();
        mView.getActivityListener().startServiceDeleteAddress(id);
    }

    @Override
    public void prioritizeAddress(String id) {
        mView.toggleFilterFab(false);
        mView.showLoadingView();
        mView.getActivityListener().startServiceSetDefaultAddress(id);
    }

    @Override
    public Token getToken() {
        return mToken;
    }

    private Subscriber<GetPeopleAddress> getPeopleAddressSubscriber(final int page, final String query) {
        return new Subscriber<GetPeopleAddress>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNetworkError();
            }

            @Override
            public void onNext(GetPeopleAddress getPeopleAddress) {
                mToken = getPeopleAddress.getToken();
                mView.setIsEmptyAddress(getPeopleAddress.getList().size() == 0);
                List<AddressViewModel> addressViewModelList =
                        AddressViewModelMapper.convertToViewModel(getPeopleAddress.getList());
                boolean hasNext = false;
                if (getPeopleAddress.getPaging() != null)
                    hasNext = PagingHandler.CheckHasNext(getPeopleAddress.getPaging().getUriNext());
                mView.showData(addressViewModelList, hasNext);
                if (page == 1 && query.isEmpty()) {
                    mView.onFirstPageSuccessRendered();
                }
            }
        };
    }
}
