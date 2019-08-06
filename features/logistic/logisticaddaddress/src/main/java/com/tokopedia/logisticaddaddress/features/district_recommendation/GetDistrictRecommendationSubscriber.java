package com.tokopedia.logisticaddaddress.features.district_recommendation;


import com.tokopedia.logisticaddaddress.domain.model.AddressResponse;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class GetDistrictRecommendationSubscriber extends Subscriber<AddressResponse> {

    private final DiscomContract.View view;
    private final AddressViewModelMapper addressViewModelMapper;

    public GetDistrictRecommendationSubscriber(DiscomContract.View view,
                                               AddressViewModelMapper addressViewModelMapper) {
        this.view = view;
        this.addressViewModelMapper = addressViewModelMapper;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            view.setLoadingState(false);
            view.showGetListError(e);
        }
    }

    @Override
    public void onNext(AddressResponse addressResponse) {
        if (view != null) {
            view.setLoadingState(false);
            if (addressResponse.getAddresses() != null && addressResponse.getAddresses().size() > 0) {
                view.renderList(addressViewModelMapper.transformToViewModel(addressResponse),
                        addressResponse.isNextAvailable());
            } else {
                view.showEmpty();
            }
        }
    }

}
