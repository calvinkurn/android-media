package com.tokopedia.challenges.view.customview;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.challenges.R;

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
                R.styleable.FloatingTxtButton,
                0,
                0
        );

        title = styleable.getString(R.styleable.FloatingTxtButton_floating_title);
        titleColor = styleable.getColor(R.styleable.FloatingTxtButton_floating_title_color, Color.BLACK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leftIcon = styleable.getDrawable(R.styleable.FloatingTxtButton_floating_left_icon);
            rightIcon = styleable.getDrawable(R.styleable.FloatingTxtButton_floating_right_icon);
        } else {
            final int drawableLeftId = styleable.getResourceId(R.styleable.FloatingTxtButton_floating_left_icon, -1);
            final int drawableRightId = styleable.getResourceId(R.styleable.FloatingTxtButton_floating_right_icon, -1);
            if (drawableLeftId != -1)
                leftIcon = AppCompatResources.getDrawable(getContext(), drawableLeftId);
            if (drawableRightId != -1)
                rightIcon = AppCompatResources.getDrawable(getContext(), drawableRightId);
        }
        background = styleable.getColor(R.styleable.FloatingTxtButton_floating_background_color, Color.WHITE);
        titleAllCaps = styleable.getBoolean(R.styleable.FloatingTxtButton_floating_title_allcaps, false);
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
        return convertDpToPixel(dp, getContext());
    }

    @SuppressWarnings("SameParameterValue")
    private int getHorizontalPaddingValue(int dp) {
        return convertDpToPixel(dp, getContext());
    }

    public void show() {
        show(-1);
    }

    private void show(final int visibility) {
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

    public void hide() {
        hide(-1);
    }

    private void hide(final int visibility) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) getLayoutParams();
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

    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * ((int)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static int convertPixelsToDp(int px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = px / ((int)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}