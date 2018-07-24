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

    private View viewDivider;

    private TextView textDepartureTripPassengerCount;
    private TextView textDepartureTripPrice;
    private TextView textReturnTripPassengerCount;
    private TextView textReturnTripPrice;
    private LinearLayout viewTotalPriceReturnTrip;

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

        viewDivider = rootview.findViewById(R.id.view_divider);

        textDepartureTripPassengerCount = rootview.findViewById(R.id.text_departure_trip_passenger_count);
        textDepartureTripPrice = rootview.findViewById(R.id.text_departure_trip_price);
        textReturnTripPassengerCount = rootview.findViewById(R.id.text_return_trip_passenger_count);
        textReturnTripPrice = rootview.findViewById(R.id.text_return_trip_price);
        viewTotalPriceReturnTrip = rootview.findViewById(R.id.view_total_price_return_trip);

        final boolean [] isPriceDetailOpened = {false};

        viewTrainReviewTotalPrice.setOnClickListener(v -> {
            if (!isPriceDetailOpened[0]) {
                isPriceDetailOpened[0] = true;
                textLabelSeeDetail.setText(getResources().getString(R.string.train_review_label_close));
                imageArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_grey));
                containerTrainReviewPriceDetail.setVisibility(View.VISIBLE);
                viewDivider.setVisibility(VISIBLE);
            } else {
                isPriceDetailOpened[0] = false;
                textLabelSeeDetail.setText(getResources().getString(R.string.train_review_label_see_detail));
                imageArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_grey));
                containerTrainReviewPriceDetail.setVisibility(View.GONE);
                viewDivider.setVisibility(GONE);
            }
        });
    }

    public void showScheduleTripsPrice(TrainScheduleDetailViewModel departureTrip, TrainScheduleDetailViewModel returnTrip) {
        textTrainReviewTotalPrice.setText(getResources().getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalPrice(),
                        false, 0).getFormattedString()));

        textDepartureTripPassengerCount.setText(getResources().getString(R.string.train_review_trip_passenger_count,
                departureTrip.getOriginStationCode(), departureTrip.getDestinationStationCode(),
                departureTrip.getNumOfAdultPassenger()));
        textDepartureTripPrice.setText(getResources().getString(R.string.train_label_currency,
                CurrencyFormatUtil.getThousandSeparatorString(departureTrip.getTotalAdultFare(),
                        false, 0).getFormattedString()));

        if (returnTrip != null) {
            viewTotalPriceReturnTrip.setVisibility(View.VISIBLE);
            textReturnTripPassengerCount.setText(getResources().getString(R.string.train_review_trip_passenger_count,
                    returnTrip.getOriginStationCode(), returnTrip.getDestinationStationCode(),
                    returnTrip.getNumOfAdultPassenger()));
            textReturnTripPrice.setText(getResources().getString(R.string.train_label_currency,
                    CurrencyFormatUtil.getThousandSeparatorString(returnTrip.getTotalAdultFare(),
                            false, 0).getFormattedString()));
        } else {
            viewTotalPriceReturnTrip.setVisibility(View.GONE);
        }
    }

    public void showNewPriceAfterDiscount(long voucherDiscountAmount) {
//        this.holderVoucherDiscount.setVisibility(VISIBLE);
//        this.voucherDiscount = voucherDiscount;
//        tvDiscount.setText(getStringIdrFormat((double) this.voucherDiscount));
//        long totalPrice = pricePlain - voucherDiscount;
//        tvSubTotalPrice.setText(getStringIdrFormat((double) totalPrice));
    }

}
