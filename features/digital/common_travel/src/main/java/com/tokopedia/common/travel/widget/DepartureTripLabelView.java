package com.tokopedia.common.travel.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.common.travel.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nabillasabbaha on 29/08/18.
 */
public class DepartureTripLabelView extends BaseCustomView {

    private static final String PNG_EXTENSION = "png";
    private static final String SVG_EXTENSION = "xml";

    private TextView tvDestination;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvPrice;
    private AppCompatImageView imageDeparture;
    private Drawable icon;

    public DepartureTripLabelView(@NonNull Context context) {
        super(context);
        init();
    }

    public DepartureTripLabelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DepartureTripLabelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_departure_trip_label, this);
        tvDestination = view.findViewById(R.id.tv_destination_departure_header);
        tvName = view.findViewById(R.id.tv_name_departure_header);
        tvTime = view.findViewById(R.id.tv_time_departure_header);
        tvPrice = view.findViewById(R.id.tv_price_departure_header);
        imageDeparture = view.findViewById(R.id.image_departure_header);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DepartureLabelView);

        // get resource full name, to use later for checking is it png or vector
        TypedValue value = new TypedValue();
        getContext().getResources().getValue(styledAttributes.getResourceId(R.styleable.DepartureLabelView_dlv_icon, R.drawable.ic_travel_flight), value, true);

        try {
            if (value.string.toString().contains(SVG_EXTENSION)) {
                icon = VectorDrawableCompat.create(getContext().getResources(), styledAttributes.getResourceId(R.styleable.DepartureLabelView_dlv_icon, R.drawable.ic_travel_flight), getContext().getTheme());
            } else if (value.string.toString().contains(PNG_EXTENSION)) {
                icon = styledAttributes.getDrawable(R.styleable.DepartureLabelView_dlv_icon);
            }
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (icon != null) {
            imageDeparture.setImageDrawable(icon);
        }
    }

    public void setValueDestination(String destination) {
        tvDestination.setText(destination);
    }

    public void setValueName(String name) {
        tvName.setText(name);
    }

    public void setValueTime(String time) {
        tvTime.setText(time);
    }

    public void setValuePrice(String price) {
        tvPrice.setText(Html.fromHtml(String.format(getResources().getString(
                R.string.travel_departure_trip_price_value), price)));
    }
}
