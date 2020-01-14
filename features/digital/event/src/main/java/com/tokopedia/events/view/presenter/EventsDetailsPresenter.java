package com.tokopedia.events.view.presenter;

import android.content.Intent;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.events.domain.GetEventDetailsRequestUseCase;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.NsqEntertainmentModel;
import com.tokopedia.events.domain.model.NsqMessage;
import com.tokopedia.events.domain.model.NsqRecentDataModel;
import com.tokopedia.events.domain.model.NsqRecentSearchModel;
import com.tokopedia.events.domain.model.NsqServiceModel;
import com.tokopedia.events.domain.model.NsqTravelRecentSearchModel;
import com.tokopedia.events.domain.model.scanticket.CheckScanOption;
import com.tokopedia.events.domain.postusecase.PostNsqEventUseCase;
import com.tokopedia.events.domain.postusecase.PostNsqTravelDataUseCase;
import com.tokopedia.events.domain.scanTicketUsecase.CheckScanOptionUseCase;
import com.tokopedia.events.view.activity.EventBookTicketActivity;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.mapper.EventDetailsViewModelMapper;
import com.tokopedia.events.view.utils.CurrencyUtil;
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
    private PostNsqEventUseCase postNsqEventUseCase;
    private PostNsqTravelDataUseCase postNsqTravelDataUseCase;
    private UserSession userSession;
    public static String EXTRA_EVENT_VIEWMODEL = "extraeventviewmodel";
    public static String EVENT_BOOK_TICKET_ID = "eventbookticketactivity";
    String url = "";
    public static String EXTRA_SEATING_PARAMETER = "hasSeatLayout";
    private EventsDetailsContract.EventDetailsView mView;
    private EventsAnalytics eventsAnalytics;

    private int hasSeatLayout;

    public EventsDetailsPresenter(GetEventDetailsRequestUseCase eventDetailsRequestUseCase, EventsAnalytics eventsAnalytics, CheckScanOptionUseCase checkScanOptionUseCase, PostNsqEventUseCase postNsqEventUseCase, PostNsqTravelDataUseCase postNsqTravelDataUseCase) {
        this.getEventDetailsRequestUseCase = eventDetailsRequestUseCase;
        this.eventsAnalytics = eventsAnalytics;
        this.checkScanOptionUseCase = checkScanOptionUseCase;
        this.postNsqEventUseCase = postNsqEventUseCase;
        this.postNsqTravelDataUseCase = postNsqTravelDataUseCase;
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
        if (eventsDetailsViewModel != null) {
            SaveInstanceCacheManager saveInstanceCacheManager = new SaveInstanceCacheManager(mView.getActivity(),true);
            saveInstanceCacheManager.put(EXTRA_EVENT_VIEWMODEL,eventsDetailsViewModel,7);
            saveInstanceCacheManager.put(EXTRA_SEATING_PARAMETER,hasSeatLayout);
            bookTicketIntent.putExtra(EVENT_BOOK_TICKET_ID, saveInstanceCacheManager.getId());
            eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_CLICK_LANJUKTAN, eventsDetailsViewModel.getTitle().toLowerCase() + "-" + getSCREEN_NAME());
        }
        mView.navigateToActivityRequest(bookTicketIntent, Utils.Constants.SELECT_TICKET_REQUEST);
    }

    @Override
    public void sendNsqEvent(String userId, EventsDetailsViewModel data) {
        NsqServiceModel nsqServiceModel = new NsqServiceModel();
        nsqServiceModel.setService(Utils.NSQ_SERVICE);
        NsqMessage nsqMessage = new NsqMessage();
        nsqMessage.setUserId(Integer.parseInt(userId));
        nsqMessage.setProductId(data.getId());
        nsqMessage.setUseCase(Utils.NSQ_USE_CASE);
        nsqMessage.setAction("product-detail");
        nsqServiceModel.setMessage(nsqMessage);
        postNsqEventUseCase.setRequestModel(nsqServiceModel);
        postNsqEventUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                CommonUtils.dumper("enter error");
                throwable.printStackTrace();
                NetworkErrorHelper.showEmptyState(mView.getActivity(),
                        mView.getRootView(), () -> sendNsqEvent(userId, data));
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
            }
        });

    }

    @Override
    public void sendNsqTravelEvent(String userId, EventsDetailsViewModel data) {
        NsqTravelRecentSearchModel nsqTravelRecentSearchModel = new NsqTravelRecentSearchModel();
        NsqServiceModel nsqServiceModel1 = new NsqServiceModel();
        nsqTravelRecentSearchModel.setService("travel_recent_search");
        NsqMessage nsqMessage1 = new NsqMessage();
        nsqMessage1.setUserId(Integer.parseInt(userId));
        nsqTravelRecentSearchModel.setNsqMessage(nsqMessage1);
        NsqRecentSearchModel nsqRecentSearchModel = new NsqRecentSearchModel();
        nsqRecentSearchModel.setDataType("event");
        NsqRecentDataModel nsqRecentDataModel = new NsqRecentDataModel();
        NsqEntertainmentModel nsqEntertainmentModel = new NsqEntertainmentModel();
        nsqEntertainmentModel.setValue(data.getTitle());
        nsqEntertainmentModel.setPrice(String.format("%s %s", "Rp", CurrencyUtil.convertToCurrencyString(data.getSalesPrice())));
        nsqEntertainmentModel.setImageUrl(data.getImageApp());
        nsqEntertainmentModel.setId(data.getTitle());
        nsqEntertainmentModel.setPricePrefix("Mulai dari");
        nsqEntertainmentModel.setUrl("https://www.tokopedia.com/events/detail/" + data.getSeoUrl());
        nsqEntertainmentModel.setAppUrl("tokopedia://events/"+data.getSeoUrl());
        nsqRecentDataModel.setNsqEntertainmentModel(nsqEntertainmentModel);
        nsqRecentSearchModel.setNsqRecentDataModel(nsqRecentDataModel);
        nsqTravelRecentSearchModel.setNsqRecentSearchModel(nsqRecentSearchModel);
        postNsqTravelDataUseCase.setTravelDataRequestModel(nsqTravelRecentSearchModel);
        postNsqTravelDataUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                CommonUtils.dumper("enter error");
                throwable.printStackTrace();
                NetworkErrorHelper.showEmptyState(mView.getActivity(),
                        mView.getRootView(), () -> sendNsqTravelEvent(userId, data));
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
            }
        });
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
