package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.model.CrackBenefit;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.model.GeneralErrorCrackResult;
import com.tokopedia.gamification.cracktoken.util.BounceBackExponentialInterpolator;
import com.tokopedia.gamification.util.HexValidator;

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
    private static final int DURATION_ANIM_BG_CRACK_RESULT = 1000;
    private static final int DURATION_ALPHA_ANIM = 1500;
    private static final int DURATION_ALPHA_ANIM_TEXT = 500;
    private static final long SLIDE_INFO_LEFT_TO_RIGHT_DURATION = 500;
    private static final long SLIDE_INFO_LEFT_TO_RIGHT_START_DELAY = 100;
    private static final long SLIDE_INFO_LEFT_TO_RIGHT_ALPHA_DURATION = 502;
    private static final long COUNTER_ANIMATION_DURATION = 500;
    private static final long COUNTER_ANIMATION_START_DELAY = 200;

    private ImageView imageViewBgCrackResult;
    private ImageView imageViewCrackResult;
    private View backgroundViewCrackResult;
    private LinearLayout containerTextCrackResult;
    private TextView textCrackResultLabel;

    private LinearLayout listCrackResultText;
    private Button buttonReturn;
    private Button buttonCta;
    private CrackResult crackResult;

    private WidgetCrackResultListener listener;
    private FrameLayout listCrackResultParent;
    private AnimatorSet counterAnimatorSet;
    private ArrayList<Animator> animationList;
    private List<TextView> tvTierInfoList;

    public interface WidgetCrackResultListener {
        void onClickCtaButton(CrackResult crackResult, String titleBtn);

        void onTrackingCloseRewardButton(CrackResult crackResult);

        void onClickReturnButton(CrackResult crackResult, String titleBtn);

        void onClickCloseButton();

        void onClickCloseButtonWhenError();

        void onCrackResultCleared();

        void renderBenefits(List<CrackBenefit> benefits, String benefitType);
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
        listCrackResultParent = view.findViewById(R.id.view_list_reward_parent);
    }

    public void showCrackResult(CrackResult crackResult) {
        this.crackResult = crackResult;
        showCrackResultImageAnimation(crackResult);
        showCrackResultBackgroundAnimation();
        showListCrackResultText(crackResult.getBenefits(), crackResult.getBenefitLabel());
        listener.renderBenefits(crackResult.getBenefits(), crackResult.getBenefitType());
        renderCtaButton(crackResult);
        renderReturnButton(crackResult);
    }

    private void showCrackResultImageAnimation(CrackResult crackResult) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        float screenHeightQuarter = metrics.heightPixels / 4;
        screenHeightQuarter = screenHeightQuarter - actionBarHeight;

        final AnimationSet animationCrackResult = new AnimationSet(true);
        Animation scaleAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_crack_result);
        animationCrackResult.addAnimation(scaleAnimationCrackResult);
        TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationCrackResult.addAnimation(translateAnimationCrackResult);
        animationCrackResult.setFillAfter(true);
        animationCrackResult.setStartOffset(500);
        animationCrackResult.setDuration(1000);
        animationCrackResult.setInterpolator(new BounceBackExponentialInterpolator(0.1, 15));

        if (crackResult.getImageBitmap() != null && !crackResult.getImageBitmap().isRecycled()) {
            imageViewCrackResult.setImageBitmap(crackResult.getImageBitmap());
            imageViewCrackResult.startAnimation(animationCrackResult);
            imageViewCrackResult.setVisibility(View.VISIBLE);
        } else {
            if (!TextUtils.isEmpty(crackResult.getImageUrl())) {
                Glide.with(getContext())
                        .load(crackResult.getImageUrl())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                imageViewCrackResult.setImageBitmap(resource);
                                imageViewCrackResult.startAnimation(animationCrackResult);
                                imageViewCrackResult.setVisibility(View.VISIBLE);
                            }
                        });
            }
        }

        AnimationSet animationBgCrackResult = new AnimationSet(true);

        Animation rotateAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_rotate_bg_crack_result);
        rotateAnimationCrackResult.setDuration(DURATION_ROTATION_CRACK_RESULT);
        animationBgCrackResult.addAnimation(rotateAnimationCrackResult);

        Animation scaleAnimationBgCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_bg_crack_result);
        scaleAnimationBgCrackResult.setDuration(DURATION_ANIM_BG_CRACK_RESULT);
        animationBgCrackResult.addAnimation(scaleAnimationBgCrackResult);

        TranslateAnimation translateAnimationBgCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        translateAnimationBgCrackResult.setDuration(DURATION_ANIM_BG_CRACK_RESULT);
        animationBgCrackResult.addAnimation(translateAnimationBgCrackResult);

        imageViewBgCrackResult.startAnimation(animationBgCrackResult);
        imageViewBgCrackResult.setVisibility(View.VISIBLE);
    }

    private void showCrackResultBackgroundAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(DURATION_ALPHA_ANIM);

        backgroundViewCrackResult.setAnimation(alphaAnimation);
        backgroundViewCrackResult.setBackgroundColor(getResources().getColor(R.color.font_black_primary_70));
        backgroundViewCrackResult.setVisibility(View.VISIBLE);
    }

    public void showListCrackResultText(List<CrackBenefit> rewardTexts, String labelCrackResult) {
        textCrackResultLabel.setText(labelCrackResult);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(DURATION_ALPHA_ANIM_TEXT);
        containerTextCrackResult.setAnimation(alphaAnimation);
        containerTextCrackResult.setVisibility(VISIBLE);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int childPosition = 0;
                for (CrackBenefit rewardText : rewardTexts) {

                    if (!TextUtils.isEmpty(rewardText.getAnimationType())) {
                        String rewardStr = "";
                        if (!TextUtils.isEmpty(rewardText.getTemplateText())) {
                            int index = rewardText.getTemplateText().indexOf(' ');
                            if (index != -1 && index < rewardText.getTemplateText().length()) {
                                rewardStr = rewardText.getTemplateText().substring(index + 1);
                            }
                        }
                        View view = listCrackResultText.getChildAt(childPosition);
                        if (view instanceof TextView) {
                            TextView textView = (TextView) view;
                            textView.setText(String.format(getContext().getString(R.string.rewards_increased_points), String.valueOf(rewardText.getValueBefore()), rewardStr));

                            String finalRewardStr = rewardStr;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    float frontWidth = measureFrontTextWidth(textView.getTextSize(),
                                            String.format(getContext().getString(R.string.rewards_increased_points_front_text), String.valueOf(rewardText.getValueBefore())));
                                    slideAnimationLeftToRight(textView, rewardText, frontWidth);
                                    initCountAnimation(textView, rewardText.getValueBefore(), rewardText.getValueAfter(), finalRewardStr);
                                }
                            }, 100);
                        }
                        childPosition++;

                    }
                }
                if (animationList != null && animationList.size() > 0) {
                    counterAnimatorSet = new AnimatorSet();
                    counterAnimatorSet.playTogether(animationList);
                    counterAnimatorSet.start();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        for (CrackBenefit rewardText : rewardTexts) {
            TextView textView = new TextView(getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            textView.setGravity(Gravity.CENTER);
            textView.setText(rewardText.getText());
            textView.setLayoutParams(layoutParams);
            if (HexValidator.validate(rewardText.getColor())) {
                textView.setTextColor(Color.parseColor(rewardText.getColor()));
            } else {
                textView.setTextColor(getContext().getResources().getColor(R.color.default_text_reward_color));
            }
            int dimenTextSize = convertSize(rewardText.getSize());
            float textSizeInPx = getContext().getResources().getDimension(dimenTextSize);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPx);
            listCrackResultText.addView(textView);
        }

    }

    private float measureFrontTextWidth(float textSize, String textToMeasure) {
        if (TextUtils.isEmpty(textToMeasure))
            return 0.0f;
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
        return textPaint.measureText(textToMeasure);
    }


    /**
     * Slide the view from left to right
     *
     * @param textView   relative textview for original position of shift
     * @param rewardText
     * @param textShift
     */
    private void slideAnimationLeftToRight(TextView textView, CrackBenefit rewardText, float textShift) {
        int multiplierColor;
        if (HexValidator.validate(rewardText.getColor())) {
            multiplierColor = Color.parseColor(rewardText.getColor());
        } else {
            multiplierColor = getContext().getResources().getColor(R.color.default_text_reward_color);
        }
        String infoString = rewardText.getTierInformation() + " " + rewardText.getMultiplier();
        Spannable wordtoSpan = new SpannableString(infoString);

        wordtoSpan.setSpan(new ForegroundColorSpan(multiplierColor), infoString.indexOf(rewardText.getMultiplier()), infoString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textViewTierInfo = new TextView(getContext());
        textViewTierInfo.setText(wordtoSpan);
        textViewTierInfo.setBackgroundResource(R.drawable.ic_multiplier);
        int padding = getResources().getDimensionPixelOffset(R.dimen.dp_8);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, textView.getHeight() - padding);
        layoutParams.topMargin = padding / 2;
        layoutParams.bottomMargin = padding / 2;
        textViewTierInfo.setGravity(Gravity.CENTER);
        textViewTierInfo.setLayoutParams(layoutParams);
        textViewTierInfo.setX(textView.getX());
        textViewTierInfo.setY(textView.getY());
        textViewTierInfo.setPadding(padding, padding, padding, padding);

        if (tvTierInfoList == null) {
            tvTierInfoList = new ArrayList<TextView>();
        }
        tvTierInfoList.add(textViewTierInfo);
        listCrackResultParent.addView(textViewTierInfo);
        slideLeftToRightInfoText(textViewTierInfo, textShift);

    }


    /**
     * Translate the textview from left right
     *
     * @param textViewTierInfo textview to be shifted
     * @param textShift        amount of shift
     */
    private void slideLeftToRightInfoText(TextView textViewTierInfo, float textShift) {
        TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0, textShift - getResources().getDimension(R.dimen.dp_2), 0f, 0f);
        translateAnimationCrackResult.setDuration(SLIDE_INFO_LEFT_TO_RIGHT_DURATION);
        translateAnimationCrackResult.setStartTime(SLIDE_INFO_LEFT_TO_RIGHT_START_DELAY);
        translateAnimationCrackResult.setFillAfter(true);
        translateAnimationCrackResult.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewTierInfo.setX(textViewTierInfo.getX() + textShift - getResources().getDimension(R.dimen.dp_2));
                slideAndFadeOutText(textViewTierInfo, textShift);
                translateAnimationCrackResult.setAnimationListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textViewTierInfo.startAnimation(translateAnimationCrackResult);
    }

    private void slideAndFadeOutText(TextView textViewTierInfo, float textShift) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(SLIDE_INFO_LEFT_TO_RIGHT_ALPHA_DURATION);
        TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0, textShift, 0f, 0f);
        translateAnimationCrackResult.setDuration(SLIDE_INFO_LEFT_TO_RIGHT_DURATION);
        translateAnimationCrackResult.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listCrackResultParent != null && textViewTierInfo != null) {
                    listCrackResultParent.removeView(textViewTierInfo);
                    tvTierInfoList.remove(textViewTierInfo);
                }
                alphaAnimation.setAnimationListener(null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        textViewTierInfo.startAnimation(translateAnimationCrackResult);
        textViewTierInfo.startAnimation(alphaAnimation);

    }

    private void initCountAnimation(TextView textView, int valueBefore, int valueAfter, String text) {
        ValueAnimator animator = ValueAnimator.ofInt(valueBefore, valueAfter);
        animator.setDuration(COUNTER_ANIMATION_DURATION);
        animator.setStartDelay(COUNTER_ANIMATION_START_DELAY);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(String.format(getContext().getString(R.string.rewards_increased_points), animation.getAnimatedValue().toString(), text));
            }
        });
        animationList = new ArrayList<>();
        animationList.add(animator);
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

    public void renderReturnButton(final CrackResult crackResult) {
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

    public void renderCtaButton(final CrackResult crackResult) {
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
        if (tvTierInfoList != null) {
            for (TextView tvTierInfoList : tvTierInfoList) {
                listCrackResultParent.removeView(tvTierInfoList);
            }
            tvTierInfoList.clear();
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
