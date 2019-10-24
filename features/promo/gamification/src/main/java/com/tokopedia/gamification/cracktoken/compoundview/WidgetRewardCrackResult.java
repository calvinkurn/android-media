package com.tokopedia.gamification.cracktoken.compoundview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.util.HexValidator;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetRewardCrackResult extends FrameLayout implements View.OnClickListener {
    private static final float REWARD_SCALE_FACTOR = 1.2f;
    private static final String BENIFIT_TYPE_REWARD_POINT = "reward_point";
    private static final String BENIFIT_TYPE_LOYALTY_POINT = "loyalty_point";
    private static final String BENIFIT_TYPE_COUPON = "coupon";
    private static final long REWARDS_VIEW_SCALE_UP_START_DELAY = 1000;
    private static final long REWARDS_VIEW_SCALE_UP_DURATION = 150;
    private static final int SLIDE_UP_TEXT_RELATIVE_DURATION = 200;
    private static final long TRANSLATE_TEXT_UP_DURATION = 500;
    private static final long NUMBER_COUNTER_DURATION = 1500;
    private static final long REWARDS_VIEW_SCALE_DOWN_DURATION = 150;
    private static final float BACKGROUND_SCALE_FACTOR_BOUND = 0.4f;
    private RelativeLayout rlPoints, rlLoyalty, rlCoupons;
    private TextView tvPoints, tvLoyalty, tvCoupons;
    private LinearLayout llRewards;
    private Paint textPaint;
    private ArrayList<TextView> tvPointsList;
    private List<CrackBenefitEntity> crackBenefits;
    private static int TYPE_POINTS = 0;
    private static int TYPE_LOYALTY = 1;
    private static int TYPE_COUPONS = 2;
    private ArrayList<TextView> tvLoyaltyList;
    private ArrayList<TextView> tvCouponsList;
    private RelativeLayout rlParent;
    private int currentPoints, currentCoupons, currentLoyalty;
    private float oneDigitWidth;


    public WidgetRewardCrackResult(@NonNull Context context) {
        super(context);
        init();
    }

    public WidgetRewardCrackResult(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetRewardCrackResult(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        rlPoints.setOnClickListener(this);
        rlLoyalty.setOnClickListener(this);
        rlCoupons.setOnClickListener(this);
    }

    public void showCounterAnimations(List<CrackBenefitEntity> crackBenefits) {
        this.crackBenefits = crackBenefits;
        tvPointsList = new ArrayList<>();
        tvLoyaltyList = new ArrayList<>();
        tvCouponsList = new ArrayList<>();
        initTextPaint();
        for (CrackBenefitEntity crackBenefit : crackBenefits) {
            int textColor;
            if (HexValidator.validate(crackBenefit.getColor())) {
                textColor = Color.parseColor(crackBenefit.getColor());
            } else {
                textColor = getResources().getColor(R.color.default_reward_color);
            }
            if (BENIFIT_TYPE_REWARD_POINT.equalsIgnoreCase(crackBenefit.getBenefitType())) {

                scaleUpRewardsView(rlPoints, tvPoints, textColor, currentPoints, crackBenefit.getValueAfter(), TYPE_POINTS);

            } else if (BENIFIT_TYPE_LOYALTY_POINT.equalsIgnoreCase(crackBenefit.getBenefitType())) {
                scaleUpRewardsView(rlLoyalty, tvLoyalty, textColor, currentLoyalty, crackBenefit.getValueAfter(), TYPE_LOYALTY);

            } else if (BENIFIT_TYPE_COUPON.equalsIgnoreCase(crackBenefit.getBenefitType())) {
                scaleUpRewardsView(rlCoupons, tvCoupons, textColor, currentCoupons, crackBenefit.getValueAfter(), TYPE_COUPONS);
            }
        }

    }

    private void scaleUpRewardsView(RelativeLayout rewardType, TextView rewardText, int textColor, int prevValue, int valueIncrease, final int viewType) {
        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, REWARD_SCALE_FACTOR);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, REWARD_SCALE_FACTOR);

        ObjectAnimator bouncePointsAnim = ObjectAnimator.ofPropertyValuesHolder(rewardType, scalex, scaley);
        if (viewType == TYPE_LOYALTY)
            rewardType.setPivotX(0);
        else
            rewardType.setPivotX(rewardType.getWidth());
        bouncePointsAnim.setStartDelay(REWARDS_VIEW_SCALE_UP_START_DELAY);
        bouncePointsAnim.setDuration(REWARDS_VIEW_SCALE_UP_DURATION);
        bouncePointsAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedFraction() > BACKGROUND_SCALE_FACTOR_BOUND) {
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
                addDummyTextInViewToAnimateBottomToUp(rewardType, rewardText, valueIncrease, textColor, viewType);
                slideDummyTextBottomToUp(viewType);
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
        measureOneDigitWidth();
    }

    private void measureOneDigitWidth() {
        String point = "0"; //Any one digit number
        oneDigitWidth = textPaint.measureText(point);  //calculates how much width it would take on screen
    }

    private void addDummyTextInViewToAnimateBottomToUp(RelativeLayout rewardType, TextView rewardText, int valueIncrease, int textColor, int viewType) {
        String points = String.valueOf(valueIncrease);
        int length = points.length();
        float dataWidth = textPaint.measureText(points);
        int backgroundHolderCoordinates[] = new int[2];
        rewardType.getLocationOnScreen(backgroundHolderCoordinates);
        int parentCoordinates[] = new int[2];
        llRewards.getLocationOnScreen(parentCoordinates);
        float x = backgroundHolderCoordinates[0] + (rewardType.getWidth() - rewardText.getPaddingRight() - dataWidth) * REWARD_SCALE_FACTOR;
        float y = backgroundHolderCoordinates[1] - parentCoordinates[1] + (rewardType.getHeight() * REWARD_SCALE_FACTOR);

        for (int i = 0; i < length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(String.valueOf(points.charAt(i)));
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * REWARD_SCALE_FACTOR);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            if (viewType == TYPE_POINTS)
                tvPointsList.add(textView);
            else if (viewType == TYPE_LOYALTY)
                tvLoyaltyList.add(textView);
            else
                tvCouponsList.add(textView);

            textView.setX(x + i * oneDigitWidth);           //Position each dummy textview after each text view
            textView.setY(y);
            rlParent.addView(textView);
        }

    }

    private void slideDummyTextBottomToUp(int viewType) {

        ArrayList<TextView> tvList;
        if (viewType == TYPE_POINTS)
            tvList = tvPointsList;
        else if (viewType == TYPE_LOYALTY)
            tvList = tvLoyaltyList;
        else
            tvList = tvCouponsList;

        int listLength = tvList.size();
        for (int i = listLength - 1; i >= 0; i--) {
            TextView tvListItem = tvList.get(i);
            AnimationSet animatorSet = new AnimationSet(true);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(TRANSLATE_TEXT_UP_DURATION);
            animatorSet.addAnimation(alphaAnimation);
            TranslateAnimation translateAnimationCrackResult = new TranslateAnimation(0, 0, 0f, -rlPoints.getHeight() + getResources().getDimensionPixelOffset(R.dimen.dp_12));
            translateAnimationCrackResult.setStartOffset((listLength - 1 - i) * SLIDE_UP_TEXT_RELATIVE_DURATION);
            translateAnimationCrackResult.setDuration(TRANSLATE_TEXT_UP_DURATION);
            animatorSet.addAnimation(translateAnimationCrackResult);
            animatorSet.setFillAfter(true);
            tvListItem.startAnimation(animatorSet);

        }
    }

    private void startCountAnimation(RelativeLayout rewardType, int valueBefore, int valueIncrease, int viewType) {
        ValueAnimator animator = ValueAnimator.ofInt(valueBefore, valueBefore + valueIncrease);
        animator.setDuration(NUMBER_COUNTER_DURATION);

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
                clearDummyViews();
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
        bounceAnim.setDuration(REWARDS_VIEW_SCALE_DOWN_DURATION);
        bounceAnim.start();
    }

    private void clearDummyViews() {
        if (tvPointsList != null) {
            for (TextView tv : tvPointsList) {
                llRewards.removeView(tv);
            }
            tvPointsList.clear();
        }
        if (tvLoyaltyList != null) {
            for (TextView tv : tvLoyaltyList) {
                llRewards.removeView(tv);
            }
            tvLoyaltyList.clear();
        }
        if (tvCouponsList != null) {
            for (TextView tv : tvCouponsList) {
                llRewards.removeView(tv);
            }
            tvCouponsList.clear();
        }
    }

    public void setRewards(int points, int coupons, int loyalty) {
        resetView();
        currentPoints = points;
        currentCoupons = coupons;
        currentLoyalty = loyalty;
        tvPoints.setText(String.valueOf(points));
        tvCoupons.setText(String.valueOf(coupons));
        tvLoyalty.setText(String.valueOf(loyalty));
    }

    private void resetView() {
        tvPoints.setTextColor(getResources().getColor(R.color.white));
        tvLoyalty.setTextColor(getResources().getColor(R.color.white));
        tvCoupons.setTextColor(getResources().getColor(R.color.white));
        rlCoupons.clearAnimation();
        rlLoyalty.clearAnimation();
        rlPoints.clearAnimation();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.rl_points || id == R.id.rl_loyalty) {
            ApplinkUtil.navigateToAssociatedPage((Activity) getContext(), ApplinkConst.TOKOPOINTS, null, CrackTokenActivity.class);
            if(id == R.id.rl_points)
                trackPointsIconClick();
            else
                trackLoyaltyIconClick();
        } else if(id == R.id. rl_coupons) {
            ApplinkUtil.navigateToAssociatedPage((Activity) getContext(), ApplinkConst.COUPON_LISTING, null, CrackTokenActivity.class);
            trackKuponIconClick();
        }
    }

    private void trackPointsIconClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                GamificationEventTracking.Category.EXPIRED_TOKEN,
                GamificationEventTracking.Action.CLICK_POINT_ICON,
                ""
        ));
    }

    private void trackLoyaltyIconClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                GamificationEventTracking.Category.EXPIRED_TOKEN,
                GamificationEventTracking.Action.CLICK_LOYALTY_ICON,
                ""
        ));
    }

    private void trackKuponIconClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                GamificationEventTracking.Category.EXPIRED_TOKEN,
                GamificationEventTracking.Action.CLICK_KUPON_ICON,
                ""
        ));
    }
}
