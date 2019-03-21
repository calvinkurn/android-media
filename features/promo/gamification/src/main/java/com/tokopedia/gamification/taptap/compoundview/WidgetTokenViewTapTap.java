package com.tokopedia.gamification.taptap.compoundview;

import android.animation.Animator;
import android.animation.AnimatorInflater;
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
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.taptap.data.entiity.TokenAsset;
import com.tokopedia.gamification.taptap.data.entiity.TokensUser;
import com.tokopedia.gamification.taptap.utils.TapTapConstants;

import java.util.List;
import java.util.Random;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * @author Rizky on 28/03/18.
 */

public class WidgetTokenViewTapTap extends FrameLayout implements TapCounterView.OnTapClickListener {

    public static final float Y_PIVOT_PERCENT = 0.9f;
    public static final int CRACK_STEP1_SHAKE_DURATION = 150;
    public static final int CRACK_STEP1_DURATION = 350;
    public static final int CRACK_STEP1_DEGREE = 3;
    public static final int CRACK_STEP2_DEGREE = 3;
    public static final int CRACK_STEP2_DURATION = 350;
    public static final int CRACK_STEP2_SHAKE_DURATION = 120;
    public static final int CRACK_STEP2_START_DELAY = 80;
    public static final int CRACK_STEP3_START_DELAY = 100;
    public static final int CRACK_STEP3_DURATION = 350;
    public static final int CRACK_STEP3_SHAKE_DURATION = 150;
    public static final int STEP2_END_MASKED_PERCENT = 30;
    public static final int STEP1_END_MASKED_PERCENT = 70;
    public static final double RATIO_LIGHT_WIDTH = 0.3;
    public static final int CRACK_STEP3_DEGREE = 4;
    private static final long CRACK_BOUNCE_DURATION = 250;
    private static final long CRACK_BOUNCE_BACK_DURATION = 200;
    private static final long GLOW_IN_OUT_DURATION = 1000;

    private static final float LOBBY_IMAGE_SCALE = 1.16976127321f;
    private static final int MIN_TAP_COUNT = 1;
    private static final int MAX_TAP_COUNT = 3;

    private volatile ImageView imageViewFull;
    private volatile MaskedHeightImageView imageViewCracked;
    private volatile ImageView imageViewLeft;
    private volatile ImageView imageViewRight;


    private volatile boolean isTokenClicked;

    private WidgetTokenListener listener;
    private View rootView;
    private AnimatorSet crackingAnimationSet;
    private AnimatorSet crackingAnimationSet1;
    private AnimatorSet crackingAnimationSet2;
    private AnimatorSet crackingAnimationSet3;
    private AnimatorSet initialGlowAnimatorSet;

    private MediaPlayer crackMediaPlayer;
    private AnimatorSet bounceAnimatorSet;
    private ImageView imageFullWhiteEgg;
    private ImageView imageSemiWhiteEgg;
    private AnimatorSet animatorSetFadeOut;
    private AnimatorSet animatorSetRotateEggs;
    private CrackResultEntity crackResult;
    private AnimatorSet animatorSetRotateBackEggs;
    private ObjectAnimator fadeInEggAfterMerge;
    private TapCounterView widgetTapCounter;
    private int tapCount;

    @Override
    public void onTapCountEnds() {

    }

    public interface WidgetTokenListener {
        void onClick();

        void showCrackResult(CrackResultEntity crackResult);

        void reShowEgg();
    }

    public WidgetTokenViewTapTap(@NonNull Context context) {
        super(context);
        init();
    }

    public WidgetTokenViewTapTap(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetTokenViewTapTap(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(WidgetTokenListener listener) {
        this.listener = listener;
    }

    private void init() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_token_tap_tap, this, true);
        imageViewFull = rootView.findViewById(R.id.imagefull);
        imageViewCracked = rootView.findViewById(R.id.imagecracked);
        imageViewLeft = rootView.findViewById(R.id.imageleft);
        imageViewRight = rootView.findViewById(R.id.imageright);
        imageFullWhiteEgg = rootView.findViewById(R.id.image_full_white_egg);
        imageSemiWhiteEgg = rootView.findViewById(R.id.image_semi_white_egg);
        widgetTapCounter = rootView.findViewById(R.id.widget_tap_counter);

        imageViewFull.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTokenClicked && tapCount == 1) {
                    clearTokenAnimationAndCrack();
                    widgetTapCounter.onTap();
                    isTokenClicked = true;
                } else if (tapCount > 1) {
                    tapCount--;
                    shakeEggOnTap();
                    widgetTapCounter.onTap();
                }

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

    @SuppressWarnings("SuspiciousNameCombination")
    private void initImageBound() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageWidth = TokenMarginUtil.getEggWidth(rootWidth, rootHeight);
        int imageHeight = imageWidth;
        int imageMarginBottom = TokenMarginUtil.getEggMarginBottom(rootHeight);
        int imageMarginTop = imageMarginBottom - imageHeight;
        int marginDiff = (int) (((imageHeight * LOBBY_IMAGE_SCALE) - imageHeight) / 2.0f);
        int imageLobbyMarginTop = imageMarginBottom - imageHeight - marginDiff;

        LayoutParams ivFullLp = (LayoutParams) imageViewFull.getLayoutParams();
        ivFullLp.width = imageWidth;
        ivFullLp.height = imageHeight;
        ivFullLp.gravity = CENTER_HORIZONTAL;
        ivFullLp.topMargin = imageMarginTop;
        imageViewFull.requestLayout();

        LayoutParams ivCrackedLp = (LayoutParams) imageViewCracked.getLayoutParams();
        ivCrackedLp.width = imageWidth;
        ivCrackedLp.height = imageHeight;
        ivCrackedLp.gravity = CENTER_HORIZONTAL;
        ivCrackedLp.topMargin = imageMarginTop;
        imageViewCracked.requestLayout();

        LayoutParams ivFullWhiteLp = (LayoutParams) imageFullWhiteEgg.getLayoutParams();
        ivFullWhiteLp.width = (int) (imageWidth * LOBBY_IMAGE_SCALE);
        ivFullWhiteLp.height = (int) (imageHeight * LOBBY_IMAGE_SCALE);
        ivFullWhiteLp.gravity = CENTER_HORIZONTAL;
        ivFullWhiteLp.topMargin = imageLobbyMarginTop;
        imageFullWhiteEgg.requestLayout();

        LayoutParams ivSemiWhiteLp = (LayoutParams) imageSemiWhiteEgg.getLayoutParams();
        ivSemiWhiteLp.width = (int) (imageWidth * LOBBY_IMAGE_SCALE);
        ivSemiWhiteLp.height = (int) (imageHeight * LOBBY_IMAGE_SCALE);
        ivSemiWhiteLp.gravity = CENTER_HORIZONTAL;
        ivSemiWhiteLp.topMargin = imageLobbyMarginTop;
        imageSemiWhiteEgg.requestLayout();

        LayoutParams ivLeftLp = (LayoutParams) imageViewLeft.getLayoutParams();
        ivLeftLp.width = imageWidth;
        ivLeftLp.height = imageHeight;
        ivLeftLp.gravity = CENTER_HORIZONTAL;
        ivLeftLp.topMargin = imageMarginTop;
        imageViewLeft.requestLayout();

        LayoutParams ivRightLp = (LayoutParams) imageViewRight.getLayoutParams();
        ivRightLp.width = imageWidth;
        ivRightLp.height = imageHeight;
        ivRightLp.gravity = CENTER_HORIZONTAL;
        ivRightLp.topMargin = imageMarginTop;
        imageViewRight.requestLayout();


        int lightImageWidth = (int) (RATIO_LIGHT_WIDTH * imageWidth);
        int lightImageHeight = lightImageWidth;
        int marginTopLightRight = imageMarginBottom - (int) (0.80 * imageHeight) - lightImageHeight / 2;
        int marginLeftLightRight = (int) (0.5 * (rootWidth + (int) (0.70 * imageWidth) - lightImageWidth));
        FrameLayout.LayoutParams ivLightRightLp = (FrameLayout.LayoutParams) widgetTapCounter.getLayoutParams();
        ivLightRightLp.width = lightImageWidth;
        ivLightRightLp.height = lightImageHeight;
        ivLightRightLp.topMargin = marginTopLightRight;
        ivLightRightLp.leftMargin = marginLeftLightRight;
        widgetTapCounter.requestLayout();


        setVisibility(View.VISIBLE);


    }

    public void setToken(TokenAsset tokenAsset, TokensUser tokenUser) {
        List<String> imageUrls = tokenAsset.getImageV2URLs();
        String full = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_FULL);
        String cracked = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_CRACKED);
        String imageLeftUrl = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_LEFT);
        String imageRightUrl = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_RIGHT);
        String imageFullWhitetUrl = tokenAsset.getGlowImgURL();
        String imageSemiWhiteUrl = tokenAsset.getGlowShadowImgURL();

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
        ImageHandler.loadImageWithSignature(imageFullWhiteEgg, imageFullWhitetUrl, stringSignature);
        ImageHandler.loadImageWithSignature(imageSemiWhiteEgg, imageSemiWhiteUrl, stringSignature);

        show();
        reset(tokenUser);
    }

    public void shakeEggOnTap() {
        imageViewFull.setPivotY(Y_PIVOT_PERCENT * imageViewFull.getHeight());
        PropertyValuesHolder pvhShake =
                PropertyValuesHolder.ofFloat(View.ROTATION, 0, -CRACK_STEP1_DEGREE, 0, CRACK_STEP1_DEGREE, 0);
        ObjectAnimator shakeAnimatorFull = ObjectAnimator.ofPropertyValuesHolder(imageViewFull, pvhShake);
        shakeAnimatorFull.setRepeatMode(ValueAnimator.REVERSE);
        shakeAnimatorFull.setDuration(350);
        shakeAnimatorFull.start();

    }

    public void setEmptyToken(TokenAsset tokenAsset, TokensUser tokenUser) {
        List<String> imageUrls = tokenAsset.getImageV2URLs();
        String empty = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_EMPTY);
        StringSignature stringSignature = new StringSignature(String.valueOf(tokenAsset.getVersion()));
        ImageHandler.loadImageWithSignature(imageViewFull, empty, stringSignature);
        reset(tokenUser);
        this.setVisibility(View.VISIBLE);

    }

    public void hide() {
        this.setVisibility(View.INVISIBLE);
        imageViewFull.setEnabled(false);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
        imageViewFull.setEnabled(true);
    }

    private void glow() {
        if (initialGlowAnimatorSet == null) {
            final PropertyValuesHolder pvhAlpha1 =
                    PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
            initialGlowAnimatorSet = new AnimatorSet();
            ObjectAnimator alphaAnimator = ObjectAnimator.ofPropertyValuesHolder(imageFullWhiteEgg, pvhAlpha1);
            alphaAnimator.setDuration(GLOW_IN_OUT_DURATION);
            alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
            alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
            initialGlowAnimatorSet.play(alphaAnimator);
        }
        imageFullWhiteEgg.setVisibility(View.VISIBLE);
        imageSemiWhiteEgg.setVisibility(View.VISIBLE);
        initialGlowAnimatorSet.start();
    }

    AnimatorSet.AnimatorListener glowFadeOutListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            imageFullWhiteEgg.setVisibility(View.INVISIBLE);
            imageSemiWhiteEgg.setVisibility(View.INVISIBLE);
            imageFullWhiteEgg.clearAnimation();
            imageSemiWhiteEgg.clearAnimation();
            imageViewFull.setEnabled(true);
            Random rand = new Random();
            tapCount = rand.nextInt((MAX_TAP_COUNT - MIN_TAP_COUNT) + 1) + MIN_TAP_COUNT;
            widgetTapCounter.setVisibility(View.VISIBLE);
            widgetTapCounter.initialize(tapCount, R.string.tap_arg_text, WidgetTokenViewTapTap.this);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public void fadeOutEggs() {
        if (initialGlowAnimatorSet != null) {
            initialGlowAnimatorSet.cancel();
        }
        final PropertyValuesHolder pvhAlpha1 =
                PropertyValuesHolder.ofFloat(View.ALPHA, imageFullWhiteEgg.getAlpha(), 0.0f);
        final PropertyValuesHolder pvhAlpha2 =
                PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
        if (animatorSetFadeOut == null) {
            animatorSetFadeOut = new AnimatorSet();
        }
        ObjectAnimator alphaAnimator1 = ObjectAnimator.ofPropertyValuesHolder(imageFullWhiteEgg, pvhAlpha1);
        alphaAnimator1.setDuration((long) (GLOW_IN_OUT_DURATION * imageFullWhiteEgg.getAlpha()));

        ObjectAnimator alphaAnimator2 = ObjectAnimator.ofPropertyValuesHolder(imageSemiWhiteEgg, pvhAlpha2);
        alphaAnimator2.setDuration(GLOW_IN_OUT_DURATION);
        animatorSetFadeOut.playSequentially(alphaAnimator1, alphaAnimator2);
        animatorSetFadeOut.addListener(glowFadeOutListener);
        animatorSetFadeOut.start();

    }

    private void shakeHardAndCrackAnimation() {
        imageViewFull.setPivotY(Y_PIVOT_PERCENT * imageViewFull.getHeight());
        imageViewCracked.setPivotY(Y_PIVOT_PERCENT * imageViewCracked.getHeight());
        imageViewCracked.setVisibility(View.VISIBLE);
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
        playSound(R.raw.crack_egg_tap_tap);
    }

    private void playRewardSound() {
        playSound(R.raw.reward_tap_tap);
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
            shakeAnimatorFull.setRepeatCount(1);
            shakeAnimatorFull.setDuration(CRACK_STEP1_DURATION);

            ObjectAnimator shakeAnimatorCrack = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhShake);
            shakeAnimatorCrack.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorCrack.setRepeatCount(1);
            shakeAnimatorCrack.setDuration(CRACK_STEP1_DURATION);

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
            shakeAnimatorFull.setRepeatCount(1);
            shakeAnimatorFull.setDuration(CRACK_STEP2_DURATION);

            ObjectAnimator shakeAnimatorCrack = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhShake);
            shakeAnimatorCrack.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimatorCrack.setRepeatCount(1);
            shakeAnimatorCrack.setDuration(CRACK_STEP2_DURATION);

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
            PropertyValuesHolder pvhMaskedCrack =
                    PropertyValuesHolder.ofInt(MaskedHeightImageView.MASKED_PERCENT, STEP2_END_MASKED_PERCENT, 0);
            ObjectAnimator maskCrackAnimator = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, pvhMaskedCrack);
            maskCrackAnimator.setInterpolator(new AccelerateInterpolator());
            maskCrackAnimator.setDuration(CRACK_STEP3_DURATION);

            crackingAnimationSet3.playTogether(maskCrackAnimator);
            crackingAnimationSet3.setStartDelay(CRACK_STEP3_START_DELAY);
        }
    }

    public void stopShaking() {
        imageViewFull.setRotation(0);
        imageViewCracked.setRotation(0);
        if (initialGlowAnimatorSet != null) {
            initialGlowAnimatorSet.cancel();
        }
        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
    }

    public void split(CrackResultEntity crackResult) {
        this.crackResult = crackResult;

        bounceAnimatorSet = new AnimatorSet();
        imageViewFull.setVisibility(View.INVISIBLE);
        imageViewFull.clearAnimation();
        imageViewCracked.setPivotY(imageViewFull.getHeight());
        imageViewCracked.setPivotX(imageViewFull.getWidth() * 0.5f);

        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
        if (initialGlowAnimatorSet != null) {
            initialGlowAnimatorSet.cancel();
        }
        if (animatorSetFadeOut != null) {
            animatorSetFadeOut.removeListener(glowFadeOutListener);
            animatorSetFadeOut.cancel();
        }


        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.25f);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.7f);

        ObjectAnimator animCrackBounce = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, scalex, scaley);
        animCrackBounce.setRepeatCount(1);
        animCrackBounce.setRepeatMode(ValueAnimator.REVERSE);
        animCrackBounce.setDuration(CRACK_BOUNCE_DURATION);

        PropertyValuesHolder scalex1 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
        PropertyValuesHolder scaley1 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);

        ObjectAnimator animCrackBounceBack = ObjectAnimator.ofPropertyValuesHolder(imageViewCracked, scalex1, scaley1);
        animCrackBounceBack.setDuration(CRACK_BOUNCE_BACK_DURATION);
        bounceAnimatorSet.playSequentially(animCrackBounce, animCrackBounceBack);
        bounceAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                imageViewCracked.clearAnimation();
                imageViewCracked.setVisibility(View.INVISIBLE);
                imageViewLeft.setVisibility(View.VISIBLE);
                imageViewRight.setVisibility(View.VISIBLE);
                imageViewLeft.setPivotX((float) (0.5 * imageViewLeft.getWidth()));
                imageViewLeft.setPivotY(imageViewLeft.getHeight());
                imageViewRight.setPivotX((float) (0.5 * imageViewLeft.getWidth()));
                imageViewRight.setPivotY(imageViewRight.getHeight());
                initRotateAnimation();
                animatorSetRotateEggs.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        bounceAnimatorSet.start();

    }

    private void initRotateAnimation() {
        if (animatorSetRotateEggs == null) {
            animatorSetRotateEggs = new AnimatorSet();
            AnimatorSet rotateLeft = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.rotate_left);
            AnimatorSet rotateRight = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.rotate_right);
            rotateLeft.setTarget(imageViewLeft);
            rotateRight.setTarget(imageViewRight);
            animatorSetRotateEggs.playTogether(rotateLeft, rotateRight);
        }
        animatorSetRotateEggs.addListener(rotateEggListener);
    }

    public void startRotateBackAnimation() {
        imageViewLeft.setPivotX((float) (0.5 * imageViewLeft.getWidth()));
        imageViewLeft.setPivotY(imageViewLeft.getHeight());
        imageViewRight.setPivotX((float) (0.5 * imageViewLeft.getWidth()));
        imageViewRight.setPivotY(imageViewRight.getHeight());
        if (animatorSetRotateBackEggs == null) {
            animatorSetRotateBackEggs = new AnimatorSet();
            AnimatorSet rotateLeftBack = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.rotate_left_back);
            AnimatorSet rotateRightBack = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.rotate_right_back);
            rotateLeftBack.setTarget(imageViewRight);
            rotateRightBack.setTarget(imageViewLeft);
            animatorSetRotateBackEggs.playTogether(rotateLeftBack, rotateRightBack);
        }
        animatorSetRotateBackEggs.addListener(rotateBackListener);
        animatorSetRotateBackEggs.start();

    }

    AnimatorSet.AnimatorListener rotateEggListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            playRewardSound();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            listener.showCrackResult(crackResult);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    AnimatorSet.AnimatorListener rotateBackListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            imageFullWhiteEgg.setVisibility(View.VISIBLE);
            imageViewLeft.setVisibility(View.INVISIBLE);
            imageViewLeft.clearAnimation();
            imageViewRight.setVisibility(View.INVISIBLE);
            imageViewRight.clearAnimation();
            imageViewFull.setVisibility(View.VISIBLE);
            final PropertyValuesHolder pvhAlpha1 =
                    PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);


            fadeInEggAfterMerge = ObjectAnimator.ofPropertyValuesHolder(imageFullWhiteEgg, pvhAlpha1);
            fadeInEggAfterMerge.setDuration(1000);
            fadeInEggAfterMerge.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageFullWhiteEgg.setVisibility(View.INVISIBLE);
                    imageFullWhiteEgg.clearAnimation();
                    listener.reShowEgg();

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });


            fadeInEggAfterMerge.start();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private void reset(TokensUser tokenUser) {
        isTokenClicked = false;

        imageViewRight.clearAnimation();
        imageViewRight.setVisibility(View.INVISIBLE);

        imageViewLeft.clearAnimation();
        imageViewLeft.setVisibility(View.INVISIBLE);

        imageFullWhiteEgg.clearAnimation();
        imageFullWhiteEgg.setVisibility(View.INVISIBLE);

        imageSemiWhiteEgg.clearAnimation();
        imageSemiWhiteEgg.setVisibility(View.INVISIBLE);

        imageViewFull.setVisibility(View.VISIBLE);
        imageViewCracked.reset();

        widgetTapCounter.setVisibility(View.INVISIBLE);

        if (TapTapConstants.TokenState.STATE_LOBBY.equalsIgnoreCase(tokenUser.getState())) {
            glow();
            imageViewFull.setEnabled(false);
        } else if (TapTapConstants.TokenState.STATE_EMPTY.equalsIgnoreCase(tokenUser.getState())) {
            imageViewFull.setEnabled(false);
        } else if (TapTapConstants.TokenState.STATE_CRACK_LIMITED.equalsIgnoreCase(tokenUser.getState())
                || TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenUser.getState())) {
            Random rand = new Random();
            tapCount = rand.nextInt((MAX_TAP_COUNT - MIN_TAP_COUNT) + 1) + MIN_TAP_COUNT;
            widgetTapCounter.setVisibility(View.VISIBLE);
            widgetTapCounter.initialize(tapCount, R.string.tap_arg_text, this);

        }

    }

    public void clearTokenAnimation() {
        if (initialGlowAnimatorSet != null) {
            initialGlowAnimatorSet.cancel();
        }
        if (crackingAnimationSet != null) {
            crackingAnimationSet.cancel();
        }
        if (bounceAnimatorSet != null) {
            bounceAnimatorSet.cancel();
        }
        if (animatorSetRotateEggs != null) {
            animatorSetRotateEggs.removeListener(rotateEggListener);
            animatorSetRotateEggs.cancel();
        }

        if (animatorSetRotateBackEggs != null) {
            animatorSetRotateBackEggs.removeListener(rotateBackListener);
            animatorSetRotateBackEggs.cancel();
        }

        if (animatorSetFadeOut != null) {
            animatorSetFadeOut.removeListener(glowFadeOutListener);
            animatorSetFadeOut.cancel();
        }

        if (fadeInEggAfterMerge != null) {
            fadeInEggAfterMerge.cancel();
        }
    }

    public void clearTokenAnimationAndCrack() {
        clearTokenAnimation();
        shakeHardAndCrackAnimation();
        listener.onClick();
    }

}
