package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.common.travel.ticker.TravelTickerFlightPage;
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId;
import com.tokopedia.common.travel.ticker.domain.TravelTickerUseCase;
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.view.presenter.FlightBaseBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingVoucherViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightInsuranceViewModel;
import com.tokopedia.flight.booking.view.viewmodel.mapper.FlightBookingCartDataMapper;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.passenger.domain.FlightPassengerDeleteAllListUseCase;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.FlightBookingCheckoutUseCase;
import com.tokopedia.flight.review.domain.FlightBookingVerifyUseCase;
import com.tokopedia.flight.review.domain.FlightCancelVoucherUseCase;
import com.tokopedia.flight.review.domain.verifybooking.model.response.CartItem;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;
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
    private final FlightPassengerDeleteAllListUseCase flightPassengerDeleteAllListUseCase;
    private final FlightCancelVoucherUseCase flightCancelVoucherUseCase;
    private FlightAnalytics flightAnalytics;
    private TravelTickerUseCase travelTickerUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightBookingReviewPresenter(FlightBookingCheckoutUseCase flightBookingCheckoutUseCase,
                                        FlightAddToCartUseCase flightAddToCartUseCase,
                                        FlightBookingCartDataMapper flightBookingCartDataMapper,
                                        FlightBookingVerifyUseCase flightBookingVerifyUseCase,
                                        FlightPassengerDeleteAllListUseCase flightPassengerDeleteAllListUseCase,
                                        FlightCancelVoucherUseCase flightCancelVoucherUseCase,
                                        FlightAnalytics flightAnalytics,
                                        TravelTickerUseCase travelTickerUseCase) {
        super(flightAddToCartUseCase, flightBookingCartDataMapper);
        this.flightBookingCheckoutUseCase = flightBookingCheckoutUseCase;
        this.flightBookingVerifyUseCase = flightBookingVerifyUseCase;
        this.flightPassengerDeleteAllListUseCase = flightPassengerDeleteAllListUseCase;
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

            if (reviewModel.getVoucherViewModel().isAutoapplySuccess()) {
                if (!(reviewModel.getVoucherViewModel().getIsCouponActive() == DEFAULT_IS_COUPON_ZERO &&
                        reviewModel.getVoucherViewModel().getIsCoupon() == DEFAULT_IS_COUPON_ONE)) {
                    renderCouponAndVoucher();
                }
            }
        } else {
            getView().hideVoucherContainer();
        }

        fetchTickerData();
    }

    @Override
    protected String getComboKey() {
        return getView().getComboKey();
    }

    @Override
    public void verifyBooking(String promoCode, int price, int adult, String cartId,
                              List<FlightBookingPassengerViewModel> flightPassengerViewModels,
                              String contactName, String country, String email, String phone,
                              List<FlightInsuranceViewModel> insurances) {
        getView().showCheckoutLoading();
        flightAnalytics.eventReviewNextClick();
        List<String> insuranceIds = new ArrayList<>();
        for (FlightInsuranceViewModel insurance : insurances) {
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
        }).map(new Func1<FlightCheckoutEntity, FlightCheckoutViewModel>() {
            @Override
            public FlightCheckoutViewModel call(FlightCheckoutEntity checkoutEntity) {
                FlightCheckoutViewModel viewModel = new FlightCheckoutViewModel();
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
                .subscribe(new Subscriber<FlightCheckoutViewModel>() {
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
                    public void onNext(FlightCheckoutViewModel flightCheckoutViewModel) {
                        getView().setNeedToRefreshOnPassengerInfo();
                        getView().navigateToTopPay(flightCheckoutViewModel);
                    }
                });
    }

    @Override
    public void onPaymentSuccess() {
        deleteListPassenger();
    }

    @Override
    public void onPaymentFailed() {
        getView().showPaymentFailedErrorMessage(R.string.flight_review_failed_checkout_message);
    }

    @Override
    public void onPaymentCancelled() {
        getView().setNeedToRefreshOnPassengerInfo();
        getView().showPaymentFailedErrorMessage(R.string.flight_review_cancel_checkout_message);
        flightAnalytics.eventPurchaseAttemptCancelled();
    }

    @Override
    public void onCancelAppliedVoucher() {
        this.flightCancelVoucherUseCase.execute(
                this.flightCancelVoucherUseCase.createEmptyParams(),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
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
    public List<FlightInsuranceViewModel> getInsurances() {
        return getView().getCurrentBookingReviewModel().getInsuranceIds();
    }

    @Override
    public void fetchTickerData() {
        compositeSubscription.add(travelTickerUseCase.createObservable(travelTickerUseCase.createRequestParams(
                TravelTickerInstanceId.Companion.getFLIGHT(), TravelTickerFlightPage.Companion.getSUMMARY()))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TravelTickerViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(TravelTickerViewModel travelTickerViewModel) {
                        if (travelTickerViewModel != null &&
                                travelTickerViewModel.getMessage().length() > 0) {
                            getView().renderTickerView(travelTickerViewModel);
                        }
                    }
                }));
    }

    @Override
    public void detachView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        flightBookingCheckoutUseCase.unsubscribe();
        flightBookingVerifyUseCase.unsubscribe();
        flightCancelVoucherUseCase.unsubscribe();
        flightPassengerDeleteAllListUseCase.unsubscribe();

        super.detachView();
    }

    private void deleteListPassenger() {
        flightPassengerDeleteAllListUseCase.execute(
                flightPassengerDeleteAllListUseCase.createEmptyRequestParams(),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getView().navigateToOrderList();
                    }
                }
        );
    }

    private void renderCouponAndVoucher() {
        FlightBookingVoucherViewModel voucherViewModel = getView().getCurrentBookingReviewModel().getVoucherViewModel();
        if (voucherViewModel.getIsCoupon() == DEFAULT_IS_COUPON_ONE) {
            getView().renderCouponInfoData();
        } else {
            getView().renderVoucherInfoData();
        }

        AttributesVoucher attributesVoucher = new AttributesVoucher();
        attributesVoucher.setVoucherCode(voucherViewModel.getCode());
        attributesVoucher.setMessage(voucherViewModel.getMessageSuccess());
        attributesVoucher.setDiscountAmountPlain(voucherViewModel.getDiscountAmount());
        getView().updateFinalTotal(attributesVoucher, getView().getCurrentBookingReviewModel());
    }
}
