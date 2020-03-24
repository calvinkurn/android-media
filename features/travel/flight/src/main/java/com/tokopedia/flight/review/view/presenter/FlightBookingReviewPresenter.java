package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.common.travel.ticker.TravelTickerFlightPage;
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId;
import com.tokopedia.common.travel.ticker.domain.TravelTickerUseCase;
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel;
import com.tokopedia.flight.bookingV2.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.bookingV2.presentation.model.BaseCartData;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingPassengerModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingVoucherModel;
import com.tokopedia.flight.bookingV2.presentation.model.FlightInsuranceModel;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.orderlist.util.FlightErrorUtil;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.FlightBookingCheckoutUseCase;
import com.tokopedia.flight.review.domain.FlightBookingVerifyUseCase;
import com.tokopedia.flight.review.domain.verifybooking.model.response.CartItem;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.review.view.model.FlightCheckoutModel;
import com.tokopedia.flight.review.view.model.mapper.FlightBookingCartDataMapper;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment.DEFAULT_IS_COUPON_ONE;
import static com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment.DEFAULT_IS_COUPON_ZERO;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPresenter extends FlightBaseBookingPresenter<FlightBookingReviewContract.View> implements FlightBookingReviewContract.Presenter {

    private final FlightBookingCheckoutUseCase flightBookingCheckoutUseCase;
    private final FlightBookingVerifyUseCase flightBookingVerifyUseCase;
    private final FlightCancelVoucherUseCase flightCancelVoucherUseCase;
    private FlightAnalytics flightAnalytics;
    private TravelTickerUseCase travelTickerUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightBookingReviewPresenter(FlightBookingCheckoutUseCase flightBookingCheckoutUseCase,
                                        FlightAddToCartUseCase flightAddToCartUseCase,
                                        FlightBookingCartDataMapper flightBookingCartDataMapper,
                                        FlightBookingVerifyUseCase flightBookingVerifyUseCase,
                                        FlightCancelVoucherUseCase flightCancelVoucherUseCase,
                                        FlightAnalytics flightAnalytics,
                                        TravelTickerUseCase travelTickerUseCase) {
        super(flightAddToCartUseCase, flightBookingCartDataMapper);
        this.flightBookingCheckoutUseCase = flightBookingCheckoutUseCase;
        this.flightBookingVerifyUseCase = flightBookingVerifyUseCase;
        this.flightCancelVoucherUseCase = flightCancelVoucherUseCase;
        this.flightAnalytics = flightAnalytics;
        this.travelTickerUseCase = travelTickerUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onViewCreated() {
        FlightBookingReviewModel reviewModel = getView().getCurrentBookingReviewModel();

        if (reviewModel.getVoucherViewModel().isEnableVoucher()) {
            getView().showVoucherContainer();

            FlightBookingVoucherModel voucherViewModel = reviewModel.getVoucherViewModel();
            if (voucherViewModel.isAutoapplySuccess()) {
                if (!(voucherViewModel.getIsCouponActive() == DEFAULT_IS_COUPON_ZERO &&
                        voucherViewModel.getIsCoupon() == DEFAULT_IS_COUPON_ONE)) {
                    getView().renderAutoApplyPromo(new PromoData(
                            voucherViewModel.getIsCoupon(),
                            voucherViewModel.getCode(),
                            voucherViewModel.getMessageSuccess(),
                            voucherViewModel.getTitleDescription(),
                            (int) voucherViewModel.getDiscountedAmount(),
                            TickerCheckoutView.State.ACTIVE
                    ));
                }
            }

        } else {
            getView().hideVoucherContainer();
        }
    }

    @Override
    protected String getComboKey() {
        return getView().getComboKey();
    }

    @Override
    public void verifyBooking(String promoCode, int price, int adult, String cartId,
                              List<FlightBookingPassengerModel> flightPassengerViewModels,
                              String contactName, String country, String email, String phone,
                              List<FlightInsuranceModel> insurances) {
        getView().showCheckoutLoading();
        flightAnalytics.eventReviewNextClick(getView().getCurrentBookingReviewModel(), getView().getComboKey());
        List<String> insuranceIds = new ArrayList<>();
        for (FlightInsuranceModel insurance : insurances) {
            insuranceIds.add(insurance.getId());
        }


        flightBookingVerifyUseCase.createObservable(
                flightBookingVerifyUseCase.createRequestParams(
                        promoCode,
                        price,
                        cartId,
                        flightPassengerViewModels,
                        contactName,
                        country,
                        email,
                        phone,
                        insuranceIds
                )
        ).flatMap(new Func1<DataResponseVerify, Observable<FlightCheckoutEntity>>() {
            @Override
            public Observable<FlightCheckoutEntity> call(DataResponseVerify dataResponseVerify) {
                if (dataResponseVerify.getAttributesData().getCartItems() != null && dataResponseVerify.getAttributesData().getCartItems().size() > 0) {
                    CartItem verifyCartItem = dataResponseVerify.getAttributesData().getCartItems().get(0);
                    int totalPrice = verifyCartItem.getConfiguration().getPrice();
                    String flightId = verifyCartItem.getMetaData().getInvoiceId();
                    String cartId = verifyCartItem.getMetaData().getCartId();
                    RequestParams requestParams;
                    if (dataResponseVerify.getAttributesData().getPromo() != null && dataResponseVerify.getAttributesData().getPromo().getCode().length() > 0) {
                        requestParams = flightBookingCheckoutUseCase.createRequestParam(cartId, flightId, totalPrice, dataResponseVerify.getAttributesData().getPromo().getCode());
                    } else {
                        requestParams = flightBookingCheckoutUseCase.createRequestParam(cartId, flightId, totalPrice);
                    }
                    return flightBookingCheckoutUseCase.createObservable(requestParams);
                }
                throw new RuntimeException("Failed to checkout");
            }
        }).map(new Func1<FlightCheckoutEntity, FlightCheckoutModel>() {
            @Override
            public FlightCheckoutModel call(FlightCheckoutEntity checkoutEntity) {
                FlightCheckoutModel viewModel = new FlightCheckoutModel();
                viewModel.setPaymentId(checkoutEntity.getAttributes().getParameter().getTransactionId());
                viewModel.setTransactionId(checkoutEntity.getAttributes().getParameter().getTransactionId());
                viewModel.setQueryString(checkoutEntity.getAttributes().getQueryString());
                viewModel.setRedirectUrl(checkoutEntity.getAttributes().getRedirectUrl());
                viewModel.setCallbackSuccessUrl(checkoutEntity.getAttributes().getCallbackUrlSuccess());
                viewModel.setCallbackFailedUrl(checkoutEntity.getAttributes().getCallbackUrlFailed());
                return viewModel;
            }
        })
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FlightCheckoutModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().setNeedToRefreshOnPassengerInfo();
                            getView().hideCheckoutLoading();
                            if (e instanceof FlightException) {
                                getView().showErrorInSnackbar(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
                            } else {
                                getView().showErrorInEmptyState(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
                            }
                        }
                    }

                    @Override
                    public void onNext(FlightCheckoutModel flightCheckoutViewModel) {
                        getView().setNeedToRefreshOnPassengerInfo();
                        getView().navigateToTopPay(flightCheckoutViewModel);
                    }
                });
    }

    @Override
    public void onPaymentSuccess() {
    }

    @Override
    public void onPaymentFailed() {
        getView().showPaymentFailedErrorMessage(com.tokopedia.flight.R.string.flight_review_failed_checkout_message);
    }

    @Override
    public void onPaymentCancelled() {
        getView().setNeedToRefreshOnPassengerInfo();
        getView().showPaymentFailedErrorMessage(com.tokopedia.flight.R.string.flight_review_cancel_checkout_message);
        flightAnalytics.eventPurchaseAttemptCancelled();
    }

    @Override
    public void onCancelAppliedVoucher() {
        this.flightCancelVoucherUseCase.execute(
                new Subscriber<GraphqlResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(GraphqlResponse response) {
                        getView().getCurrentBookingReviewModel().getVoucherViewModel()
                                .setAutoapplySuccess(false);
                    }
                }
        );
    }

    public Subscriber<FlightCheckoutEntity> getSubscriberSubmitData() {
        return new Subscriber<FlightCheckoutEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorSubmitData(e);
                }
            }

            @Override
            public void onNext(FlightCheckoutEntity checkoutEntity) {
                getView().onSuccessSubmitData();
            }
        };
    }

    @Override
    protected RequestParams getRequestParam() {
        RequestParams requestParams;
        if (getView().isRoundTrip()) {
            requestParams = addToCartUseCase.createRequestParam(
                    getView().getCurrentBookingReviewModel().getAdult(),
                    getView().getCurrentBookingReviewModel().getChildren(),
                    getView().getCurrentBookingReviewModel().getInfant(),
                    getView().getCurrentBookingReviewModel().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getReturnTripId(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId() + "_" + getView().getReturnTripId()),
                    calculateTotalPassengerFare(),
                    getView().getComboKey()
            );
        } else {
            requestParams = addToCartUseCase.createRequestParam(
                    getView().getCurrentBookingReviewModel().getAdult(),
                    getView().getCurrentBookingReviewModel().getChildren(),
                    getView().getCurrentBookingReviewModel().getInfant(),
                    getView().getCurrentBookingReviewModel().getFlightClass().getId(),
                    getView().getDepartureTripId(),
                    getView().getIdEmpotencyKey(getView().getDepartureTripId()),
                    calculateTotalPassengerFare()
            );
        }
        return requestParams;
    }

    @Override
    protected BaseCartData getCurrentCartData() {
        return getView().getCurrentCartData();
    }

    @Override
    protected void updateTotalPrice(int totalPrice) {
        getView().setTotalPrice(totalPrice);
    }

    @Override
    protected void onCountDownTimestampChanged(String timestamp) {
        getView().setTimeStamp(timestamp);
    }

    @Override
    public List<FlightInsuranceModel> getInsurances() {
        return getView().getCurrentBookingReviewModel().getInsuranceIds();
    }

    @Override
    public void fetchTickerData() {
        travelTickerUseCase.execute(travelTickerUseCase.createRequestParams(
                TravelTickerInstanceId.Companion.getFLIGHT(), TravelTickerFlightPage.Companion.getSUMMARY()),
                new Subscriber<TravelTickerViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(TravelTickerViewModel travelTickerViewModel) {
                        if (travelTickerViewModel.getMessage().length() > 0) {
                            getView().renderTickerView(travelTickerViewModel);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        flightBookingCheckoutUseCase.unsubscribe();
        flightBookingVerifyUseCase.unsubscribe();
        travelTickerUseCase.unsubscribe();

        super.detachView();
    }
}
