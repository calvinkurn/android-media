package com.tokopedia.flight.orderlist.view.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.orderlist.R;
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.OrderSimpleViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.mapper.FlightOrderToCancellationJourneyMapper;
import com.tokopedia.flight.orderlist.view.viewmodel.mapper.FlightOrderViewModelMapper;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.sessioncommon.data.profile.ProfileInfo;
import com.tokopedia.sessioncommon.data.profile.ProfilePojo;
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase;
import com.tokopedia.sortfilter.SortFilterItem;
import com.tokopedia.unifycomponents.ChipsUnify;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 12/6/17.
 */

public class FlightOrderListPresenter extends BaseDaggerPresenter<FlightOrderListContract.View>
        implements FlightOrderListContract.Presenter {

    private static final String AIRLINES_CONTACT_URL = TokopediaUrl.Companion.getInstance().getWEB() + "bantuan/kontak-maskapai-penerbangan/";

    private UserSessionInterface userSession;
    private FlightGetOrdersUseCase flightGetOrdersUseCase;
    private FlightOrderViewModelMapper flightOrderViewModelMapper;
    private FlightOrderToCancellationJourneyMapper flightOrderToCancellationJourneyMapper;
    private GetProfileUseCase getProfileUseCase;
    private CompositeSubscription compositeSubscription;

    private String userResendEmail = "";

    @Inject
    public FlightOrderListPresenter(UserSessionInterface userSession,
                                    FlightGetOrdersUseCase flightGetOrdersUseCase,
                                    FlightOrderViewModelMapper flightOrderViewModelMapper,
                                    FlightOrderToCancellationJourneyMapper flightOrderToCancellationJourneyMapper,
                                    GetProfileUseCase getProfileUseCase) {
        this.userSession = userSession;
        this.flightGetOrdersUseCase = flightGetOrdersUseCase;
        this.flightOrderViewModelMapper = flightOrderViewModelMapper;
        this.flightOrderToCancellationJourneyMapper = flightOrderToCancellationJourneyMapper;
        this.getProfileUseCase = getProfileUseCase;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadData(String selectedFilter, final int page, final int perPage) {
        flightGetOrdersUseCase.execute(
                flightGetOrdersUseCase.createRequestParam(page - 1 >= 0 ? page - 1 : page, selectedFilter, perPage),
                new Subscriber<List<? extends FlightOrder>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().showGetListError(e);
                        }
                    }


                    @Override
                    public void onNext(List<? extends FlightOrder> flightOrders) {
                        List<Visitable> visitables = flightOrderViewModelMapper.transform((List<FlightOrder>) flightOrders);
                        if (page == 1) {
                            buildAndRenderFilterList();
                        }
                        getView().renderList(visitables, visitables.size() >= perPage);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        detachView();
        flightGetOrdersUseCase.unsubscribe();
        getProfileUseCase.unsubscribe();
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onDownloadEticket(String invoiceId) {
        getView().navigateToInputEmailForm(invoiceId, userSession.getUserId(), userResendEmail);
    }

    private void buildAndRenderFilterList() {
        List<OrderSimpleViewModel> filtersMap = new ArrayList<>();
        filtersMap.add(new OrderSimpleViewModel("", getView().getString(R.string.flight_order_status_all_label)));
        filtersMap.add(new OrderSimpleViewModel("700,800", getView().getString(R.string.flight_order_status_success_label)));
        filtersMap.add(new OrderSimpleViewModel("600", getView().getString(R.string.flight_order_status_failed_label)));
        filtersMap.add(new OrderSimpleViewModel("102,101", getView().getString(R.string.flight_order_status_waiting_for_payment_label)));
        filtersMap.add(new OrderSimpleViewModel("200,300", getView().getString(R.string.flight_order_status_in_progress_label)));
        filtersMap.add(new OrderSimpleViewModel("610,650", getView().getString(R.string.flight_order_status_refund_label)));

        List<SortFilterItem> filterItems = new ArrayList<>();
        boolean isAnyItemSelected = false;
        for (OrderSimpleViewModel entry : filtersMap) {

            String chipsType = ChipsUnify.TYPE_NORMAL;
            if (getView().getSelectedFilter().equalsIgnoreCase(entry.getLabel())) {
                isAnyItemSelected = true;
                chipsType = ChipsUnify.TYPE_SELECTED;
            }

            SortFilterItem finishFilter = new SortFilterItem(entry.getDescription(),
                    chipsType, ChipsUnify.SIZE_MEDIUM, () -> {
                getView().selectFilter(entry.getLabel());
                return Unit.INSTANCE;});
            filterItems.add(finishFilter);
        }

        if (!isAnyItemSelected && filterItems.size() > 0) {
            filterItems.get(0).setType(ChipsUnify.TYPE_SELECTED);
        }

        getView().renderOrderStatus(filterItems);
    }

    @Override
    public void onGetProfileData() {
        getProfileUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ProfilePojo profilePojo = graphqlResponse.getData(ProfilePojo.class);
                ProfileInfo profileInfo = profilePojo.getProfileInfo();
                if (profileInfo.getEmail().length() > 0 && isViewAttached()) {
                    userResendEmail = profileInfo.getEmail();
                }
            }
        });
    }

    @Override
    public void onCancelButtonClicked(FlightOrderSuccessViewModel flightOrderSuccessViewModel) {
        List<FlightCancellationJourney> items = transformOrderToCancellation(flightOrderSuccessViewModel.getOrderJourney());

        boolean isRefundable = false;
        for (FlightCancellationJourney item : items) {
            if (item.isRefundable()) {
                isRefundable = true;
            }
        }

        if (isRefundable) {
            getView().showRefundableCancelDialog(flightOrderSuccessViewModel.getId(), items,
                    flightOrderSuccessViewModel.getOrderJourney());
        } else {
            getView().showNonRefundableCancelDialog(flightOrderSuccessViewModel.getId(), items,
                    flightOrderSuccessViewModel.getOrderJourney());
        }
    }

    @Override
    public void onMoreAirlineInfoClicked() {
        getView().navigateToWebview(AIRLINES_CONTACT_URL);
    }

    private List<FlightCancellationJourney> transformOrderToCancellation(List<FlightOrderJourney> flightOrderJourney) {
        return flightOrderToCancellationJourneyMapper.transform(flightOrderJourney);
    }
}
