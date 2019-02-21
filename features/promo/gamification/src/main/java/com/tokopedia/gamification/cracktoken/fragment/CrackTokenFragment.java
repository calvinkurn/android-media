package com.tokopedia.gamification.cracktoken.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.gamification.GamificationComponentInstance;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetCrackResult;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetRemainingToken;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetTokenOnBoarding;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetTokenView;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.presenter.CrackTokenPresenter;
import com.tokopedia.gamification.cracktoken.util.TokenMarginUtil;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenUser;

import javax.inject.Inject;

/**
 * @author Rizky on 28/03/18.
 */

public class CrackTokenFragment extends BaseDaggerFragment implements CrackTokenContract.View {

    private static final String FPM_RENDER = "ft_gamification";
    private static final String FPM_CRACKING = "ft_gamification_cracking_egg";
    public static final int VIBRATE_DURATION = 500;
    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;
    private static final int REQUEST_CODE_LOGIN = 112;

    @Inject
    CrackTokenPresenter crackTokenPresenter;

    private View rootView;

    private CountDownTimer countDownTimer;

    private TextView textCountdownTimer;
    private WidgetTokenView widgetTokenView;
    private WidgetCrackResult widgetCrackResult;
    private WidgetRemainingToken widgetRemainingToken;
    private WidgetTokenOnBoarding widgetTokenOnBoarding;
    private LinearLayout layoutTimer;
    private ProgressBar progressBar;
    private AbstractionRouter abstractionRouter;
    private TextView infoTitlePage;

    private TokenData tokenData;

    private ImageView ivContainer;
    private long prevTimeStamp;

    private ActionListener listener;
    private Handler crackTokenErrorhandler;
    private Handler crackTokenSuccessHandler;
    private PerformanceMonitoring fpmRender;
    private PerformanceMonitoring fpmCrack;

    public static Fragment newInstance() {
        return new CrackTokenFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fpmRender = PerformanceMonitoring.start(FPM_RENDER);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_crack_token, container, false);

        ivContainer = rootView.findViewById(R.id.iv_container);
        textCountdownTimer = rootView.findViewById(R.id.text_countdown_timer);
        widgetTokenView = rootView.findViewById(R.id.widget_token_view);
        widgetCrackResult = rootView.findViewById(R.id.widget_reward);
        widgetRemainingToken = rootView.findViewById(R.id.widget_remaining_token_view);
        layoutTimer = rootView.findViewById(R.id.layout_timer);
        progressBar = rootView.findViewById(R.id.progress_bar);
        infoTitlePage = rootView.findViewById(R.id.text_info_page);

        widgetTokenOnBoarding = rootView.findViewById(R.id.widget_token_onboarding);

        abstractionRouter = (AbstractionRouter) getActivity().getApplication();

        widgetCrackResult.setListener(new WidgetCrackResult.WidgetCrackResultListener() {
            @Override
            public void onClickCtaButton(CrackResult crackResult, String titleBtn) {
                if (crackResult.isCrackButtonDismiss(crackResult.getCtaButton())) {
                    widgetCrackResult.clearCrackResult();

                    crackTokenPresenter.getGetTokenTokopoints();
                } else if (crackResult.isCrackButtonRedirect(crackResult.getCtaButton())) {
                    trackingButtonClick(crackResult.getBenefitType(), titleBtn);

                    ApplinkUtil.navigateToAssociatedPage(getActivity(), crackResult.getCtaButton().getApplink(),
                            crackResult.getCtaButton().getUrl(),
                            CrackTokenActivity.class);
                }
            }

            @Override
            public void onClickReturnButton(CrackResult crackResult, String titleBtn) {
                if (crackResult.isCrackButtonDismiss(crackResult.getReturnButton())) {
                    widgetCrackResult.clearCrackResult();

                    if (crackResult.isCrackTokenSuccess()) {
                        trackingButtonClick(crackResult.getBenefitType(), titleBtn);
                    } else if (crackResult.isCrackTokenExpired()) {
                        trackingExpiredBtnClick();
                    } else if (crackResult.isTryAgainBtn()) {
                        trackingTryAgainBtnClick();
                    }

                    crackTokenPresenter.getGetTokenTokopoints();
                } else if (crackResult.isCrackButtonRedirect(crackResult.getReturnButton())) {
                    trackingButtonClick(crackResult.getBenefitType(), titleBtn);

                    ApplinkUtil.navigateToAssociatedPage(getActivity(),
                            crackResult.getReturnButton().getApplink(),
                            crackResult.getReturnButton().getUrl(),
                            CrackTokenActivity.class);
                }
            }

            @Override
            public void onClickCloseButton() {
                widgetCrackResult.clearCrackResult();
                crackTokenPresenter.getGetTokenTokopoints();
            }

            @Override
            public void onClickCloseButtonWhenError() {
                getActivity().onBackPressed();
            }

            @Override
            public void onCrackResultCleared() {
                listener.showToolbar();
            }

            @Override
            public void onTrackingCloseRewardButton(CrackResult crackResult) {
                trackingCloseRewardButtonClick(crackResult);
            }
        });

        return rootView;
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
        crackTokenPresenter.initializePage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        crackTokenPresenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        // restart the timer (only if the timer was paused in onPaused)
        if (tokenData != null) {
            if (prevTimeStamp > 0) {
                long currentTimeStamp = System.currentTimeMillis();
                int diffSeconds = (int) ((currentTimeStamp - prevTimeStamp) / 1000L);
                TokenUser tokenUser = tokenData.getHome().getTokensUser();
                int prevTimeRemainingSecond = tokenUser.getTimeRemainingSeconds();
                tokenUser.setTimeRemainingSeconds(prevTimeRemainingSecond - diffSeconds);

                showTimer(tokenData);

                prevTimeStamp = 0;
            }

            widgetTokenOnBoarding.showHandOnboarding();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the previous time to enable the timer in onResume.
        if (tokenData != null) {
            if (tokenData.isShowCountDown() && countDownTimer != null) {
                prevTimeStamp = System.currentTimeMillis();
            } else {
                prevTimeStamp = 0;
            }
            stopTimer();
            widgetTokenOnBoarding.hideHandOnBoarding(false);
        }
        if (crackTokenSuccessHandler != null) {
            crackTokenSuccessHandler.removeCallbacksAndMessages(null);
        }
        if (crackTokenErrorhandler != null) {
            crackTokenErrorhandler.removeCallbacksAndMessages(null);
        }
    }

    private void renderViewCrackEgg() {
        TokenUser tokenUser = tokenData.getHome().getTokensUser();

        infoTitlePage.setText(tokenData.getHome().getTokensUser().getTitle());

        ImageHandler.loadImageWithSignature(ivContainer, tokenUser.getBackgroundAsset().getBackgroundImgUrl(),
                new StringSignature(tokenUser.getBackgroundAsset().getVersion()));

        widgetTokenView.setToken(tokenUser.getTokenAsset());
        widgetTokenView.setListener(new WidgetTokenView.WidgetTokenListener() {
            @Override
            public void onClick() {
                fpmCrack = PerformanceMonitoring.start(FPM_CRACKING);
                stopTimer();
                hideInfoTitle();
                vibrate();
                widgetTokenOnBoarding.hideHandOnBoarding(true);
                listener.hideToolbar();
                TokenUser tokenUser = tokenData.getHome().getTokensUser();
                crackTokenPresenter.crackToken(tokenUser.getTokenUserID(), tokenUser.getCampaignID());

                trackingLuckyEggClick();
            }
        });

        widgetRemainingToken.show();
        widgetRemainingToken.showRemainingToken(
                tokenUser.getTokenAsset().getSmallImgUrl(),
                tokenData.getSumTokenStr(),
                tokenData.getHome().getCountingMessage());

        showTimer(tokenData);
        showInfoTitle();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(VIBRATE_DURATION);
            }
        }
    }

    private void hideInfoTitle() {
        infoTitlePage.setVisibility(View.GONE);
    }

    private void showInfoTitle() {
        infoTitlePage.setVisibility(View.VISIBLE);
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        textCountdownTimer.setVisibility(View.GONE);
    }

    private void initTimerBound() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageHeight = TokenMarginUtil.getEggWidth(rootWidth, rootHeight);
        int marginTop = TokenMarginUtil.getEggMarginBottom(rootHeight) - imageHeight
                - getContext().getResources().getDimensionPixelOffset(R.dimen.dp_112);

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) layoutTimer.getLayoutParams();
        ivFullLp.gravity = Gravity.CENTER_HORIZONTAL;
        ivFullLp.topMargin = marginTop;
        layoutTimer.requestLayout();
        layoutTimer.setVisibility(View.VISIBLE);
    }

    private void showTimer(@NonNull TokenData tokenData) {
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

        TokenUser tokenUser = tokenData.getHome().getTokensUser();
        if (tokenUser.getShowTime()) {
            textCountdownTimer.setVisibility(View.VISIBLE);
            showCountdownTimer(tokenUser.getTimeRemainingSeconds());
        } else {
            textCountdownTimer.setVisibility(View.GONE);
        }
    }

    private void showCountdownTimer(final int timeRemainingSeconds) {
        if (timeRemainingSeconds > 0) {
            stopTimer();
            countDownTimer = new CountDownTimer(timeRemainingSeconds * COUNTDOWN_INTERVAL_SECOND,
                    COUNTDOWN_INTERVAL_SECOND) {
                @Override
                public void onTick(long millisUntilFinished) {
                    CrackTokenFragment.this.onTick(millisUntilFinished);
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

    private void onTick(long millisUntilFinished) {
        if (!isAdded()) {
            return;
        }
        int timeRemainingSeconds = (int) (millisUntilFinished / COUNTDOWN_INTERVAL_SECOND);
        timeRemainingSeconds--;
        tokenData.getHome().getTokensUser().setTimeRemainingSeconds(timeRemainingSeconds);
        if (timeRemainingSeconds <= 0) {
            stopTimer();
            widgetTokenView.hide();
            widgetTokenOnBoarding.hideHandOnBoarding(false);
            crackTokenPresenter.getGetTokenTokopoints();
        } else {
            setUIFloatingTimer(timeRemainingSeconds);
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
            textCountdownTimer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void navigateToLoginPage() {
        if (getActivity().getApplication() instanceof GamificationRouter
                && ((GamificationRouter) getActivity().getApplication()).getLoginIntent() != null) {
            startActivityForResult(((GamificationRouter) getActivity().getApplication()).getLoginIntent(), REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void closePage() {
        getActivity().finish();
    }

    @Override
    public String getSuccessRewardLabel() {
        return getString(R.string.success_reward_label);
    }

    @Override
    public void onSuccessGetToken(TokenData tokenData) {
        listener.showToolbar();
        if (tokenData.getSumToken() == 0) {
            listener.directPageToCrackEmpty(tokenData);
        } else {
            this.tokenData = tokenData;
            crackTokenPresenter.downloadAllAsset(getContext(), this.tokenData);
        }
    }

    @Override
    public void onSuccessDownloadAllAsset() {
        renderViewCrackEgg();
        widgetTokenOnBoarding.showHandOnboarding();
        trackingLuckyEggView();
        if (fpmRender != null)
            fpmRender.stopTrace();
    }

    @Override
    public void onErrorGetToken(CrackResult crackResult) {
        listener.hideToolbar();
        widgetCrackResult.showCrackResult(crackResult);
    }

    @Override
    public void onSuccessCrackToken(final CrackResult crackResult) {
        if ((crackResult.getImageBitmap() == null || crackResult.getImageBitmap().isRecycled()) &&
                !TextUtils.isEmpty(crackResult.getImageUrl())) {
            Glide.with(getContext())
                    .load(crackResult.getImageUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            crackResult.setImageBitmap(resource);
                            showCrackWidgetSuccess(crackResult);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            crackResult.setImageBitmap(null);
                            // image load is failed, but we need to show the text instead.
                            showCrackWidgetSuccess(crackResult);
                        }
                    });
        } else {
            showCrackWidgetSuccess(crackResult);
        }
    }

    private void showCrackWidgetSuccess(final CrackResult crackResult) {
        initCrackTokenSuccessHandler();
        crackTokenSuccessHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                widgetTokenView.split();

                widgetCrackResult.showCrackResult(crackResult);

                trackingRewardLuckyEggView(crackResult.getBenefitType());
            }
        }, widgetTokenView.isCrackPercentageFull() ? 1 : 1000);
    }

    private void initCrackTokenSuccessHandler() {
        if (crackTokenSuccessHandler == null) {
            crackTokenSuccessHandler = new Handler();
        } else {
            crackTokenSuccessHandler.removeCallbacksAndMessages(null);
        }
    }

    public boolean isShowReward() {
        return widgetCrackResult.isShowReward();
    }

    public void dismissReward() {
        widgetCrackResult.dismissReward();
    }

    @Override
    public void onErrorCrackToken(final CrackResult crackResult) {
        initCrackTokenErrorHandler();
        crackTokenErrorhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                widgetTokenView.stopShaking();

                widgetCrackResult.showCrackResult(crackResult);
            }
        }, 1000);
    }

    @Override
    public void onFinishCrackToken() {
        if (fpmCrack != null)
            fpmCrack.stopTrace();
    }

    private void initCrackTokenErrorHandler() {
        if (crackTokenErrorhandler == null) {
            crackTokenErrorhandler = new Handler();
        } else {
            crackTokenErrorhandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActionListener) {
            listener = (ActionListener) context;
        }
    }

    private void trackingLuckyEggView() {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                            GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                            GamificationEventTracking.Action.IMPRESSION,
                            String.valueOf(tokenData.getFloating().getTokenId())
                    );
        }
    }

    private void trackingLuckyEggClick() {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                            GamificationEventTracking.Action.CRACK_LUCKY_EGG,
                            String.valueOf(tokenData.getFloating().getTokenId())
                    );
        }
    }

    private void trackingRewardLuckyEggView(String benefitType) {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                            GamificationEventTracking.Category.VIEW_REWARD,
                            GamificationEventTracking.Action.IMPRESSION,
                            benefitType
                    );
        }
    }

    private void trackingButtonClick(String benefitType, String buttonTitle) {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Category.REWARD_CLICK + benefitType,
                            GamificationEventTracking.Action.CLICK,
                            buttonTitle
                    );
        }
    }

    private void trackingTryAgainBtnClick() {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Category.ERROR_PAGE,
                            GamificationEventTracking.Action.CLICK_TRY_AGAIN,
                            ""
                    );
        }
    }

    private void trackingExpiredBtnClick() {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Category.EXPIRED_TOKEN,
                            GamificationEventTracking.Action.CLICK_OK,
                            ""
                    );
        }
    }

    private void trackingCloseRewardButtonClick(CrackResult crackResult) {
        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
            String category = "";
            if (crackResult.isCrackTokenSuccess()) {
                if (crackResult.isTokenUserInvalid()) {
                    category = GamificationEventTracking.Category.ERROR_PAGE;
                } else if (crackResult.isCrackTokenExpired()) {
                    category = GamificationEventTracking.Category.EXPIRED_TOKEN;
                }
            } else {
                category = GamificationEventTracking.Category.ERROR_PAGE;
            }

            if (!category.equals("")) {
                abstractionRouter
                        .getAnalyticTracker()
                        .sendEventTracking(
                                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                                category,
                                GamificationEventTracking.Action.CLICK_CLOSE_BUTTON,
                                ""
                        );
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                crackTokenPresenter.onLoginDataReceived();
                break;
        }
    }

    public interface ActionListener {
        void directPageToCrackEmpty(TokenData tokenData);

        void hideToolbar();

        void showToolbar();
    }
}