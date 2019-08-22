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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.taptap.data.entiity.TokenAsset;
import com.tokopedia.gamification.taptap.data.entiity.TokensUser;
import com.tokopedia.gamification.taptap.utils.TapTapConstants;
import com.tokopedia.gamification.taptap.utils.TokenMarginUtilTapTap;
import com.tokopedia.gamification.taptap.utils.TapTapAnalyticsTrackerUtil;

import java.util.List;
import java.util.Random;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * @author Rizky on 28/03/18.
 */

public class WidgetTokenViewTapTap extends FrameLayout implements TapCounterView.OnTapClickListener {

    public static final float Y_PIVOT_PERCENT = 0.9f;
    public static final int CRACK_STEP1_DURATION = 100;
    public static final int CRACK_STEP1_DEGREE = 3;
    public static final int CRACK_STEP2_DEGREE = 3;
    public static final int CRACK_STEP2_DURATION = 100;
    public static final int CRACK_STEP2_START_DELAY = 30;
    public static final int CRACK_STEP3_START_DELAY = 50;
    public static final int CRACK_STEP3_DURATION = 100;
    public static final int STEP2_END_MASKED_PERCENT = 30;
    public static final int STEP1_END_MASKED_PERCENT = 70;
    public static final double RATIO_LIGHT_WIDTH = 0.3;
    private static final long CRACK_BOUNCE_DURATION = 100;
    private static final long CRACK_BOUNCE_BACK_DURATION = 100;
    private static final long GLOW_IN_OUT_DURATION = 1000;

    private static final float LOBBY_IMAGE_SCALE = 1.16976127321f;
    private static final int MIN_TAP_COUNT = 1;
    private static final int MAX_TAP_COUNT = 5;
    private static final float TAP_BACKGROUND_IMAGE_HEIGHT_SCALE = 1.10909f;
    private static final long ROTATE_EGGS_DURATION = 150;
    private static final float ROTATE_LEFT_FROM_ANGLE = -3;
    private static final float ROTATE_LEFT_TO_ANGLE = -50;
    private static final float ROTATE_RIGHT_FROM_ANGLE = 3;
    private static final float ROTATE_RIGHT_TO_ANGLE = 50;
    private static final float ROTATE_LEFT_BACK_FROM_ANGLE = 50;
    private static final float ROTATE_LEFT_BACK_TO_ANGLE = 0;
    private static final float ROTATE_RIGHT_BACK_FROM_ANGLE = -50;
    private static final float ROTATE_RIGHT_BACK_TO_ANGLE = 0;

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
    private long tokenUserId;

    @Override
    public void onTapCountEnds() {

    }

    public interface WidgetTokenListener {
        void onClick();

        void showCrackResult(CrackResultEntity crackResult);

        void reShowEgg();

        void reShowFromLobby();
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
                onFullEggClick();
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

    private void onFullEggClick() {
        if (!isTokenClicked && tapCount == 1) {
            clearTokenAnimationAndCrack();
            widgetTapCounter.onTap();
            isTokenClicked = true;
            sendEggClickEvent();
        } else if (tapCount > 1) {
            playTapSound();
            tapCount--;
            shakeEggOnTap();
            widgetTapCounter.onTap();
        }
    }

    private void sendEggClickEvent() {
        TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                TapTapAnalyticsTrackerUtil.ActionKeys.TAP_EGG_CLICKED,
                String.valueOf(tokenUserId));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void initImageBound() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageWidth = TokenMarginUtilTapTap.getEggWidth(rootWidth, rootHeight);
        int imageHeight = imageWidth;
        int imageMarginBottom = TokenMarginUtilTapTap.getEggMarginBottom(rootHeight);
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
        int lightImageHeight = (int) (TAP_BACKGROUND_IMAGE_HEIGHT_SCALE * lightImageWidth);
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
        if (imageUrls == null || imageUrls.size() < GamificationConstants.EggImageUrlIndex.IMAGE_ARRAY_SIZE_NORMAL) {
            return;
        }
        this.tokenUserId = tokenUser.getTokenUserID();
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

    public void setEmptyToken(TokenAsset tokenAsset, TokensUser tokenUser) {
        List<String> imageUrls = tokenAsset.getImageV2URLs();
        if (imageUrls == null || imageUrls.size() < GamificationConstants.EggImageUrlIndex.IMAGE_ARRAY_SIZE_EMPTY) {
            return;
        }
        this.tokenUserId = tokenUser.getTokenUserID();
        String empty = imageUrls.get(GamificationConstants.EggImageUrlIndex.INDEX_TOKEN_EMPTY);
        StringSignature stringSignature = new StringSignature(String.valueOf(tokenAsset.getVersion()));
        ImageHandler.loadImageWithSignature(imageViewFull, empty, stringSignature);
        reset(tokenUser);
        this.setVisibility(View.VISIBLE);

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
            if (listener != null)
                listener.reShowFromLobby();
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

    private void playEggBounce() {
        playSound(R.raw.egg_bounce_tap_tap);
    }

    private void playTapSound() {
        playSound(R.raw.egg_tap_response_tap_tap);
    }

    private void playReverseEggSound() {
        playSound(R.raw.reverse_egg_tap_tap);
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

    public void stopMediaPlayer() {
        if (crackMediaPlayer != null && crackMediaPlayer.isPlaying()) {
            crackMediaPlayer.stop();
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
        playEggBounce();

    }

    private void initRotateAnimation() {
        if (animatorSetRotateEggs == null) {
            animatorSetRotateEggs = new AnimatorSet();
            initRotateAnimatorSet();
        }
        animatorSetRotateEggs.addListener(rotateEggListener);
    }

    private void initRotateAnimatorSet() {
        //rotate Left
        AnimatorSet rotateLeft = new AnimatorSet();
        final PropertyValuesHolder pvhRotateLeft =
                PropertyValuesHolder.ofFloat(View.ROTATION, ROTATE_LEFT_FROM_ANGLE, ROTATE_LEFT_TO_ANGLE);
        final PropertyValuesHolder pvhRotateLeftTranslateY =
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, getResources().getInteger(R.integer.translate_egg_to_y_delta));
        ObjectAnimator rotateLeftAnimator = ObjectAnimator.ofPropertyValuesHolder(imageViewLeft, pvhRotateLeft);
        ObjectAnimator rotateLeftTranslateY = ObjectAnimator.ofPropertyValuesHolder(imageViewLeft, pvhRotateLeftTranslateY);
        rotateLeft.playTogether(rotateLeftAnimator, rotateLeftTranslateY);
        rotateLeft.setDuration(ROTATE_EGGS_DURATION);

        //rotate Right
        AnimatorSet rotateRight = new AnimatorSet();
        final PropertyValuesHolder pvhRotateRight =
                PropertyValuesHolder.ofFloat(View.ROTATION, ROTATE_RIGHT_FROM_ANGLE, ROTATE_RIGHT_TO_ANGLE);
        final PropertyValuesHolder pvhRotateRightTranslateY =
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, getResources().getInteger(R.integer.translate_egg_to_y_delta));


        ObjectAnimator rotateRightAnimator = ObjectAnimator.ofPropertyValuesHolder(imageViewRight, pvhRotateRight);
        ObjectAnimator rotateRightTranslateY = ObjectAnimator.ofPropertyValuesHolder(imageViewRight, pvhRotateRightTranslateY);
        rotateRight.playTogether(rotateRightAnimator, rotateRightTranslateY);
        rotateRight.setDuration(ROTATE_EGGS_DURATION);
        animatorSetRotateEggs.playTogether(rotateLeft, rotateRight);
    }

    public void startRotateBackAnimation() {
        imageViewLeft.setPivotX((float) (0.5 * imageViewLeft.getWidth()));
        imageViewLeft.setPivotY(imageViewLeft.getHeight());
        imageViewRight.setPivotX((float) (0.5 * imageViewLeft.getWidth()));
        imageViewRight.setPivotY(imageViewRight.getHeight());
        if (animatorSetRotateBackEggs == null) {
            animatorSetRotateBackEggs = new AnimatorSet();
            initRotateBackAnimatorSet();

        }
        animatorSetRotateBackEggs.addListener(rotateBackListener);
        animatorSetRotateBackEggs.start();
        playReverseEggSound();

    }

    private void initRotateBackAnimatorSet() {

        AnimatorSet rotateLeftBack = new AnimatorSet();
        final PropertyValuesHolder pvhRotateLeftBack =
                PropertyValuesHolder.ofFloat(View.ROTATION, ROTATE_LEFT_BACK_FROM_ANGLE, ROTATE_LEFT_BACK_TO_ANGLE);
        final PropertyValuesHolder pvhRotateLeftTranslateYBack =
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getResources().getInteger(R.integer.translate_egg_to_y_delta), 0);


        ObjectAnimator rotateLeftAnimatorBack = ObjectAnimator.ofPropertyValuesHolder(imageViewRight, pvhRotateLeftBack);
        ObjectAnimator rotateLeftTranslateYBack = ObjectAnimator.ofPropertyValuesHolder(imageViewRight, pvhRotateLeftTranslateYBack);
        rotateLeftBack.playTogether(rotateLeftAnimatorBack, rotateLeftTranslateYBack);
        rotateLeftBack.setDuration(ROTATE_EGGS_DURATION);


        AnimatorSet rotateRightBack = new AnimatorSet();
        final PropertyValuesHolder pvhRotateRightBack =
                PropertyValuesHolder.ofFloat(View.ROTATION, ROTATE_RIGHT_BACK_FROM_ANGLE, ROTATE_RIGHT_BACK_TO_ANGLE);
        final PropertyValuesHolder pvhRotateRightTranslateYBack =
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getResources().getInteger(R.integer.translate_egg_to_y_delta), 0);


        ObjectAnimator rotateRightAnimatorBack = ObjectAnimator.ofPropertyValuesHolder(imageViewLeft, pvhRotateRightBack);
        ObjectAnimator rotateRightTranslateYBack = ObjectAnimator.ofPropertyValuesHolder(imageViewLeft, pvhRotateRightTranslateYBack);
        rotateRightBack.playTogether(rotateRightAnimatorBack, rotateRightTranslateYBack);
        rotateRightBack.setDuration(ROTATE_EGGS_DURATION);
        animatorSetRotateBackEggs.playTogether(rotateLeftBack, rotateRightBack);

    }

    AnimatorSet.AnimatorListener rotateEggListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            playRewardSound();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (listener != null)
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
                    if (listener != null)
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

    public void resetForUnlimitedCrack(TokensUser tokensUser) {
        reset(tokensUser);
    }

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
        imageViewCracked.setVisibility(View.INVISIBLE);
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

    public void releaseMediaPlayer() {
        if (crackMediaPlayer != null) {
            crackMediaPlayer.release();
        }
    }


    /**
     * This is method is clearing all Animation and releasing media player
     *  called from onDestroyView of Fragment
     * */
    public void releaseResourcesOnDestroy() {
        clearTokenAnimation();
        releaseMediaPlayer();
    }

    public void clearTokenAnimationAndCrack() {
        clearTokenAnimation();
        shakeHardAndCrackAnimation();
        if (listener != null)
            listener.onClick();
    }

}
