package com.tokopedia.train.search.presentation.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/15/18.
 */

public class TrainSearchViewHolder extends AbstractViewHolder<TrainScheduleViewModel> {

    private static final int OUT_OF_STOCK = 0;
    private static final int LIMIT_TICKET = 10;

    @LayoutRes
    public static int LAYOUT = R.layout.item_train_schedule;

    private LinearLayout flagItemLayout;
    private TextView trainNameTv;
    private TextView classNameTv;
    private TextView originCodeTv;
    private TextView departureTimeTv;
    private TextView destinationCodeTv;
    private TextView arrivalTimeTv;
    private TextView durationTv;
    private TextView availabilitySeatTv;
    private TextView priceTv;
    private TextView detailScheduleTv;
    private TrainSearchAdapterTypeFactory.OnTrainSearchListener listener;
    private Context context;
    private AppCompatImageView imageRound1;
    private AppCompatImageView imageRound3;

    public TrainSearchViewHolder(View itemView, TrainSearchAdapterTypeFactory.OnTrainSearchListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        flagItemLayout = itemView.findViewById(R.id.flag_item);
        trainNameTv = itemView.findViewById(R.id.train_name);
        classNameTv = itemView.findViewById(R.id.class_name);
        originCodeTv = itemView.findViewById(R.id.origin_code);
        departureTimeTv = itemView.findViewById(R.id.departure_time);
        destinationCodeTv = itemView.findViewById(R.id.destination_code);
        arrivalTimeTv = itemView.findViewById(R.id.arrival_time);
        durationTv = itemView.findViewById(R.id.duration);
        availabilitySeatTv = itemView.findViewById(R.id.availability_seat);
        priceTv = itemView.findViewById(R.id.price);
        detailScheduleTv = itemView.findViewById(R.id.tap_for_details);
        imageRound1 = itemView.findViewById(R.id.image1);
        imageRound3 = itemView.findViewById(R.id.image3);
        this.listener = listener;
    }

    @Override
    public void bind(TrainScheduleViewModel trainScheduleViewModel) {
        setDataSchedule(trainScheduleViewModel);
        setColorTextItem(trainScheduleViewModel.getAvailableSeat() == OUT_OF_STOCK);
    }

    private void setColorTextItem(boolean isScheduleOutOfStock) {
        trainNameTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.font_black_primary_70));
        classNameTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.font_black_primary_70));
        originCodeTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.font_black_primary_70));
        departureTimeTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.font_black_primary_70));
        destinationCodeTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.font_black_primary_70));
        arrivalTimeTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.font_black_primary_70));
        priceTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.font_black_primary_70));
        detailScheduleTv.setTextColor(ContextCompat.getColor(context, isScheduleOutOfStock ?
                R.color.font_black_disabled_38 : R.color.tkpd_main_green));
        imageRound1.setImageResource(isScheduleOutOfStock ?
                R.drawable.ic_round_fill_grey : R.drawable.ic_round_fill_green);
        imageRound3.setImageResource(isScheduleOutOfStock ?
                R.drawable.ic_round_fill_grey : R.drawable.ic_round_fill_green);
    }

    private void setDataSchedule(TrainScheduleViewModel trainScheduleViewModel) {
        setFlagTrainSchedule(trainScheduleViewModel.getAvailableSeat() == OUT_OF_STOCK,
                trainScheduleViewModel.isFastestFlag(),
                trainScheduleViewModel.isCheapestFlag());
        trainNameTv.setText(trainScheduleViewModel.getTrainName());
        classNameTv.setText(trainScheduleViewModel.getDisplayClass() + " (" +
                trainScheduleViewModel.getClassTrain() + ")");
        originCodeTv.setText(trainScheduleViewModel.getOrigin());
        departureTimeTv.setText(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getDepartureTimestamp()));
        destinationCodeTv.setText(trainScheduleViewModel.getDestination());
        arrivalTimeTv.setText(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getArrivalTimestamp()));
        durationTv.setText(trainScheduleViewModel.getDisplayDuration());

        setAvailabilitySeat(trainScheduleViewModel.getAvailableSeat());

        priceTv.setText(trainScheduleViewModel.getDisplayAdultFare());
        detailScheduleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDetailClicked(trainScheduleViewModel, getAdapterPosition());
            }
        });
    }

    private void setFlagTrainSchedule(boolean isOutOfStock, boolean isFastestFlag, boolean isCheapestFlag) {
        clearHolder(flagItemLayout);
        flagItemLayout.setVisibility(isFastestFlag || isCheapestFlag ? View.VISIBLE : View.GONE);
        if (isOutOfStock) {
            setContentFlag(isCheapestFlag ? getString(R.string.train_search_cheapest_label) :
                            getString(R.string.train_search_fastest_label), R.color.font_white_disabled_38,
                    R.color.font_black_disabled_38, R.color.font_black_disabled_38);
        } else {
            if (isCheapestFlag) {
                setContentFlag(getString(R.string.train_search_cheapest_label), R.color.light_green, R.color.tkpd_main_green,
                        R.color.tkpd_main_green);
            }
            if (isFastestFlag) {
                setContentFlag(getString(R.string.train_search_fastest_label), R.color.light_orange, R.color.orange,
                        R.color.deep_orange);
            }
        }
    }

    private void setContentFlag(String title, int colorInside, int colorOutside, int colorText) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flag_layout, flagItemLayout, false);
        TextView flagItem = view.findViewById(R.id.flag_name);
        LinearLayout flagLayout = view.findViewById(R.id.layout_flag);

        flagItem.setText(title);
        flagItem.setTextColor(ContextCompat.getColor(context, colorText));
        flagLayout.setBackgroundResource(R.drawable.bg_round_corner_custom);

        LayerDrawable layerDrawable = (LayerDrawable) flagLayout.getBackground();
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.layer_inside);
        gradientDrawable.setColor(ContextCompat.getColor(context, colorInside));

        LayerDrawable layerDrawable1 = (LayerDrawable) flagLayout.getBackground();
        GradientDrawable gradientDrawable1 = (GradientDrawable) layerDrawable1.findDrawableByLayerId(R.id.layer_outside);
        gradientDrawable1.setStroke(1, ContextCompat.getColor(context, colorOutside));

        if (flagItemLayout.getChildCount() > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flagItem.getLayoutParams();
            layoutParams.setMargins(10, 0, 0, 0);
            flagLayout.setLayoutParams(layoutParams);
        }
        flagItemLayout.addView(view);
    }

    private void setAvailabilitySeat(int availableSeat) {
        if (availableSeat > LIMIT_TICKET) {
            availabilitySeatTv.setVisibility(View.INVISIBLE);
        } else {
            availabilitySeatTv.setVisibility(View.VISIBLE);
            if (availableSeat == OUT_OF_STOCK) {
                availabilitySeatTv.setText(getString(R.string.train_search_seat_full_label));
                setColorTextDisable(availabilitySeatTv);
            } else if (availableSeat > OUT_OF_STOCK) {
                availabilitySeatTv.setText(String.format(getString(R.string.train_search_remains_seat_label), availableSeat));
                setColorTextEnable(availabilitySeatTv, R.color.colorRed);
            }
        }
    }

    private void setColorTextDisable(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.font_black_disabled_38));
    }

    private void setColorTextEnable(TextView textView, int colorText) {
        textView.setTextColor(ContextCompat.getColor(context, colorText));
    }

    private void clearHolder(LinearLayout linearLayout) {
        if (linearLayout.getChildCount() > 0) {
            linearLayout.removeAllViews();
        }
    }
}
