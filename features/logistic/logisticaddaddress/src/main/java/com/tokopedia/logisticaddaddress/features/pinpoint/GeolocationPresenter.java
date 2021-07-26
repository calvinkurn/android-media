package com.tokopedia.logisticaddaddress.features.pinpoint;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractor;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractorImpl;
import com.tokopedia.logisticaddaddress.di.GeolocationScope;
import com.tokopedia.logisticaddaddress.domain.mapper.GeolocationMapper;
import com.tokopedia.logisticCommon.data.entity.response.KeroMapsAutofill;
import com.tokopedia.logisticCommon.domain.usecase.RevGeocodeUseCase;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Subscriber;

@GeolocationScope
public class GeolocationPresenter implements GeolocationContract.GeolocationPresenter {

    private final GeolocationContract.GeolocationView view;
    private final RetrofitInteractor retrofitInteractor;
    private final UserSession userSession;
    private final RevGeocodeUseCase revGeocodeUseCase;
    private final GeolocationMapper mapper;

    @Inject
    public GeolocationPresenter(RetrofitInteractorImpl retrofitInteractor,
                                UserSession userSession, RevGeocodeUseCase revGeocodeUseCase,
                                GoogleMapFragment googleMapFragment, GeolocationMapper geolocationMapper) {
        this.userSession = userSession;
        this.view = googleMapFragment;
        this.retrofitInteractor = retrofitInteractor;
        this.revGeocodeUseCase = revGeocodeUseCase;
        this.mapper = geolocationMapper;
    }

    @Override
    public void getReverseGeoCoding(String latitude, String longitude) {
        String keyword = String.format("%s,%s", latitude, longitude);
        view.setLoading(true);
        revGeocodeUseCase.execute(keyword)
                .subscribe(new Subscriber<KeroMapsAutofill>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setLoading(false);
                        view.setValuePointer("Error");
                    }

                    @Override
                    public void onNext(KeroMapsAutofill keroMapsAutofill) {
                        view.setLoading(false);
                        view.setValuePointer(keroMapsAutofill.getData().getFormattedAddress());
                        view.setNewLocationPass(mapper.map(keroMapsAutofill));
                    }
                });
    }

    @Override
    public void geoCode(String placeId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("placeid", placeId);
        retrofitInteractor.generateLatLng(
                AuthHelper.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), param),
                new RetrofitInteractor.GenerateLatLongListener() {
                    @Override
                    public void onSuccess(CoordinateUiModel model) {
                        view.moveMap(model.getCoordinate());
                    }

                    @Override
                    public void onError(String errorMessage) {
                        view.toastMessage(errorMessage);
                    }
                });
    }

    @Override
    public void onDestroy() {
        retrofitInteractor.unSubscribe();
        revGeocodeUseCase.unsubscribe();
    }

    @Override
    public RetrofitInteractor getInteractor() {
        return this.retrofitInteractor;
    }

}