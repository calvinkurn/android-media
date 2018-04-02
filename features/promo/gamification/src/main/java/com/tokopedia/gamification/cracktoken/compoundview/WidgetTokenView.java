package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.customview.MaskedHeightImageView;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * @author Rizky on 28/03/18.
 */

public class WidgetTokenView extends FrameLayout {

    private ImageView imageViewFull;
    private MaskedHeightImageView imageViewCracked;
    private ImageView imageViewLeft;
    private ImageView imageViewRight;

    private ImageView imageViewLightLeft;
    private ImageView imageViewLightRight;

    private String imageRightUrl;
    private String imageLeftUrl;

    private boolean isTokenClicked;

    private WidgetTokenListener listener;
    private View rootView;

    public interface WidgetTokenListener {
        void onClick();
    }

    public WidgetTokenView(@NonNull Context context) {
        super(context);
        init();
    }

    public WidgetTokenView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetTokenView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(WidgetTokenListener listener) {
        this.listener = listener;
    }

    private void init() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_token, this, true);
        imageViewFull = rootView.findViewById(R.id.imagefull);
        imageViewCracked = rootView.findViewById(R.id.imagecracked);
        imageViewLeft = rootView.findViewById(R.id.imageleft);
        imageViewRight = rootView.findViewById(R.id.imageright);
        imageViewLightLeft = rootView.findViewById(R.id.image_light_left);
        imageViewLightRight = rootView.findViewById(R.id.image_light_top);

        imageViewFull.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTokenClicked) {
                    clearTokenAnimation();
                    shakeHard();
                    crack();
                    listener.onClick();
                }
                isTokenClicked = true;
            }
        });
        setVisibility(View.INVISIBLE);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initImageBound();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void initImageBound() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageWidth = (int) (0.5 * Math.min(rootWidth, rootHeight));
        int imageHeight = imageWidth;
        int imageMarginTop = (int) (0.64 * (rootHeight)) - imageHeight;

        int lightImageWidth = (int) (0.8 * imageWidth);
        int lightImageHeight = lightImageWidth;

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) imageViewFull.getLayoutParams();
        ivFullLp.width = imageWidth;
        ivFullLp.height = imageHeight;
        ivFullLp.gravity = CENTER_HORIZONTAL;
        ivFullLp.topMargin = imageMarginTop;
        imageViewFull.requestLayout();

        FrameLayout.LayoutParams ivCrackedLp = (FrameLayout.LayoutParams) imageViewCracked.getLayoutParams();
        ivCrackedLp.width = imageWidth;
        ivCrackedLp.height = imageHeight;
        ivCrackedLp.gravity = CENTER_HORIZONTAL;
        ivCrackedLp.topMargin = imageMarginTop;
        imageViewCracked.requestLayout();

        FrameLayout.LayoutParams ivLeftLp = (FrameLayout.LayoutParams) imageViewLeft.getLayoutParams();
        ivLeftLp.width = imageWidth;
        ivLeftLp.height = imageHeight;
        ivLeftLp.gravity = CENTER_HORIZONTAL;
        ivLeftLp.topMargin = imageMarginTop;
        imageViewLeft.requestLayout();

        FrameLayout.LayoutParams ivRightLp = (FrameLayout.LayoutParams) imageViewRight.getLayoutParams();
        ivRightLp.width = imageWidth;
        ivRightLp.height = imageHeight;
        ivRightLp.gravity = CENTER_HORIZONTAL;
        ivRightLp.topMargin = imageMarginTop;
        imageViewRight.requestLayout();

        // to show the light on the top left
        int marginTopLightLeft = (int) (0.64 * (rootHeight)) - (int)(0.75 * imageHeight) - lightImageHeight/2;
        int marginLeftLightLeft = (int) (0.5 * ( rootWidth - (int)(0.65 * imageWidth) - lightImageWidth ));
        FrameLayout.LayoutParams ivLightLeftLp = (FrameLayout.LayoutParams) imageViewLightLeft.getLayoutParams();
        ivLightLeftLp.width = lightImageWidth;
        ivLightLeftLp.height = lightImageHeight;
        ivLightLeftLp.topMargin = marginTopLightLeft;
        ivLightLeftLp.leftMargin = marginLeftLightLeft;
        imageViewLightLeft.requestLayout();

        // to show the light on the top right
        int marginTopLightRight = (int) (0.64 * (rootHeight)) - (int)(0.95 * imageHeight) - lightImageHeight/2;
        int marginLeftLightRight = (int) (0.5 * ( rootWidth + (int)(0.35 * imageWidth) - lightImageWidth ));
        FrameLayout.LayoutParams ivLightRightLp = (FrameLayout.LayoutParams) imageViewLightRight.getLayoutParams();
        ivLightRightLp.width = lightImageWidth;
        ivLightRightLp.height = lightImageHeight;
        ivLightRightLp.topMargin = marginTopLightRight;
        ivLightRightLp.leftMargin = marginLeftLightRight;
        imageViewLightLeft.requestLayout();

        setVisibility(View.VISIBLE);
    }

    public void setToken(String full, String cracked, String right, String left) {
        imageRightUrl = right;
        imageLeftUrl = left;
        ImageHandler.LoadImage(imageViewFull, full);
        Glide.with(getContext())
                .load(cracked)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageViewCracked.setImageBitmap(resource);
                    }
                });

        shake();
        showLightAnimation();
    }

    private void showLightAnimation() {
        AlphaAnimation alphaAnimationLeft = new AlphaAnimation(0f, 1f);
        alphaAnimationLeft.setDuration(100);
        alphaAnimationLeft.setRepeatCount(Animation.INFINITE);
        alphaAnimationLeft.setStartOffset(5000);
        alphaAnimationLeft.setRepeatMode(Animation.REVERSE);

        AlphaAnimation alphaAnimationRight = new AlphaAnimation(0f, 1f);
        alphaAnimationRight.setDuration(100);
        alphaAnimationRight.setRepeatCount(Animation.INFINITE);
        alphaAnimationRight.setStartOffset(8000);
        alphaAnimationRight.setRepeatMode(Animation.REVERSE);

        imageViewLightLeft.setAnimation(alphaAnimationLeft);
        imageViewLightRight.setAnimation(alphaAnimationRight);
    }

    private void shake() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        imageViewFull.setAnimation(animation);
        imageViewCracked.setAnimation(animation);
    }

    private void shakeHard() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake_hard);
        imageViewFull.setAnimation(animation);
        imageViewCracked.setAnimation(animation);
    }

    public void stopShaking() {
        imageViewFull.clearAnimation();
        imageViewCracked.clearAnimation();
    }

    public void crack() {
        ValueAnimator crackingEggValAnimator = ValueAnimator.ofInt(100, 0);
        crackingEggValAnimator.setInterpolator(new DecelerateInterpolator());
        crackingEggValAnimator.setDuration(1000);
        crackingEggValAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                imageViewCracked.setPercentMasked(animatedValue);
            }
        });
        crackingEggValAnimator.start();
    }

    public void split() {
        imageViewFull.setVisibility(View.GONE);
        imageViewCracked.setVisibility(View.GONE);

        ImageHandler.LoadImage(imageViewRight, imageRightUrl);
        ImageHandler.LoadImage(imageViewLeft, imageLeftUrl);
        imageViewLeft.setVisibility(View.VISIBLE);
        imageViewRight.setVisibility(View.VISIBLE);

        imageViewFull.clearAnimation();
        imageViewCracked.clearAnimation();

        Animation rotateRightAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_right_and_translate);
        imageViewRight.setAnimation(rotateRightAnimation);
        Animation rotateLeftAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_left_and_translate);
        imageViewLeft.setAnimation(rotateLeftAnimation);
    }

    public void reset() {
        isTokenClicked = false;

        imageViewRight.clearAnimation();
        imageViewRight.setVisibility(View.GONE);

        imageViewLeft.clearAnimation();
        imageViewLeft.setVisibility(View.GONE);

        imageViewFull.setVisibility(View.VISIBLE);
        imageViewCracked.setPercentMasked(100);

        showLightAnimation();
        shake();
    }

    public void clearTokenAnimation() {
        imageViewLightLeft.clearAnimation();
        imageViewLightLeft.setVisibility(View.GONE);
        imageViewLightRight.clearAnimation();
        imageViewLightRight.setVisibility(View.GONE);

        imageViewFull.clearAnimation();
        imageViewCracked.clearAnimation();
    }

}
