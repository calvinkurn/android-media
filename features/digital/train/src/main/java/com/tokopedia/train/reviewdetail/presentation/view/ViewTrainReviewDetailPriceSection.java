package com.tokopedia.train.reviewdetail.presentation.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.passenger.domain.model.TrainTrip;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * Created by Rizky on 24/07/18.
 */
public class ViewTrainReviewDetailPriceSection extends LinearLayout {

    private LinearLayout viewTrainReviewTotalPrice;
    private TextView textTrainReviewTotalPrice;
    private LinearLayout containerTrainReviewPriceDetail;
    private ImageView imageArrow;
    private TextView textLabelSeeDetail;

    private View viewDivider1;
    private View viewDivider2;

    private TextView textDepartureTripAdultPassengerCount;
    private TextView textDepartureTripAdultTotalPrice;

    private TextView textDepartureTripInfantPassengerCount;
    private TextView textDepartureTripInfantTotalPrice;
    private LinearLayout viewInfantTotalPriceDepartureTrip;

    private TextView textReturnTripAdultPassengerCount;
    private TextView textReturnTripAdultTotalPrice;
    private LinearLayout viewAdultTotalPriceReturnTrip;

    private TextView textReturnTripInfantPassengerCount;
    private TextView textReturnTripInfantTotalPrice;
    private LinearLayout viewInfantTotalPriceReturnTrip;

    private LinearLayout viewDiscountChannelDepartureTrip;
    private TextView textLabelDiscountChannelDepartureTrip;
    private TextView textDiscountChannelDepartureTrip;
    private LinearLayout viewDiscountChannelReturnTrip;
    private TextView textLabelDiscountChannelReturnTrip;
    private TextView textDiscountChannelReturnTrip;

    private LinearLayout viewExtraFeeDepartureTrip;
    private TextView textLabelExtraFeeDepartureTrip;
    private TextView textExtraFeeDepartureTrip;
    private LinearLayout viewExtraFeeReturnTrip;
    private TextView textLabelExtraFeeReturnTrip;
    private TextView textExtraFeeReturnTrip;

    private LinearLayout viewDiscountVoucher;
    private TextView textDiscountVoucher;
    private LinearLayout viewSubtotalPrice;
    private TextView textSubtotalPrice;

    private double totalPrice;

    public ViewTrainReviewDetailPriceSection(Context context) {
        super(context);
        init();
    }

    public ViewTrainReviewDetailPriceSection(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewTrainReviewDetailPriceSection(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewTrainReviewDetailPriceSection(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        View rootview = inflate(getContext(), R.layout.view_train_review_detail_price_section, this);

        viewTrainReviewTotalPrice = rootview.findViewById(R.id.view_train_review_total_price);
        textTrainReviewTotalPrice = rootview.findViewById(R.id.text_train_review_total_price);
        containerTrainReviewPriceDetail = rootview.findViewById(R.id.container_train_review_price_detail);
        imageArrow = rootview.findViewById(R.id.train_review_image_arrow);
        textLabelSeeDetail = rootview.findViewById(R.id.train_review_text_label_see_detail);

        viewDivider1 = rootview.findViewById(R.id.view_divider);
        viewDivider2 = rootview.findViewById(R.id.view_divider_2);

        textDepartureTripAdultPassengerCount = rootview.findViewById(R.id.text_departure_trip_adult_passenger_count);
        textDepartureTripAdultTotalPrice = rootview.findViewById(R.id.text_departure_trip_adult_total_price);

        viewInfantTotalPriceDepartureTrip = rootview.findViewById(R.id.view_infant_total_price_departure_trip);
        textDepartureTripInfantPassengerCount = rootview.findViewById(R.id.text_departure_trip_infant_passenger_count);
        textDepartureTripInfantTotalPrice = rootview.findViewById(R.id.text_departure_trip_infant_total_price);

        viewAdultTotalPriceReturnTrip = rootview.findViewById(R.id.view_adult_total_price_return_trip);
        textReturnTripAdultPassengerCount = rootview.findViewById(R.id.text_return_trip_adult_passenger_count);
        textReturnTripAdultTotalPrice = rootview.findViewById(R.id.text_return_trip_adult_total_price);

        viewInfantTotalPriceReturnTrip = rootview.findViewById(R.id.view_infant_total_price_return_trip);
        textReturnTripInfantPassengerCount = rootview.findViewById(R.id.text_return_trip_infant_passenger_count);
        textReturnTripInfantTotalPrice = rootview.findViewById(R.id.text_return_trip_infant_total_price);

        viewDiscountChannelDepartureTrip = rootview.findViewById(R.id.view_discount_channel_departure_trip);
        textLabelDiscountChannelDepartureTrip = rootview.findViewById(R.id.text_label_discount_channel_departure_trip);
        textDiscountChannelDepartureTrip = rootview.findViewById(R.id.text_discount_channel_departure_trip);

        viewDiscountChannelReturnTrip = rootview.findViewById(R.id.view_discount_channel_return_trip);
        textLabelDiscountChannelReturnTrip = rootview.findViewById(R.id.text_label_discount_channel_return_trip);
        textDiscountChannelReturnTrip = rootview.findViewById(R.id.text_discount_channel_return_trip);

        viewExtraFeeDepartureTrip = rootview.findViewById(R.id.view_extra_fee_departure_trip);
        textLabelExtraFeeDepartureTrip = rootview.findViewById(R.id.text_label_extra_fee_departure_trip);
        textExtraFeeDepartureTrip = rootview.findViewById(R.id.text_extra_fee_departure_trip);

        viewExtraFeeReturnTrip = rootview.findViewById(R.id.view_extra_fee_return_trip);
        textLabelExtraFeeReturnTrip = rootview.findViewById(R.id.text_label_extra_fee_return_trip);
        textExtraFeeReturnTrip = rootview.findViewById(R.id.text_extra_fee_return_trip);

        viewDiscountVoucher = rootview.findViewById(R.id.view_discount_voucher);
        textDiscountVoucher = rootview.findViewById(R.id.text_discount_voucher);

        viewSubtotalPrice = rootview.findViewById(R.id.view_subtotal_price);
        textSubtotalPrice = rootview.findViewById(R.id.text_subtotal_price);

        final boolean [] isPriceDetailOpened = {false};

        viewTrainReviewTotalPrice.setOnClickListener(v -> {
            if (!isPriceDetailOpened[0]) {
                isPriceDetailOpened[0] = true;
                textLabelSeeDetail.setText(getResources().getString(R.string.train_review_label_close));
                imageArrow.setImageDrawable(MethodChecker.getDrawable(imageArrow.getContext(),R.drawable.ic_arrow_up_grey));
                containerTrainReviewPriceDetail.setVisibility(View.VISIBLE);
                viewDivider1.setVisibility(VISIBLE);
            } else {
                isPriceDetailOpened[0] = false;
                textLabelSeeDetail.setText(getResources().getString(R.string.train_review_label_see_detail));
                imageArrow.setImageDrawable(MethodChecker.getDrawable(imageArrow.getContext(),R.drawable.ic_arrow_down_grey));
                containerTrainReviewPriceDetail.setVisibility(View.GONE);
                viewDivider1.setVisibility(GONE);
            }
        });
    }

    public void showScheduleTripsPrice(TrainSoftbook trainSoftbook) {
        TrainTrip departureTrip = trainSoftbook.getDepartureTrips().get(0);
        showDepartureTripPrice(departureTrip);

        double totalReturnPrice = 0;
        if (trainSoftbook.getReturnTrips() != null && !trainSoftbook.getReturnTrips().isEmpty()) {
            TrainTrip returnTrip = trainSoftbook.getReturnTrips().get(0);
            totalReturnPrice = returnTrip.getTotalPrice();
            showReturnTripPrice(returnTrip);
        } else {
            viewAdultTotalPriceReturnTrip.setVisibility(View.GONE);
            viewDiscountChannelReturnTrip.setVisibility(GONE);
            viewExtraFeeReturnTrip.setVisibility(GONE);
        }

        totalPrice = departureTrip.getTotalPrice() + totalReturnPrice;

        textTrainReviewTotalPrice.setText(getResources().getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(totalPrice,
                        false, 0).getFormattedString()));
    }

    private void showDepartureTripPrice(TrainTrip departureTrip) {
        String origin = departureTrip.getOrg();
        String destination = departureTrip.getDes();
        int numOfAdultPassenger = departureTrip.getNumOfAdultPassenger();
        int numOfInfantPassenger = departureTrip.getNumOfInfantPassenger();

        textDepartureTripAdultPassengerCount.setText(getResources().getString(R.string.train_review_trip_adult_passenger_count,
                origin, destination, numOfAdultPassenger));
        textDepartureTripAdultTotalPrice.setText(
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalPriceAdult(),
                        false, 0).getFormattedString());

        if (numOfInfantPassenger > 0) {
            viewInfantTotalPriceDepartureTrip.setVisibility(VISIBLE);
            textDepartureTripInfantPassengerCount.setText(getResources().getString(R.string.train_review_trip_infant_passenger_count,
                    origin, destination, numOfInfantPassenger));
            textDepartureTripInfantTotalPrice.setText(
                    CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalPriceInfant(),
                            false, 0).getFormattedString());
        }

        textLabelDiscountChannelDepartureTrip.setText(getResources().getString(R.string.train_review_label_discount_channel,
                origin, destination));
        textDiscountChannelDepartureTrip.setText(
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getDiscount(),
                        false, 0).getFormattedString());

        textLabelExtraFeeDepartureTrip.setText(getResources().getString(R.string.train_review_label_extra_fee,
                origin, destination));
        textExtraFeeDepartureTrip.setText(
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getExtraFee(),
                        false, 0).getFormattedString());
    }

    private void showReturnTripPrice(TrainTrip returnTrip) {
        String origin = returnTrip.getOrg();
        String destination = returnTrip.getDes();
        int numOfAdultPassenger = returnTrip.getNumOfAdultPassenger();
        int numOfInfantPassenger = returnTrip.getNumOfInfantPassenger();

        viewAdultTotalPriceReturnTrip.setVisibility(View.VISIBLE);
        textReturnTripAdultPassengerCount.setText(getResources().getString(R.string.train_review_trip_adult_passenger_count,
                origin, destination, numOfAdultPassenger));
        textReturnTripAdultTotalPrice.setText(
                CurrencyFormatUtil.getThousandSeparatorString(returnTrip.getTotalPriceAdult(),
                        false, 0).getFormattedString());

        if (numOfInfantPassenger > 0) {
            viewInfantTotalPriceReturnTrip.setVisibility(View.VISIBLE);
            textReturnTripInfantPassengerCount.setText(getResources().getString(R.string.train_review_trip_infant_passenger_count,
                    origin, destination, numOfInfantPassenger));
            textReturnTripInfantTotalPrice.setText(
                    CurrencyFormatUtil.getThousandSeparatorString(returnTrip.getTotalPriceInfant(),
                            false, 0).getFormattedString());
        }

        viewDiscountChannelReturnTrip.setVisibility(VISIBLE);
        textLabelDiscountChannelReturnTrip.setText(getResources().getString(R.string.train_review_label_discount_channel,
                origin, destination));
        textDiscountChannelReturnTrip.setText(
                CurrencyFormatUtil.getThousandSeparatorString(returnTrip.getDiscount(),
                        false, 0).getFormattedString());

        viewExtraFeeReturnTrip.setVisibility(VISIBLE);
        textLabelExtraFeeReturnTrip.setText(getResources().getString(R.string.train_review_label_extra_fee,
                origin, destination));
        textExtraFeeReturnTrip.setText(
                CurrencyFormatUtil.getThousandSeparatorString(returnTrip.getExtraFee(),
                        false, 0).getFormattedString());
    }

    public void showNewPriceAfterDiscount(long voucherDiscountAmount) {
        viewDivider2.setVisibility(VISIBLE);

        viewSubtotalPrice.setVisibility(VISIBLE);
        textSubtotalPrice.setText(CurrencyFormatUtil.getThousandSeparatorString(totalPrice,
                false, 0).getFormattedString());

        viewDiscountVoucher.setVisibility(VISIBLE);
        String discountAmount = CurrencyFormatUtil.getThousandSeparatorString(voucherDiscountAmount,
                false, 0).getFormattedString();
        textDiscountVoucher.setText(discountAmount);

        double discountedPrice = totalPrice - voucherDiscountAmount;

        textTrainReviewTotalPrice.setText(getResources().getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(discountedPrice,
                        false, 0).getFormattedString()));
    }

    public void removeDiscount() {
        textTrainReviewTotalPrice.setText(getResources().getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(totalPrice,
                        false, 0).getFormattedString()));

        viewDivider2.setVisibility(GONE);
        viewSubtotalPrice.setVisibility(GONE);
        viewDiscountVoucher.setVisibility(GONE);
    }

}