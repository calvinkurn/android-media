package com.tokopedia.gamification.cracktoken.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationComponentInstance;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetCrackResult;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetRemainingToken;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetTokenOnBoarding;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetTokenView;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.model.CrackResult;
import com.tokopedia.gamification.cracktoken.presenter.CrackTokenPresenter;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.floating.view.model.TokenData;
import com.tokopedia.gamification.floating.view.model.TokenUser;
import com.tokopedia.gamification.applink.ApplinkUtil;

import javax.inject.Inject;

/**
 * @author Rizky on 28/03/18.
 */

public class CrackTokenFragment extends BaseDaggerFragment implements CrackTokenContract.View {

    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;

    public static final double RATIO_MARGIN_TOP_TIMER = 0.05;

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

    public static Fragment newInstance() {
        return new CrackTokenFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
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
            public void onClickCtaButton(CrackResult crackResult) {
                if (crackResult.isCrackButtonDismiss(crackResult.getCtaButton())) {
                    widgetCrackResult.clearCrackResult();

                    crackTokenPresenter.getGetTokenTokopoints();
                } else if (crackResult.isCrackButtonRedirect(crackResult.getCtaButton())) {
                    trackingButtonClick(GamificationEventTracking.Category.POINT_AND_LOYALTY_REWARD,
                            GamificationEventTracking.Action.CLICK_TO_TOKOPOINT,
                            "");

                    ApplinkUtil.navigateToAssociatedPage(getActivity(), crackResult.getCtaButton().getApplink(),
                            crackResult.getCtaButton().getUrl(),
                            CrackTokenActivity.class);
                }
            }

            @Override
            public void onClickReturnButton(CrackResult crackResult) {
                if (crackResult.isCrackButtonDismiss(crackResult.getReturnButton())) {
                    widgetCrackResult.clearCrackResult();

                    if (crackResult.isCrackTokenSuccess()) {
                        trackingButtonClick(GamificationEventTracking.Category.POINT_AND_LOYALTY_REWARD,
                                GamificationEventTracking.Action.CLICK_CRACK_OTHER_EGG,
                                "");
                    } else if (crackResult.isCrackTokenExpired()) {
                        trackingButtonClick(GamificationEventTracking.Category.EXPIRED_TOKEN,
                                GamificationEventTracking.Action.CLICK_OK,
                                "");
                    }

                    crackTokenPresenter.getGetTokenTokopoints();
                } else if (crackResult.isCrackButtonRedirect(crackResult.getReturnButton())) {
                    if (crackResult.isCrackTokenSuccess()) {
                        trackingButtonClick(GamificationEventTracking.Category.COUPON_REWARD,
                                GamificationEventTracking.Action.CLICK_USE_GIFT,
                                widgetCrackResult.getBenefitType());
                    } else {
                        trackingButtonClick(GamificationEventTracking.Category.ERROR_PAGE,
                                GamificationEventTracking.Action.CLICK_TRY_AGAIN,
                                "");
                    }

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
        crackTokenPresenter.getGetTokenTokopoints();
    }

    @Override
    public void onResume() {
        super.onResume();
        // restart the timer (only if the timer was paused in onPaused)
        if (tokenData != null && prevTimeStamp > 0) {
            long currentTimeStamp = System.currentTimeMillis();
            int diffSeconds = (int) ((currentTimeStamp - prevTimeStamp) / 1000L);
            TokenUser tokenUser = tokenData.getHome().getTokensUser();
            int prevTimeRemainingSecond = tokenUser.getTimeRemainingSeconds();
            tokenUser.setTimeRemainingSeconds(prevTimeRemainingSecond - diffSeconds);

            showTimer(tokenData);

            prevTimeStamp = 0;
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
        }
    }

    private void renderViewCrackEgg() {
        widgetTokenView.reset();

        TokenUser tokenUser = tokenData.getHome().getTokensUser();

        infoTitlePage.setText(tokenData.getHome().getTokensUser().getTitle());
        ImageHandler.loadImageAndCache(ivContainer, tokenUser.getBackgroundAsset().getBackgroundImgUrl());

        widgetTokenView.setToken(tokenUser.getTokenAsset());
        widgetTokenView.setListener(new WidgetTokenView.WidgetTokenListener() {
            @Override
            public void onClick() {
                stopTimer();
                hideHandOnBoarding();
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
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        textCountdownTimer.setVisibility(View.GONE);
    }

    private void initTimerBound() {
        int rootHeight = rootView.getHeight();
        int imageMarginTop = (int) (RATIO_MARGIN_TOP_TIMER * rootHeight);

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) layoutTimer.getLayoutParams();
        ivFullLp.topMargin = imageMarginTop;
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
        int timeRemainingSeconds = (int) (millisUntilFinished / COUNTDOWN_INTERVAL_SECOND);
        tokenData.getHome().getTokensUser().setTimeRemainingSeconds(timeRemainingSeconds);
        if (timeRemainingSeconds <= 0) {
            stopTimer();
            widgetTokenView.hide();
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
    public void onSuccessGetToken(TokenData tokenData) {
        if (tokenData.getSumToken() == 0) {
            listener.directPageToCrackEmpty();
        } else {
            this.tokenData = tokenData;
            renderViewCrackEgg();
            showHandOnBoarding();
            trackingLuckyEggView();
        }
    }

    private void showHandOnBoarding() {
        widgetTokenOnBoarding.showHandOnboarding();
    }

    private void hideHandOnBoarding() {
        widgetTokenOnBoarding.hideHandOnBoarding();
    }

    @Override
    public void onErrorGetToken(CrackResult crackResult) {
        widgetCrackResult.showCrackResult(crackResult);
    }

    @Override
    public void onSuccessCrackToken(final CrackResult crackResult) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                widgetTokenView.split();

                widgetCrackResult.showCrackResult(crackResult);

                trackingRewardLuckyEggView(crackResult.getBenefits().get(0).getText());
            }
        }, widgetTokenView.isCrackPercentageFull() ? 1 : 1500);
    }

    @Override
    public void onErrorCrackToken(final CrackResult crackResult) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                widgetTokenView.stopShaking();

                widgetCrackResult.showCrackResult(crackResult);
            }
        }, 1000);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActionListener) {
            listener = (ActionListener) context;
        }
    }

    private void trackingLuckyEggView() {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                            GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                            GamificationEventTracking.Action.IMPRESSION_LUCKY_EGG,
                            tokenData.getFloating().getTokenAsset().getName()
                    );
        }
    }

    private void trackingLuckyEggClick() {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                            GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                            GamificationEventTracking.Action.CRACK_LUCKY_EGG,
                            tokenData.getFloating().getTokenAsset().getName()
                    );
        }
    }

    private void trackingRewardLuckyEggView(String benefitName) {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                            GamificationEventTracking.Category.VIEW_REWARD,
                            GamificationEventTracking.Action.IMPRESSION_LUCKY_EGG,
                            benefitName
                    );
        }
    }

    private void trackingButtonClick(String category, String action, String label) {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            abstractionRouter
                    .getAnalyticTracker()
                    .sendEventTracking(
                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                            category,
                            action,
                            label
                    );
        }
    }

    private void trackingCloseRewardButtonClick(CrackResult crackResult) {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            String category = "";
            if (crackResult.isCrackTokenSuccess()) {
                if (crackResult.isBenefitTypeCoupon()) {
                    category = GamificationEventTracking.Category.COUPON_REWARD;
                } else {
                    category = GamificationEventTracking.Category.POINT_AND_LOYALTY_REWARD;
                }
            } else {
                if (crackResult.isCrackTokenExpired()) {
                    category = GamificationEventTracking.Category.EXPIRED_TOKEN;
                } else {
                    category = GamificationEventTracking.Category.ERROR_PAGE;
                }
            }
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

    public interface ActionListener {
        void directPageToCrackEmpty();
    }
}