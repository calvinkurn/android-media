package com.tokopedia.digital_deals.view.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ExpandableTextView extends TextView {
    private final List<OnExpandListener> onExpandListeners;
    private TimeInterpolator expandInterpolator;
    private TimeInterpolator collapseInterpolator;

    private final int maxLines;
    private long animationDuration;
    private boolean isAnimating;
    private boolean isExpanded;
    private int collapsedHeight;

    public ExpandableTextView(final Context context) {
        this(context, null);
    }

    public ExpandableTextView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextView(final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, com.tokopedia.digital_deals.R.styleable.ExpandableTextView, defStyle, 0);
        this.animationDuration = attributes.getInt(com.tokopedia.digital_deals.R.styleable.ExpandableTextView_animation_duration, 200);
        attributes.recycle();
        this.maxLines = this.getMaxLines();
        this.onExpandListeners = new ArrayList<>();
        this.expandInterpolator = new AccelerateDecelerateInterpolator();
        this.collapseInterpolator = new AccelerateDecelerateInterpolator();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, int heightMeasureSpec) {
        if (this.maxLines == 0 && !this.isExpanded && !this.isAnimating) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public boolean toggle() {
        return this.isExpanded
                ? this.collapse()
                : this.expand();
    }

    public boolean expand() {
        if (!this.isExpanded && !this.isAnimating && this.maxLines >= 0) {
            this.notifyOnExpand();
            this.measure
                    (
                            MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    );
            this.collapsedHeight = this.getMeasuredHeight();
            this.isAnimating = true;
            this.setMaxLines(Integer.MAX_VALUE);
            this.measure
                    (
                            MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                    );

            final int expandedHeight = this.getMeasuredHeight();
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(this.collapsedHeight, expandedHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    ExpandableTextView.this.setHeight((int) animation.getAnimatedValue());
                }
            });

            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {

                    ExpandableTextView.this.setMaxHeight(Integer.MAX_VALUE);
                    ExpandableTextView.this.setMinHeight(0);

                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ExpandableTextView.this.setLayoutParams(layoutParams);


                    ExpandableTextView.this.isExpanded = true;
                    ExpandableTextView.this.isAnimating = false;
                }
            });

            valueAnimator.setInterpolator(this.expandInterpolator);

            valueAnimator
                    .setDuration(this.animationDuration)
                    .start();

            return true;
        }

        return false;
    }

    public boolean collapse() {
        if (this.isExpanded && !this.isAnimating && this.maxLines >= 0) {
            this.notifyOnCollapse();
            final int expandedHeight = this.getMeasuredHeight();
            this.isAnimating = true;
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(expandedHeight, this.collapsedHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(final ValueAnimator animation) {
                    ExpandableTextView.this.setHeight((int) animation.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animation) {
                    ExpandableTextView.this.isExpanded = false;
                    ExpandableTextView.this.isAnimating = false;
                    ExpandableTextView.this.setMaxLines(ExpandableTextView.this.maxLines);
                    final ViewGroup.LayoutParams layoutParams = ExpandableTextView.this.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ExpandableTextView.this.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.setInterpolator(this.collapseInterpolator);
            valueAnimator
                    .setDuration(this.animationDuration)
                    .start();
            return true;
        }
        return false;
    }

    public void setInterpolator(final TimeInterpolator interpolator) {
        this.expandInterpolator = interpolator;
        this.collapseInterpolator = interpolator;
    }

    public void setAnimationDuration(final long animationDuration) {
        this.animationDuration = animationDuration;
    }

    public void addOnExpandListener(final OnExpandListener onExpandListener) {
        this.onExpandListeners.add(onExpandListener);
    }

    public void removeOnExpandListener(final OnExpandListener onExpandListener) {
        this.onExpandListeners.remove(onExpandListener);
    }


    public boolean isExpanded() {
        return this.isExpanded;
    }


    private void notifyOnCollapse() {
        for (final OnExpandListener onExpandListener : this.onExpandListeners) {
            onExpandListener.onCollapse(this);
        }
    }

    private void notifyOnExpand() {
        for (final OnExpandListener onExpandListener : this.onExpandListeners) {
            onExpandListener.onExpand(this);
        }
    }

    public interface OnExpandListener {
        void onExpand(@NonNull ExpandableTextView view);

        void onCollapse(@NonNull ExpandableTextView view);
    }


}
