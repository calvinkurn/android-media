package com.tokopedia.core.manage.people.address.presenter;

import com.tokopedia.core.manage.people.address.interactor.DistrictRecommendationRetrofitInteractor;
import com.tokopedia.core.manage.people.address.interactor.DistrictRecommendationRetrofitInteractorImpl;
import com.tokopedia.core.manage.people.address.listener.DistrictRecomendationFragmentView;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class DistrictRecomendationFragmentPresenterImpl implements DistrictRecomendationFragmentPresenter {

    private ArrayList<Address> addresses = new ArrayList<>();
    private DistrictRecomendationFragmentView view;
    private DistrictRecommendationRetrofitInteractor retrofitInteractor;
    private int lastPage = 1;
    private String keroToken;

    public DistrictRecomendationFragmentPresenterImpl(DistrictRecomendationFragmentView view) {
        this.view = view;
        retrofitInteractor = new DistrictRecommendationRetrofitInteractorImpl();
    }

    @Override
    public void searchAddress(String query) {
        view.showLoading();
        retrofitInteractor.getDistrictRecommendation(getParams(query));

        view.hideLoading();
    }

    @Override
    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setToken(String token) {
        keroToken = token;
    }

    private TKPDMapParam<String, String> getParams(String query) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(DistrictRecommendationRetrofitInteractor.Params.PAGE, String.valueOf(lastPage++));
        params.put(DistrictRecommendationRetrofitInteractor.Params.TOKEN, keroToken);
        params.put(DistrictRecommendationRetrofitInteractor.Params.UT, "");
        params.put(DistrictRecommendationRetrofitInteractor.Params.QUERY, query);

        return params;
    }

}
