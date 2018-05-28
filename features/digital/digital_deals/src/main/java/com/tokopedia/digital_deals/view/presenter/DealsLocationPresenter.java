package com.tokopedia.digital_deals.view.presenter;

import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.domain.GetLocationListRequestUseCase;
import com.tokopedia.digital_deals.domain.GetSearchNextUseCase;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.ValuesItemDomain;
import com.tokopedia.digital_deals.view.contractor.DealsLocationContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class DealsLocationPresenter extends BaseDaggerPresenter<DealsLocationContract.View> implements DealsLocationContract.Presenter {

    private GetLocationListRequestUseCase getSearchLocationListRequestUseCase;
    private List<LocationViewModel> mTopLocations;
    private List<LocationViewModel> mAllLocations;
    private String highlight;
    private boolean SEARCH_SUBMITTED = false;
    private boolean isTopLocations = true;

    @Inject
    public DealsLocationPresenter(GetLocationListRequestUseCase getLocationListRequestUseCase) {
        this.getSearchLocationListRequestUseCase = getLocationListRequestUseCase;
    }

    @Override
    public void getLocationListBySearch(String searchText) {
        highlight = searchText;
        List<LocationViewModel> locationViewModels = new ArrayList<>();
        for (LocationViewModel location : mAllLocations) {
            Log.d("skhkhdkfh", " " + searchText + "  " + location.getName());
            if (location.getName().trim().toLowerCase().contains(searchText.trim().toLowerCase())) {
                Log.d("skhkhdkfh111", " " + searchText + "  " + location.getName());

                locationViewModels.add(location);
            }
        }
        Log.d("Mlistsize", "  " + locationViewModels.size());
        getView().renderFromSearchResults(locationViewModels, !isTopLocations);
    }

    public void getLocations() {
        getView().showProgressBar();
        getSearchLocationListRequestUseCase.execute(getView().getParams(), new Subscriber<LocationDomainModel>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getLocations();
                    }
                });
            }

            @Override
            public void onNext(LocationDomainModel locationDomainModel) {
//                if (SEARCH_SUBMITTED)

                mTopLocations = Utils.getSingletonInstance()
                        .convertIntoLocationListItemsViewModel(locationDomainModel.getLocations());
                mAllLocations = Utils.getSingletonInstance()
                        .convertIntoLocationListItemsViewModel(locationDomainModel.getLocations());

                getView().renderFromSearchResults(mTopLocations, isTopLocations);
                getView().showViews();
                getView().hideProgressBar();

            }
        });

    }


    @Override
    public void onDestroy() {
        getSearchLocationListRequestUseCase.unsubscribe();
    }

    @Override
    public void searchTextChanged(String searchText) {
        SEARCH_SUBMITTED = false;
        if (searchText != null && !searchText.equals("")) {
            if (searchText.length() > 0) {
                getLocationListBySearch(searchText);
            }
            if (searchText.length() == 0) {
                getView().renderFromSearchResults(mTopLocations, isTopLocations);
            }
        } else {
            getView().renderFromSearchResults(mTopLocations, isTopLocations);
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        SEARCH_SUBMITTED = true;
        Log.d("InsideSearchSubmitted", " " + SEARCH_SUBMITTED);
        getLocationListBySearch(searchText);

    }

    @Override
    public void onSearchResultClick(LocationViewModel searchViewModel) {

    }


}
