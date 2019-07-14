package com.tokopedia.logisticaddaddress.features.district_recommendation;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRecommendation;
import com.tokopedia.logisticaddaddress.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class DistrictRecommendationPresenter extends BaseDaggerPresenter<DistrictRecommendationContract.View>
        implements DistrictRecommendationContract.Presenter {

    private final GetDistrictRequestUseCase getDistrictRequestUseCase;
    private final AddressViewModelMapper addressViewModelMapper;
    private final GetDistrictRecommendation getDataNoTokenUsecase;

    @Inject
    public DistrictRecommendationPresenter(GetDistrictRequestUseCase getDistrictRequestUseCase,
                                           GetDistrictRecommendation getDistrictRecommendation,
                                           AddressViewModelMapper addressViewModelMapper) {
        this.getDistrictRequestUseCase = getDistrictRequestUseCase;
        this.getDataNoTokenUsecase = getDistrictRecommendation;
        this.addressViewModelMapper = addressViewModelMapper;
    }

    @Override
    public void attachView(DistrictRecommendationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getDistrictRequestUseCase.unsubscribe();
        getDataNoTokenUsecase.unsubscribe();
        super.detachView();
    }

    @Override
    public void loadData(String query, int page) {
        getDataNoTokenUsecase.execute(query, page)
                .doOnSubscribe(() -> getView().showLoading())
                .doOnTerminate(() -> getView().hideLoading())
                .subscribe(new Subscriber<AddressResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showGetListError(e);
                    }

                    @Override
                    public void onNext(AddressResponse addressResponse) {
                        if (addressResponse.getAddresses() != null && addressResponse.getAddresses().size() > 0) {
                            getView().renderList(addressViewModelMapper.transformToViewModel(addressResponse),
                                    addressResponse.isNextAvailable());
                        } else {
                            getView().showNoResultMessage();
                        }
                    }
                });
    }

    @Override
    public void loadData(String query, Token token, int page) {
        RequestParams params = RequestParams.create();
        params.putString(GetDistrictRequestUseCase.PARAM_PAGE, String.valueOf(page));
        params.putString(GetDistrictRequestUseCase.PARAM_TOKEN,
                token.getDistrictRecommendation());
        params.putString(GetDistrictRequestUseCase.PARAM_UT,
                String.valueOf(token.getUt()));
        params.putString(GetDistrictRequestUseCase.PARAM_QUERY, query);

        if (getView() != null) {
            getView().showLoading();
            getDistrictRequestUseCase.execute(params, new GetDistrictRecommendationSubscriber(
                    getView(), this, addressViewModelMapper));
        }
    }

}
