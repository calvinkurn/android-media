package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.customview.MaskedHeightImageView;
import com.tokopedia.gamification.floating.view.model.TokenAsset;

import java.util.List;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * @author Rizky on 28/03/18.
 */

public class WidgetTokenView extends FrameLayout {

    public static final float Y_PIVOT_PERCENT = 0.9f;
    public static final int CRACK_STEP1_SHAKE_DURATION = 150;
    public static final int CRACK_STEP1_DURATION = 350;
    public static final int CRACK_STEP1_DEGREE = 3;
    public static final int CRACK_STEP2_DEGREE = 6;
    public static final int CRACK_STEP2_DURATION = 350;
    public static final int CRACK_STEP2_SHAKE_DURATION = 120;
    public static final int CRACK_STEP2_START_DELAY = 80;
    public static final int CRACK_STEP3_START_DELAY = 100;
    public static final int CRACK_STEP3_DURATION = 350;
    public static final int CRACK_STEP3_SHAKE_DURATION = 150;
    public static final int STEP2_END_MASKED_PERCENT = 30;
    public static final int STEP1_END_MASKED_PERCENT = 70;
    public static final double RATIO_IMAGE_WIDTH = 0.5;
    public static final double RATIO_IMAGE_MARGIN_BOTTOM = 0.64;
    public static final double RATIO_LIGHT_WIDTH = 0.8;
    public static final int CRACK_STEP3_DEGREE = 4;

    private ImageView imageViewFull;
    private MaskedHeightImageView imageViewCracked;
    private ImageView imageViewLeft;
    private ImageView imageViewRight;

    private ImageView imageViewLightLeft;
    private ImageView imageViewLightRight;

    private boolean isTokenClicked;

    private WidgetTokenListener listener;
    private View rootView;
    private AnimatorSet crackingAnimationSet;
    private AnimatorSet crackingAnimationSet1;
    private AnimatorSet crackingAnimationSet2;
    private AnimatorSet crackingAnimationSet3;
    private ObjectAnimator shakeAnimatorSlow;

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
                    shakeHardAndCrackAnimation();
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
    private void initImageBound() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageWidth = (int) (RATIO_IMAGE_WIDTH * Math.min(rootWidth, rootHeight));
        int imageHeight = imageWidth;
        int imageMarginTop = (int) (RATIO_IMAGE_MARGIN_BOTTOM * (rootHeight)) - imageHeight;

        int lightImageWidth = (int) (RATIO_LIGHT_WIDTH * imageWidth);
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
        int marginTopLightLeft = (int) (RATIO_IMAGE_MARGIN_BOTTOM * (rootHeight)) - (int) (0.75 * imageHeight) - lightImageHeight / 2;
        int marginLeftLightLeft = (int) (0.5 * (rootWidth - (int) (0.65 * imageWidth) - lightImageWidth));
        FrameLayout.LayoutParams ivLightLeftLp = (FrameLayout.LayoutParams) imageViewLightLeft.getLayoutParams();
        ivLightLeftLp.width = lightImageWidth;
        ivLightLeftLp.height = lightImageHeight;
        ivLightLeftLp.topMargin = marginTopLightLeft;
        ivLightLeftLp.leftMargin = marginLeftLightLeft;
        imageViewLightLeft.requestLayout();

        // to show the light on the top right
        int marginTopLightRight = (int) (RATIO_IMAGE_MARGIN_BOTTOM * (rootHeight)) - (int) (0.95 * imageHeight) - lightImageHeight / 2;
        int marginLeftLightRight = (int) (0.5 * (rootWidth + (int) (0.35 * imageWidth) - lightImageWidth));
        FrameLayout.LayoutParams ivLightRightLp = (FrameLayout.LayoutParams) imageViewLightRight.getLayoutParams();
        ivLightRightLp.width = lightImageWidth;
        ivLightRightLp.height = lightImageHeight;
        ivLightRightLp.topMargin = marginTopLightRight;
        ivLightRightLp.leftMargin = marginLeftLightRight;
        imageViewLightLeft.requestLayout();

        setVisibility(View.VISIBLE);
    }

    public void setToken(TokenAsset tokenAsset) {
        List<String> imageUrls = tokenAsset.getImageUrls();
        String full = imageUrls.get(0);
        String cracked = imageUrls.get(4);
        String imageRightUrl = imageUrls.get(5);
        String imageLeftUrl = imageUrls.get(6);

        ImageHandler.loadImageAndCache(imageViewFull, full);
        Glide.with(getContext())
                .load(cracked)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageViewCracked.setImageBitmap(resource);
                    }
                });
        ImageHandler.loadImageAndCache(imageViewRight, imageRightUrl);
        ImageHandler.loadImageAndCache(imageViewLeft, imageLeftUrl);

        reset();

        this.setVisibility(View.VISIBLE);
    }

    public void hide() {
        this.setVisibility(View.INVISIBLE);
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
        PropertyValuesHolder pvhShake =
                PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -8, 8);
        shakeAnimatorSlow = ObjectAnimator.ofPropertyValuesHolder(imageViewFull, pvhShake);
        shakeAnimatorSlow.setRepeatMode(ValueAnimator.REVERSE);
        shakeAnimatorSlow.setRepeatCount(5);
        shakeAnimatorSlow.setDuration(500 / 5);
        shakeAnimatorSlow.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewFull.setTranslationX(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                imageViewFull.setTranslationX(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        shakeAnimatorSlow.start();
    }

    private void shakeHardAndCrackAnimation() {
        imageViewFull.setPivotY(Y_PIVOT_PERCENT * imageViewFull.getHeight());
        imageViewCracked.setPivotY(Y_PIVOT_PERCENT * imageViewCracked.getHeight());

        imageViewCracked.setVisibility(VISIBLE);

        initCrackingAnimationSet();
        crackingAnimationSet.start();
    }

    private void initCrackingAnimationSet() {
        if (crackingAnimationSet == null) {
            crackingAnimationSet = new AnimatorSet();
            initCracking1();
            initCracking2();
            initCracking3();
            crackingAnimationSet.playSequentially(crackingAnimationSet1, crackingAnimationSet2, crackingAnimationSet3);
        }
    }

    public boolean isCrackPercentageFull() {
        return imageViewCracked.isFullyHiddenByMask();
    }

    private void initCracking1() {
        if (crackingAnimationSet1 == null) {
            crackingAnimationSet1 = new AnimatorSet();
            PropertyValuesHolder pvhShake =
                    PropertyValuesHolder.ofFloat(View.ROTATION, 0, -CRACK_STEP1_DEGREE, 0, CRACK_STEP1_DEGREE, 0);
            ObjectAnimator shakeAnimatorFull = ObjectAnimator.ofPropertyValuesHolder(imageViewFull, pvhShake);
            shakeAnimatorFull.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorFull.setRepeatCount(CRACK_STEP1_DURATION / (2 * CRACK_STEP1_SHAKE_DURATION));
            shakeAnimatorFull.setDuration(CRACK_STEP1_SHAKE_DURATION);

            ObjectAnimator shakeAnimatorCrack = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhShake);
            shakeAnimatorCrack.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorCrack.setRepeatCount(CRACK_STEP1_DURATION / (2 * CRACK_STEP1_SHAKE_DURATION));
            shakeAnimatorCrack.setDuration(CRACK_STEP1_SHAKE_DURATION);

            PropertyValuesHolder pvhMaskedCrack =
                    PropertyValuesHolder.ofInt(MaskedHeightImageView.MASKED_PERCENT, 100, STEP1_END_MASKED_PERCENT);
            ObjectAnimator maskCrackAnimator = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhMaskedCrack);
            maskCrackAnimator.setInterpolator(new AccelerateInterpolator());
            maskCrackAnimator.setDuration(CRACK_STEP1_DURATION);

            crackingAnimationSet1.playTogether(shakeAnimatorFull, shakeAnimatorCrack, maskCrackAnimator);
        }
    }

    private void initCracking2() {
        if (crackingAnimationSet2 == null) {
            crackingAnimationSet2 = new AnimatorSet();
            PropertyValuesHolder pvhShake =
                    PropertyValuesHolder.ofFloat(View.ROTATION, 0, -CRACK_STEP2_DEGREE, 0, CRACK_STEP2_DEGREE, 0);
            ObjectAnimator shakeAnimatorFull = ObjectAnimator.ofPropertyValuesHolder(imageViewFull, pvhShake);
            shakeAnimatorFull.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorFull.setRepeatCount(CRACK_STEP2_DURATION / (2 * CRACK_STEP2_SHAKE_DURATION));
            shakeAnimatorFull.setDuration(CRACK_STEP2_SHAKE_DURATION);

            ObjectAnimator shakeAnimatorCrack = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhShake);
            shakeAnimatorCrack.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorCrack.setRepeatCount(CRACK_STEP2_DURATION / (2 * CRACK_STEP2_SHAKE_DURATION));
            shakeAnimatorCrack.setDuration(CRACK_STEP2_SHAKE_DURATION);

            PropertyValuesHolder pvhMaskedCrack =
                    PropertyValuesHolder.ofInt(MaskedHeightImageView.MASKED_PERCENT, STEP1_END_MASKED_PERCENT, STEP2_END_MASKED_PERCENT);
            ObjectAnimator maskCrackAnimator = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhMaskedCrack);
            maskCrackAnimator.setInterpolator(new AccelerateInterpolator());
            maskCrackAnimator.setDuration(CRACK_STEP2_DURATION);

            crackingAnimationSet2.playTogether(shakeAnimatorFull, shakeAnimatorCrack, maskCrackAnimator);
            crackingAnimationSet2.setStartDelay(CRACK_STEP2_START_DELAY);
        }
    }

    private void initCracking3() {
        if (crackingAnimationSet3 == null) {
            crackingAnimationSet3 = new AnimatorSet();
            PropertyValuesHolder pvhShake =
                    PropertyValuesHolder.ofFloat(View.ROTATION, 0, -CRACK_STEP3_DEGREE, 0, CRACK_STEP3_DEGREE, 0);
            ObjectAnimator shakeAnimatorFull = ObjectAnimator.ofPropertyValuesHolder(imageViewFull, pvhShake);
            shakeAnimatorFull.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorFull.setRepeatCount(ValueAnimator.INFINITE);
            shakeAnimatorFull.setDuration(CRACK_STEP3_SHAKE_DURATION);

            ObjectAnimator shakeAnimatorCrack = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhShake);
            shakeAnimatorCrack.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorCrack.setRepeatCount(ValueAnimator.INFINITE);
            shakeAnimatorCrack.setDuration(CRACK_STEP3_SHAKE_DURATION);

            PropertyValuesHolder pvhMaskedCrack =
                    PropertyValuesHolder.ofInt(MaskedHeightImageView.MASKED_PERCENT, STEP2_END_MASKED_PERCENT, 0);
            ObjectAnimator maskCrackAnimator = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhMaskedCrack);
            maskCrackAnimator.setInterpolator(new AccelerateInterpolator());
            maskCrackAnimator.setDuration(CRACK_STEP3_DURATION);

            crackingAnimationSet3.playTogether(shakeAnimatorFull, shakeAnimatorCrack, maskCrackAnimator);
            crackingAnimationSet3.setStartDelay(CRACK_STEP3_START_DELAY);
        }
    }

    public void stopShaking() {
        imageViewFull.setRotation(0);
        imageViewCracked.setRotation(0);
        if (shakeAnimatorSlow != null) {
            shakeAnimatorSlow.cancel();
        }
        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
    }

    public void split() {
        imageViewFull.setVisibility(View.GONE);
        imageViewCracked.setVisibility(View.GONE);

        imageViewLeft.setVisibility(View.VISIBLE);
        imageViewRight.setVisibility(View.VISIBLE);

        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
        if (shakeAnimatorSlow != null) {
            shakeAnimatorSlow.cancel();
        }

        Animation rotateRightAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_right_and_translate);
        imageViewRight.setAnimation(rotateRightAnimation);
        Animation rotateLeftAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_left_and_translate);
        imageViewLeft.setAnimation(rotateLeftAnimation);
    }

    private void reset() {
        isTokenClicked = false;

        imageViewRight.clearAnimation();
        imageViewRight.setVisibility(View.GONE);

        imageViewLeft.clearAnimation();
        imageViewLeft.setVisibility(View.GONE);

        imageViewFull.setVisibility(View.VISIBLE);
        imageViewCracked.reset();

        showLightAnimation();
        shake();
    }

    public void clearTokenAnimation() {
        imageViewLightLeft.clearAnimation();
        imageViewLightLeft.setVisibility(View.GONE);
        imageViewLightRight.clearAnimation();
        imageViewLightRight.setVisibility(View.GONE);

        if (shakeAnimatorSlow != null) {
            shakeAnimatorSlow.cancel();
        }
        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
    }

}
