package com.tokopedia.gamification.cracktoken.presentation.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.presentation.compoundview.WidgetCrackResult;
import com.tokopedia.gamification.cracktoken.presentation.compoundview.WidgetRemainingToken;
import com.tokopedia.gamification.cracktoken.presentation.compoundview.WidgetTokenView;
import com.tokopedia.gamification.cracktoken.presentation.model.CrackBenefit;
import com.tokopedia.gamification.floatingtoken.model.TokenData;
import com.tokopedia.gamification.floatingtoken.model.TokenUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rizky on 28/03/18.
 */

public class CrackTokenFragment extends BaseDaggerFragment {

    private static final String ARGS_TOKEN_USER_ID = "tokenUserId";
    private static final String ARGS_BACKGROUND_IMAGE_URL = "backgroundImageUrl";
    private static final String ARGS_SMALL_IMAGE_URL = "smallImageUrl";
    private static final String ARGS_IMAGE_URL1 = "imageUrl1";
    private static final String ARGS_IMAGE_URL2 = "imageUrl2";
    private static final String ARGS_IMAGE_URL3 = "imageUrl3";
    private static final String ARGS_IMAGE_URL4 = "imageUrl4";
    private static final String ARGS_TIME_REMAINING_SECONDS = "timeRemainingSeconds";

    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;

    private CountDownTimer countDownTimer;

    private RelativeLayout rootContainer;
    private TextView textCountdownTimer;
    private WidgetTokenView widgetTokenView;
    private WidgetCrackResult widgetCrackResult;
    private WidgetRemainingToken widgetRemainingToken;

    private int tokenUserId;
    private String backgroundImageUrl;
    private String smallImageUrl;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    private int timeRemainingSeconds;

    boolean isClicked;

    public static Fragment newInstance(TokenData tokenData) {
        TokenUser tokenUser = tokenData.getHome().getTokensUser();
        Fragment fragment = new CrackTokenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_TOKEN_USER_ID, tokenUser.getTokenUserID());
        bundle.putString(ARGS_BACKGROUND_IMAGE_URL, tokenUser.getBackgroundImgUrl());
        bundle.putString(ARGS_SMALL_IMAGE_URL, tokenUser.getTokenAsset().getSmallImgUrl());
        bundle.putString(ARGS_IMAGE_URL1, tokenUser.getTokenAsset().getImageUrls().get(0));
        bundle.putString(ARGS_IMAGE_URL2, tokenUser.getTokenAsset().getImageUrls().get(1));
        bundle.putString(ARGS_IMAGE_URL3, tokenUser.getTokenAsset().getImageUrls().get(2));
        bundle.putString(ARGS_IMAGE_URL4, tokenUser.getTokenAsset().getImageUrls().get(3));
        bundle.putInt(ARGS_TIME_REMAINING_SECONDS, tokenUser.getTimeRemainingSeconds());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        tokenUserId = bundle.getInt(ARGS_TOKEN_USER_ID);
        backgroundImageUrl = bundle.getString(ARGS_BACKGROUND_IMAGE_URL);
        smallImageUrl = bundle.getString(ARGS_SMALL_IMAGE_URL);
        imageUrl1 = bundle.getString(ARGS_IMAGE_URL1);
        imageUrl2 = bundle.getString(ARGS_IMAGE_URL2);
        imageUrl3 = bundle.getString(ARGS_IMAGE_URL3);
        imageUrl4 = bundle.getString(ARGS_IMAGE_URL4);
        timeRemainingSeconds = bundle.getInt(ARGS_TIME_REMAINING_SECONDS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_crack_token, container, false);

        rootContainer = v.findViewById(R.id.root_container);
        textCountdownTimer = v.findViewById(R.id.text_countdown_timer);
        widgetTokenView = v.findViewById(R.id.widget_token_view);
        widgetCrackResult = v.findViewById(R.id.widget_reward);
        widgetRemainingToken = v.findViewById(R.id.widget_remaining_token_view);

        Glide.with(this)
                .load(backgroundImageUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(getResources(), resource);
                        rootContainer.setBackground(drawable);
                    }
                });

        showCountdownTimer(timeRemainingSeconds);

        widgetTokenView.setToken(imageUrl1, imageUrl2, imageUrl3, imageUrl4);

        widgetTokenView.setListener(new WidgetTokenView.WidgetTokenListener() {
            @Override
            public void onClick() {
                countDownTimer.cancel();
                textCountdownTimer.setVisibility(View.GONE);

                // TODO: call api to get reward and next token here
                // if get reward succeed, then call widgetTokenView.split() and showCrackResult()
                // else if failed, then call widgetTokenView.stopShaking() and show error

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        widgetTokenView.split();
                        showSuccessCrackResult();
                    }
                }, 5000);
            }
        });

        widgetCrackResult.setListener(new WidgetCrackResult.WidgetRewardListener() {
            @Override
            public void onClickCtaButton(String applink) {
                widgetCrackResult.clearReward();
                resetEgg();
            }
        });

        widgetRemainingToken.showRemainingToken(smallImageUrl, 20);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void showCountdownTimer(int timeRemainingSeconds) {
        countDownTimer = new CountDownTimer(timeRemainingSeconds, COUNTDOWN_INTERVAL_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                setUIFloatingTimer((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void setUIFloatingTimer(long timeRemainingSeconds) {
        int seconds = (int) timeRemainingSeconds;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        minutes = minutes % 60;
        seconds = seconds % 60;
        textCountdownTimer.setText(String.format(getString(R.string.countdown_format), hours, minutes, seconds));
    }

    private void showSuccessCrackResult() {
        List<CrackBenefit> rewardTexts = new ArrayList<>();
        rewardTexts.add(new CrackBenefit("+50 Points", "#ffdc00", 34));

        String rewardCouponUrl = "https://ecs7.tokopedia.net/assets/images/gamification/benefit/rewards-coupon.png";
        widgetCrackResult.showCrackResult(rewardCouponUrl, "Selamat anda mendapatkan", rewardTexts, "Cek dan Gunakan Hadiah Anda", "");
    }

    private void showErrorCrackResult() {
        List<CrackBenefit> rewardTexts = new ArrayList<>();
        rewardTexts.add(new CrackBenefit("Terjadi Kesalahan Teknis", "#ffffff", 40));

        String rewardCouponUrl = "https://ecs7.tokopedia.net/assets/images/gamification/benefit/rewards-coupon.png";
        widgetCrackResult.showCrackResult(rewardCouponUrl, "Maaf, sayang sekali sepertinya", rewardTexts, "Coba Lagi", "");
    }

    private void resetEgg() {
        isClicked = false;
        showCountdownTimer(timeRemainingSeconds);
        widgetTokenView.reset();
        textCountdownTimer.setVisibility(View.VISIBLE);
    }


}
