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
import android.widget.Switch;
import android.widget.TextView;

import com.tokopedia.shop.settings.R;


public class SwitchLabelView extends FrameLayout {
    private String titleText;
    private float titleTextSize;
    private int titleTextStyleValue;
    private TextView titleTextView;
    @ColorInt
    private int titleColorValue;
    private Switch aSwitch;

    private OnSwitchLabelViewListener onSwitchLabelViewListener;
    public interface OnSwitchLabelViewListener{
        void onChecked(boolean isChecked);
    }

    public void setOnSwitchLabelViewListener(OnSwitchLabelViewListener onSwitchLabelViewListener) {
        this.onSwitchLabelViewListener = onSwitchLabelViewListener;
    }

    public SwitchLabelView(Context context) {
        super(context);
        init(null);
    }

    public SwitchLabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SwitchLabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchLabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        applyAttrs(attrs);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_switch_label_view,
                this, true);
        titleTextView = view.findViewById(R.id.text_view_title);
        titleTextView.setText(titleText);
        titleTextView.setTypeface(null, titleTextStyleValue);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        titleTextView.setTextColor(titleColorValue);
        aSwitch = view.findViewById(R.id.switchWidget);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onSwitchLabelViewListener!= null) {
                    onSwitchLabelViewListener.onChecked(isChecked);
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aSwitch.setChecked(!aSwitch.isChecked());
            }
        });
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchLabelView);
        try {
            titleText = styledAttributes.getString(R.styleable.SwitchLabelView_slv_title);
            titleColorValue = styledAttributes.getColor(R.styleable.SwitchLabelView_slv_title_color, ContextCompat.getColor(getContext(), R.color.font_black_primary_70));
            titleTextStyleValue = styledAttributes.getInt(R.styleable.SwitchLabelView_slv_title_text_style, Typeface.NORMAL);
            titleTextSize = styledAttributes.getDimension(R.styleable.SwitchLabelView_slv_title_text_size, getResources().getDimension(R.dimen.sp_16));
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
            titleTextView.setTextColor(ContextCompat.getColor(getContext(), com.tokopedia.design.R.color.font_black_disabled_38));
        }
    }

    public void setChecked(boolean isChecked) {
        aSwitch.setChecked(isChecked);
    }

    public boolean isChecked() {
        return aSwitch.isChecked();
    }
}
