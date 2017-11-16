package com.tokopedia.design.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by nathan on 10/3/17.
 */

public class BottomActionView extends BaseCustomView {

    @DrawableRes
    public static final int DEFAULT_ICON = R.drawable.ic_search_icon;

    private View linearLayoutButton1;
    private View linearLayoutButton2;

    private String label1;
    @DrawableRes
    private int icon1Res;
    private String label2;
    @DrawableRes
    private int icon2Res;
    private View vMark1;
    private View vMark2;

    public BottomActionView(Context context) {
        super(context);
        init();
    }

    public BottomActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BottomActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.BottomActionView);
        try {
            icon1Res = styledAttributes.getResourceId(R.styleable.BottomActionView_bav_icon_1, DEFAULT_ICON);
            label1 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_1);
            icon2Res = styledAttributes.getResourceId(R.styleable.BottomActionView_bav_icon_2, DEFAULT_ICON);
            label2 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_2);
        } finally {
            styledAttributes.recycle();
        }
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_bottom_action_view, this);
        linearLayoutButton1 = view.findViewById(R.id.linear_layout_button_1);
        ImageView icon1ImageView = (ImageView) linearLayoutButton1.findViewById(R.id.image_view_icon_1);
        TextView label1textView = (TextView) linearLayoutButton1.findViewById(R.id.text_view_label_1);
        vMark1 = linearLayoutButton1.findViewById(R.id.v_mark_1);

        View separatorView = (View) view.findViewById(R.id.view_separator);

        linearLayoutButton2 = view.findViewById(R.id.linear_layout_button_2);
        ImageView icon2ImageView = (ImageView) linearLayoutButton2.findViewById(R.id.image_view_icon_2);
        TextView label2textView = (TextView) linearLayoutButton2.findViewById(R.id.text_view_label_2);
        vMark2 = linearLayoutButton2.findViewById(R.id.v_mark_2);

        icon1ImageView.setImageResource(icon1Res);
        if (!TextUtils.isEmpty(label1)) {
            label1textView.setText(label1);
        }
        icon2ImageView.setImageResource(icon2Res);
        if (!TextUtils.isEmpty(label2)) {
            label2textView.setText(label2);
        }
        if (!TextUtils.isEmpty(label2)) {
            separatorView.setVisibility(View.VISIBLE);
            linearLayoutButton2.setVisibility(View.VISIBLE);
        } else {
            separatorView.setVisibility(View.GONE);
            linearLayoutButton2.setVisibility(View.VISIBLE);
        }
    }

    public void setMarkLeft(boolean isVisible) {
        vMark1.setVisibility(isVisible? View.VISIBLE: View.INVISIBLE);
    }

    public void setMarkRight(boolean isVisible) {
        vMark2.setVisibility(isVisible? View.VISIBLE: View.INVISIBLE);
    }

    public void setButton1OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton1.setOnClickListener(onClickListener);
    }

    public void setButton2OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton2.setOnClickListener(onClickListener);
    }

}
