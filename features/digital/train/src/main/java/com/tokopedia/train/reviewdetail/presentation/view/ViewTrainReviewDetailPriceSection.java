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
import com.tokopedia.train.scheduledetail.presentation.model.TrainScheduleDetailViewModel;

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

    private TextView textDepartureTripPassengerCount;
    private TextView textDepartureTripPrice;
    private TextView textReturnTripPassengerCount;
    private TextView textReturnTripPrice;
    private LinearLayout viewTotalPriceReturnTrip;

    private LinearLayout viewDiscountChannelDepartureTrip;
    private TextView textLabelDiscountChannelDepartureTrip;
    private TextView textDiscountChannelDepartureTrip;
    private LinearLayout viewDiscountChannelReturnTrip;
    private TextView textLabelDiscountChannelReturnTrip;
    private TextView textDiscountChannelReturnTrip;

    private LinearLayout viewDiscountVoucher;
    private TextView textDiscountVoucher;

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

        textDepartureTripPassengerCount = rootview.findViewById(R.id.text_departure_trip_passenger_count);
        textDepartureTripPrice = rootview.findViewById(R.id.text_departure_trip_price);
        textReturnTripPassengerCount = rootview.findViewById(R.id.text_return_trip_passenger_count);
        textReturnTripPrice = rootview.findViewById(R.id.text_return_trip_price);
        viewTotalPriceReturnTrip = rootview.findViewById(R.id.view_total_price_return_trip);

        viewDiscountChannelDepartureTrip = rootview.findViewById(R.id.view_discount_channel_departure_trip);
        textLabelDiscountChannelDepartureTrip = rootview.findViewById(R.id.text_label_discount_channel_departure_trip);
        textDiscountChannelDepartureTrip = rootview.findViewById(R.id.text_discount_channel_departure_trip);
        viewDiscountChannelReturnTrip = rootview.findViewById(R.id.view_discount_channel_return_trip);
        textLabelDiscountChannelReturnTrip = rootview.findViewById(R.id.text_label_discount_channel_return_trip);
        textDiscountChannelReturnTrip = rootview.findViewById(R.id.text_discount_channel_return_trip);
        viewDiscountVoucher = rootview.findViewById(R.id.view_discount_voucher);
        textDiscountVoucher = rootview.findViewById(R.id.text_discount_voucher);

        final boolean [] isPriceDetailOpened = {false};

        viewTrainReviewTotalPrice.setOnClickListener(v -> {
            if (!isPriceDetailOpened[0]) {
                isPriceDetailOpened[0] = true;
                textLabelSeeDetail.setText(getResources().getString(R.string.train_review_label_close));
                imageArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_grey));
                containerTrainReviewPriceDetail.setVisibility(View.VISIBLE);
                viewDivider1.setVisibility(VISIBLE);
            } else {
                isPriceDetailOpened[0] = false;
                textLabelSeeDetail.setText(getResources().getString(R.string.train_review_label_see_detail));
                imageArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_grey));
                containerTrainReviewPriceDetail.setVisibility(View.GONE);
                viewDivider1.setVisibility(GONE);
            }
        });
    }

    public void showScheduleTripsPrice(TrainSoftbook trainSoftbook, TrainScheduleDetailViewModel departureTrip, TrainScheduleDetailViewModel returnTrip) {
        textTrainReviewTotalPrice.setText(getResources().getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalPrice(),
                        false, 0).getFormattedString()));

        showDepartureTripPrice(trainSoftbook, departureTrip);

        if (returnTrip != null) {
            showReturnTripPrice(trainSoftbook, returnTrip);
        } else {
            viewTotalPriceReturnTrip.setVisibility(View.GONE);
            viewDiscountChannelReturnTrip.setVisibility(GONE);
        }
    }

    private void showDepartureTripPrice(TrainSoftbook trainSoftbook, TrainScheduleDetailViewModel departureTrip) {
        textDepartureTripPassengerCount.setText(getResources().getString(R.string.train_review_trip_passenger_count,
                departureTrip.getOriginStationCode(), departureTrip.getDestinationStationCode(),
                departureTrip.getNumOfAdultPassenger()));
        textDepartureTripPrice.setText(
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalAdultFare(),
                        false, 0).getFormattedString());

        textLabelDiscountChannelDepartureTrip.setText(getResources().getString(R.string.train_review_label_discount_channel,
                departureTrip.getOriginStationCode(), departureTrip.getDestinationStationCode()));
        textDiscountChannelDepartureTrip.setText(
                CurrencyFormatUtil.getThousandSeparatorString(trainSoftbook.getDepartureTrips().get(0).getDiscount(),
                        false, 0).getFormattedString());
    }

    private void showReturnTripPrice(TrainSoftbook trainSoftbook, TrainScheduleDetailViewModel returnTrip) {
        viewTotalPriceReturnTrip.setVisibility(View.VISIBLE);
        textReturnTripPassengerCount.setText(getResources().getString(R.string.train_review_trip_passenger_count,
                returnTrip.getOriginStationCode(), returnTrip.getDestinationStationCode(),
                returnTrip.getNumOfAdultPassenger()));
        textReturnTripPrice.setText(
                CurrencyFormatUtil.getThousandSeparatorString(returnTrip.getTotalAdultFare(),
                        false, 0).getFormattedString());

        viewDiscountChannelReturnTrip.setVisibility(VISIBLE);
        textLabelDiscountChannelReturnTrip.setText(getResources().getString(R.string.train_review_label_discount_channel,
                returnTrip.getOriginStationCode(), returnTrip.getDestinationStationCode()));
        textDiscountChannelReturnTrip.setText(
                CurrencyFormatUtil.getThousandSeparatorString(trainSoftbook.getReturnTrips().get(0).getDiscount(),
                        false, 0).getFormattedString());
    }

    public void showNewPriceAfterDiscount(long voucherDiscountAmount) {
        viewDivider2.setVisibility(VISIBLE);
        viewDiscountVoucher.setVisibility(VISIBLE);
        String discountAmountInRp = CurrencyFormatUtil.getThousandSeparatorString(voucherDiscountAmount,
                false, 0).getFormattedString();
        textDiscountVoucher.setText(discountAmountInRp);
    }

}