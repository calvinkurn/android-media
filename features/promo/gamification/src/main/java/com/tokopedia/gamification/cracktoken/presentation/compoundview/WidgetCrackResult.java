package com.tokopedia.gamification.cracktoken.presentation.compoundview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gamification.R;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.cracktoken.presentation.model.RewardTextStyle;

import java.util.List;

/**
 * Created by Rizky on 29/03/18.
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

    public void showCrackResult(String urlImageCrackResult, String labelCrackResult, List<RewardTextStyle> listRewardText, String textCtaButton, String applink) {
        showRewardImageAnimation(urlImageCrackResult);
        showRewardBackgroundAnimation();
        showListRewardText(listRewardText, labelCrackResult);
        showCtaButton(textCtaButton, applink);
    }

    private void showRewardImageAnimation(String urlImageCrackResult) {
        ImageHandler.LoadImage(imageViewReward, urlImageCrackResult);

        Animation animationCoupon = AnimationUtils.loadAnimation(getContext(), R.anim.animation_reward);
        imageViewReward.startAnimation(animationCoupon);
        imageViewReward.setVisibility(View.VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotation_bg_reward);
        animationSet.addAnimation(rotate);

        Animation animationRotation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_bg_reward);
        animationSet.addAnimation(animationRotation);

        imageViewBgReward.startAnimation(animationSet);
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

    public void showListRewardText(List<RewardTextStyle> rewardTexts, String labelCrackResult) {
        textCrackResultLabel.setText(labelCrackResult);
        for (RewardTextStyle rewardText : rewardTexts) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setText(rewardText.getText());
            textView.setTextColor(Color.parseColor(rewardText.getColor()));
            textView.setTextSize(rewardText.getSize());
            listRewardText.addView(textView);
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(500);
        containerTextReward.setAnimation(alphaAnimation);
        containerTextReward.setVisibility(VISIBLE);
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
