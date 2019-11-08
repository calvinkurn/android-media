package com.tokopedia.flight.review.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalPayment;
import com.tokopedia.applink.internal.ApplinkConstInternalPromo;
import com.tokopedia.common.payment.model.PaymentCode;
import com.tokopedia.common.payment.model.PaymentPassData;
import com.tokopedia.common.travel.ticker.TravelTickerUtils;
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel;
import com.tokopedia.common.travel.widget.CountdownTimeView;
import com.tokopedia.design.component.ticker.TickerView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.FlightSimpleAdapter;
import com.tokopedia.flight.booking.view.fragment.FlightBookingNewPriceDialogFragment;
import com.tokopedia.flight.booking.view.viewmodel.BaseCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.constant.FlightFlowConstant;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightFlowUtil;
import com.tokopedia.flight.common.util.FlightRequestUtil;
import com.tokopedia.flight.common.view.FullDividerItemDecoration;
import com.tokopedia.flight.dashboard.view.activity.FlightDashboardActivity;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapter;
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapterTypeFactory;
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;
import com.tokopedia.flight.orderlist.network.model.FlightOrderError;
import com.tokopedia.flight.orderlist.network.model.FlightOrderException;
import com.tokopedia.flight.orderlist.util.FlightErrorUtil;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.view.activity.OnBackActionListener;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapter;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapterTypeFactory;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;
import com.tokopedia.flight.review.view.presenter.FlightBookingReviewContract;
import com.tokopedia.flight.review.view.presenter.FlightBookingReviewPresenter;
import com.tokopedia.promocheckout.common.data.ConstantKt;
import com.tokopedia.promocheckout.common.util.TickerCheckoutUtilKt;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/9/17.
 */

public class FlightBookingReviewFragment extends BaseDaggerFragment implements
        FlightBookingReviewContract.View, OnBackActionListener, TickerCheckoutView.ActionListener {

    public static final String HACHIKO_FLIGHT_KEY = "flight";
    public static final String EXTRA_NEED_TO_REFRESH = "EXTRA_NEED_TO_REFRESH";
    public static final String EXTRA_COUPON_CHANGED = "EXTRA_COUPON_CHANGED";
    public static final String EXTRA_DATA_REVIEW = "EXTRA_DATA_REVIEW";
    public static final String EXTRA_COMBO_KEY = "EXTRA_COMBO_KEY";
    public static final int RESULT_ERROR_VERIFY = 874;
    public static final String RESULT_ERROR_CODE = "RESULT_ERROR_CODE";
    private static final String INTERRUPT_DIALOG_TAG = "interrupt_dialog";
    private static final String FLIGHT_CHECKOUT_TRACE = "tr_flight_checkout";
    private static final String EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA";

    private static final int REQUEST_CODE_NEW_PRICE_DIALOG = 3;
    private static final int REQUEST_CODE_TOPPAY = 100;
    public static final int DEFAULT_IS_COUPON_ZERO = 0;
    public static final int DEFAULT_IS_COUPON_ONE = 1;

    @Inject
    FlightBookingReviewPresenter flightBookingReviewPresenter;

    FlightBookingReviewModel flightBookingReviewModel;
    private LinearLayout fullPageLoading;
    private LinearLayout discountAppliedLayout;
    private NestedScrollView containerFullPage;
    private CountdownTimeView reviewTime;
    private TextView reviewDetailDepartureFlight;
    private RecyclerView recyclerViewDepartureFlight;
    private TextView reviewDetailReturnFlight;
    private RecyclerView recyclerViewReturnFlight;
    private RecyclerView recyclerViewDataPassenger;
    private RecyclerView recyclerViewDetailPrice;
    private TextView reviewTotalPrice;
    private TextView reviewDiscountPrice;
    private AppCompatTextView reviewFinalTotalPrice;
    private Button buttonSubmit;
    private TickerView tickerView;
    private TickerCheckoutView promoTicker;
    private PromoData promoData = new PromoData();
    private View containerFlightReturn;
    private ProgressDialog progressDialog;
    private FlightSimpleAdapter flightBookingReviewPriceAdapter;
    private LinearLayout promoContainer;
    private boolean isPassengerInfoPageNeedToRefresh = false;
    private boolean isCouponVoucherChanged = false;

    private PerformanceMonitoring performanceMonitoring;
    private boolean isTraceStop = false;

    public static FlightBookingReviewFragment createInstance(FlightBookingReviewModel flightBookingReviewModel,
                                                             String comboKey) {
        FlightBookingReviewFragment flightBookingReviewFragment = new FlightBookingReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DATA_REVIEW, flightBookingReviewModel);
        bundle.putString(EXTRA_COMBO_KEY, comboKey);
        flightBookingReviewFragment.setArguments(bundle);
        return flightBookingReviewFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class)
                .inject(this);
        flightBookingReviewPresenter.attachView(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isPassengerInfoPageNeedToRefresh = savedInstanceState.getBoolean(EXTRA_NEED_TO_REFRESH);
            isCouponVoucherChanged = savedInstanceState.getBoolean(EXTRA_COUPON_CHANGED);
            flightBookingReviewModel = savedInstanceState.getParcelable(EXTRA_DATA_REVIEW);
        } else {
            flightBookingReviewModel = getArguments().getParcelable(EXTRA_DATA_REVIEW);
        }

        performanceMonitoring = PerformanceMonitoring.start(FLIGHT_CHECKOUT_TRACE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_NEED_TO_REFRESH, isPassengerInfoPageNeedToRefresh);
        outState.putBoolean(EXTRA_COUPON_CHANGED, isCouponVoucherChanged);
        outState.putParcelable(EXTRA_DATA_REVIEW, flightBookingReviewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_review, container, false);
        fullPageLoading = (LinearLayout) view.findViewById(R.id.full_page_loading);
        discountAppliedLayout = (LinearLayout) view.findViewById(R.id.voucher_applied_layout);
        containerFullPage = (NestedScrollView) view.findViewById(R.id.container_full_page);
        reviewTime = (CountdownTimeView) view.findViewById(R.id.countdown_finish_transaction);
        reviewDetailDepartureFlight = (TextView) view.findViewById(R.id.review_detail_departure_flight);
        recyclerViewDepartureFlight = (RecyclerView) view.findViewById(R.id.recycler_view_departure_flight);
        reviewDetailReturnFlight = (TextView) view.findViewById(R.id.review_detail_return_flight);
        recyclerViewReturnFlight = (RecyclerView) view.findViewById(R.id.recycler_view_return_flight);
        recyclerViewDataPassenger = (RecyclerView) view.findViewById(R.id.recycler_view_data_passenger);
        recyclerViewDetailPrice = (RecyclerView) view.findViewById(R.id.recycler_view_detail_price);
        reviewTotalPrice = (TextView) view.findViewById(R.id.total_price);
        reviewDiscountPrice = (TextView) view.findViewById(R.id.tv_discount_voucher);
        reviewFinalTotalPrice = (AppCompatTextView) view.findViewById(R.id.tv_total_final_price);
        buttonSubmit = (Button) view.findViewById(R.id.button_submit);
        promoTicker = (TickerCheckoutView) view.findViewById(R.id.flight_promo_ticker_view);
        promoContainer = (LinearLayout) view.findViewById(R.id.flight_promo_ticker_container);
        containerFlightReturn = view.findViewById(R.id.container_flight_return);
        tickerView = view.findViewById(R.id.flight_ticker_view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(com.tokopedia.flight.R.string.flight_booking_loading_title));
        progressDialog.setCancelable(false);

        reviewTime.setListener(new CountdownTimeView.OnActionListener() {
            @Override
            public void onFinished() {
                if (getActivity() != null && !(getActivity()).isFinishing()) {
                    progressDialog.show();
                    flightBookingReviewPresenter.onUpdateCart();
                }
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionVerifyAndCheckoutBooking();
            }
        });
        reviewDetailReturnFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FlightDetailActivity.createIntent(getActivity(), flightBookingReviewModel.getDetailViewModelListReturn(), false));
            }
        });
        reviewDetailDepartureFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FlightDetailActivity.createIntent(getActivity(), flightBookingReviewModel.getDetailViewModelListDeparture(), false));
            }
        });
        promoTicker.setActionListener(this);
        return view;
    }

    private void actionVerifyAndCheckoutBooking() {
        flightBookingReviewPresenter.verifyBooking(
                promoData.getPromoCode(),
                flightBookingReviewModel.getTotalPriceNumeric(),
                flightBookingReviewModel.getAdult(),
                flightBookingReviewModel.getId(),
                flightBookingReviewModel.getDetailPassengersData(),
                flightBookingReviewModel.getContactName(),
                flightBookingReviewModel.getPhoneCode().getCountryId(),
                flightBookingReviewModel.getContactEmail(),
                flightBookingReviewModel.getContactPhone(),
                flightBookingReviewModel.getInsuranceIds()
        );
    }

    void initView() {
        FlightDetailRouteTypeFactory flightDetailRouteTypeFactory = new FlightDetailAdapterTypeFactory(new FlightDetailAdapterTypeFactory.OnFlightDetailListener() {
            @Override
            public int getItemCount() {
                return flightBookingReviewModel.getDetailViewModelListDeparture().getRouteList().size();
            }
        }, true);
        List<Visitable> departureRoute = new ArrayList<>();
        departureRoute.addAll(flightBookingReviewModel.getDetailViewModelListDeparture().getRouteList());
        FlightDetailAdapter departureFlightAdapter = new FlightDetailAdapter(flightDetailRouteTypeFactory, departureRoute);
        recyclerViewDepartureFlight.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDepartureFlight.setAdapter(departureFlightAdapter);
        if (flightBookingReviewModel.getDetailViewModelListReturn() != null &&
                flightBookingReviewModel.getDetailViewModelListReturn().getRouteList().size() > 0) {
            FlightDetailRouteTypeFactory arrivalFlightDetailRouteTypeFactory1 = new FlightDetailAdapterTypeFactory(new FlightDetailAdapterTypeFactory.OnFlightDetailListener() {
                @Override
                public int getItemCount() {
                    return flightBookingReviewModel.getDetailViewModelListReturn().getRouteList().size();
                }
            }, true);
            List<Visitable> arrivalRoute = new ArrayList<>();
            arrivalRoute.addAll(flightBookingReviewModel.getDetailViewModelListReturn().getRouteList());
            FlightDetailAdapter returnFlightAdapter = new FlightDetailAdapter(arrivalFlightDetailRouteTypeFactory1, arrivalRoute);
            recyclerViewReturnFlight.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewReturnFlight.setAdapter(returnFlightAdapter);
            containerFlightReturn.setVisibility(View.VISIBLE);
        } else {
            containerFlightReturn.setVisibility(View.GONE);
        }
        FlightBookingReviewPassengerAdapterTypeFactory flightBookingReviewPassengerAdapterTypeFactory = new FlightBookingReviewPassengerAdapterTypeFactory();
        FlightBookingReviewPassengerAdapter flightBookingReviewPassengerAdapter2 = new FlightBookingReviewPassengerAdapter(flightBookingReviewPassengerAdapterTypeFactory);
        flightBookingReviewPassengerAdapter2.addElement(flightBookingReviewModel.getDetailPassengers());
        if (flightBookingReviewModel.getDetailPassengers().size() > 1) {
            recyclerViewDataPassenger.addItemDecoration(new FullDividerItemDecoration(recyclerViewDataPassenger.getContext()));
        }
        recyclerViewDataPassenger.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDataPassenger.setAdapter(flightBookingReviewPassengerAdapter2);
        flightBookingReviewPriceAdapter = new FlightSimpleAdapter();
        flightBookingReviewPriceAdapter.setViewModels(flightBookingReviewModel.getFlightReviewFares());
        recyclerViewDetailPrice.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDetailPrice.setAdapter(flightBookingReviewPriceAdapter);

        updateFinalTotal(getCurrentBookingReviewModel());
        reviewTime.setExpiredDate(flightBookingReviewModel.getDateFinishTime());
        reviewTime.start();
    }

    private void updateFinalTotal(FlightBookingReviewModel currentBookingReviewModel) {
        updateFinalTotal(null, currentBookingReviewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        flightBookingReviewPresenter.onViewCreated();
        flightBookingReviewPresenter.fetchTickerData();
        stopTrace();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ConstantKt.getREQUST_CODE_PROMO_LIST() || requestCode == ConstantKt.getREQUEST_CODE_PROMO_DETAIL()) && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA())) {
                promoData = data.getParcelableExtra(TickerCheckoutUtilKt.getEXTRA_PROMO_DATA());
                // Check between apply promo code or cancel promo from promo detail
                switch (promoData.getState()) {
                    case EMPTY: {
                        promoData.setPromoCode("");
                        resetPromoTicker();
                        break;
                    }
                    case FAILED: {
                        promoData.setPromoCode("");
                        renderPromoTicker();
                        break;
                    }
                    case ACTIVE: {
                        renderPromoTicker();
                        break;
                    }
                }
            }
        } else {
            switch (requestCode) {
                case REQUEST_CODE_NEW_PRICE_DIALOG:
                    if (resultCode != Activity.RESULT_OK) {
                        FlightFlowUtil.actionSetResultAndClose(getActivity(),
                                getActivity().getIntent(),
                                FlightFlowConstant.PRICE_CHANGE
                        );
                    }
                    break;
                case REQUEST_CODE_TOPPAY:
                    hideCheckoutLoading();
                    reviewTime.start();
                    int paymentSuccess = PaymentCode.PAYMENT_SUCCESS;
                    int paymentFailed = PaymentCode.PAYMENT_FAILED;
                    int paymentCancel = PaymentCode.PAYMENT_CANCELLED;
                    if (resultCode == paymentSuccess) {
                        flightBookingReviewPresenter.onPaymentSuccess();
                    } else if (resultCode == paymentFailed) {
                        flightBookingReviewPresenter.onPaymentFailed();
                    } else if (resultCode == paymentCancel) {
                        flightBookingReviewPresenter.onPaymentCancelled();
                    }
                    break;
            }
        }
    }

    @Override
    public void updateFinalTotal(AttributesVoucher attributesVoucher, FlightBookingReviewModel currentBookingReviewModel) {

        int totalFinal = 0;
        if (attributesVoucher != null && Math.round(attributesVoucher.getDiscountAmountPlain()) > 0) {
            discountAppliedLayout.setVisibility(View.VISIBLE);
            reviewDiscountPrice.setText(getFormattedDiscountPrice(attributesVoucher.getDiscountAmountPlain()));
            reviewTotalPrice.setText(flightBookingReviewModel.getTotalPrice());
            totalFinal = (int) (currentBookingReviewModel.getTotalPriceNumeric() - attributesVoucher.getDiscountAmountPlain());
        } else {
            discountAppliedLayout.setVisibility(View.GONE);
            totalFinal = currentBookingReviewModel.getTotalPriceNumeric();
        }
        reviewFinalTotalPrice.setText(FlightCurrencyFormatUtil.Companion.convertToIdrPrice(totalFinal));
    }

    private String getFormattedDiscountPrice(double discountAmountPlain) {
        return String.format(getString(R.string.flight_review_minus_discount_prefix), FlightCurrencyFormatUtil.Companion.convertToIdrPrice((int) Math.round(discountAmountPlain)));
    }

    private void renderPromoTicker() {
        promoTicker.setState(promoData.getState());
        promoTicker.setTitle(promoData.getTitle());
        promoTicker.setDesc(promoData.getDescription());
    }

    @Override
    public void renderAutoApplyPromo(PromoData promoData) {
        this.promoData = promoData;
        renderPromoTicker();
    }

    private void resetPromoTicker() {
        promoTicker.resetView();
    }

    @Override
    public String getComboKey() {
        if (getArguments() != null) {
            return getArguments().getString(EXTRA_COMBO_KEY);
        }
        return null;
    }

    @Override
    public void stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring.stopTrace();
            isTraceStop = true;
        }
    }

    @Override
    public void onErrorSubmitData(Throwable e) {

    }

    @Override
    public void onSuccessSubmitData() {

    }

    @Override
    public void onClickUsePromo() {
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_FLIGHT);
        intent.putExtra("EXTRA_COUPON_ACTIVE", flightBookingReviewModel.getVoucherViewModel().getIsCouponActive());
        intent.putExtra("EXTRA_CART_ID", flightBookingReviewModel.getId());
        startActivityForResult(intent, ConstantKt.getREQUST_CODE_PROMO_LIST());
    }

    @Override
    public void onClickDetailPromo() {
        Intent intent;
        String promoCode = promoData.getPromoCode();
        if (!promoCode.isEmpty()) {
            int requestCode;
            if (promoData.getTypePromo() == PromoData.CREATOR.getTYPE_VOUCHER()) {
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_LIST_FLIGHT);
                intent.putExtra("EXTRA_PROMO_CODE", promoCode);
                intent.putExtra("EXTRA_COUPON_ACTIVE", flightBookingReviewModel.getVoucherViewModel().getIsCouponActive());
                requestCode = ConstantKt.getREQUST_CODE_PROMO_LIST();
            } else {
                intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalPromo.PROMO_DETAIL_FLIGHT);
                intent.putExtra("EXTRA_IS_USE", true);
                intent.putExtra("EXTRA_KUPON_CODE", promoCode);
                requestCode = ConstantKt.getREQUEST_CODE_PROMO_DETAIL();
            }
            intent.putExtra("EXTRA_CART_ID", flightBookingReviewModel.getId());
            startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(getActivity(), com.tokopedia.promocheckout.common.R.string.promo_none_applied, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDisablePromoDiscount() {
        promoData.setPromoCode("");
        isCouponVoucherChanged = true;
        flightBookingReviewPresenter.onCancelAppliedVoucher();
        updateFinalTotal(null, getCurrentBookingReviewModel());
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void showUpdateDataErrorStateLayout(Throwable t) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), FlightErrorUtil.getMessageFromException(getActivity(), t),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        progressDialog.show();
                        flightBookingReviewPresenter.onUpdateCart();
                    }
                }
        );
    }

    @Override
    public void showExpireTransactionDialog(String message) {
        if (isAdded()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(com.tokopedia.flight.R.string.flight_booking_expired_booking_label);
            dialog.setPositiveButton(getActivity().getString(com.tokopedia.abstraction.R.string.title_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FlightFlowUtil.actionSetResultAndClose(getActivity(),
                                    getActivity().getIntent(),
                                    FlightFlowConstant.EXPIRED_JOURNEY
                            );
                        }
                    });
            dialog.setCancelable(false);
            dialog.create().show();
        }
    }

    @Override
    public void showSoldOutDialog() {
        if (isAdded()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(com.tokopedia.flight.R.string.flight_booking_sold_out_label);
            dialog.setPositiveButton(getActivity().getString(com.tokopedia.abstraction.R.string.title_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FlightFlowUtil.actionSetResultAndClose(getActivity(),
                                    getActivity().getIntent(),
                                    FlightFlowConstant.EXPIRED_JOURNEY
                            );
                        }
                    });
            dialog.setCancelable(false);
            dialog.create().show();
        }
    }

    @Override
    public void setCartId(String id) {
        flightBookingReviewModel.setId(id);
    }

    @Override
    public void setTimeStamp(String timestamp) {
        isPassengerInfoPageNeedToRefresh = true;
        flightBookingReviewModel.setDateFinishTime(FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT, timestamp));
    }

    @Override
    public void showPriceChangesDialog(String newTotalPrice, String oldTotalPrice) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment previousDialog = getFragmentManager().findFragmentByTag(INTERRUPT_DIALOG_TAG);
        if (previousDialog != null) {
            fragmentTransaction.remove(previousDialog);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment dialogFragment = FlightBookingNewPriceDialogFragment.newInstance(newTotalPrice, oldTotalPrice);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_NEW_PRICE_DIALOG);
        dialogFragment.show(getFragmentManager().beginTransaction(), INTERRUPT_DIALOG_TAG);
    }

    @Override
    public void hideUpdatePriceLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showUpdatePriceLoading() {
        progressDialog.show();
    }

    @Override
    public FlightDetailViewModel getDepartureFlightDetailViewModel() {
        return flightBookingReviewModel.getDetailViewModelListDeparture();
    }

    @Override
    public FlightDetailViewModel getReturnFlightDetailViewModel() {
        return flightBookingReviewModel.getDetailViewModelListReturn();
    }

    @Override
    public List<FlightBookingPassengerViewModel> getFlightBookingPassengers() {
        return flightBookingReviewModel.getDetailPassengersData();
    }

    @Override
    public void renderPriceListDetails(List<SimpleViewModel> simpleViewModels) {
        flightBookingReviewModel.setFlightReviewFares(simpleViewModels);
        flightBookingReviewPriceAdapter.setViewModels(simpleViewModels);
        flightBookingReviewPriceAdapter.notifyDataSetChanged();
    }

    @Override
    public void renderFinishTimeCountDown(Date date) {
        reviewTime.cancel();
        reviewTime.setExpiredDate(date);
        reviewTime.start();
    }

    @Override
    public void setTotalPrice(int totalPrice) {
        flightBookingReviewModel.setTotalPriceNumeric(totalPrice);
        flightBookingReviewModel.setTotalPrice(
                FlightCurrencyFormatUtil.Companion.convertToIdrPrice(totalPrice)
        );
        reviewTotalPrice.setText(flightBookingReviewModel.getTotalPrice());
        updateFinalTotal(getCurrentBookingReviewModel());
    }

    @Override
    public BaseCartData getCurrentCartData() {
        BaseCartData baseCartData = new BaseCartData();
        baseCartData.setId(flightBookingReviewModel.getId());
        baseCartData.setNewFarePrices(flightBookingReviewModel.getFarePrices());
        List<FlightBookingAmenityViewModel> amenityViewModels = new ArrayList<>();
        for (FlightBookingPassengerViewModel passengerViewModel : flightBookingReviewModel.getDetailPassengersData()) {
            for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : passengerViewModel.getFlightBookingLuggageMetaViewModels()) {
                amenityViewModels.addAll(flightBookingAmenityMetaViewModel.getAmenities());
            }
            for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : passengerViewModel.getFlightBookingMealMetaViewModels()) {
                amenityViewModels.addAll(flightBookingAmenityMetaViewModel.getAmenities());
            }
        }
        baseCartData.setAmenities(amenityViewModels);
        baseCartData.setTotal(flightBookingReviewModel.getTotalPriceNumeric());
        baseCartData.setAdult(flightBookingReviewModel.getAdult());
        baseCartData.setChild(flightBookingReviewModel.getChildren());
        baseCartData.setInfant(flightBookingReviewModel.getInfant());
        return baseCartData;
    }

    @Override
    public FlightBookingReviewModel getCurrentBookingReviewModel() {
        return flightBookingReviewModel;
    }

    @Override
    public String getDepartureTripId() {
        return flightBookingReviewModel.getDepartureTripId();
    }

    @Override
    public String getReturnTripId() {
        return flightBookingReviewModel.getReturnTripId();
    }

    @Override
    public String getIdEmpotencyKey(String s) {
        return generateIdEmpotency(s);
    }

    private String generateIdEmpotency(String requestId) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = FlightRequestUtil.md5(timeMillis);
        return String.format(getString(com.tokopedia.flight.R.string.flight_booking_id_empotency_format), requestId, token.isEmpty() ? timeMillis : token);
    }

    @Override
    public boolean isRoundTrip() {
        return flightBookingReviewModel.getReturnTripId() != null && flightBookingReviewModel.getReturnTripId().length() > 0;
    }

    @Override
    public void onErrorVerifyCode(Throwable e) {
        if (e instanceof FlightException) {
            for (FlightOrderError flightError : ((FlightOrderException) e).getErrorList()) {
                if (FlightErrorUtil.getErrorCode(flightError) >= 14 && FlightErrorUtil.getErrorCode(flightError) <= 21) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_ERROR_CODE, FlightErrorUtil.getErrorCode(flightError));
                    getActivity().setResult(RESULT_ERROR_VERIFY, intent);
                    getActivity().finish();
                }
            }
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), FlightErrorUtil.getMessageFromException(getActivity(), e));
        }
    }

    @Override
    public void showCheckoutLoading() {
        fullPageLoading.setVisibility(View.VISIBLE);
        containerFullPage.setVisibility(View.GONE);
    }

    @Override
    public void hideCheckoutLoading() {
        fullPageLoading.setVisibility(View.GONE);
        containerFullPage.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToTopPay(FlightCheckoutViewModel flightCheckoutViewModel) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setPaymentId(flightCheckoutViewModel.getPaymentId());
        paymentPassData.setTransactionId(flightCheckoutViewModel.getTransactionId());
        paymentPassData.setRedirectUrl(flightCheckoutViewModel.getRedirectUrl());
        paymentPassData.setCallbackFailedUrl(flightCheckoutViewModel.getCallbackFailedUrl());
        paymentPassData.setCallbackSuccessUrl(flightCheckoutViewModel.getCallbackSuccessUrl());
        paymentPassData.setQueryString(flightCheckoutViewModel.getQueryString());

        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalPayment.PAYMENT_CHECKOUT);
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        startActivityForResult(intent, REQUEST_CODE_TOPPAY);
    }

    @Override
    public void navigateToOrderList() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
        Intent homeTokopedia = RouteManager.getIntent(getContext(), ApplinkConst.HOME);
        Intent homepageFlight = FlightDashboardActivity.getCallingIntent(getActivity());
        Intent ordersFlight = RouteManager.getIntent(getContext(), ApplinkConst.FLIGHT_ORDER);
        taskStackBuilder.addNextIntent(homeTokopedia);
        taskStackBuilder.addNextIntent(homepageFlight);
        taskStackBuilder.addNextIntent(ordersFlight);
        taskStackBuilder.startActivities();
    }

    @Override
    public void showPaymentFailedErrorMessage(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorInSnackbar(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void showErrorInEmptyState(String message) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(), message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        actionVerifyAndCheckoutBooking();
                    }
                }
        );
    }

    @Override
    public void showVoucherContainer() {
        promoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVoucherContainer() {
        promoContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flightBookingReviewPresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getActivity().getIntent();

        if (isPassengerInfoPageNeedToRefresh) {
            intent.putExtra(EXTRA_NEED_TO_REFRESH, isPassengerInfoPageNeedToRefresh);
        }

        if (isCouponVoucherChanged) {
            intent.putExtra(EXTRA_COUPON_CHANGED, flightBookingReviewModel.getVoucherViewModel());
        }

        getActivity().setResult(Activity.RESULT_CANCELED, intent);
        getActivity().finish();

    }

    @Override
    public boolean isCanGoBack() {
        return !isPassengerInfoPageNeedToRefresh;
    }

    @Override
    public void setNeedToRefreshOnPassengerInfo() {
        isPassengerInfoPageNeedToRefresh = true;
    }

    @Override
    public void renderTickerView(TravelTickerViewModel travelTickerViewModel) {
        TravelTickerUtils.INSTANCE.buildTravelTicker(getContext(), travelTickerViewModel, tickerView);
    }


}