package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.scanticket.CheckScanOption;
import com.tokopedia.events.domain.scanTicketUsecase.CheckScanOptionUseCase;
import com.tokopedia.events.view.activity.EventBookTicketActivity;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.mapper.EventDetailsViewModelMapper;
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
import rx.functions.Func1;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsPresenter
        extends BaseDaggerPresenter<EventsDetailsContract.EventDetailsView>
        implements EventsDetailsContract.Presenter {

    private GetEventDetailsRequestUseCase getEventDetailsRequestUseCase;
    private EventsDetailsViewModel eventsDetailsViewModel;
    private CheckScanOptionUseCase checkScanOptionUseCase;
    private UserSession userSession;
    public static String EXTRA_EVENT_VIEWMODEL = "extraeventviewmodel";
    String url = "";
    public static String EXTRA_SEATING_PARAMETER = "hasSeatLayout";

    private int hasSeatLayout;

    @Inject
    public EventsDetailsPresenter(GetEventDetailsRequestUseCase eventDetailsRequestUseCase, CheckScanOptionUseCase checkScanOptionUseCase) {
        this.getEventDetailsRequestUseCase = eventDetailsRequestUseCase;
        this.checkScanOptionUseCase = checkScanOptionUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }


    @Override
    public void attachView(EventsDetailsContract.EventDetailsView view) {
        super.attachView(view);
        Intent inIntent = getView().getActivity().getIntent();
        int from = inIntent.getIntExtra(EventDetailsActivity.FROM, 1);
        CategoryItemsViewModel dataFromHome = inIntent.getParcelableExtra("homedata");
        try {
            if (from == EventDetailsActivity.FROM_HOME_OR_SEARCH) {
                checkForScan(dataFromHome.getId());
                getView().renderFromHome(dataFromHome);
                url = dataFromHome.getUrl();
            } else if (from == EventDetailsActivity.FROM_DEEPLINK) {
                url = inIntent.getExtras().getString(EventDetailsActivity.EXTRA_EVENT_NAME_KEY);
            }
        } catch (NullPointerException e) {
            url = dataFromHome.getUrl();
            e.printStackTrace();
        }
    }

    @Override
    public void getEventDetails() {
        getView().showProgressBar();
        com.tokopedia.core.base.domain.RequestParams params = com.tokopedia.core.base.domain.RequestParams.create();
        params.putString("detailsurl", url);
        getEventDetailsRequestUseCase.getExecuteObservableAsync(params).map(new Func1<EventDetailsDomain, EventsDetailsViewModel>() {
            @Override
            public EventsDetailsViewModel call(EventDetailsDomain eventDetailsDomain) {
                return convertIntoEventDetailsViewModel(eventDetailsDomain);
            }
        }).subscribe(new Subscriber<EventsDetailsViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                CommonUtils.dumper("enter error");
                throwable.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getEventDetails();
                            }
                        });
            }

            @Override
            public void onNext(EventsDetailsViewModel detailsViewModel) {
                getView().renderFromCloud(detailsViewModel);   //chained using mapl
                hasSeatLayout = eventsDetailsViewModel.getHasSeatLayout();
                checkForScan(detailsViewModel.getId());
                getView().hideProgressBar();
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
        getView().showProgressBar();
        Intent bookTicketIntent = new Intent(getView().getActivity(), EventBookTicketActivity.class);
        bookTicketIntent.putExtra(EXTRA_EVENT_VIEWMODEL, eventsDetailsViewModel);
        bookTicketIntent.putExtra(EXTRA_SEATING_PARAMETER, hasSeatLayout);
        getView().navigateToActivityRequest(bookTicketIntent, Utils.Constants.SELECT_TICKET_REQUEST);
        UnifyTracking.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_LANJUKTAN, eventsDetailsViewModel.getTitle().toLowerCase() + "-" + getSCREEN_NAME());
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

                    getView().setMenuItemVisibility(checkScanOption.isSuccess());
                }
            });
        }
    }

}
