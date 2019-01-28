package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.model.CrackBenefit;
import com.tokopedia.gamification.util.HexValidator;

import java.util.ArrayList;
import java.util.List;

public class WidgetRewardCrackResult extends FrameLayout {
    private static final float REWARD_SCALE_FACTOR = 1.2f;
    private Context context;
    private RelativeLayout rlPoints, rlLoyalty, rlCoupons;
    private TextView tvPoints, tvLoyalty, tvCoupons;
    private LinearLayout llRewards;
    private Paint textPaint;
    private ArrayList<TextView> tvPointsList;
    private List<CrackBenefit> crackBenefits;
    private static int TYPE_POINTS = 0;
    private static int TYPE_LOYALTY = 1;
    private static int TYPE_COUPONS = 2;
    private ArrayList<TextView> tvLoyaltyList;
    private ArrayList<TextView> tvCouponsList;
    private RelativeLayout rlParent;
    private int currentPoints, currentCoupons, currentLoyalty;


    public WidgetRewardCrackResult(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WidgetRewardCrackResult(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public WidgetRewardCrackResult(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_rewards, this, true);
        llRewards = view.findViewById(R.id.ll_rewards);
        rlParent = view.findViewById(R.id.rl_parent);
        rlPoints = view.findViewById(R.id.rl_points);
        rlLoyalty = view.findViewById(R.id.rl_loyalty);
        rlCoupons = view.findViewById(R.id.rl_coupons);
        tvPoints = view.findViewById(R.id.tv_points);
        tvLoyalty = view.findViewById(R.id.tv_loyalty);
        tvCoupons = view.findViewById(R.id.tv_coupons);
    }

    public void showCounterAnimations(List<CrackBenefit> crackBenefits) {
        this.crackBenefits = crackBenefits;
        tvPointsList = new ArrayList<>();
        tvLoyaltyList = new ArrayList<>();
        tvCouponsList = new ArrayList<>();
        initTextPaint();
        for (CrackBenefit crackBenefit : crackBenefits) {
            int textColor;
            if (HexValidator.validate(crackBenefit.getColor())) {
                textColor = Color.parseColor(crackBenefit.getColor());
            } else {
                textColor = getResources().getColor(R.color.default_reward_color);
            }
            if ("reward_point".equalsIgnoreCase(crackBenefit.getBenefitType())) {

                scaleUpRewards(rlPoints, tvPoints, textColor, currentPoints, crackBenefit.getValueAfter(), TYPE_POINTS);

            } else if ("loyalty_point".equalsIgnoreCase(crackBenefit.getBenefitType())) {
                scaleUpRewards(rlLoyalty, tvLoyalty, textColor, currentLoyalty, crackBenefit.getValueAfter(), TYPE_LOYALTY);

            } else if ("coupon".equalsIgnoreCase(crackBenefit.getBenefitType())) {
                scaleUpRewards(rlCoupons, tvCoupons, textColor, currentCoupons, crackBenefit.getValueAfter(), TYPE_COUPONS);
            }
        }

    }

    private void scaleUpRewards(RelativeLayout rewardType, TextView rewardText, int textColor, int prevValue, int valueIncrease, final int viewType) {
        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, REWARD_SCALE_FACTOR);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, REWARD_SCALE_FACTOR);

        ObjectAnimator bouncePointsAnim = ObjectAnimator.ofPropertyValuesHolder(rewardType, scalex, scaley);
        rewardType.setPivotX(rewardType.getWidth());
        bouncePointsAnim.setStartDelay(1000);
        bouncePointsAnim.setDuration(150);
        bouncePointsAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedFraction() > 0.4) {
                    rewardText.setTextColor(textColor);
                    bouncePointsAnim.removeUpdateListener(this);
                }
            }
        });
        bouncePointsAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                addTextToAnimate(rewardType, rewardText, prevValue, valueIncrease, textColor, viewType);
                translateText(viewType);
                startCountAnimation(rewardType, prevValue, valueIncrease, viewType);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        bouncePointsAnim.start();

    }


    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp_12));
//        textPaint.setColor(Color.parseColor("#000000"));
    }

    private void addTextToAnimate(RelativeLayout rewardType, TextView rewardText, int valueBefore, int valueIncrease, int textColor, int viewType) {
        String points = String.valueOf(valueIncrease);
        String point = "6";
        int length = points.length();
        float dataWidth = textPaint.measureText(points);
        float pointWidth = textPaint.measureText(point);
        int coordinates[] = new int[2];
        rewardType.getLocationOnScreen(coordinates);
        int coordinates1[] = new int[2];
        llRewards.getLocationOnScreen(coordinates1);
        float x = coordinates[0] + (rewardType.getWidth() - rewardText.getPaddingRight() - dataWidth) * REWARD_SCALE_FACTOR;
        float y = coordinates[1] - coordinates1[1] + (rewardType.getHeight() * REWARD_SCALE_FACTOR);

        for (int i = 0; i < length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(String.valueOf(points.charAt(i)));
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * REWARD_SCALE_FACTOR);
            if (viewType == TYPE_POINTS)
                tvPointsList.add(textView);
            else if (viewType == TYPE_LOYALTY)
                tvLoyaltyList.add(textView);
            else
                tvCouponsList.add(textView);

            textView.setX(x + i * pointWidth);
            textView.setY(y);
            rlParent.addView(textView);
        }

    }

    private void translateText(int viewType) {

        ArrayList<TextView> tvList;
        if (viewType == TYPE_POINTS)
            tvList = tvPointsList;
        else if (viewType == TYPE_LOYALTY)
            tvList = tvLoyaltyList;
        else
            tvList = tvCouponsList;

        int listLength=tvList.size();
        for (int i = listLength - 1; i >= 0; i--) {
            TextView tvListItem = tvList.get(i);
            AnimationSet animatorSet = new AnimationSet(true);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(500);
            animatorSet.addAnimation(alphaAnimation);
            TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0, 0, 0f, -rlPoints.getHeight() + getResources().getDimensionPixelOffset(R.dimen.dp_12));
            translateAnimationCrackResult.setStartOffset((listLength - 1 - i) * 200);
            translateAnimationCrackResult.setDuration(500);
            animatorSet.addAnimation(translateAnimationCrackResult);
            animatorSet.setFillAfter(true);
            tvListItem.startAnimation(animatorSet);

        }
    }

    private void startCountAnimation(RelativeLayout rewardType, int valueBefore, int valueIncrease, int viewType) {
        ValueAnimator animator = ValueAnimator.ofInt(valueBefore, valueBefore + valueIncrease);
        animator.setDuration(1500);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (viewType == TYPE_POINTS)
                    tvPoints.setText(animation.getAnimatedValue().toString());
                else if (viewType == TYPE_LOYALTY)
                    tvLoyalty.setText(animation.getAnimatedValue().toString());
                else
                    tvCoupons.setText(animation.getAnimatedValue().toString());
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                scaleDownPointsSection(rewardType);
                for (TextView tv : tvPointsList) {
                    llRewards.removeView(tv);
                }
                for (TextView tv : tvLoyaltyList) {
                    llRewards.removeView(tv);
                }
                for (TextView tv : tvCouponsList) {
                    llRewards.removeView(tv);
                }
                tvPointsList.clear();
                tvLoyaltyList.clear();
                tvCouponsList.clear();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void scaleDownPointsSection(RelativeLayout rewardType) {
        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f);
        ObjectAnimator bounceAnim = ObjectAnimator.ofPropertyValuesHolder(rewardType, scalex, scaley);
        bounceAnim.setDuration(150);
        bounceAnim.start();
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void setRewards(int points, int coupons, int loyalty) {
        reset();
        currentPoints = points;
        currentCoupons = coupons;
        currentLoyalty = loyalty;
        tvPoints.setText(String.valueOf(points));
        tvCoupons.setText(String.valueOf(coupons));
        tvLoyalty.setText(String.valueOf(loyalty));
    }

    private void reset() {
        tvPoints.setTextColor(getResources().getColor(R.color.white));
        tvLoyalty.setTextColor(getResources().getColor(R.color.white));
        tvCoupons.setTextColor(getResources().getColor(R.color.white));
        rlCoupons.clearAnimation();
        rlLoyalty.clearAnimation();
        rlPoints.clearAnimation();
    }
}
