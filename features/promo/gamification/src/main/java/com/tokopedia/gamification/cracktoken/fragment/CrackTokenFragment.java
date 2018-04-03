package com.tokopedia.gamification.cracktoken.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
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
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.CrackBenefit;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.presenter.CrackTokenPresenter;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Rizky on 28/03/18.
 */

public class CrackTokenFragment extends BaseDaggerFragment implements CrackTokenContract.View {

    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;
    public static final String EXTRA_TOKEN_DATA = "extra_token_data";

    private CountDownTimer countDownTimer;

    private ViewGroup rootContainer;
    private TextView textCountdownTimer;
    private WidgetTokenView widgetTokenView;
    private WidgetCrackResult widgetCrackResult;
    private WidgetRemainingToken widgetRemainingToken;

    private int tokenUserId;
    private int campaignId;
    private String backgroundImageUrl;
    private String smallImageUrl;
    private String fullEggImg;
    private String crackedEggImg;
    private String rightCrackedEggImg;
    private String leftCrackedEggImg;
    private int timeRemainingSeconds;
    private boolean isCountdownTimerShow;

    private TokenData tokenData;
    private View rootView;

    @Inject
    CrackTokenPresenter crackTokenPresenter;

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
        crackTokenPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO argument here for initial only.. use if (savedInstance == null) {..}
        Bundle bundle = getArguments();
        tokenData = bundle.getParcelable(EXTRA_TOKEN_DATA);
        initDataCrackEgg(tokenData);
        renderViewCrackEgg();
    }

    private void initDataCrackEgg(TokenData tokenData) {
        TokenUser tokenUser = tokenData.getHome().getTokensUser();
        tokenUserId = tokenUser.getTokenUserID();
        campaignId = tokenUser.getCampaignID();
        backgroundImageUrl = tokenUser.getTokenAsset().getBackgroundImgUrl();
        smallImageUrl = tokenUser.getTokenAsset().getSmallImgUrl();
        fullEggImg = tokenUser.getTokenAsset().getImageUrls().get(0);
        crackedEggImg = tokenUser.getTokenAsset().getImageUrls().get(4);
        rightCrackedEggImg = tokenUser.getTokenAsset().getImageUrls().get(6);
        leftCrackedEggImg = tokenUser.getTokenAsset().getImageUrls().get(5);
        timeRemainingSeconds = tokenUser.getTimeRemainingSeconds();
        isCountdownTimerShow = tokenUser.getShowTime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_crack_token, container, false);

        rootContainer = rootView.findViewById(R.id.root_container);
        textCountdownTimer = rootView.findViewById(R.id.text_countdown_timer);
        widgetTokenView = rootView.findViewById(R.id.widget_token_view);
        widgetCrackResult = rootView.findViewById(R.id.widget_reward);
        widgetRemainingToken = rootView.findViewById(R.id.widget_remaining_token_view);
        return rootView;
    }

    private void renderViewCrackEgg() {
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

        if (isCountdownTimerShow) {
            textCountdownTimer.setVisibility(View.VISIBLE);
            showCountdownTimer(timeRemainingSeconds);
        } else {
            textCountdownTimer.setVisibility(View.GONE);
        }

        widgetTokenView.setToken(fullEggImg, crackedEggImg, rightCrackedEggImg, leftCrackedEggImg);
        widgetTokenView.setListener(new WidgetTokenView.WidgetTokenListener() {
            @Override
            public void onClick() {
                if (isCountdownTimerShow) {
                    countDownTimer.cancel();
                    textCountdownTimer.setVisibility(View.GONE);
                }
                crackTokenPresenter.crackToken(tokenUserId, campaignId);
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

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initTimerBound();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void initTimerBound() {
        int rootHeight = rootView.getHeight();
        int imageMarginTop = (int) (0.15 * rootHeight);

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) textCountdownTimer.getLayoutParams();
        ivFullLp.topMargin = imageMarginTop;
        textCountdownTimer.requestLayout();

        textCountdownTimer.setVisibility(View.VISIBLE);
    }

    private void showCountdownTimer(int timeRemainingSeconds) {
        if (timeRemainingSeconds > 0) {
            countDownTimer = new CountDownTimer(timeRemainingSeconds, COUNTDOWN_INTERVAL_SECOND) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setUIFloatingTimer((int) (millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {

                }
            }.start();
            textCountdownTimer.setVisibility(View.VISIBLE);
        } else {
            textCountdownTimer.setVisibility(View.GONE);
        }
    }

    private void setUIFloatingTimer(long timeRemainingSeconds) {
        if (textCountdownTimer != null) {
            int seconds = (int) timeRemainingSeconds;
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes % 60;
            seconds = seconds % 60;
            textCountdownTimer.setText(String.format(getString(R.string.countdown_format), hours, minutes, seconds));
        }
    }

    private void resetEgg() {
        widgetTokenView.reset();
        initDataCrackEgg(tokenData);
        renderViewCrackEgg();
    }

    @Override
    public void onSuccessGetToken(TokenData tokenData) {
        this.tokenData = tokenData;
    }

    @Override
    public void onErrorGetToken(Throwable throwable) {

    }

    @Override
    public void onSuccessCrackToken(CrackResult crackResult) {
        widgetTokenView.split();
        List<CrackBenefit> rewardTexts = new ArrayList<>();
        rewardTexts.add(new CrackBenefit("+50 Points", "#ffdc00", 34));

        String rewardCouponUrl = "https://ecs7.tokopedia.net/assets/images/gamification/benefit/rewards-coupon.png";
        widgetCrackResult.showCrackResult(rewardCouponUrl, "Selamat anda mendapatkan", rewardTexts, "Cek dan Gunakan Hadiah Anda", "");
    }

    @Override
    public void onErrorCrackToken(Throwable throwable) {
        widgetTokenView.stopShaking();
        List<CrackBenefit> rewardTexts = new ArrayList<>();
        rewardTexts.add(new CrackBenefit("Terjadi Kesalahan Teknis", "#ffffff", 40));

        String rewardCouponUrl = "https://ecs7.tokopedia.net/assets/images/gamification/benefit/rewards-coupon.png";
        widgetCrackResult.showCrackResult(rewardCouponUrl, "Maaf, sayang sekali sepertinya", rewardTexts, "Coba Lagi", "");
    }
}