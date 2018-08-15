package com.tokopedia.shop.settings.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.shop.settings.R;

public class RadioButtonLabelView extends FrameLayout {

    private String titleText;
    private float titleTextSize;
    private int titleTextStyleValue;
    private TextView titleTextView;
    @ColorInt
    private int titleColorValue;
    private RadioButton radioButton;

    private OnRadioButtonLabelViewListener onRadioButtonLabelViewListener;
    public interface OnRadioButtonLabelViewListener{
        void onChecked(boolean isChecked);
    }

    public void setOnRadioButtonLabelViewListener(OnRadioButtonLabelViewListener onRadioButtonLabelViewListener) {
        this.onRadioButtonLabelViewListener = onRadioButtonLabelViewListener;
    }

    public RadioButtonLabelView(Context context) {
        super(context);
        init(null);
    }

    public RadioButtonLabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RadioButtonLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RadioButtonLabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        applyAttrs(attrs);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_label_view_radio_button,
                this, true);
        titleTextView = view.findViewById(R.id.text_view_title);
        titleTextView.setText(titleText);
        titleTextView.setTypeface(null, titleTextStyleValue);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleTextView.setTextColor(titleColorValue);
        radioButton = view.findViewById(R.id.radio_button);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onRadioButtonLabelViewListener!= null) {
                    onRadioButtonLabelViewListener.onChecked(isChecked);
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radioButton.isChecked()) {
                    radioButton.setChecked(true);
                }
            }
        });
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.RadioButtonLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.RadioButtonLabelView_rblv_title);
            titleColorValue = styledAttributes.getColor(R.styleable.RadioButtonLabelView_rblv_title_color, ContextCompat.getColor(getContext(), R.color.font_black_primary_70));
            titleTextStyleValue = styledAttributes.getInt(R.styleable.RadioButtonLabelView_rblv_title_text_style, Typeface.NORMAL);
            titleTextSize = styledAttributes.getDimension(R.styleable.RadioButtonLabelView_rblv_title_text_size, getResources().getDimension(R.dimen.sp_16));
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        setClickable(enabled);
        if (enabled) {
            titleTextView.setTextColor(titleColorValue);
        } else {
            titleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.font_black_disabled_38));
        }
    }

    public void setChecked(boolean isChecked) {
        radioButton.setChecked(isChecked);
    }

    public boolean isChecked() {
        return radioButton.isChecked();
    }


}
