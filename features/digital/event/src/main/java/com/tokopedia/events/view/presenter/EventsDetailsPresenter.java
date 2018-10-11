package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.view.activity.EventBookTicketActivity;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.mapper.EventDetailsViewModelMapper;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsPresenter
        extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventsDetailsContract.EventDetailPresenter {

    private GetEventDetailsRequestUseCase getEventDetailsRequestUseCase;
    private EventsDetailsViewModel eventsDetailsViewModel;
    public static String EXTRA_EVENT_VIEWMODEL = "extraeventviewmodel";
    String url = "";
    public static String EXTRA_SEATING_PARAMETER = "hasSeatLayout";
    private EventsDetailsContract.EventDetailsView mView;
    private EventsAnalytics eventsAnalytics;

    private int hasSeatLayout;

    public EventsDetailsPresenter(GetEventDetailsRequestUseCase eventDetailsRequestUseCase, EventsAnalytics eventsAnalytics) {
        this.getEventDetailsRequestUseCase = eventDetailsRequestUseCase;
        this.eventsAnalytics = eventsAnalytics;
    }

    @Override
    public boolean onClickOptionMenu(int id) {
        return false;
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

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventsDetailsContract.EventDetailsView) view;
        getEventDetails();
        Intent inIntent = mView.getActivity().getIntent();
        int from = inIntent.getIntExtra(EventDetailsActivity.FROM, 1);
        CategoryItemsViewModel dataFromHome = inIntent.getParcelableExtra("homedata");
        try {
            if (from == EventDetailsActivity.FROM_HOME_OR_SEARCH) {
                mView.renderFromHome(dataFromHome);
                url = dataFromHome.getUrl();
            } else if (from == EventDetailsActivity.FROM_DEEPLINK) {
                url = inIntent.getExtras().getString(EventDetailsActivity.EXTRA_EVENT_NAME_KEY);
            }
        } catch (NullPointerException e) {
            url = dataFromHome.getUrl();
            e.printStackTrace();
        }
    }

    public void getEventDetails() {
        mView.showProgressBar();
        RequestParams params = RequestParams.create();
        params.putString("detailsurl", url);
        getEventDetailsRequestUseCase.getExecuteObservable(params).map(eventDetailsDomain -> convertIntoEventDetailsViewModel(eventDetailsDomain)).subscribe(new Subscriber<EventsDetailsViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                CommonUtils.dumper("enter error");
                throwable.printStackTrace();
                mView.hideProgressBar();
                NetworkErrorHelper.showEmptyState(mView.getActivity(),
                        mView.getRootView(), () -> getEventDetails());
            }

            @Override
            public void onNext(EventsDetailsViewModel detailsViewModel) {
                mView.renderFromCloud(detailsViewModel);   //chained using mapl
                hasSeatLayout = eventsDetailsViewModel.getHasSeatLayout();
                mView.hideProgressBar();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    @Override
    public String getSCREEN_NAME() {
        return EventsGAConst.EVENTS_PRODUCT_PAGE;
    }


    private EventsDetailsViewModel convertIntoEventDetailsViewModel(EventDetailsDomain eventDetailsDomain) {
        eventsDetailsViewModel = new EventsDetailsViewModel();
        if (eventDetailsDomain != null) {
            EventDetailsViewModelMapper.mapDomainToViewModel(eventDetailsDomain, eventsDetailsViewModel);
        }
        return eventsDetailsViewModel;
    }

    public void bookBtnClick() {
        mView.showProgressBar();
        Intent bookTicketIntent = new Intent(mView.getActivity(), EventBookTicketActivity.class);
        bookTicketIntent.putExtra(EXTRA_EVENT_VIEWMODEL, eventsDetailsViewModel);
        bookTicketIntent.putExtra(EXTRA_SEATING_PARAMETER, hasSeatLayout);
        mView.navigateToActivityRequest(bookTicketIntent, Utils.Constants.SELECT_TICKET_REQUEST);
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_LANJUKTAN, eventsDetailsViewModel.getTitle() + "-" + getSCREEN_NAME());
    }
}
