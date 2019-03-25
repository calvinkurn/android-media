package com.tokopedia.gamification.taptap.compoundview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.model.GeneralErrorCrackResult;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.taptap.utils.TokenMarginUtilTapTap;
import com.tokopedia.gamification.util.HexValidator;
import com.tokopedia.gamification.util.TapTapAnalyticsTrackerUtil;

import java.util.List;

/**
 * @author Rizky on 29/03/18.
 */

public class WidgetCrackResultTapTap extends RelativeLayout {

    private static final long REWARDS_TEXT_TRANSLATE_START_DELAY = 100;
    private static final long REWARDS_IMAGE_TRANSLATE_DURATION = 150;
    private static final long REWARDS_IMAGE_STABLE_DURATION_AFTER_FIRST_TRANSLATE = 700;
    private static final long REWARD_TEXT_FADE_OUT_START_DELAY = 600;
    private static final long REWARD_TEXT_FADE_OUT_DURATION = 100;

    private ImageView imageViewCrackResult;

    private LinearLayout listCrackResultText;

    private WidgetCrackResultListener listener;
    private AnimatorSet rewardTranslateAnimatorSet;

    public interface WidgetCrackResultListener {
        void onCrackResultCleared();
    }

    public WidgetCrackResultTapTap(Context context) {
        super(context);
        init();
    }

    public WidgetCrackResultTapTap(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetCrackResultTapTap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_crack_result_tap_tap, this, true);
        imageViewCrackResult = rootView.findViewById(R.id.image_reward);
        listCrackResultText = rootView.findViewById(R.id.view_list_reward_text);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initRewardBound(rootView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void initRewardBound(View rootView) {
        int rootHeight = rootView.getHeight();
        int imageMarginBottom = rootView.getHeight() - TokenMarginUtilTapTap.getEggMarginBottom(rootHeight) + getContext().getResources().getDimensionPixelOffset(R.dimen.dp_32);
        RelativeLayout.LayoutParams tvFullLp = (RelativeLayout.LayoutParams) listCrackResultText.getLayoutParams();
        tvFullLp.bottomMargin = imageMarginBottom;
        listCrackResultText.requestLayout();

    }

    public void showCrackResult(CrackResultEntity crackResult) {
        showListCrackResultText(crackResult.getBenefits());
        showCrackResultImageAnimation(crackResult);
        TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.VIEW_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                TapTapAnalyticsTrackerUtil.ActionKeys.REWARDS_IMPRESSION,
                crackResult.getBenefitType());

    }

    private void showCrackResultImageAnimation(CrackResultEntity crackResult) {
        initRewardsTranslateAnimator();

        if (crackResult.getImageBitmap() != null && !crackResult.getImageBitmap().isRecycled()) {
            imageViewCrackResult.setImageBitmap(crackResult.getImageBitmap());
            rewardTranslateAnimatorSet.start();
            imageViewCrackResult.setVisibility(View.VISIBLE);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getContext() != null) {
                        listCrackResultText.setVisibility(View.VISIBLE);
                    }
                }
            }, REWARDS_TEXT_TRANSLATE_START_DELAY);
        } else {
            if (!TextUtils.isEmpty(crackResult.getImageUrl())) {
                Glide.with(getContext())
                        .load(crackResult.getImageUrl())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                imageViewCrackResult.setImageBitmap(resource);
                                rewardTranslateAnimatorSet.start();
                                imageViewCrackResult.setVisibility(View.VISIBLE);
                            }
                        });
            }
        }

    }

    void initRewardsTranslateAnimator() {
        if (rewardTranslateAnimatorSet == null) {
            rewardTranslateAnimatorSet = new AnimatorSet();

            // Set Reward Image Translate Animation
            AnimatorSet animatorSetTranslate2 = new AnimatorSet();

            PropertyValuesHolder pvhAlpha =
                    PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f);
            ObjectAnimator fadeInAnimator = ObjectAnimator.ofPropertyValuesHolder(imageViewCrackResult, pvhAlpha);


            PropertyValuesHolder pvhTranslateImageResult =
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, getResources().getDimension(R.dimen.translate_image_distance));
            ObjectAnimator translate1 = ObjectAnimator.ofPropertyValuesHolder(imageViewCrackResult, pvhTranslateImageResult);


            AnimatorSet animatorSetTranslate1 = new AnimatorSet();
            animatorSetTranslate1.setDuration(REWARDS_IMAGE_TRANSLATE_DURATION);
            animatorSetTranslate1.playTogether(fadeInAnimator, translate1);


            PropertyValuesHolder pvhTranslate2 =
                    PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, getResources().getDimension(R.dimen.translate_image_distance), getResources().getDimension(R.dimen.translate_image_again_distance));
            ObjectAnimator translate2 = ObjectAnimator.ofPropertyValuesHolder(imageViewCrackResult, pvhTranslate2);


            PropertyValuesHolder pvhAlpha2 =
                    PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);

            ObjectAnimator fadeOut = ObjectAnimator.ofPropertyValuesHolder(imageViewCrackResult, pvhAlpha2);

            animatorSetTranslate2.setStartDelay(REWARDS_IMAGE_STABLE_DURATION_AFTER_FIRST_TRANSLATE);
            animatorSetTranslate2.setDuration(REWARDS_IMAGE_TRANSLATE_DURATION);
            animatorSetTranslate2.playTogether(translate2, fadeOut);


            //Set Reward Text Translate Animation
            ObjectAnimator translateRewardsText = ObjectAnimator.ofPropertyValuesHolder(listCrackResultText, pvhTranslateImageResult);

            ObjectAnimator fadeInAnimatorReward = ObjectAnimator.ofPropertyValuesHolder(listCrackResultText, pvhAlpha);

            ObjectAnimator fadeOutAnimatorReward = ObjectAnimator.ofPropertyValuesHolder(listCrackResultText, pvhAlpha2);
            fadeOutAnimatorReward.setStartDelay(REWARD_TEXT_FADE_OUT_START_DELAY);
            fadeOutAnimatorReward.setDuration(REWARD_TEXT_FADE_OUT_DURATION);

            AnimatorSet animatorSetTranslateRewards = new AnimatorSet();
            animatorSetTranslateRewards.playTogether(fadeInAnimatorReward, translateRewardsText);
            animatorSetTranslateRewards.setDuration(REWARDS_IMAGE_TRANSLATE_DURATION);

            AnimatorSet rewardsAnimatorSet = new AnimatorSet();
            rewardsAnimatorSet.playSequentially(animatorSetTranslateRewards, fadeOutAnimatorReward);
            rewardsAnimatorSet.setStartDelay(REWARDS_TEXT_TRANSLATE_START_DELAY);


            AnimatorSet imageTranslateAnimatorSet = new AnimatorSet();
            imageTranslateAnimatorSet.playSequentially(animatorSetTranslate1, animatorSetTranslate2);
            rewardTranslateAnimatorSet.playTogether(rewardsAnimatorSet, imageTranslateAnimatorSet);
            rewardTranslateAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        }
        rewardTranslateAnimatorSet.addListener(rewardTranslateListener);
    }

    AnimatorSet.AnimatorListener rewardTranslateListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            clearCrackResult();
            if (listener != null)
                listener.onCrackResultCleared();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public void showListCrackResultText(List<CrackBenefitEntity> rewardTexts) {
        for (CrackBenefitEntity rewardText : rewardTexts) {
            TextView textView = new TextView(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setText(rewardText.getText());
            textView.setMaxWidth((int) (getScreenWidth() / 2.3f));
            textView.setLayoutParams(layoutParams);
            if (HexValidator.validate(rewardText.getColor())) {
                textView.setTextColor(Color.parseColor(rewardText.getColor()));
            } else {
                textView.setTextColor(getContext().getResources().getColor(R.color.default_text_reward_color));
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.sp_16));
            listCrackResultText.addView(textView);
        }

    }


    public void setListener(WidgetCrackResultListener listener) {
        this.listener = listener;
    }

    public void clearCrackResult() {
        if (rewardTranslateAnimatorSet != null) {
            rewardTranslateAnimatorSet.removeListener(rewardTranslateListener);
            rewardTranslateAnimatorSet.cancel();
        }
        imageViewCrackResult.clearAnimation();
        imageViewCrackResult.setVisibility(View.INVISIBLE);
        listCrackResultText.clearAnimation();
        listCrackResultText.removeAllViews();
        listCrackResultText.setVisibility(View.INVISIBLE);
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}
