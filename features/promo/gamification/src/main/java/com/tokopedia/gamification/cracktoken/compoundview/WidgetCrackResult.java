package com.tokopedia.gamification.cracktoken.compoundview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import java.util.List;

/**
 * @author Rizky on 29/03/18.
 */

public class WidgetCrackResult extends RelativeLayout {

    private ImageView imageViewBgReward;
    private ImageView imageViewReward;
    private View backgroundViewReward;
    private LinearLayout containerTextReward;
    private TextView textCrackResultLabel;

    private LinearLayout listRewardText;
    private Button buttonCta;

    private WidgetRewardListener listener;

    public interface WidgetRewardListener {
        void onClickCtaButton(String applink);
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_reward, this, true);
        imageViewBgReward = view.findViewById(R.id.image_bg_reward);
        imageViewReward = view.findViewById(R.id.image_reward);
        backgroundViewReward = view.findViewById(R.id.background_view_reward);
        containerTextReward = view.findViewById(R.id.container_text_reward);
        listRewardText = view.findViewById(R.id.view_list_reward_text);
        buttonCta = view.findViewById(R.id.button_cta);
        textCrackResultLabel = view.findViewById(R.id.text_crack_result_label);
    }

    public void showCrackResult(String urlImageCrackResult, String labelCrackResult, List<CrackBenefit> listRewardText,
                                String textCtaButton, String applink) {
        showRewardImageAnimation(urlImageCrackResult);
        showRewardBackgroundAnimation();
        showListRewardText(listRewardText, labelCrackResult);
        showCtaButton(textCtaButton, applink);
    }

    public void showErrorCrackResult(Bitmap errorBitmap, String labelCrackResult, List<CrackBenefit> listRewardText,
                                     String textCtaButton, String applink) {
        showErrorImageAnimation(errorBitmap);
        showRewardBackgroundAnimation();
        showListRewardText(listRewardText, labelCrackResult);
        showCtaButton(textCtaButton, applink);
    }

    private void showErrorImageAnimation(Bitmap errorBitmap) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenHeightQuarter = metrics.heightPixels/4;
        screenHeightQuarter = screenHeightQuarter - actionBarHeight;

        AnimationSet animationCrackResult = new AnimationSet(true);
        Animation scaleAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_crack_result);
        animationCrackResult.addAnimation(scaleAnimationCrackResult);
        TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationCrackResult.addAnimation(translateAnimationCrackResult);
        animationCrackResult.setFillAfter(true);
        animationCrackResult.setDuration(1000);

        imageViewReward.setImageBitmap(errorBitmap);

        imageViewReward.startAnimation(animationCrackResult);
        imageViewReward.setVisibility(View.VISIBLE);

        AnimationSet animationBgCrackResult = new AnimationSet(true);
        Animation scaleAnimationBgCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_bg_reward);
        animationBgCrackResult.addAnimation(scaleAnimationBgCrackResult);
        TranslateAnimation translateAnimationBgCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationBgCrackResult.addAnimation(translateAnimationBgCrackResult);
        animationBgCrackResult.setDuration(1000);
        animationBgCrackResult.setFillAfter(true);

        imageViewBgReward.startAnimation(animationBgCrackResult);
        imageViewBgReward.setVisibility(View.VISIBLE);
    }

    private void showRewardImageAnimation(String urlImageCrackResult) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenHeightQuarter = metrics.heightPixels/4;
        screenHeightQuarter = screenHeightQuarter - actionBarHeight;

        AnimationSet animationCrackResult = new AnimationSet(true);
        Animation scaleAnimationCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_scale_crack_result);
        animationCrackResult.addAnimation(scaleAnimationCrackResult);
        TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationCrackResult.addAnimation(translateAnimationCrackResult);
        animationCrackResult.setFillAfter(true);
        animationCrackResult.setDuration(1000);

        Glide.with(getContext())
                .load(urlImageCrackResult)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageViewReward.setImageBitmap(resource);
                    }
                });

        imageViewReward.startAnimation(animationCrackResult);
        imageViewReward.setVisibility(View.VISIBLE);

        AnimationSet animationBgCrackResult = new AnimationSet(true);
        Animation scaleAnimationBgCrackResult = AnimationUtils.loadAnimation(getContext(), R.anim.animation_bg_reward);
        animationBgCrackResult.addAnimation(scaleAnimationBgCrackResult);
        TranslateAnimation translateAnimationBgCrackResult = new TranslateAnimation(0f, 0f, 0f, -screenHeightQuarter);
        animationBgCrackResult.addAnimation(translateAnimationBgCrackResult);
        animationBgCrackResult.setDuration(1000);
        animationBgCrackResult.setFillAfter(true);

        imageViewBgReward.startAnimation(animationBgCrackResult);
        imageViewBgReward.setVisibility(View.VISIBLE);
    }

    private void showRewardBackgroundAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(1500);

        backgroundViewReward.setAnimation(alphaAnimation);
        backgroundViewReward.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        backgroundViewReward.setVisibility(View.VISIBLE);
    }

    public void showListRewardText(List<CrackBenefit> rewardTexts, String labelCrackResult) {
        textCrackResultLabel.setText(labelCrackResult);
        for (CrackBenefit rewardText : rewardTexts) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(rewardText.getText());
            textView.setTextColor(Color.parseColor(rewardText.getColor()));
            int dimenTextSize = convertSize(rewardText.getSize());
            float textSizeInPx = getContext().getResources().getDimension(dimenTextSize);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeInPx);
            listRewardText.addView(textView);
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(500);
        containerTextReward.setAnimation(alphaAnimation);
        containerTextReward.setVisibility(VISIBLE);
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

    public void showCtaButton(String textCtaButton, final String applink) {
        buttonCta.setText(textCtaButton);

        buttonCta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickCtaButton(applink);
            }
        });
    }

    public void setListener(WidgetRewardListener listener) {
        this.listener = listener;
    }

    public void clearReward() {
        backgroundViewReward.clearAnimation();
        backgroundViewReward.setVisibility(View.GONE);
        imageViewReward.clearAnimation();
        imageViewReward.setVisibility(View.GONE);
        imageViewBgReward.clearAnimation();
        imageViewBgReward.setVisibility(View.GONE);
        listRewardText.removeAllViews();
        containerTextReward.setVisibility(GONE);
    }

}
