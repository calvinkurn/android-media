package com.tokopedia.gm.statistic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.gm.R;

/**
 * Created by User on 7/19/2017.
 */

public class CircleTextView extends FrameLayout {
    private ImageView ivIcon;
    private TextView tvText;
    private TextView tvValue;

    private int circleColor;
    private CharSequence text;
    private CharSequence value;

    public CircleTextView(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleTextView);
        circleColor = a.getColor(R.styleable.CircleTextView_circle_color, 0);
        text = a.getString(R.styleable.CircleTextView_text);
        value = a.getString(R.styleable.CircleTextView_value);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_circle_text_view, this);
        ivIcon = (ImageView) view.findViewById(R.id.iv_circle);
        tvText = (TextView) view.findViewById(R.id.tv_text);
        tvValue = (TextView) view.findViewById(R.id.tv_value);
        setUI();
        setAddStatesFromChildren(true);
    }

    private void setUI(){
        setCircleColor(circleColor);
        setText(text);
        setValue(value);
    }

    public void setCircleColor(int circleColor) {
        if (circleColor == 0) {
            ivIcon.setVisibility(View.INVISIBLE);
            return;
        }
        this.circleColor = circleColor;
        ColorFilter cf = new PorterDuffColorFilter(circleColor, PorterDuff.Mode.SRC_ATOP);
        ivIcon.clearColorFilter();
        ivIcon.setColorFilter(cf);
        ivIcon.setVisibility(View.VISIBLE);
    }

    public void setText(CharSequence text) {
        this.text = text;
        this.tvText.setText(text);
    }

    public void setValue(CharSequence value) {
        this.value = value;
        this.tvValue.setText(value);
    }
}
