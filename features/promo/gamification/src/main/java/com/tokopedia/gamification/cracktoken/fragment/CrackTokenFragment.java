package com.tokopedia.gamification.cracktoken.fragment;

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
import com.tokopedia.gamification.GamificationComponentInstance;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetCrackResult;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetRemainingToken;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetTokenView;
import com.tokopedia.gamification.cracktoken.model.CrackBenefit;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rizky on 28/03/18.
 */

public class CrackTokenFragment extends BaseDaggerFragment {

    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;
    public static final String EXTRA_TOKEN_DATA = "extra_token_data";

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
    private TokenData tokenData;

    public static Fragment newInstance(TokenData tokenData) {
        Fragment fragment = new CrackTokenFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_TOKEN_DATA, tokenData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        GamificationComponent gamificationComponent =
                GamificationComponentInstance.getComponent(getActivity().getApplication());
        gamificationComponent.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        tokenData = bundle.getParcelable(EXTRA_TOKEN_DATA);
        TokenUser tokenUser = tokenData.getHome().getTokensUser();
        tokenUserId = tokenUser.getTokenUserID();
        backgroundImageUrl = tokenUser.getBackgroundImgUrl();
        smallImageUrl = tokenUser.getTokenAsset().getSmallImgUrl();
        imageUrl1 = tokenUser.getTokenAsset().getImageUrls().get(0);
        imageUrl2 = tokenUser.getTokenAsset().getImageUrls().get(1);
        imageUrl3 = tokenUser.getTokenAsset().getImageUrls().get(2);
        imageUrl4 = tokenUser.getTokenAsset().getImageUrls().get(3);
        timeRemainingSeconds = tokenUser.getTimeRemainingSeconds();
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