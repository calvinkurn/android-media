package com.tokopedia.district_recommendation.view;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 17/11/18.
 */

public class DistrictRecommendationPresenter extends BaseDaggerPresenter<DistrictRecommendationContract.View>
        implements DistrictRecommendationContract.Presenter {

    private final GetDistrictRequestUseCase getDistrictRequestUseCase;
    private final AddressViewModelMapper addressViewModelMapper;

    @Inject
    public DistrictRecommendationPresenter(GetDistrictRequestUseCase getDistrictRequestUseCase,
                                           AddressViewModelMapper addressViewModelMapper) {
        this.getDistrictRequestUseCase = getDistrictRequestUseCase;
        this.addressViewModelMapper = addressViewModelMapper;
    }

    @Override
    public void attachView(DistrictRecommendationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getDistrictRequestUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void loadData(String query, Token token, int page) {
        RequestParams params = RequestParams.create();
        params.putString(GetDistrictRequestUseCase.PARAM_PAGE, String.valueOf(page));
        params.putString(GetDistrictRequestUseCase.PARAM_TOKEN,
                token.getDistrictRecommendation());
        params.putString(GetDistrictRequestUseCase.PARAM_UT,
                String.valueOf(token.getUnixTime()));
        params.putString(GetDistrictRequestUseCase.PARAM_QUERY, query);

        if (getView() != null) {
            getView().showLoading();
            getDistrictRequestUseCase.execute(params, new GetDistrictRecommendationSubscriber(
                    getView(), this, addressViewModelMapper));
        }
    }

}
