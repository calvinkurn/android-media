package com.tokopedia.train.reviewdetail.presentation.fragment;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.common.travel.widget.CountdownTimeView;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ticker.TickerView;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.checkout.presentation.model.TrainCheckoutViewModel;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainAnalytics;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.common.util.TrainFlowConstant;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.homepage.presentation.activity.TrainHomepageActivity;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.di.DaggerTrainReviewDetailComponent;
import com.tokopedia.train.reviewdetail.di.TrainReviewDetailComponent;
import com.tokopedia.train.reviewdetail.presentation.NonScrollableLinearLayoutManager;
import com.tokopedia.train.reviewdetail.presentation.adapter.TrainPassengerAdapterTypeFactory;
import com.tokopedia.train.reviewdetail.presentation.contract.TrainReviewDetailContract;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModel;
import com.tokopedia.train.reviewdetail.presentation.presenter.TrainReviewDetailPresenter;
import com.tokopedia.train.reviewdetail.presentation.view.ViewTrainReviewDetailPriceSection;
import com.tokopedia.train.scheduledetail.presentation.activity.TrainScheduleDetailActivity;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailFragment extends BaseListFragment<TrainReviewPassengerInfoViewModel, TrainPassengerAdapterTypeFactory>
        implements TrainReviewDetailContract.View, VoucherCartHachikoView.ActionListener {

    private static final String HACHIKO_TRAIN_KEY = "train";

    private static final int REQUEST_CODE_TOPPAY = 100;
    private static final int REQUEST_CODE_LOYALTY = 200;

    private static final String TRAIN_CHECKOUT_TRACE = "tr_train_checkout";
    private static final String PARAM_CHECKOUT_CLIENT = "android";
    private static final String PARAM_CHECKOUT_VERSION = "kai-" + GlobalConfig.VERSION_NAME;

    @Inject
    TrainAnalytics trainAnalytics;

    @Inject
    TrainReviewDetailPresenter trainReviewDetailPresenter;

    @Inject
    TrainRouter trainRouter;

    @Inject
    TrainFlowUtil trainFlowUtil;

    private CountdownTimeView countdownTimeView;
    private TickerView tickerView;
    private CardWithAction cardDepartureTrip;
    private CardWithAction cardReturnTrip;
    private ViewTrainReviewDetailPriceSection viewTrainReviewDetailPriceSection;
    private VoucherCartHachikoView voucherCartHachikoView;
    private AppCompatButton buttonSubmit;
    private LinearLayout containerTrainReview;
    private ProgressBar progressBar;

    private static final String ARGS_TRAIN_SOFTBOOK = "ARGS_TRAIN_SOFTBOOK";
    private static final String ARGS_TRAIN_SCHEDULE_BOOKING = "ARGS_TRAIN_SCHEDULE_BOOKING";

    private TrainSoftbook trainSoftbook;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;

    private TrainReviewDetailComponent trainReviewDetailComponent;

    private TrainScheduleDetailViewModel departureTripViewModel;
    private TrainScheduleDetailViewModel returnTripViewModel;

    private String appliedVoucherCode;
    private boolean traceStop;
    private PerformanceMonitoring performanceMonitoring;

    public static Fragment newInstance(TrainSoftbook trainSoftbook,
                                       TrainScheduleBookingPassData trainScheduleBookingPassData) {
        TrainReviewDetailFragment fragment = new TrainReviewDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_TRAIN_SOFTBOOK, trainSoftbook);
        bundle.putParcelable(ARGS_TRAIN_SCHEDULE_BOOKING, trainScheduleBookingPassData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        trainSoftbook = getArguments().getParcelable(ARGS_TRAIN_SOFTBOOK);
        trainScheduleBookingPassData = getArguments().getParcelable(ARGS_TRAIN_SCHEDULE_BOOKING);

        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(TRAIN_CHECKOUT_TRACE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainReviewDetailPresenter.attachView(this);

        trainReviewDetailPresenter.getScheduleTrips(trainScheduleBookingPassData.getDepartureScheduleId(),
                trainScheduleBookingPassData.getReturnScheduleId());

        trainReviewDetailPresenter.getScheduleTripsPrice(
                trainScheduleBookingPassData.getDepartureScheduleId(),
                trainScheduleBookingPassData.getReturnScheduleId(),
                trainScheduleBookingPassData.getAdultPassenger(),
                trainScheduleBookingPassData.getInfantPassenger());

        countdownTimeView.setListener(() -> trainReviewDetailPresenter.onRunningOutOfTime());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_train_review_detail, container, false);

        countdownTimeView = rootview.findViewById(R.id.ct_countdown);
        cardDepartureTrip = rootview.findViewById(R.id.train_departure_info);
        cardReturnTrip = rootview.findViewById(R.id.train_return_info);
        tickerView = rootview.findViewById(R.id.ticker_view);
        viewTrainReviewDetailPriceSection = rootview.findViewById(R.id.view_train_review_detail_price_section);
        voucherCartHachikoView = rootview.findViewById(R.id.voucher_cart_hachiko_view);
        buttonSubmit = rootview.findViewById(R.id.button_train_review_submit);
        containerTrainReview = rootview.findViewById(R.id.container_train_review);
        progressBar = rootview.findViewById(R.id.train_review_progress_bar);

        voucherCartHachikoView.setActionListener(this);
        voucherCartHachikoView.setPromoAndCouponLabel();

        ArrayList<String> messages = new ArrayList<>();
        messages.add("Cek kembali detail pesanan Anda sebelum lanjut ke halaman pembayaran");
        tickerView.setVisibility(View.INVISIBLE);
        tickerView.setListMessage(messages);
        tickerView.setHighLightColor(ContextCompat.getColor(getActivity(), R.color.yellow_200));
        tickerView.hideCloseButton();
        tickerView.buildView();

        cardDepartureTrip.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getDepartureScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.travel_slide_up_in, R.anim.travel_anim_stay);
        });

        cardReturnTrip.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getReturnScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.travel_slide_up_in, R.anim.travel_anim_stay);
        });

        buttonSubmit.setOnClickListener(v -> {
            if (returnTripViewModel != null) {
                trainAnalytics.eventProceedToPayment(
                        departureTripViewModel,
                        returnTripViewModel
                );
            } else {
                if (departureTripViewModel != null) {
                    trainAnalytics.eventProceedToPayment(
                            departureTripViewModel.getOriginStationCode(),
                            departureTripViewModel.getDestinationStationCode(),
                            departureTripViewModel.getTrainClass(),
                            departureTripViewModel.getTrainName()
                    );
                }
            }

            trainReviewDetailPresenter.checkout(trainSoftbook.getReservationId(),
                    trainSoftbook.getReservationCode(),
                    appliedVoucherCode,
                    PARAM_CHECKOUT_CLIENT,
                    PARAM_CHECKOUT_VERSION);
        });

        viewTrainReviewDetailPriceSection.showScheduleTripsPrice(trainSoftbook);

        return rootview;
    }

    @Override
    public void loadData(int page) {

    }

    @NonNull
    @Override
    protected BaseListAdapter<TrainReviewPassengerInfoViewModel, TrainPassengerAdapterTypeFactory> createAdapterInstance() {
        return super.createAdapterInstance();
    }

    @Override
    protected TrainPassengerAdapterTypeFactory getAdapterTypeFactory() {
        return new TrainPassengerAdapterTypeFactory();
    }

    @Override
    public void onItemClicked(TrainReviewPassengerInfoViewModel trainSeatPassengerViewModel) {

    }

    @Override
    protected void initInjector() {
        if (trainReviewDetailComponent == null) {
            trainReviewDetailComponent = DaggerTrainReviewDetailComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getActivity().getApplication())).build();
        }
        trainReviewDetailComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showDepartureTrip(TrainScheduleViewModel departureTrip) {
        cardDepartureTrip.setContent(getString(R.string.train_review_trip, departureTrip.getOrigin(),
                departureTrip.getDestination()));
        cardDepartureTrip.setContentInfo(
                getString(R.string.train_review_trip_date, TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TrainDateUtil.DEFAULT_VIEW_FORMAT, departureTrip.getDepartureTimestamp())));
        cardDepartureTrip.setSubContent(getString(R.string.train_review_trip_train, departureTrip.getTrainName(),
                departureTrip.getTrainNumber()));

        String timeDepartureDepartureString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, departureTrip.getDepartureTimestamp());
        String timeDepartureArrivalString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, departureTrip.getArrivalTimestamp());

        String departureHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, departureTrip.getDepartureTimestamp());
        String arrivalHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, departureTrip.getArrivalTimestamp());
        int deviationDay = Integer.parseInt(arrivalHour) - Integer.parseInt(departureHour);
        String deviationDayString = deviationDay > 0 ? " (+" + deviationDay + "h)" : "";

        cardDepartureTrip.setSubContentInfo(getString(R.string.train_review_trip_time_and_duration,
                timeDepartureDepartureString, timeDepartureArrivalString + deviationDayString));

        trainReviewDetailPresenter.getPassengers(trainSoftbook, departureTrip.getOrigin(),
                departureTrip.getDestination());
    }

    @Override
    public void showReturnTrip(TrainScheduleViewModel returnTrip) {
        cardReturnTrip.setContent(getString(R.string.train_review_trip, returnTrip.getOrigin(),
                returnTrip.getDestination()));
        cardReturnTrip.setContentInfo(
                getString(R.string.train_review_trip_date, TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TrainDateUtil.DEFAULT_VIEW_FORMAT, returnTrip.getDepartureTimestamp())));
        cardReturnTrip.setSubContent(getString(R.string.train_review_trip_train, returnTrip.getTrainName(),
                returnTrip.getTrainNumber()));

        String timeReturnDepartureString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, returnTrip.getDepartureTimestamp());
        String timeReturnArrivalString = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_TIME, returnTrip.getArrivalTimestamp());

        String departureHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, returnTrip.getDepartureTimestamp());
        String arrivalHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TrainDateUtil.FORMAT_DAY, returnTrip.getArrivalTimestamp());
        int deviationDay = Integer.parseInt(arrivalHour) - Integer.parseInt(departureHour);
        String deviationDayString = deviationDay > 0 ? " (+" + deviationDay + "h)" : "";

        cardReturnTrip.setSubContentInfo(getString(R.string.train_review_trip_time_and_duration,
                timeReturnDepartureString, timeReturnArrivalString + deviationDayString));
    }

    @Override
    public void hideReturnTrip() {
        cardReturnTrip.setVisibility(View.GONE);
    }

    @Override
    public void startCountdown(Date expiredDate) {
        countdownTimeView.setExpiredDate(expiredDate);
        countdownTimeView.start();
    }

    @Override
    public void navigateToTopPayActivity(TrainCheckoutViewModel trainCheckoutViewModel) {
        Intent intent = trainRouter.getTopPayIntent(
                getActivity(), trainCheckoutViewModel);
        startActivityForResult(intent, REQUEST_CODE_TOPPAY);
    }

    @Override
    public void showPaymentFailedErrorMessage(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNeedToRefreshOnPassengerInfo() {

    }

    @Override
    public void navigateToOrderList() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
        Intent intent = trainRouter.getHomeIntent(getActivity());
        taskStackBuilder.addNextIntent(intent);
        Intent trainHomepage = TrainHomepageActivity.getCallingIntent(getActivity());
        Intent trainOrders = trainRouter.getTrainOrderListIntent(getActivity());
        taskStackBuilder.addNextIntent(trainHomepage);
        taskStackBuilder.addNextIntent(trainOrders);
        taskStackBuilder.startActivities();
    }

    @Override
    public void showCheckoutLoading() {
        containerTrainReview.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public String getExpireDate() {
        return trainSoftbook.getExpiryTimestamp();
    }

    @Override
    public void showExpiredPaymentDialog() {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.RETORIC);
        dialog.setTitle(getString(R.string.train_seat_expired_payment_time));
        dialog.setDesc(getString(R.string.train_seat_expired_payment_time_desc));
        dialog.setBtnOk(getString(R.string.train_seat_expired_payment_time_cta));
        dialog.setOnOkClickListener(v -> {
            dialog.dismiss();
            trainFlowUtil.actionSetResultAndClose(
                    getActivity(),
                    getActivity().getIntent(),
                    TrainFlowConstant.RESEARCH
            );
        });
        dialog.show();
    }

    @Override
    public void showCheckoutFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getPassengerTypeAdult() {
        return getString(R.string.kai_homepage_adult_passenger);
    }

    @Override
    public String getPassengerTypeChild() {
        return getString(R.string.kai_homepage_infant_passenger);
    }

    @Override
    public void showScheduleTripsPrice(TrainScheduleDetailViewModel departureTrip, TrainScheduleDetailViewModel returnTrip) {
        this.departureTripViewModel = departureTrip;
        this.returnTripViewModel = returnTrip;
    }

    @Override
    public void onClickUseVoucher() {
        Intent intent = trainRouter.getIntentOfLoyaltyActivityWithCoupon(
                getActivity(), HACHIKO_TRAIN_KEY, trainSoftbook.getReservationId(), trainSoftbook.getReservationCode());
        startActivityForResult(intent, REQUEST_CODE_LOYALTY);
    }

    @Override
    public void disableVoucherDiscount() {
        viewTrainReviewDetailPriceSection.removeDiscount();
    }

    @Override
    public void trackingSuccessVoucher(String voucherName) {
        trainAnalytics.eventVoucherSuccess(voucherName);
    }

    @Override
    public void trackingCancelledVoucher() {

    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        VerticalRecyclerView verticalRecyclerView = view.findViewById(R.id.recycler_view);
        verticalRecyclerView.setLayoutManager(new NonScrollableLinearLayoutManager(getActivity()));
        return verticalRecyclerView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_TOPPAY:
                hideCheckoutLoading();
                countdownTimeView.start();
                int paymentSuccess = trainRouter.getTopPayPaymentSuccessCode();
                int paymentFailed = trainRouter.getTopPayPaymentFailedCode();
                int paymentCancel = trainRouter.getTopPayPaymentCancelCode();
                if (resultCode == paymentSuccess) {
                    trainReviewDetailPresenter.onPaymentSuccess();
                } else if (resultCode == paymentFailed) {
                    trainReviewDetailPresenter.onPaymentFailed();
                } else if (resultCode == paymentCancel) {
                    trainReviewDetailPresenter.onPaymentCancelled();
                }
                break;
            case REQUEST_CODE_LOYALTY:
                if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        appliedVoucherCode = bundle.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE, "");
                        String voucherMessage = bundle.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE, "");
                        long voucherDiscountAmount = bundle.getLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT);

                        voucherCartHachikoView.setVoucher(appliedVoucherCode, voucherMessage);

                        if (voucherDiscountAmount > 0) {
                            viewTrainReviewDetailPriceSection.showNewPriceAfterDiscount(voucherDiscountAmount);
                        }
                    }
                } else if (resultCode == IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        appliedVoucherCode = bundle.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE);
                        String couponMessage = bundle.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE);
                        long couponDiscount = bundle.getLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT);
                        String coupontitle = bundle.getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_TITLE);
                        long couponCashback = bundle.getLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CASHBACK_AMOUNT);
                        voucherCartHachikoView.setCoupon(coupontitle, couponMessage, appliedVoucherCode);
                        if (couponDiscount > 0) {
                            viewTrainReviewDetailPriceSection.showNewPriceAfterDiscount(couponDiscount);
                        }
                    }
                }
                break;
        }
    }

    private void hideCheckoutLoading() {
        progressBar.setVisibility(View.GONE);
        containerTrainReview.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace();
            traceStop = true;
        }
    }

    @Override
    public void onDestroy() {
        trainReviewDetailPresenter.detachView();
        super.onDestroy();
    }
}