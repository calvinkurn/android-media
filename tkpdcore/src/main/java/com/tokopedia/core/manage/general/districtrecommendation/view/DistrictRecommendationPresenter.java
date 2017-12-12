package com.tokopedia.core.manage.general.districtrecommendation.view;

import android.text.TextUtils;

import com.tokopedia.core.R;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.AddressResponse;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.domain.usecase.GetDistrictRequestUseCase;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class DistrictRecommendationPresenter extends BaseDaggerPresenter<DistrictRecommendationContract.View>
        implements DistrictRecommendationContract.Presenter {

    private ArrayList<Address> addresses = new ArrayList<>();
    private int lastPage = 0;
    private boolean hasNext;
    private Token token;
    private GetDistrictRequestUseCase getDistrictRequestUseCase;

    @Override
    public void attachView(DistrictRecommendationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getDistrictRequestUseCase.unsubscribe();
    }

    @Inject
    public DistrictRecommendationPresenter(GetDistrictRequestUseCase getDistrictRequestUseCase) {
        this.getDistrictRequestUseCase = getDistrictRequestUseCase;
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public void searchAddress(String query) {
        addresses.clear();
        getView().notifyUpdateAdapter();
        lastPage = 0;
        getView().setInitialLoading();
        query(query);
    }

    private void query(String query) {
        getView().showLoading();
        getDistrictRequestUseCase.execute(getParams(query, token, ++lastPage), new Subscriber<AddressResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoading();
                    String message;
                    if (e instanceof UnknownHostException || e instanceof ConnectException ||
                            e instanceof SocketTimeoutException) {
                        message = getView().getActivity().getResources().getString(R.string.msg_no_connection);
                    } else if (e instanceof UnProcessableHttpException) {
                        message = TextUtils.isEmpty(e.getMessage()) ?
                                getView().getActivity().getResources().getString(R.string.msg_no_connection) :
                                e.getMessage();
                    } else {
                        message = getView().getActivity().getResources().getString(R.string.default_request_error_unknown);
                    }
                    getView().showNoConnection(message);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onNext(AddressResponse addressResponse) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    addresses.addAll(addressResponse.getAddresses());
                    hasNext = addressResponse.isNextAvailable();
                    getView().notifyUpdateAdapter();
                    getView().updateRecommendation();
                }
            }
        });
    }

    @Override
    public void searchNextIfAvailable(String keyword) {
        if (hasNext) {
            getView().setLoadMoreLoading();
            query(keyword);
        }
    }

    @Override
    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    @Override
    public void clearData() {
        addresses.clear();
        getView().notifyUpdateAdapter();
        getView().updateRecommendation();
    }

    private RequestParams getParams(String query, Token token, int page) {
        RequestParams params = RequestParams.create();
        params.putString(GetDistrictRequestUseCase.PARAM_PAGE, String.valueOf(page));
        params.putString(GetDistrictRequestUseCase.PARAM_TOKEN,
                token.getDistrictRecommendation());
        params.putString(GetDistrictRequestUseCase.PARAM_UT,
                String.valueOf(token.getUnixTime()));
        params.putString(GetDistrictRequestUseCase.PARAM_QUERY, query);
        return params;
    }

}
