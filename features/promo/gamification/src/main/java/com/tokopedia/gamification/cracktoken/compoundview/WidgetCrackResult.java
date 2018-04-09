package com.tokopedia.gamification.cracktoken.compoundview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.model.CrackBenefit;
import com.tokopedia.gamification.cracktoken.model.CrackButton;
import com.tokopedia.gamification.cracktoken.model.CrackResult;

import java.util.List;

/**
 * @author Rizky on 29/03/18.
 */

public class WidgetCrackResult extends RelativeLayout {

    private ImageView imageViewBgCrackResult;
    private ImageView imageViewCrackResult;
    private View backgroundViewCrackResult;
    private LinearLayout containerTextCrackResult;
    private TextView textCrackResultLabel;

    private LinearLayout listCrackResultText;
    private Button buttonReturn;
    private TextView buttonCta;
    private ImageView closeRewardBtn;
    private String benefitType;

    private WidgetCrackResultListener listener;

    public interface WidgetCrackResultListener {
        void onClickCtaButton(CrackResult crackResult, String titleBtn);

        void onTrackingCloseRewardButton(CrackResult crackResult);

        void onClickReturnButton(CrackResult crackResult, String titleBtn);

        void onClickCloseButton();
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
        closeRewardBtn = view.findViewById(R.id.close_reward);
    }

    public void showCrackResult(CrackResult crackResult) {
        this.benefitType = crackResult.getBenefitType();
        showCrackResultImageAnimation(crackResult);
        showCrackResultBackgroundAnimation();
        showListCrackResultText(crackResult.getBenefits(), crackResult.getBenefitLabel());
        renderCtaButton(crackResult);
        renderReturnButton(crackResult);
        renderCloseReward(crackResult);
    }

    private void showCrackResultImageAnimation(CrackResult crackResult) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenHeightQuarter = metrics.heightPixels / 4;
        screenHeightQuarter = screenHeightQuarter - actionBarHeight;

        final AnimationSet animationCrackResult = new AnimationSet(true);
        Animation scaleAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_crack_result);
        animationCrackResult.addAnimation(scaleAnimationCrackResult);
        TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationCrackResult.addAnimation(translateAnimationCrackResult);
        animationCrackResult.setFillAfter(true);
        animationCrackResult.setDuration(600);
        animationCrackResult.setInterpolator(new FastOutSlowInInterpolator());

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
        rotateAnimationCrackResult.setDuration(15000);
        animationBgCrackResult.addAnimation(rotateAnimationCrackResult);

        Animation scaleAnimationBgCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_bg_crack_result);
        scaleAnimationBgCrackResult.setDuration(1000);
        animationBgCrackResult.addAnimation(scaleAnimationBgCrackResult);

        TranslateAnimation translateAnimationBgCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        translateAnimationBgCrackResult.setDuration(1000);
        animationBgCrackResult.addAnimation(translateAnimationBgCrackResult);

        imageViewBgCrackResult.startAnimation(animationBgCrackResult);
        imageViewBgCrackResult.setVisibility(View.VISIBLE);
    }

    private void showCrackResultBackgroundAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(1500);

        backgroundViewCrackResult.setAnimation(alphaAnimation);
        backgroundViewCrackResult.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        backgroundViewCrackResult.setVisibility(View.VISIBLE);
    }

    public void showListCrackResultText(List<CrackBenefit> rewardTexts, String labelCrackResult) {
        textCrackResultLabel.setText(labelCrackResult);
        for (CrackBenefit rewardText : rewardTexts) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(rewardText.getText());
            textView.setTextColor(Color.parseColor(rewardText.getColor()));
            int dimenTextSize = convertSize(rewardText.getSize());
            float textSizeInPx = getContext().getResources().getDimension(dimenTextSize);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPx);
            listCrackResultText.addView(textView);
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(500);
        containerTextCrackResult.setAnimation(alphaAnimation);
        containerTextCrackResult.setVisibility(VISIBLE);
    }

    public String getBenefitType() {
        return this.benefitType;
    }

    private int convertSize(String size) {
        switch (size) {
            case "large":
                return R.dimen.text_size_reward_large;
            case "medium":
                return R.dimen.text_size_reward_medium;
            case "small":
                return R.dimen.text_size_reward_small;
            default:
                return R.dimen.text_size_reward_medium;
        }
    }

    public void renderReturnButton(final CrackResult crackResult) {
        if (crackResult.getReturnButton() != null) {
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
    }

    public void renderCtaButton(final CrackResult crackResult) {
        if (crackResult.getCtaButton() != null) {
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
    }

    private void renderCloseReward(final CrackResult crackResult) {
        closeRewardBtn.setVisibility(VISIBLE);
        closeRewardBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickCloseButton();
                listener.onTrackingCloseRewardButton(crackResult);
            }
        });
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
        closeRewardBtn.setVisibility(GONE);
    }

}
