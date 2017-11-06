package com.tokopedia.core.manage.people.address.presenter;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.manage.people.address.interactor.DistrictRecommendationRetrofitInteractor;
import com.tokopedia.core.manage.people.address.interactor.DistrictRecommendationRetrofitInteractorImpl;
import com.tokopedia.core.manage.people.address.listener.DistrictRecomendationFragmentView;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.AddressResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class DistrictRecomendationFragmentPresenterImpl implements DistrictRecomendationFragmentPresenter {

    private ArrayList<Address> addresses = new ArrayList<>();
    private DistrictRecomendationFragmentView view;
    private DistrictRecommendationRetrofitInteractor retrofitInteractor;
    private int lastPage = 0;
    private boolean hasNext;
    private String keroToken = "123"; // Dummy token
    private String keroUt = "1504521176"; // Dummy Unix Time

    public DistrictRecomendationFragmentPresenterImpl(DistrictRecomendationFragmentView view) {
        this.view = view;
        retrofitInteractor = new DistrictRecommendationRetrofitInteractorImpl();
    }

    @Override
    public void searchAddress(String query) {
        addresses.clear();
        lastPage = 0;
        query(query);
    }

    private void query(String query) {
        view.showLoading();
        retrofitInteractor.getDistrictRecommendation(getParams(query))
                .subscribe(new DefaultSubscriber<AddressResponse>() {
                    @Override
                    public void onNext(AddressResponse addressResponse) {
                        view.hideLoading();
                        addresses.addAll(addressResponse.getAddresses());
                        hasNext = addressResponse.isNextAvailable();
                        view.updateRecommendation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void searchNextIfAvailable(String keyword) {
        if (hasNext) {
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
        view.updateRecommendation();
    }

    public void setToken(String token) {
        keroToken = token;
    }

    private TKPDMapParam<String, String> getParams(String query) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(DistrictRecommendationRetrofitInteractor.Params.PAGE, String.valueOf(++lastPage));
        params.put(DistrictRecommendationRetrofitInteractor.Params.TOKEN, keroToken);
        params.put(DistrictRecommendationRetrofitInteractor.Params.UT, keroUt);
        params.put(DistrictRecommendationRetrofitInteractor.Params.QUERY, query);
        return params;
    }

}
