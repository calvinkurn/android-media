package com.tokopedia.core.manage.people.address.presenter;

import android.content.Context;

import com.tokopedia.core.manage.people.address.interactor.DistrictRecommendationRetrofitInteractor;
import com.tokopedia.core.manage.people.address.interactor.DistrictRecommendationRetrofitInteractorImpl;
import com.tokopedia.core.manage.people.address.listener.DistrictRecomendationFragmentView;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Address;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.AddressResponse;
import com.tokopedia.core.manage.people.address.model.districtrecomendation.Token;
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
    private Token token;

    public DistrictRecomendationFragmentPresenterImpl(Context context, DistrictRecomendationFragmentView view,
                                                      Token token) {
        this.view = view;
        this.token = token;
        retrofitInteractor = new DistrictRecommendationRetrofitInteractorImpl(context);
    }

    @Override
    public void searchAddress(String query) {
        addresses.clear();
        lastPage = 0;
        query(query);
    }

    private void query(String query) {
        view.showLoading();
        retrofitInteractor.getDistrictRecommendation(
                DistrictRecommendationRetrofitInteractorImpl.getParams(query, token, ++lastPage),
                new DistrictRecommendationRetrofitInteractor.DistrictRecommendationListener() {
                    @Override
                    public void onSuccess(AddressResponse model) {
                        view.hideLoading();
                        addresses.addAll(model.getAddresses());
                        hasNext = model.isNextAvailable();
                        view.updateRecommendation();
                    }

                    @Override
                    public void onFailed(String error) {
                        view.hideLoading();
                        view.showMessage(error);
                    }

                    @Override
                    public void onTimeout(String timeoutError) {
                        view.hideLoading();
                        view.showNoConnection(timeoutError);
                    }

                    @Override
                    public void onNoConnection() {
                        view.hideLoading();
                        view.showNoConnection("");
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

}
