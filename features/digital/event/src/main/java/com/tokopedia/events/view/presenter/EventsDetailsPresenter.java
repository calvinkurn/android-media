package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.scanticket.CheckScanOption;
import com.tokopedia.events.domain.scanTicketUsecase.CheckScanOptionUseCase;
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
import com.tokopedia.user.session.UserSession;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsPresenter
        extends BaseDaggerPresenter<EventBaseContract.EventBaseView>
        implements EventsDetailsContract.EventDetailPresenter {

    private GetEventDetailsRequestUseCase getEventDetailsRequestUseCase;
    private EventsDetailsViewModel eventsDetailsViewModel;
    private CheckScanOptionUseCase checkScanOptionUseCase;
    private UserSession userSession;
    public static String EXTRA_EVENT_VIEWMODEL = "extraeventviewmodel";
    String url = "";
    public static String EXTRA_SEATING_PARAMETER = "hasSeatLayout";
    private EventsDetailsContract.EventDetailsView mView;
    private EventsAnalytics eventsAnalytics;

    private int hasSeatLayout;

    public EventsDetailsPresenter(GetEventDetailsRequestUseCase eventDetailsRequestUseCase, EventsAnalytics eventsAnalytics, CheckScanOptionUseCase checkScanOptionUseCase) {
        this.getEventDetailsRequestUseCase = eventDetailsRequestUseCase;
        this.eventsAnalytics = eventsAnalytics;
        this.checkScanOptionUseCase = checkScanOptionUseCase;
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

    @Override
    public void attachView(EventBaseContract.EventBaseView view) {
        super.attachView(view);
        mView = (EventsDetailsContract.EventDetailsView) view;
        Intent inIntent = mView.getActivity().getIntent();
        int from = inIntent.getIntExtra(EventDetailsActivity.FROM, 1);
        CategoryItemsViewModel dataFromHome = inIntent.getParcelableExtra("homedata");
        try {
            if (from == EventDetailsActivity.FROM_HOME_OR_SEARCH) {
                checkForScan(dataFromHome.getId());
                mView.renderFromHome(dataFromHome);
                url = dataFromHome.getUrl();
            } else if (from == EventDetailsActivity.FROM_DEEPLINK) {
                url = inIntent.getExtras().getString(EventDetailsActivity.EXTRA_EVENT_NAME_KEY);
            }
        } catch (NullPointerException e) {
            url = dataFromHome.getUrl();
            e.printStackTrace();
        }
        getEventDetails();
    }

    public void getEventDetails() {
        mView.showProgressBar();
        RequestParams params = RequestParams.create();
        params.putString("detailsurl", url);
        getEventDetailsRequestUseCase.getExecuteObservable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(eventDetailsDomain -> convertIntoEventDetailsViewModel(eventDetailsDomain)).subscribe(new Subscriber<EventsDetailsViewModel>() {
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
                checkForScan(detailsViewModel.getId());
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
        bookTicketIntent.putExtra(EXTRA_SEATING_PARAMETER, hasSeatLayout);
        if (eventsDetailsViewModel != null) {
            bookTicketIntent.putExtra(EXTRA_EVENT_VIEWMODEL, eventsDetailsViewModel);
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_LANJUKTAN, eventsDetailsViewModel.getTitle().toLowerCase() + "-" + getSCREEN_NAME());
        }
        mView.navigateToActivityRequest(bookTicketIntent, Utils.Constants.SELECT_TICKET_REQUEST);
    }

    public void checkForScan(int productId) {
        if(getView() == null) {
            return;
        }
        RequestParams params = RequestParams.create();
        userSession = new UserSession(getView().getActivity());
        if (userSession != null && userSession.isLoggedIn()) {
            params.putInt("user_id", Integer.parseInt(userSession.getUserId()));
            params.putString("email", userSession.getEmail());
            params.putInt("product_id", productId);
            checkScanOptionUseCase.setRequestParams(params);
            checkScanOptionUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                    if (getView() == null) {
                        return;
                    }

                    Type token = new TypeToken<DataResponse<CheckScanOption>>() {
                    }.getType();
                    RestResponse restResponse = typeRestResponseMap.get(token);

                    DataResponse data = restResponse.getData();

                    CheckScanOption checkScanOption = (CheckScanOption) data.getData();

                    mView.setMenuItemVisibility(checkScanOption.isSuccess());
                }
            });
        }
    }

}
