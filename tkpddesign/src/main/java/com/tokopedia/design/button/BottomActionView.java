package com.tokopedia.design.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
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

    private LinearLayout linearLayoutButton1;
    private ImageView icon1ImageView;
    private TextView label1textView;

    private View separatorView;

    private LinearLayout linearLayoutButton2;
    private ImageView icon2ImageView;
    private TextView label2textView;

    private String label1;
    @DrawableRes
    private int icon1Res;
    private String label2;
    @DrawableRes
    private int icon2Res;
    private boolean isBav1Display, isBav2Display;

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
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.BottomActionView);
        try {
            icon1Res = styledAttributes.getResourceId(R.styleable.BottomActionView_bav_icon_1, DEFAULT_ICON);
            isBav1Display = styledAttributes.getBoolean(R.styleable.BottomActionView_bav_1_display, false);
            label1 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_1);
            icon2Res = styledAttributes.getResourceId(R.styleable.BottomActionView_bav_icon_2, DEFAULT_ICON);
            label2 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_2);
            isBav2Display = styledAttributes.getBoolean(R.styleable.BottomActionView_bav_2_display, false);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_bottom_action_view, this);
        linearLayoutButton1 = (LinearLayout) view.findViewById(R.id.linear_layout_button_1);
        icon1ImageView = (ImageView) view.findViewById(R.id.image_view_icon_1);
        label1textView = (TextView) view.findViewById(R.id.text_view_label_1);
        separatorView = (View) view.findViewById(R.id.view_separator);
        linearLayoutButton2 = (LinearLayout) view.findViewById(R.id.linear_layout_button_2);
        icon2ImageView = (ImageView) view.findViewById(R.id.image_view_icon_2);
        label2textView = (TextView) view.findViewById(R.id.text_view_label_2);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
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
        if(isBav2Display){
            linearLayoutButton2.setVisibility(VISIBLE);
        }else{
            linearLayoutButton2.setVisibility(GONE);
        }

        if(isBav1Display){
            linearLayoutButton1.setVisibility(VISIBLE);
        }else{
            linearLayoutButton1.setVisibility(GONE);
        }
        if((isBav1Display ? 1 : 0) + (isBav2Display? 1 : 0) == 1){
            separatorView.setVisibility(View.GONE);
        }
        invalidate();
        requestLayout();
    }

    public void setButton1OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton1.setOnClickListener(onClickListener);
    }

    public void setButton2OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton2.setOnClickListener(onClickListener);
    }
}
