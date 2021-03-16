package com.tokopedia.home.widget;

/**
 * Created by errysuprayogi on 3/16/18.
 */

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.home.R;
import com.tokopedia.home.util.DimensionUtils;


public class FloatingTextButton extends FrameLayout {

    private CardView container;
    private ImageView leftIconView;
    private ImageView rightIconView;
    private TextView titleView;

    private String title;
    private int titleColor;
    private Drawable leftIcon;
    private Drawable rightIcon;
    private int background;
    private boolean titleAllCaps;
    private static final String TAG = FloatingTextButton.class.getSimpleName();
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final Long DURATION = 250L;
    private ViewPropertyAnimatorCompat animation = null;
    private boolean forceHide = false;
    private boolean animationStart;
    private boolean floatingIsShown;

    public FloatingTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout(context);
        initAttributes(attrs);
        initView();
    }

    public FloatingTextButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateLayout(context);
        initAttributes(attrs);
        initView();
    }

    public FloatingTextButton(@NonNull Context context) {
        super(context);
        inflateLayout(context);
        initView();
    }

    public void setTitle(String newTitle) {
        title = newTitle;

        if (newTitle == null || newTitle.isEmpty()) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
        }

        titleView.setText(newTitle);
    }

    public String getTitle() {
        return title;
    }

    public void setTitleColor(@ColorInt int color) {
        titleColor = color;
        titleView.setTextColor(color);
    }

    public @ColorInt
    int getTitleColor() {
        return titleColor;
    }

    public void setBackgroundColor(@ColorInt int color) {
        background = color;
        container.setCardBackgroundColor(color);
    }

    public @ColorInt
    int getBackgroundColor() {
        return background;
    }

    public void setLeftIconDrawable(Drawable drawable) {
        leftIcon = drawable;
        if (drawable != null) {
            leftIconView.setVisibility(VISIBLE);
            leftIconView.setImageDrawable(drawable);
        } else {
            leftIconView.setVisibility(GONE);
        }
    }

    public void setRightIconDrawable(Drawable drawable) {
        rightIcon = drawable;
        if (drawable != null) {
            rightIconView.setVisibility(VISIBLE);
            rightIconView.setImageDrawable(drawable);
        } else {
            rightIconView.setVisibility(GONE);
        }
    }

    public Drawable getLeftIconDrawable() {
        return leftIcon;
    }

    public Drawable getRightIconDrawable() {
        return rightIcon;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        container.setOnClickListener(listener);
    }

    @Override
    public boolean hasOnClickListeners() {
        return container.hasOnClickListeners();
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener listener) {
        container.setOnLongClickListener(listener);
    }

    private void inflateLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_floating_text_button, this, true);

        container = view.findViewById(R.id.layout_button_container);

        leftIconView = view.findViewById(R.id.layout_button_image_left);
        rightIconView = view.findViewById(R.id.layout_button_image_right);

        titleView = view.findViewById(R.id.layout_button_text);
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray styleable = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.FloatingTextButton,
                0,
                0
        );

        title = styleable.getString(R.styleable.FloatingTextButton_floating_title);
        titleColor = styleable.getColor(R.styleable.FloatingTextButton_floating_title_color, androidx.core.content.ContextCompat.getColor(getContext(),com.tokopedia.unifyprinciples.R.color.Unify_N700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leftIcon = styleable.getDrawable(R.styleable.FloatingTextButton_floating_left_icon);
            rightIcon = styleable.getDrawable(R.styleable.FloatingTextButton_floating_right_icon);
        } else {
            final int drawableLeftId = styleable.getResourceId(R.styleable.FloatingTextButton_floating_left_icon, -1);
            final int drawableRightId = styleable.getResourceId(R.styleable.FloatingTextButton_floating_right_icon, -1);
            if (drawableLeftId != -1)
                leftIcon = AppCompatResources.getDrawable(getContext(), drawableLeftId);
            if (drawableRightId != -1)
                rightIcon = AppCompatResources.getDrawable(getContext(), drawableRightId);
        }
        background = styleable.getColor(R.styleable.FloatingTextButton_floating_background_color, androidx.core.content.ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
        titleAllCaps = styleable.getBoolean(R.styleable.FloatingTextButton_floating_title_allcaps, false);
        titleView.setAllCaps(titleAllCaps);
        styleable.recycle();
    }

    private void initView() {
        setTitle(title);
        setTitleColor(titleColor);
        setLeftIconDrawable(leftIcon);
        setRightIconDrawable(rightIcon);
        setBackgroundColor(background);
    }


    @SuppressWarnings("SameParameterValue")
    private int getVerticalPaddingValue(int dp) {
        return DimensionUtils.convertDpToPixel(dp, getContext());
    }

    @SuppressWarnings("SameParameterValue")
    private int getHorizontalPaddingValue(int dp) {
        return DimensionUtils.convertDpToPixel(dp, getContext());
    }

    public void show() {
        show(-1);
    }

    private void show(final int visibility) {
        if (!floatingIsShown) {
            floatingIsShown = true;
            if (forceHide)
                return;
            animate().translationY(0).setInterpolator(new DecelerateInterpolator(2))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            animationStart = true;
                            if (visibility >= 0) {
                                FloatingTextButton.super.setVisibility(visibility);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            animationStart = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            animationStart = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .setDuration(DURATION)
                    .start();
        }
    }

    public void hide() {
        hide(-1);
    }

    private void hide(final int visibility) {
        if (floatingIsShown) {
            floatingIsShown = false;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            animate().translationY(getHeight() + fab_bottomMargin).setInterpolator(new AccelerateInterpolator(2))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            animationStart = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            animationStart = false;
                            if (visibility >= 0) {
                                FloatingTextButton.super.setVisibility(visibility);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            animationStart = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .setDuration(DURATION)
                    .start();
        }
    }

    public boolean isAnimationStart() {
        return animationStart;
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            if (visibility == VISIBLE) {
                show(visibility);
            } else if ((visibility == INVISIBLE) || (visibility == GONE)) {
                hide(visibility);
            }
        }
    }

    public void forceHide() {
        forceHide = true;
        hide();
    }

    public void resetState() {
        this.forceHide = false;
    }
}