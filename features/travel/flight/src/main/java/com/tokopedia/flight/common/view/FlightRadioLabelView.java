package com.tokopedia.flight.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tokopedia.flight.R;
import com.tokopedia.unifycomponents.BaseCustomView;

public class FlightRadioLabelView extends BaseCustomView {
    private TextView titleTextView;
    private SwitchCompat toggleSwitchCompat;
    private OnCheckChangeListener listener;
    private String title;

    public interface OnCheckChangeListener {
        void onCheckedChanged(Boolean checked);
    }

    public FlightRadioLabelView(@NonNull Context context) {
        super(context);
        init();
    }

    public FlightRadioLabelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FlightRadioLabelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.FlightRadioLabelView);
        try {
            title = styledAttributes.getString(R.styleable.FlightRadioLabelView_frlv_title);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), com.tokopedia.flight.R.layout.widget_flight_radio_label_view, this);
        titleTextView = (TextView) view.findViewById(com.tokopedia.flight.R.id.title);
        toggleSwitchCompat = (SwitchCompat) view.findViewById(com.tokopedia.flight.R.id.toggle);
        toggleSwitchCompat.setThumbDrawable(ContextCompat.getDrawable(getContext(), com.tokopedia.flight.R.drawable.ic_flight_uncheck_thumb));
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleSwitchCompat.setThumbDrawable(ContextCompat.getDrawable(getContext(), com.tokopedia.flight.R.drawable.ic_flight_check_thumb));
                } else {
                    toggleSwitchCompat.setThumbDrawable(ContextCompat.getDrawable(getContext(), com.tokopedia.flight.R.drawable.ic_flight_uncheck_thumb));
                }
                if (listener != null) {
                    listener.onCheckedChanged(isChecked);
                }
            }
        };
        toggleSwitchCompat.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        titleTextView.setText(title);
    }

    public void setListener(OnCheckChangeListener listener) {
        this.listener = listener;
    }

    public void setChecked(boolean checked) {
        if (checked) {
            toggleSwitchCompat.setThumbDrawable(ContextCompat.getDrawable(getContext(), com.tokopedia.flight.R.drawable.ic_flight_check_thumb));
        } else {
            toggleSwitchCompat.setThumbDrawable(ContextCompat.getDrawable(getContext(), com.tokopedia.flight.R.drawable.ic_flight_uncheck_thumb));
        }
        toggleSwitchCompat.setChecked(checked);
    }
}
