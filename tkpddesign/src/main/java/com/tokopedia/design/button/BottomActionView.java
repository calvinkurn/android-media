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

    int mAnimState = ANIM_STATE_NONE;

    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_SHOWING = 2;

    private static final Interpolator FAST_OUT_LINEAR_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
    private static final Interpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

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
            label1 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_1);
            icon2Res = styledAttributes.getResourceId(R.styleable.BottomActionView_bav_icon_2, DEFAULT_ICON);
            label2 = styledAttributes.getString(R.styleable.BottomActionView_bav_label_2);
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
        invalidate();
        requestLayout();
    }

    public void setButton1OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton1.setOnClickListener(onClickListener);
    }

    public void setButton2OnClickListener(OnClickListener onClickListener) {
        linearLayoutButton2.setOnClickListener(onClickListener);
    }

    public void hide(){
        if (isOrWillBeHidden()) {
            // We either are or will soon be hidden, skip the call
            return;
        }

        mAnimState = ANIM_STATE_HIDING;

        Animation anim = android.view.animation.AnimationUtils.loadAnimation(
                getContext(), R.anim.design_fab_out);
        anim.setInterpolator(FAST_OUT_LINEAR_IN_INTERPOLATOR);
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimState = ANIM_STATE_NONE;
                BottomActionView.this.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(anim);
    }

    public void show(){
        if (isOrWillBeShown()) {
            // We either are or will soon be visible, skip the call
            return;
        }

        mAnimState = ANIM_STATE_SHOWING;

        this.setVisibility(View.VISIBLE);
        Animation anim = android.view.animation.AnimationUtils.loadAnimation(
                getContext(), R.anim.design_fab_in);
        anim.setDuration(200);
        anim.setInterpolator(LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimState = ANIM_STATE_NONE;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.startAnimation(anim);
    }

    boolean isOrWillBeHidden() {
        if (this.getVisibility() == View.VISIBLE) {
            // If we currently visible, return true if we're animating to be hidden
            return mAnimState == ANIM_STATE_HIDING;
        } else {
            // Otherwise if we're not visible, return true if we're not animating to be shown
            return mAnimState != ANIM_STATE_SHOWING;
        }
    }

    boolean isOrWillBeShown() {
        if (this.getVisibility() != View.VISIBLE) {
            // If we not currently visible, return true if we're animating to be shown
            return mAnimState == ANIM_STATE_SHOWING;
        } else {
            // Otherwise if we're visible, return true if we're not animating to be hidden
            return mAnimState != ANIM_STATE_HIDING;
        }
    }
}
