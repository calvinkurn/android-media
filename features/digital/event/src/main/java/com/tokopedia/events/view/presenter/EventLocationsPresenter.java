package com.tokopedia.events.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.events.domain.GetEventsLocationListRequestUseCase;
import com.tokopedia.events.domain.model.EventLocationDomain;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventsLocationContract;
import com.tokopedia.events.view.viewmodel.EventLocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class EventLocationsPresenter extends BaseDaggerPresenter<EventBaseContract.EventBaseView> implements EventsLocationContract.EventLocationsPresenter {

    private GetEventsLocationListRequestUseCase getEventsLocationListRequestUseCase;
    private EventsLocationContract.EventLocationsView mView;

    public EventLocationsPresenter(GetEventsLocationListRequestUseCase getEventsLocationListRequestUseCase) {
        this.getEventsLocationListRequestUseCase = getEventsLocationListRequestUseCase;
    }


    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventsLocationContract.EventLocationsView) view;
        getLocationsListList();
    }

    @Override
    public boolean onClickOptionMenu(int id) {
        mView.getActivity().onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {

    }

    @Override
    public void onDestroy() {

    }

    private void getLocationsListList() {

        getEventsLocationListRequestUseCase.execute(RequestParams.EMPTY, new Subscriber<List<EventLocationDomain>>() {
            @Override
            public void onCompleted() {
                Timber.d("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("enter error");
            }

            @Override
            public void onNext(List<EventLocationDomain> eventLocationDomains) {
                mView.renderLocationList(convertIntoVeiwModel(eventLocationDomains));
                Timber.d("enter onNext");
            }
        });
    }

    private List<EventLocationViewModel> convertIntoVeiwModel(List<EventLocationDomain> eventLocationDomains) {
        List<EventLocationViewModel> eventLocationViewModels = new ArrayList<>();
        EventLocationViewModel eventLocationViewModel;
        if (eventLocationDomains != null) {
            for (EventLocationDomain eventsCategoryDomain : eventLocationDomains) {
                eventLocationViewModel =new EventLocationViewModel();
                eventLocationViewModel.setId(eventsCategoryDomain.getId());
                eventLocationViewModel.setName(eventsCategoryDomain.getName());
                eventLocationViewModel.setSearchName(eventsCategoryDomain.getSearchName());
                eventLocationViewModel.setDistrict(eventsCategoryDomain.getDistrict());
                eventLocationViewModel.setCategoryId(eventsCategoryDomain.getCategoryId());
                eventLocationViewModel.setIcon(eventsCategoryDomain.getIcon());

                eventLocationViewModels.add(eventLocationViewModel);
            }
        }
        return eventLocationViewModels;
    }

}
