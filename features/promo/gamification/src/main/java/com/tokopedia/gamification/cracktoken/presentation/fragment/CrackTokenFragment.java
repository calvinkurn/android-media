package com.tokopedia.gamification.cracktoken.presentation.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.presentation.compoundview.WidgetTokenView;

/**
 * @author Rizky on 28/03/18.
 */

public class CrackTokenFragment extends BaseDaggerFragment implements WidgetTokenView.OnWidgetTokenViewListener {

    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;

    private CountDownTimer countDownTimer;

    private TextView textCountdownTimer;

    private WidgetTokenView widgetTokenView;

//    private ImageView imageHand;

    private ImageView imageReward;
    private ImageView imageBgReward;
    private View backgroundViewReward;
    private LinearLayout containerTextReward;
    private Button buttonCheckReward;

    private int layoutWidth;

    Interpolator decAccInterpolator;

    boolean isClicked;

    public static CrackTokenFragment newInstance() {
        return new CrackTokenFragment();
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_crack_token, container, false);

        textCountdownTimer = v.findViewById(R.id.text_countdown_timer);

        widgetTokenView = v.findViewById(R.id.widget_token_view);

//        imageHand = v.findViewById(R.id.image_hand);

        backgroundViewReward = v.findViewById(R.id.background_view_reward);
        containerTextReward = v.findViewById(R.id.container_text_reward);
        buttonCheckReward = v.findViewById(R.id.button_check_reward);
        imageReward = v.findViewById(R.id.image_reward);
        imageBgReward = v.findViewById(R.id.image_bg_reward);

        decAccInterpolator = new AccelerateDecelerateInterpolator();

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.special_sprite);
        Bitmap fullEggBitmap = getFullEgg(bitmap);
        Bitmap crackedEgg = getCrackedEgg(bitmap);
        Bitmap leftCrackedEgg = getCrackedLeftEgg(bitmap);
        Bitmap rightCrackedEgg = getCrackedRightEgg(bitmap);
        bitmap.recycle();

        showCountdownTimer();

        //        showLightAnimation();

        widgetTokenView.setListener(this);

        widgetTokenView.setToken(fullEggBitmap, crackedEgg, rightCrackedEgg, leftCrackedEgg);

        widgetTokenView.shake();

        widgetTokenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
//                    imageViewLightLeft.clearAnimation();
//                    imageViewLightLeft.setVisibility(View.GONE);
//                    imageViewLightRight.clearAnimation();
//                    imageViewLightRight.setVisibility(View.GONE);
//
//                    imageViewFull.clearAnimation();
//                    imageViewCracked.clearAnimation();

                    widgetTokenView.shakeHard();

                    countDownTimer.cancel();

                    textCountdownTimer.setVisibility(View.GONE);

                    widgetTokenView.crack();
                }
                isClicked = true;
            }
        });

        buttonCheckReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundViewReward.clearAnimation();
                backgroundViewReward.setVisibility(View.GONE);
                imageReward.clearAnimation();
                imageReward.setVisibility(View.GONE);
                imageBgReward.clearAnimation();
                imageBgReward.setVisibility(View.GONE);
                containerTextReward.setVisibility(View.GONE);
                resetEgg();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        showHandAnimation(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

//    private void showHandAnimation(View v) {
//        Log.d("Egg2Fragment", String.valueOf(layoutWidth));
//        TranslateAnimation translateAnimation = new TranslateAnimation(
//                0, -500, 0, 0
//        );
//        translateAnimation.setDuration(1500);
//        translateAnimation.setFillAfter(true);
//        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        imageHand.setAnimation(translateAnimation);
//        translateAnimation.start();
//    }

    private void showCountdownTimer() {
        countDownTimer = new CountDownTimer(30000000, COUNTDOWN_INTERVAL_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                int hours = minutes / 60;
                minutes = minutes % 60;
                seconds = seconds % 60;
                textCountdownTimer.setText(
                        String.format("%02d", hours) + ":" +
                                String.format("%02d", minutes) + ":" +
                                String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void showRewards() {
        showRewardImageAnimation();
        showRewardBackgroundAnimation();
        showRewardTextAnimation();
    }

    private void showRewardImageAnimation() {
        imageReward.setVisibility(View.VISIBLE);
        containerTextReward.setVisibility(View.GONE);

        Animation animationCoupon = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_reward);
        imageReward.setAnimation(animationCoupon);

        animationCoupon.start();

        imageBgReward.setVisibility(View.VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);
        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation_bg_reward);
        animationSet.addAnimation(rotate);

        Animation animationRotation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_bg_reward);
        animationSet.addAnimation(animationRotation);

        imageBgReward.setAnimation(animationSet);
        animationSet.start();
    }

    private void showRewardBackgroundAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(1500);

        backgroundViewReward.setAnimation(alphaAnimation);
        backgroundViewReward.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        backgroundViewReward.setVisibility(View.VISIBLE);
    }

    private void showRewardTextAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(500);

        containerTextReward.setAnimation(alphaAnimation);
        containerTextReward.setVisibility(View.VISIBLE);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                backgroundViewReward.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void resetEgg() {
        isClicked = false;

        widgetTokenView.reset();

//        imageViewRight.clearAnimation();
//        imageViewRight.setVisibility(View.GONE);
//
//        imageViewLeft.clearAnimation();
//        imageViewLeft.setVisibility(View.GONE);
//
//        imageViewFull.setVisibility(View.VISIBLE);
//        imageViewCracked.setPercentMasked(100);

        textCountdownTimer.setVisibility(View.VISIBLE);

        showCountdownTimer();

        //        showLightAnimation();
        widgetTokenView.shake();
    }

    private Bitmap getFullEgg(Bitmap bitmap) {
        return getSprite(bitmap, 0, 7, false);
    }

    private Bitmap getCrackedEgg(Bitmap bitmap) {
        return getSprite(bitmap, 4, 7, true);
    }

    private Bitmap getCrackedLeftEgg(Bitmap bitmap) {
        return getSprite(bitmap, 6, 7, false);
    }

    private Bitmap getCrackedRightEgg(Bitmap bitmap) {
        return getSprite(bitmap, 5, 7, false);
    }

    private Bitmap getSprite(Bitmap bitmap, int pos, int totalSprite, boolean isHalfWidth) {
        int resultedWidth = bitmap.getWidth() / totalSprite;
        int resultedHeight = bitmap.getHeight();
        Bitmap bmOverlay = Bitmap.createBitmap(resultedWidth, resultedHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);

        int offsetWidth = (isHalfWidth ? resultedWidth / 4 : 0);
        canvas.drawBitmap(bitmap,
                new Rect(pos * resultedWidth + offsetWidth,
                        0,
                        (pos + 1) * resultedWidth - offsetWidth,
                        resultedHeight),
                new Rect(offsetWidth, 0, resultedWidth - offsetWidth, resultedHeight), null);

        return bmOverlay;
    }

    @Override
    public void onTokenCracked() {
        widgetTokenView.split();
    }
}
