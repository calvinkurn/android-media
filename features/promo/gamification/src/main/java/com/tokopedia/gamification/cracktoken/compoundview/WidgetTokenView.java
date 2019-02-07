package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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
import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationConstants;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.customview.MaskedHeightImageView;
import com.tokopedia.gamification.cracktoken.util.TokenMarginUtil;
import com.tokopedia.gamification.data.entity.TokenAssetEntity;

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
    public static final double RATIO_LIGHT_WIDTH = 0.8;
    public static final int CRACK_STEP3_DEGREE = 4;
    private static final long INFINITE_BOUNCE_START_DELAY = 2000;
    private static final long INFINITE_BOUNCE_DURATION = 180;

    private ImageView imageViewFull;
    private MaskedHeightImageView imageViewCracked;
    private ImageView imageViewLeft;
    private ImageView imageViewRight;


    private volatile boolean isTokenClicked;

    private WidgetTokenListener listener;
    private View rootView;
    private AnimatorSet crackingAnimationSet;
    private AnimatorSet crackingAnimationSet1;
    private AnimatorSet crackingAnimationSet2;
    private AnimatorSet crackingAnimationSet3;
    private AnimatorSet initialBounceAnimatorSet;

    private MediaPlayer crackMediaPlayer;
    private volatile boolean isAnimatedFirstTime = true;
    private volatile long animationStartTime;

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

        imageViewFull.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBounceAnimationFirstTimeAndBeforeBound())
                    clearTokenAnimationAndCrack();
                isTokenClicked = true;

            }
        });

        hide();

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

    private boolean isBounceAnimationFirstTimeAndBeforeBound() {
        if (!isAnimatedFirstTime) {
            return System.currentTimeMillis() - animationStartTime < 2000;
        }
        return false;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void initImageBound() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageWidth = TokenMarginUtil.getEggWidth(rootWidth, rootHeight);
        int imageHeight = imageWidth;
        int imageMarginBottom = TokenMarginUtil.getEggMarginBottom(rootHeight);
        int imageMarginTop = imageMarginBottom - imageHeight;

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

        setVisibility(View.VISIBLE);
    }

    public void setToken(TokenAssetEntity tokenAsset) {
        List<String> imageUrls = tokenAsset.getImagev2Urls();
        String full = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_FULL);
        String cracked = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_CRACKED);
        String imageLeftUrl = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_LEFT);
        String imageRightUrl = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_RIGHT);

        StringSignature stringSignature = new StringSignature(String.valueOf(tokenAsset.getVersion()));

        ImageHandler.loadImageWithSignature(imageViewFull, full, stringSignature);
        Glide.with(getContext())
                .load(cracked)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .signature(new StringSignature(String.valueOf(tokenAsset.getVersion())))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageViewCracked.setImageBitmap(resource);
                    }
                });
        ImageHandler.loadImageWithSignature(imageViewRight, imageRightUrl, stringSignature);
        ImageHandler.loadImageWithSignature(imageViewLeft, imageLeftUrl, stringSignature);

        reset();
        show();
    }

    public void hide() {
        this.setVisibility(View.INVISIBLE);
        imageViewFull.setEnabled(false);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
        imageViewFull.setEnabled(true);
    }

    private void shake() {
        imageViewFull.setPivotY(imageViewFull.getHeight());
        imageViewFull.setPivotX(imageViewFull.getWidth() * 0.5f);

        if (initialBounceAnimatorSet == null) {
            initialBounceAnimatorSet = new AnimatorSet();

            PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.1f);
            PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.9f);
            ObjectAnimator bounceAnim = ObjectAnimator.ofPropertyValuesHolder(imageViewFull, scalex, scaley);
            bounceAnim.setRepeatCount(2);
            bounceAnim.setRepeatMode(ValueAnimator.REVERSE);
            bounceAnim.setDuration(INFINITE_BOUNCE_DURATION);

            PropertyValuesHolder scalex1 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
            PropertyValuesHolder scaley1 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
            ObjectAnimator bounceBackAnim = ObjectAnimator.ofPropertyValuesHolder(imageViewFull, scalex1, scaley1);
            bounceBackAnim.setDuration(INFINITE_BOUNCE_DURATION);
            initialBounceAnimatorSet.playSequentially(bounceAnim, bounceBackAnim);
        }
        initialBounceAnimatorSet.addListener(bounceListener);
        initialBounceAnimatorSet.start();
    }

    Animator.AnimatorListener bounceListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            animationStartTime = System.currentTimeMillis();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimatedFirstTime = false;
            if (isTokenClicked) {
                clearTokenAnimationAndCrack();
            } else {
                if (initialBounceAnimatorSet != null) {
                    initialBounceAnimatorSet.setStartDelay(2000);
                    initialBounceAnimatorSet.start();
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private void shakeHardAndCrackAnimation() {
        imageViewFull.setPivotY(Y_PIVOT_PERCENT * imageViewFull.getHeight());
        imageViewCracked.setPivotY(Y_PIVOT_PERCENT * imageViewCracked.getHeight());
        imageViewCracked.setVisibility(VISIBLE);
        initCrackingAnimationSet();
        playCrack();
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

    private void playCrack() {
        playSound(R.raw.crack);
    }

    private void playRewardSound() {
        playSound(R.raw.reward);
    }

    public void playSound(int resId) {
        if (crackMediaPlayer == null) {
            crackMediaPlayer = new MediaPlayer();
            crackMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
        try {
            if (crackMediaPlayer.isPlaying()) {
                crackMediaPlayer.stop();
            }
            crackMediaPlayer.reset();
            AssetFileDescriptor afd = getContext().getResources().openRawResourceFd(resId);
            if (afd == null) {
                return;
            }
            crackMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            crackMediaPlayer.prepareAsync();
        } catch (Exception e) {
            // not play sound.
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
        if (initialBounceAnimatorSet != null) {
            initialBounceAnimatorSet.removeListener(bounceListener);
            initialBounceAnimatorSet.cancel();
        }
        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
    }

    public void split() {
        playRewardSound();
        AnimatorSet bounceAnimatorSet = new AnimatorSet();
        AnimatorSet bounceLeftAnimatorSet = new AnimatorSet();
        AnimatorSet bounceRightAnimatorSet = new AnimatorSet();
        imageViewFull.setVisibility(View.GONE);
        imageViewCracked.setVisibility(View.GONE);
        imageViewLeft.setVisibility(View.VISIBLE);
        imageViewRight.setVisibility(View.VISIBLE);

        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
        if (initialBounceAnimatorSet != null) {
            initialBounceAnimatorSet.removeListener(bounceListener);
            initialBounceAnimatorSet.cancel();
        }
        imageViewLeft.setPivotY(imageViewLeft.getHeight());
        imageViewLeft.setPivotX(imageViewLeft.getWidth() * 0.5f);
        imageViewRight.setPivotY(imageViewRight.getHeight());
        imageViewRight.setPivotX(imageViewRight.getWidth() * 0.5f);

        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.25f);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.7f);

        ObjectAnimator animLeftBounce = ObjectAnimator.ofPropertyValuesHolder(imageViewLeft, scalex, scaley);
        animLeftBounce.setRepeatCount(1);
        animLeftBounce.setRepeatMode(ValueAnimator.REVERSE);
        animLeftBounce.setDuration(200);

        PropertyValuesHolder scalex1 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
        PropertyValuesHolder scaley1 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);

        ObjectAnimator animLeftBounceBack = ObjectAnimator.ofPropertyValuesHolder(imageViewLeft, scalex1, scaley1);
        animLeftBounceBack.setDuration(150);

        ObjectAnimator animRightBounce = ObjectAnimator.ofPropertyValuesHolder(imageViewRight, scalex, scaley);
        animRightBounce.setRepeatCount(1);
        animRightBounce.setRepeatMode(ValueAnimator.REVERSE);
        animRightBounce.setDuration(200);

        ObjectAnimator animRightBounceBack = ObjectAnimator.ofPropertyValuesHolder(imageViewRight, scalex1, scaley1);
        animRightBounceBack.setDuration(150);

        bounceLeftAnimatorSet.playSequentially(animLeftBounce, animLeftBounceBack);
        bounceRightAnimatorSet.playSequentially(animRightBounce, animRightBounceBack);
        bounceAnimatorSet.playTogether(bounceLeftAnimatorSet, bounceRightAnimatorSet);
        bounceAnimatorSet.start();

        Animation rotateRightAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_right_and_translate);
        rotateRightAnimation.setStartOffset(300);
        imageViewRight.setAnimation(rotateRightAnimation);
        Animation rotateLeftAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_left_and_translate);
        rotateLeftAnimation.setStartOffset(300);
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

        shake();
    }

    public void clearTokenAnimationAndCrack() {
        if (initialBounceAnimatorSet != null) {
            initialBounceAnimatorSet.removeListener(bounceListener);
            initialBounceAnimatorSet.cancel();

        }
        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
        shakeHardAndCrackAnimation();
        listener.onClick();
    }

}
