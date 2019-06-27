package com.tokopedia.district_recommendation.view;

import com.tokopedia.district_recommendation.domain.model.AddressResponse;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class GetDistrictRecommendationSubscriber extends Subscriber<AddressResponse> {

    private final DistrictRecommendationContract.View view;
    private final DistrictRecommendationContract.Presenter presenter;
    private final AddressViewModelMapper addressViewModelMapper;

    public GetDistrictRecommendationSubscriber(DistrictRecommendationContract.View view,
                                               DistrictRecommendationContract.Presenter presenter,
                                               AddressViewModelMapper addressViewModelMapper) {
        this.view = view;
        this.presenter = presenter;
        this.addressViewModelMapper = addressViewModelMapper;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (view != null) {
            view.hideLoading();
            view.showGetListError(e);
        }
    }

    @Override
    public void onNext(AddressResponse addressResponse) {
        if (view != null) {
            view.hideLoading();
            if (addressResponse.getAddresses() != null && addressResponse.getAddresses().size() > 0) {
                view.renderList(addressViewModelMapper.transformToViewModel(addressResponse),
                        addressResponse.isNextAvailable());
            } else {
                view.showNoResultMessage();
            }
        }
    }

}
