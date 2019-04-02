package com.tokopedia.gm.statistic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.gm.R;
import com.tokopedia.gm.statistic.constant.GMStatConstant;
import com.tokopedia.seller.common.utils.KMNumbers;

/**
 * Created by User on 7/10/2017.
 */

public class ArrowPercentageView extends FrameLayout {
    private double percentage = GMStatConstant.NO_DATA_AVAILABLE;
    private float textSize;

    private ImageView ivArrowIcon;
    private TextView tvPercentage;
    private int downDrawableSrc = R.drawable.ic_rectangle_down;
    private int upDrawableSrc = R.drawable.ic_rectangle_up;
    private int stagnantDrawableSrc = 0;
    private int redColor = R.color.tkpd_main_orange;
    private int greenColor = R.color.tkpd_main_green;
    private int greyColor = R.color.grey_400;
    private int noDataRes = R.string.gm_statistic_no_data;

    public ArrowPercentageView(Context context) {
        super(context);
        apply(null, 0);
        init();
    }

    public ArrowPercentageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(attrs, 0);
        init();
    }

    public ArrowPercentageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArrowPercentageView);
        if (a.hasValue(R.styleable.ArrowPercentageView_arrow_percentage_value)) {
            percentage = a.getFloat(R.styleable.ArrowPercentageView_arrow_percentage_value, 0);
        }
        textSize = a.getDimension(R.styleable.ArrowPercentageView_arrow_percentage_text_size, 0);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_gm_percentage, this);
        ivArrowIcon = (ImageView) view.findViewById(R.id.iv_arrow_icon);
        tvPercentage = (TextView) view.findViewById(R.id.tv_percentage);
        if (textSize > 0) {
            tvPercentage.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        setUIPercentage();
        setAddStatesFromChildren(true);
    }

    private void setUIPercentage(){
        if (percentage == GMStatConstant.NO_DATA_AVAILABLE) {
            setVisibility(View.INVISIBLE);
        } else {
            setVisibility(View.VISIBLE);
            if (percentage < 0) {
                ivArrowIcon.setImageResource(downDrawableSrc);
                tvPercentage.setTextColor(ContextCompat.getColor(getContext(), redColor));
            } else if (percentage > 0) {
                ivArrowIcon.setImageResource(upDrawableSrc);
                tvPercentage.setTextColor(ContextCompat.getColor(getContext(), greenColor));
            } else if (percentage == 0) { // percentage is 0
                ivArrowIcon.setImageResource(upDrawableSrc);
                tvPercentage.setTextColor(ContextCompat.getColor(getContext(), greenColor));
            }
            tvPercentage.setText(KMNumbers.formatToPercentString(percentage).replace("-", ""));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    // input percentage 1 for 100%
    public void setPercentage(double percentage){
        this.percentage = percentage;
        setUIPercentage();
    }

    public void setNoDataPercentage(){
        setPercentage(GMStatConstant.NO_DATA_AVAILABLE);
    }

}
