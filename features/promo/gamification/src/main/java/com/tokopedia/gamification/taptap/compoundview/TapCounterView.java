package com.tokopedia.gamification.taptap.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.gamification.R;

public class TapCounterView extends FrameLayout {

    private int requiredTap;

    private int resTextStrId;
    private Context mContext;

    private TextView tapCountTextView;
    private OnTapClickListener listener;


    public TapCounterView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public TapCounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public TapCounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_tap_view, null);
        this.addView(view);
        tapCountTextView = view.findViewById(R.id.tv_tapCount);
        setVisibility(INVISIBLE);
    }

    public void initialize(int requiredTap, int resId, OnTapClickListener listener) {
        this.requiredTap = requiredTap;
        resTextStrId = resId;
        this.listener = listener;
        tapCountTextView.setText(mContext.getString(resId, requiredTap));
        countViewDisplayWithAnimation();
    }

    public void onTap() {
        if (requiredTap == 1)
            hideCountViewAnimate();
        else {
            requiredTap--;
            animateTextView();
        }
    }

    private void animateTextView() {
        this.clearAnimation();
        tapCountTextView.clearAnimation();
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0, 1f, 0f,
                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scaleAnimation.setDuration(200);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        tapCountTextView.setAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tapCountTextView.setText(mContext.getString(resTextStrId, requiredTap));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void countViewDisplayWithAnimation() {
        tapCountTextView.setText(mContext.getString(resTextStrId, requiredTap));
        setVisibility(VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f,
                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scaleAnimation.setDuration(200);
        this.setAnimation(scaleAnimation);
    }

    private void hideCountViewAnimate() {
        this.clearAnimation();
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0, 1f, 0f,
                Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scaleAnimation.setDuration(200);
        this.setAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(listener!= null) {
                    listener.onTapCountEnds();
                    hideView();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void hideView() {
        this.clearAnimation();
        setVisibility(INVISIBLE);
    }

    public interface OnTapClickListener{
        void onTapCountEnds();
    }


}



