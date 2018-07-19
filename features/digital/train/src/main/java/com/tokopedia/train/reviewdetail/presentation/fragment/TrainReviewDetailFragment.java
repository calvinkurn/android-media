package com.tokopedia.train.reviewdetail.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.component.CardWithAction;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.DaggerTrainReviewDetailComponent;
import com.tokopedia.train.reviewdetail.NonScrollableLinearLayoutManager;
import com.tokopedia.train.reviewdetail.TrainReviewDetailComponent;
import com.tokopedia.train.reviewdetail.presentation.adapter.TrainPassengerAdapterTypeFactory;
import com.tokopedia.train.reviewdetail.presentation.contract.TrainReviewDetailContract;
import com.tokopedia.train.reviewdetail.presentation.model.TrainReviewPassengerInfoViewModel;
import com.tokopedia.train.reviewdetail.presentation.presenter.TrainReviewDetailPresenter;
import com.tokopedia.train.scheduledetail.presentation.activity.TrainScheduleDetailActivity;
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.seat.presentation.widget.CountdownTimeView;

import javax.inject.Inject;

/**
 * Created by Rizky on 02/07/18.
 */
public class TrainReviewDetailFragment extends BaseListFragment<TrainReviewPassengerInfoViewModel, TrainPassengerAdapterTypeFactory>
        implements TrainReviewDetailContract.View, VoucherCartHachikoView.ActionListener {

    private static final String HACHIKO_TRAIN_KEY = "train";

    private static final int REQUEST_CODE_LOYALTY = 200;

    @Inject
    TrainReviewDetailPresenter trainReviewDetailPresenter;

    private CountdownTimeView countdownTimeView;
    private CardWithAction cardDepartureTrip;
    private CardWithAction cardReturnTrip;
    private LinearLayout viewTrainReviewTotalPrice;
    private TextView textTrainReviewTotalPrice;
    private LinearLayout containerTrainReviewPriceDetail;
    private TextView textDepartureTripPassengerCount;
    private TextView textDepartureTripPrice;
    private TextView textReturnTripPassengerCount;
    private TextView textReturnTripPrice;
    private LinearLayout viewTotalPriceReturnTrip;
    private VoucherCartHachikoView voucherCartHachikoView;
    private ImageView imageArrow;
    private TextView textLabelSeeDetail;

    private static final String ARGS_TRAIN_SOFTBOOK = "ARGS_TRAIN_SOFTBOOK";
    private static final String ARGS_TRAIN_SCHEDULE_BOOKING = "ARGS_TRAIN_SCHEDULE_BOOKING";

    private TrainSoftbook trainSoftbook;
    private TrainScheduleBookingPassData trainScheduleBookingPassData;

    private TrainReviewDetailComponent trainReviewDetailComponent;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_train_review_detail, container, false);

        countdownTimeView = rootview.findViewById(R.id.ct_countdown);
        cardDepartureTrip = rootview.findViewById(R.id.train_departure_info);
        cardReturnTrip = rootview.findViewById(R.id.train_return_info);
        viewTrainReviewTotalPrice = rootview.findViewById(R.id.view_train_review_total_price);
        textTrainReviewTotalPrice = rootview.findViewById(R.id.text_train_review_total_price);
        containerTrainReviewPriceDetail = rootview.findViewById(R.id.container_train_review_price_detail);
        textDepartureTripPassengerCount = rootview.findViewById(R.id.text_departure_trip_passenger_count);
        textDepartureTripPrice = rootview.findViewById(R.id.text_departure_trip_price);
        textReturnTripPassengerCount = rootview.findViewById(R.id.text_return_trip_passenger_count);
        textReturnTripPrice = rootview.findViewById(R.id.text_return_trip_price);
        viewTotalPriceReturnTrip = rootview.findViewById(R.id.view_total_price_return_trip);
        voucherCartHachikoView = rootview.findViewById(R.id.voucher_cart_hachiko_view);
        imageArrow = rootview.findViewById(R.id.train_review_image_arrow);
        textLabelSeeDetail = rootview.findViewById(R.id.train_review_text_label_see_detail);

        countdownTimeView.setListener(() -> {
            // TODO: navigate back to booking passenger page using setResult
        });

        voucherCartHachikoView.setActionListener(this);

        cardDepartureTrip.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getDepartureScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
        });

        cardReturnTrip.setActionListener(() -> {
            Intent intent = TrainScheduleDetailActivity.createIntent(getActivity(),
                    trainScheduleBookingPassData.getReturnScheduleId(),
                    trainScheduleBookingPassData.getAdultPassenger(),
                    trainScheduleBookingPassData.getInfantPassenger(),
                    false);
            startActivity(intent);
        });

        final boolean [] isPriceDetailOpened = {false};

        viewTrainReviewTotalPrice.setOnClickListener(v -> {
            if (!isPriceDetailOpened[0]) {
                isPriceDetailOpened[0] = true;
                textLabelSeeDetail.setText(getString(R.string.train_review_label_close));
                imageArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_grey));
                containerTrainReviewPriceDetail.setVisibility(View.VISIBLE);
            } else {
                isPriceDetailOpened[0] = false;
                textLabelSeeDetail.setText(getString(R.string.train_review_label_see_detail));
                imageArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_grey));
                containerTrainReviewPriceDetail.setVisibility(View.GONE);
            }
        });

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
                getString(R.string.train_review_trip_date, TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                        TrainDateUtil.DEFAULT_VIEW_FORMAT, departureTrip.getDepartureTimestamp())));
        cardDepartureTrip.setSubContent(getString(R.string.train_review_trip_train, departureTrip.getTrainName(),
                departureTrip.getTrainNumber()));

        String timeDepartureDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, departureTrip.getDepartureTimestamp());
        String timeDepartureArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, departureTrip.getArrivalTimestamp());

        cardDepartureTrip.setSubContentInfo(getString(R.string.train_review_trip_time_and_duration,
                timeDepartureDepartureString, timeDepartureArrivalString, departureTrip.getDisplayDuration()));

        trainReviewDetailPresenter.getPassengers(trainSoftbook, departureTrip.getOrigin(),
                departureTrip.getDestination());
    }

    @Override
    public void showReturnTrip(TrainScheduleViewModel returnTrip) {
        cardReturnTrip.setContent(getString(R.string.train_review_trip, returnTrip.getOrigin(),
                returnTrip.getDestination()));
        cardReturnTrip.setContentInfo(
                getString(R.string.train_review_trip_date, TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                        TrainDateUtil.DEFAULT_VIEW_FORMAT, returnTrip.getDepartureTimestamp())));
        cardReturnTrip.setSubContent(getString(R.string.train_review_trip_train, returnTrip.getTrainName(),
                returnTrip.getTrainNumber()));

        String timeReturnDepartureString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, returnTrip.getDepartureTimestamp());
        String timeReturnArrivalString = TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, returnTrip.getArrivalTimestamp());

        cardReturnTrip.setSubContentInfo(getString(R.string.train_review_trip_time_and_duration,
                timeReturnDepartureString, timeReturnArrivalString, returnTrip.getDisplayDuration()));
    }

    @Override
    public void hideReturnTrip() {
        cardReturnTrip.setVisibility(View.GONE);
    }

    @Override
    public void showScheduleTripsPrice(TrainScheduleDetailViewModel departureTrip, TrainScheduleDetailViewModel returnTrip) {
        textTrainReviewTotalPrice.setText(getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalPrice(),
                        false, 0).getFormattedString()));
        textDepartureTripPassengerCount.setText(getString(R.string.train_review_trip_passenger_count,
                departureTrip.getOriginStationCode(), departureTrip.getDestinationStationCode(), departureTrip.getNumOfAdultPassenger()));
        textDepartureTripPrice.setText(getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalAdultFare(),
                        false, 0).getFormattedString()));

        if (returnTrip != null) {
            viewTotalPriceReturnTrip.setVisibility(View.VISIBLE);
            textReturnTripPassengerCount.setText(getString(R.string.train_review_trip_passenger_count,
                    returnTrip.getOriginStationCode(), returnTrip.getDestinationStationCode(), returnTrip.getNumOfAdultPassenger()));
            textReturnTripPrice.setText(getString(R.string.train_label_currency,
                    CurrencyFormatUtil.getThousandSeparatorString(returnTrip.getTotalAdultFare(),
                            false, 0).getFormattedString()));
        } else {
            viewTotalPriceReturnTrip.setVisibility(View.GONE);
        }

        countdownTimeView.setExpiredDate(TrainDateUtil.stringToDate(TrainDateUtil.FORMAT_DATE_API_DETAIL,
                trainSoftbook.getExpiryTimestamp()));
        countdownTimeView.start();
    }

    @Override
    public void onClickUseVoucher() {
        if (getActivity().getApplication() instanceof TrainRouter) {
            Intent intent = ((TrainRouter) getActivity().getApplication()).getIntentOfLoyaltyActivityWithoutCoupon(
                    getActivity(), HACHIKO_TRAIN_KEY);
            startActivityForResult(intent, REQUEST_CODE_LOYALTY);
        }
    }

    @Override
    public void disableVoucherDiscount() {

    }

    @Override
    public void trackingSuccessVoucher(String voucherName) {

    }

    @Override
    public void trackingCancelledVoucher() {

    }

    @Override
    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new NonScrollableLinearLayoutManager(getActivity());
    }

}