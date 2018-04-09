package com.tokopedia.gm.statistic.view.widget.circleprogress;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.gm.R;

/**
 * Created by hendry on 7/12/2017.
 */

public class DonutProgressLayout extends FrameLayout {
    private TextView tvTitle;
    private TextView tvAmount;
    private CharSequence amountString;
    private CharSequence titleString;
    private float amountTextSize;
    private float titleTextSize;
    private ViewGroup contentView;

    public DonutProgressLayout(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public DonutProgressLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public DonutProgressLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.DonutProgressLayout);
        if (a.hasValue(R.styleable.DonutProgressLayout_donut_pl_title)) {
            titleString = a.getString(R.styleable.DonutProgressLayout_donut_pl_title);
        }
        if (a.hasValue(R.styleable.DonutProgressLayout_donut_pl_amount)) {
            amountString = a.getString(R.styleable.DonutProgressLayout_donut_pl_amount);
        }
        titleTextSize = a.getDimension(R.styleable.DonutProgressLayout_donut_pl_title_text_size, 0);
        amountTextSize = a.getDimension(R.styleable.DonutProgressLayout_donut_pl_amount_text_size, 0);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_donut_progress_layout, this);
        contentView = (ViewGroup) view.findViewById(R.id.frame_content);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvAmount = (TextView) view.findViewById(R.id.tv_amount);
    }

    @Override
    public void addView(View child, int index, final ViewGroup.LayoutParams params) {
        if (child instanceof DonutProgress) {
            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(params);
            flp.gravity = Gravity.CENTER_HORIZONTAL | (flp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK);
            contentView.addView(child, flp);

            contentView.setLayoutParams(flp);
            setUI();
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private void setUI() {
        setAmount(amountString);
        setTitle(titleString);
    }

    public void setAmount(CharSequence amount) {
        amountString = amount;
        if (TextUtils.isEmpty(amount)) {
            tvAmount.setVisibility(View.GONE);
        } else {
            tvAmount.setText(amount);
            if (amountTextSize != 0) {
                tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_PX, amountTextSize);
            }
            tvAmount.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(CharSequence title) {
        titleString = title;
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
            if (titleTextSize != 0) {
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
            }
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    public TextView getTitleTextView() {
        return tvTitle;
    }

    public TextView getAmountTextView() {
        return tvAmount;
    }


}
