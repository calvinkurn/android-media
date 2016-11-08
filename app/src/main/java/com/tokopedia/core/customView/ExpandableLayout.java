package com.tokopedia.core.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tokopedia.core.R;

/**
 * Created by Angga.Prasetiyo on 26/04/2016.
 */
public class ExpandableLayout extends RelativeLayout {
    private static final String TAG = ExpandableLayout.class.getSimpleName();

    private Boolean isAnimationRunning = false;
    private Boolean isOpened = false;
    private Integer duration;
    private FrameLayout contentLayout;
    private FrameLayout headerLayout;
    private Animation animation;
    private View headerView;
    private View contentView;
    private ImageView ivIndicator;
    private IndicatorListener indicatorListener;
    private boolean enableAnimation;

    public ExpandableLayout(Context context) {
        super(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        final View rootView = View.inflate(context, R.layout.view_expandable_layout, this);
        headerLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_header_layout);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ExpandableLayout);
        final int headerID = typedArray.getResourceId(
                R.styleable.ExpandableLayout_el_header_layout, -1);
        final int contentID = typedArray.getResourceId(
                R.styleable.ExpandableLayout_el_content_layout, -1);
        enableAnimation = typedArray.getBoolean(
                R.styleable.ExpandableLayout_el_enable_anim, true);
        contentLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_content_layout);

        if (headerID == -1 || contentID == -1)
            throw new IllegalArgumentException("HeaderLayout and ContentLayout cannot be null!");

        if (isInEditMode())
            return;

        duration = typedArray.getInt(R.styleable.ExpandableLayout_el_duration_anim,
                getContext().getResources().getInteger(android.R.integer.config_shortAnimTime));
        headerView = View.inflate(context, headerID, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        headerLayout.addView(headerView);
        ivIndicator = (ImageView) ((ViewGroup) headerView).getChildAt(1);
        contentView = View.inflate(context, contentID, null);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        contentLayout.addView(contentView);
        contentLayout.setVisibility(GONE);
        headerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimationRunning) {
                    if (contentLayout.getVisibility() == VISIBLE)
                        collapse(contentLayout);
                    else
                        expand(contentLayout);

                    isAnimationRunning = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAnimationRunning = false;
                        }
                    }, duration);
                }
            }
        });

        typedArray.recycle();
    }

    private void expand(final View v) {
        if (ivIndicator != null && indicatorListener != null)
            ivIndicator.setImageResource(indicatorListener.getDrawableResourceExpand());

        if (enableAnimation) {
            v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();
            v.getLayoutParams().height = 0;
            v.setVisibility(VISIBLE);

            animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1)
                        isOpened = true;
                    v.getLayoutParams().height = (interpolatedTime == 1)
                            ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                    v.requestLayout();
                }


                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            animation.setDuration(duration);
            v.startAnimation(animation);
        } else {
            v.setVisibility(VISIBLE);
        }

    }

    private void collapse(final View v) {
        if (ivIndicator != null && indicatorListener != null)
            ivIndicator.setImageResource(indicatorListener.getDrawableResourceCollapse());

        if (enableAnimation) {
            final int initialHeight = v.getMeasuredHeight();
            animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                        isOpened = false;
                    } else {
                        v.getLayoutParams().height = initialHeight
                                - (int) (initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            animation.setDuration(duration);
            v.startAnimation(animation);
        } else {
            v.setVisibility(GONE);
        }

    }

    public Boolean isOpened() {
        return isOpened;
    }

    public void show() {
        if (!isAnimationRunning) {
            expand(contentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAnimationRunning = false;
                }
            }, duration);
        }
    }

    public FrameLayout getHeaderLayout() {
        return headerLayout;
    }

    public FrameLayout getContentLayout() {
        return contentLayout;
    }

    public void hide() {
        if (!isAnimationRunning) {
            collapse(contentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAnimationRunning = false;
                }
            }, duration);
        }
    }

    @Override
    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        animation.setAnimationListener(animationListener);
    }

    public View getHeaderView() {
        return headerView;
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentViewBenefit) {
        contentLayout.addView(contentViewBenefit);
    }

    public void setIndicatorListener(IndicatorListener listener) {
        this.indicatorListener = listener;
        if (isOpened) {
            if (ivIndicator != null && indicatorListener != null)
                ivIndicator.setImageResource(indicatorListener.getDrawableResourceExpand());
        } else {
            if (ivIndicator != null && indicatorListener != null)
                ivIndicator.setImageResource(indicatorListener.getDrawableResourceCollapse());
        }
    }

    public interface IndicatorListener {
        int getDrawableResourceExpand();

        int getDrawableResourceCollapse();
    }
}
