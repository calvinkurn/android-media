package com.tokopedia.tokopoints.view.customview;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

public class SwipeCardView extends FrameLayout implements View.OnTouchListener {

    private final static int MIN_SWIPE_AMOUNT_PX = 10;
    private final static int CONTAINER_TEXT = 0;
    private final static int CONTAINER_CHECK = 1;
    private final static int CONTAINER_CODE = 2;
    private final static int DELAY_SHOW_CHECK_MS = 150;
    private final static int DELAY_BACK_MS = 250;

    private TextView mTextSwipeTitle;
    private RelativeLayout mTouchView;
    private ImageButton mSwipeIcon;
    private View mView;
    private int mMinWidth = -1;
    private boolean isSwipeEnable = true;
    private ViewFlipper mCouponContainer;
    private TextView mTextCoupon;
    private TextView mBtnCopyCode;
    private OnSwipeListener mOnSwipeListener;

    public SwipeCardView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SwipeCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SwipeCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mTouchView.getLayoutParams();
        mMinWidth = params.width;
        mTouchView.setOnTouchListener(this);

        mBtnCopyCode.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(CommonConstant.CLIPBOARD_COUPON_CODE, mTextCoupon.getText().toString());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), getResources().getString(R.string.tp_mes_copy_code), Toast.LENGTH_LONG).show();
            }

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                    AnalyticsTrackerUtil.ActionKeys.COPY_CODE,
                    mTextCoupon.getText().toString());
        });
    }

    private void initView(Context context) {
        mView = View.inflate(context, R.layout.tp_layout_swipe, this);
        mTextSwipeTitle = mView.findViewById(R.id.text_swipe);
        mTouchView = mView.findViewById(R.id.rel_touch_view);
        mSwipeIcon = mView.findViewById(R.id.btn_swipe_icon);
        mCouponContainer = mView.findViewById(R.id.container_inner);
        mTextCoupon = mView.findViewById(R.id.text_code);
        mBtnCopyCode = mView.findViewById(R.id.btn_copy_code);
    }

    float mPreviousX;
    float x1;

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (!isSwipeEnable) {
            return false;
        }

        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                this.getParent().requestDisallowInterceptTouchEvent(true);
                x1 = event.getX();
            }
            case MotionEvent.ACTION_MOVE: {
                this.getParent().requestDisallowInterceptTouchEvent(true);
                float dx = x - mPreviousX;

                if (event.getX() > mMinWidth
                        && isLeftSwipe(dx)
                        && event.getX() < getMaxSwipeWidth()) {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mTouchView.getLayoutParams();
                    layoutParams.width = (int) event.getX();
                    mTouchView.setLayoutParams(layoutParams);
                    if (event.getX() > getMaxSwipeWidth() * .3) {
                        mTextSwipeTitle.setTextColor(Color.WHITE);
                        mSwipeIcon.setVisibility(GONE);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                this.getParent().requestDisallowInterceptTouchEvent(false);

                if (Math.abs(x1 - event.getX()) > MIN_SWIPE_AMOUNT_PX) {
                    if (event.getX() > getMaxSwipeWidth() * .75) {
                        isSwipeEnable = false;
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mTouchView.getLayoutParams();
                        ValueAnimator anim = ValueAnimator.ofInt(layoutParams.width, getMaxSwipeWidth());
                        anim.addUpdateListener(valueAnimator -> {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams12 = mTouchView.getLayoutParams();
                            layoutParams12.width = val;
                            mTouchView.setLayoutParams(layoutParams12);
                        });
                        anim.setDuration(DELAY_SHOW_CHECK_MS);
                        anim.start();
                        mCouponContainer.postDelayed(() -> {
                            mCouponContainer.setDisplayedChild(CONTAINER_CHECK);
                            mSwipeIcon.setVisibility(GONE);

                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onComplete();
                            }
                        }, DELAY_SHOW_CHECK_MS);
                    } else {
                        isSwipeEnable = true;
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mTouchView.getLayoutParams();
                        ValueAnimator anim = ValueAnimator.ofInt(layoutParams.width, mMinWidth);
                        anim.addUpdateListener(valueAnimator -> {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams1 = mTouchView.getLayoutParams();
                            layoutParams1.width = val;
                            mTouchView.setLayoutParams(layoutParams1);
                        });

                        anim.setDuration(DELAY_BACK_MS);
                        anim.start();
                        mTextSwipeTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.black_38));
                        mSwipeIcon.setVisibility(VISIBLE);

                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onPartialSwipe();
                        }
                    }
                }
                break;
            }
        }

        mPreviousX = x;
        return true;
    }

    private int getMaxSwipeWidth() {
        return mView.getWidth() - getResources().getDimensionPixelOffset(R.dimen.dp_6);
    }

    private boolean isLeftSwipe(float dx) {
        return dx > 0;
    }

    public void setCouponCode(String code) {
        if (code == null || code.isEmpty()) {
            return;
        }

        mCouponContainer.setDisplayedChild(CONTAINER_CODE);
        mTextCoupon.setText(code);
        isSwipeEnable = false;
    }

    public void reset() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mTouchView.getLayoutParams();
        ValueAnimator anim = ValueAnimator.ofInt(layoutParams.width, mMinWidth);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams1 = mTouchView.getLayoutParams();
            layoutParams1.width = val;
            mTouchView.setLayoutParams(layoutParams1);
        });

        anim.setDuration(DELAY_BACK_MS);
        anim.start();
        mTextSwipeTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.black_38));

        mCouponContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeIcon.setVisibility(VISIBLE);
                isSwipeEnable = true;
                mCouponContainer.setDisplayedChild(CONTAINER_TEXT);
            }
        }, DELAY_BACK_MS);
    }


    public void setOnSwipeListener(OnSwipeListener listener) {
        this.mOnSwipeListener = listener;
    }

    public void setTitle(String title) {
        mTextSwipeTitle.setText(title);
    }

    public interface OnSwipeListener {
        void onComplete();

        void onPartialSwipe();
    }
}
