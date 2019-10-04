package com.tokopedia.digital_deals.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetLocationCityUseCase;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.response.LocationResponse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class DealsLocationPresenter extends BaseDaggerPresenter<DealsLocationContract.View> implements DealsLocationContract.Presenter {


    private GetLocationCityUseCase getLocationCityUseCase;
    private List<Location> mTopLocations;

    @Inject
    public DealsLocationPresenter(GetLocationCityUseCase getLocationCityUseCase) {
        this.getLocationCityUseCase = getLocationCityUseCase;
    }

    @Override
    public void attachView(DealsLocationContract.View view) {
        super.attachView(view);
        getCities();
    }

    public void getCities() {
        if (getView() == null) {
            return;
        }
        getView().showProgressBar(true);
        getLocationCityUseCase.setRequestParams(getView().getParams());
        getLocationCityUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getCities();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<LocationResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                LocationResponse locationResponse = (LocationResponse) dataResponse.getData();
                mTopLocations = locationResponse.getLocations();
                getView().showProgressBar(false);
                getView().renderPopularCities(mTopLocations);
            }
        });
    }


    @Override
    public void onDestroy() {
        getLocationCityUseCase.unsubscribe();
    }
}
