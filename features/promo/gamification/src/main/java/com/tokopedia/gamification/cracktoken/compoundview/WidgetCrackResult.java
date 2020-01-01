package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tokopedia.abstraction.common.utils.HexValidator;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.model.GeneralErrorCrackResult;
import com.tokopedia.gamification.cracktoken.util.AnimatorArray;
import com.tokopedia.gamification.cracktoken.util.BounceBackExponentialInterpolator;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rizky on 29/03/18.
 */

public class WidgetCrackResult extends RelativeLayout {

    private static final String TEXT_SIZE_REWARD_LARGE = "large";
    private static final String TEXT_SIZE_REWARD_MEDIUM = "medium";
    private static final String TEXT_SIZE_REWARD_SMALL = "small";
    private static final int DURATION_ROTATION_CRACK_RESULT = 15000;
    private static final int DURATION_ALPHA_ANIM = 800;
    private static final int DURATION_ALPHA_ANIM_TEXT = 500;
    private static final long SLIDE_INFO_LEFT_TO_RIGHT_DURATION = 250;
    private static final long SLIDE_INFO_LEFT_TO_RIGHT_START_DELAY = 100;
    private static final long SLIDE_INFO_LEFT_TO_RIGHT_ALPHA_DURATION = 250;
    private static final long COUNTER_ANIMATION_DURATION = 500;
    private static final long SLIDE_INFO_LEFT_TO_RIGHT_ALPHA_DURATION_START_OFFSET = 500;

    private ImageView imageViewBgCrackResult;
    private ImageView imageViewCrackResult;
    private View backgroundViewCrackResult;
    private LinearLayout containerTextCrackResult;
    private TextView textCrackResultLabel;
    private TextView tvRewardsTip;
    private TextView tvRewardsText;

    private LinearLayout listCrackResultText;
    private Typography buttonReturn;
    private Typography buttonCta;
    private CrackResultEntity crackResult;

    private WidgetCrackResultListener listener;
    private AnimatorSet counterAnimatorSet;
    private ArrayList<Animator> animationList;

    public interface WidgetCrackResultListener {
        void onClickCtaButton(CrackResultEntity crackResult, String titleBtn);

        void onTrackingCloseRewardButton(CrackResultEntity crackResult);

        void onClickReturnButton(CrackResultEntity crackResult, String titleBtn);

        void onClickCloseButton();

        void onClickCloseButtonWhenError();

        void onCrackResultCleared();

        void renderBenefits(List<CrackBenefitEntity> benefits, String benefitType);
    }

    public WidgetCrackResult(Context context) {
        super(context);
        init();
    }

    public WidgetCrackResult(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetCrackResult(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_crack_result, this, true);
        imageViewBgCrackResult = view.findViewById(R.id.image_bg_reward);
        imageViewCrackResult = view.findViewById(R.id.image_reward);
        backgroundViewCrackResult = view.findViewById(R.id.background_view_reward);
        containerTextCrackResult = view.findViewById(R.id.container_text_reward);
        listCrackResultText = view.findViewById(R.id.view_list_reward_text);
        buttonReturn = view.findViewById(R.id.button_return);
        buttonCta = view.findViewById(R.id.button_cta);
        textCrackResultLabel = view.findViewById(R.id.text_crack_result_label);
        tvRewardsTip = view.findViewById(R.id.tvRewardsTip);

        animationList = new ArrayList<>();
    }

    public void showCrackResult(CrackResultEntity crackResult) {
        this.crackResult = crackResult;
        showCrackResultImageAnimation(crackResult);
        showCrackResultBackgroundAnimation();
        showListCrackResultText(crackResult.getBenefits(), crackResult.getBenefitLabel());
        listener.renderBenefits(crackResult.getBenefits(), crackResult.getBenefitType());
        renderCtaButton(crackResult);
        renderReturnButton(crackResult);
    }

    private void showCrackResultImageAnimation(CrackResultEntity crackResult) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        float screenHeightQuarter = metrics.heightPixels * (12.0f / 100);
        screenHeightQuarter = screenHeightQuarter - actionBarHeight;

        final AnimationSet animationCrackResult = new AnimationSet(true);
        Animation scaleAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_crack_result);
        animationCrackResult.addAnimation(scaleAnimationCrackResult);
        TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationCrackResult.addAnimation(translateAnimationCrackResult);
        animationCrackResult.setFillAfter(true);
        animationCrackResult.setDuration(1000);
        animationCrackResult.setInterpolator(new BounceBackExponentialInterpolator(0.1, 15));

        if (crackResult.getImageBitmap() != null && !crackResult.getImageBitmap().isRecycled()) {
            imageViewCrackResult.setImageBitmap(crackResult.getImageBitmap());
            startImageResultAnimation(imageViewCrackResult, animationCrackResult);
        } else {
            if (!TextUtils.isEmpty(crackResult.getImageUrl())) {
                Glide.with(getContext())
                        .asBitmap()
                        .load(crackResult.getImageUrl())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageViewCrackResult.setImageBitmap(resource);
                                imageViewCrackResult.startAnimation(animationCrackResult);
                                imageViewCrackResult.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        }

        AnimationSet animationBgCrackResult = new AnimationSet(true);

        Animation rotateAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_bg_crack_result);
        rotateAnimationCrackResult.setDuration(DURATION_ROTATION_CRACK_RESULT);
        animationBgCrackResult.addAnimation(rotateAnimationCrackResult);

        Animation scaleAnimationBgCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_bg_crack_result);
        animationBgCrackResult.addAnimation(scaleAnimationBgCrackResult);

        TranslateAnimation translateAnimationBgCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationBgCrackResult.addAnimation(translateAnimationBgCrackResult);

        startImageResultAnimation(imageViewBgCrackResult, animationBgCrackResult);
    }

    private void startImageResultAnimation(ImageView imageViewCrack, AnimationSet animationCrackResult) {
        post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null) {
                    imageViewCrack.startAnimation(animationCrackResult);
                    imageViewCrack.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showCrackResultBackgroundAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(DURATION_ALPHA_ANIM);

        backgroundViewCrackResult.setAnimation(alphaAnimation);
        backgroundViewCrackResult.setBackgroundColor(getResources().getColor(R.color.font_black_primary_70));
        backgroundViewCrackResult.setVisibility(View.VISIBLE);
    }

    public void showListCrackResultText(List<CrackBenefitEntity> rewardTexts, String labelCrackResult) {

        StringBuilder tempRewardPlaceHolderStrBuilder = new StringBuilder();
        StringBuilder tempRewardInitialStrBuilder = new StringBuilder();

        textCrackResultLabel.setText(labelCrackResult);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(DURATION_ALPHA_ANIM_TEXT);
        containerTextCrackResult.setAnimation(alphaAnimation);
        containerTextCrackResult.setVisibility(View.VISIBLE);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listCrackResultText.getChildCount() == 1) {

                    CrackBenefitEntity animatedRewardText = null;
                    View v = listCrackResultText.getChildAt(0);
                    if (v instanceof TextView) {
                        TextView textView = (TextView) v;
                        List<Pair<Integer, Integer>> rewardTextPair = new ArrayList<>();
                        for (CrackBenefitEntity rewardText : rewardTexts) {
                            if (!TextUtils.isEmpty(rewardText.getAnimationType())) {
                                int before = rewardText.getValueBefore();
                                int after = rewardText.getValueAfter();
                                rewardTextPair.add(new Pair<>(before, after));

                                animatedRewardText = rewardText;
                            }
                        }

                        String placeHolderText = tempRewardPlaceHolderStrBuilder.toString();

                        AnimatorArray animatorArray = new AnimatorArray();
                        animatorArray.toInt(rewardTextPair, list -> {
                            String text = String.format(placeHolderText, list.toArray());
                            textView.setText(text);
                            return 1;
                        });
                        for (ValueAnimator valueAnimator : animatorArray.getAnimatorList()) {
                            valueAnimator.setDuration(COUNTER_ANIMATION_DURATION);
                        }
                        animationList.clear();
                        animationList.addAll(animatorArray.getAnimatorList());

                        if (animatedRewardText != null) {
                            setDataInTooltip(animatedRewardText);
                        }
                    }
                    startCounterAnimation(rewardTexts != null ? rewardTexts.size() : 0);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        int margin = getContext().getResources().getDimensionPixelOffset(R.dimen.dp_20);
        layoutParams.setMargins(margin, 0, margin, 0);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setMaxLines(2);
        textView.setLayoutParams(layoutParams);

        float textSizeInPx = getContext().getResources().getDimension(R.dimen.gami_text_size_reward);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPx);


        for (CrackBenefitEntity rewardText : rewardTexts) {

            if (!TextUtils.isEmpty(tempRewardPlaceHolderStrBuilder)) {
                tempRewardPlaceHolderStrBuilder.append(", ");
            }

            if (!TextUtils.isEmpty(tempRewardInitialStrBuilder)) {
                tempRewardInitialStrBuilder.append(", ");
            }

            if (!TextUtils.isEmpty(rewardText.getAnimationType())) {
                String rewardStr = "";
                if (!TextUtils.isEmpty(rewardText.getTemplateText())) {
                    int index = rewardText.getTemplateText().indexOf(' ');
                    if (index != -1 && index < rewardText.getTemplateText().length()) {
                        rewardStr = rewardText.getTemplateText().substring(index + 1);
                    }
                }
                tempRewardInitialStrBuilder.append(String.format(getContext().getString(R.string.rewards_increased_points), String.valueOf(rewardText.getValueBefore()), rewardStr));

                tempRewardPlaceHolderStrBuilder.append(getContext().getString(R.string.gf_placeholder_text));
                tempRewardPlaceHolderStrBuilder.append(" ");
                tempRewardPlaceHolderStrBuilder.append(rewardStr);
            } else {
                tempRewardPlaceHolderStrBuilder.append(rewardText.getText());
                tempRewardInitialStrBuilder.append(rewardText.getText());
            }

            if (HexValidator.validate(rewardText.getColor())) {
                textView.setTextColor(Color.parseColor(rewardText.getColor()));
            } else {
                textView.setTextColor(getContext().getResources().getColor(R.color.default_text_reward_color));
            }
        }
        textView.setText(tempRewardInitialStrBuilder);
        listCrackResultText.addView(textView);
        tvRewardsText = textView;

    }

    private void startCounterAnimation(int rewardsCount) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null) {
                    if (animationList != null && animationList.size() > 0) {
                        counterAnimatorSet = new AnimatorSet();
                        counterAnimatorSet.playTogether(animationList);
                        counterAnimatorSet.start();
                    }
                }
            }
        }, rewardsCount * 150);
    }

    private void setDataInTooltip(CrackBenefitEntity rewardText) {
        int multiplierColor;
        if (HexValidator.validate(rewardText.getColor())) {
            multiplierColor = Color.parseColor(rewardText.getColor());
        } else {
            multiplierColor = getContext().getResources().getColor(R.color.default_text_reward_color);
        }
        String infoString = rewardText.getTierInformation() + " " + rewardText.getMultiplier() + "X";
        Spannable wordtoSpan = new SpannableString(infoString);

        wordtoSpan.setSpan(new ForegroundColorSpan(multiplierColor), infoString.indexOf(rewardText.getMultiplier()), infoString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textViewTierInfo = tvRewardsTip;
        textViewTierInfo.setTypeface(textViewTierInfo.getTypeface(), Typeface.BOLD);
        textViewTierInfo.setText(wordtoSpan);
        textViewTierInfo.clearAnimation();

        tvRewardsTip.setAlpha(0f);
        tvRewardsTip.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            tvRewardsTip.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            tvRewardsTip.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        int height = tvRewardsTip.getMeasuredHeight();
                        animateTooltip(textViewTierInfo, height);
                    }
                });
    }


    private void animateTooltip(TextView textViewTierInfo, float height) {

        int[] locationOfTvRewards = new int[2];
        tvRewardsText.getLocationInWindow(locationOfTvRewards);

        int finalY = (int) (locationOfTvRewards[1] - getStatusBarHeight() - height);
        int startY = (int) (finalY - dpToPx(getContext(), 10));
        textViewTierInfo.setY(startY);

        AnimatorSet animatorSet = new AnimatorSet();
        long translationDuration = 700L;

        ValueAnimator translateAnimator = ValueAnimator.ofFloat(startY, finalY);
        translateAnimator.setDuration(translationDuration);
        translateAnimator.setStartDelay(SLIDE_INFO_LEFT_TO_RIGHT_START_DELAY);
        translateAnimator.addUpdateListener(animation -> {
            textViewTierInfo.setY((Float) animation.getAnimatedValue());
        });

        long alphaDuration = 300L;
        ValueAnimator alphaAnimation = ValueAnimator.ofFloat(0, 1);
        alphaAnimation.setDuration(alphaDuration);
        alphaAnimation.setStartDelay(SLIDE_INFO_LEFT_TO_RIGHT_START_DELAY);

        alphaAnimation.addUpdateListener(animation -> {
            textViewTierInfo.setAlpha((Float) animation.getAnimatedValue());
        });

        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textViewTierInfo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                slideAndFadeOutText(textViewTierInfo);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //Do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //Do nothing
            }
        });

        animatorSet.playTogether(translateAnimator, alphaAnimation);
        animatorSet.start();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private Float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private void slideAndFadeOutText(TextView textViewTierInfo) {
        long fadeOutDuration = 700L;
        long startOffsetDuration = 1000L;
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(fadeOutDuration);
        alphaAnimation.setStartOffset(startOffsetDuration);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvRewardsTip.setVisibility(View.GONE);
                alphaAnimation.setAnimationListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textViewTierInfo.startAnimation(alphaAnimation);
    }

    private int convertSize(String size) {
        switch (size) {
            case TEXT_SIZE_REWARD_LARGE:
                return R.dimen.text_size_reward_large;
            case TEXT_SIZE_REWARD_MEDIUM:
                return R.dimen.text_size_reward_medium;
            case TEXT_SIZE_REWARD_SMALL:
                return R.dimen.text_size_reward_small;
            default:
                return R.dimen.text_size_reward_medium;
        }
    }

    public void renderReturnButton(final CrackResultEntity crackResult) {
        if (crackResult.getReturnButton() != null) {
            if (crackResult.isCrackButtonVisible(crackResult.getReturnButton())) {
                buttonReturn.setVisibility(VISIBLE);
                buttonReturn.setText(crackResult.getReturnButton().getTitle());
                buttonReturn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickReturnButton(crackResult, buttonReturn.getText().toString());
                    }
                });
            } else {
                buttonReturn.setVisibility(GONE);
            }
        } else {
            buttonReturn.setVisibility(GONE);
        }
    }

    public void renderCtaButton(final CrackResultEntity crackResult) {
        if (crackResult.getCtaButton() != null) {
            if (crackResult.isCrackButtonVisible(crackResult.getCtaButton())) {
                buttonCta.setVisibility(VISIBLE);
                buttonCta.setText(crackResult.getCtaButton().getTitle());
                buttonCta.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickCtaButton(crackResult, buttonCta.getText().toString());
                    }
                });
            } else {
                buttonCta.setVisibility(GONE);
            }
        } else {
            buttonCta.setVisibility(GONE);
        }
    }

    public void setListener(WidgetCrackResultListener listener) {
        this.listener = listener;
    }

    public void clearCrackResult() {
        backgroundViewCrackResult.clearAnimation();
        backgroundViewCrackResult.setVisibility(View.GONE);
        imageViewCrackResult.clearAnimation();
        imageViewCrackResult.setVisibility(View.GONE);
        imageViewBgCrackResult.clearAnimation();
        imageViewBgCrackResult.setVisibility(View.GONE);
        listCrackResultText.removeAllViews();
        containerTextCrackResult.setVisibility(GONE);
        listener.onCrackResultCleared();
        if (counterAnimatorSet != null) {
            counterAnimatorSet.cancel();
        }
        if (animationList != null) {
            animationList.clear();
        }
    }

    public boolean isShowReward() {
        return imageViewBgCrackResult.isShown() && !isShowCrackError();
    }

    /**
     * to check is the error is actually generated custom error crack or actual crack result from server.
     */
    private boolean isShowCrackError() {
        return crackResult != null && crackResult instanceof GeneralErrorCrackResult;
    }

    public void dismissReward() {
        listener.onClickCloseButton();
    }

}
