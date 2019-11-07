package com.tokopedia.gamification.cracktoken.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetCrackResult;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetEggSource;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetRewardCrackResult;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetTokenOnBoarding;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetTokenView;
import com.tokopedia.gamification.cracktoken.contract.CrackTokenContract;
import com.tokopedia.gamification.cracktoken.presenter.CrackTokenPresenter;
import com.tokopedia.gamification.cracktoken.util.TokenMarginUtil;
import com.tokopedia.gamification.data.entity.CrackBenefitEntity;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.HomeActionButton;
import com.tokopedia.gamification.data.entity.HomeSmallButton;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.data.entity.TokenUserEntity;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.di.GamificationComponentInstance;
import com.tokopedia.gamification.taptap.compoundview.NetworkErrorHelper;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.unifyprinciples.Typography;

import java.util.List;

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
    private WidgetEggSource widgetEggSource;
    private WidgetTokenOnBoarding widgetTokenOnBoarding;
    private ProgressBar progressBar;
    private TextView infoTitlePage;
    private FrameLayout crackLayoutTooltip;

    private ImageView imageRemainingToken;
    private TextView tvCounter;
    private FrameLayout flRemainingToken;
    private TokenDataEntity tokenData;
    private ImageView ivContainer;
    private long prevTimeStamp;
    private ActionListener listener;
    private Handler crackTokenErrorhandler;
    private Handler crackTokenSuccessHandler;
    private Handler tooltipHandler;
    private WidgetRewardCrackResult widgetRewards;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private PerformanceMonitoring fpmRender;
    private PerformanceMonitoring fpmCrack;
    private ImageView ivPrize;
    private FrameLayout flPrize;

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
        crackLayoutTooltip = rootView.findViewById(R.id.tooltip_crack_layout);
        ivPrize = rootView.findViewById(R.id.daily_prize);
        flPrize = rootView.findViewById(R.id.fl_prize);
        textCountdownTimer = rootView.findViewById(R.id.text_countdown_timer);
        widgetTokenView = rootView.findViewById(R.id.widget_token_view);
        widgetCrackResult = rootView.findViewById(R.id.widget_reward);
        widgetEggSource = rootView.findViewById(R.id.widget_remaining_token_view);
        widgetRewards = rootView.findViewById(R.id.widget_rewards);
        progressBar = rootView.findViewById(R.id.progress_bar);
        infoTitlePage = rootView.findViewById(R.id.text_info_page);
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.toko_points_title));
        imageRemainingToken = toolbar.findViewById(R.id.image_remaining_token);
        tvCounter = toolbar.findViewById(R.id.tv_floating_counter);
        flRemainingToken = toolbar.findViewById(R.id.fl_remaining_token);
        widgetTokenOnBoarding = rootView.findViewById(R.id.widget_token_onboarding);
        setUpToolBar();

        widgetCrackResult.setListener(new WidgetCrackResult.WidgetCrackResultListener() {
            @Override
            public void onClickCtaButton(CrackResultEntity crackResult, String titleBtn) {
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
            public void onClickReturnButton(CrackResultEntity crackResult, String titleBtn) {
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
                setToolbarColor(getResources().getColor(R.color.black), getResources().getColor(R.color.toolbar_color));
            }

            @Override
            public void renderBenefits(List<CrackBenefitEntity> benefits, String benefitType) {
                widgetRewards.showCounterAnimations(benefits);
            }

            @Override
            public void onTrackingCloseRewardButton(CrackResultEntity crackResult) {
                trackingCloseRewardButtonClick(crackResult);
            }
        });

        return rootView;
    }

    private void showToolTip() {
        if (!widgetTokenOnBoarding.hasSeenOnBoardingFromPref()) {
            tooltipHandler = new Handler();
            tooltipHandler.postDelayed(() -> {
                if (crackLayoutTooltip != null)
                    crackLayoutTooltip.setVisibility(View.GONE);
            }, 4000);
            crackLayoutTooltip.setVisibility(View.VISIBLE);
            crackLayoutTooltip.setOnClickListener(v -> crackLayoutTooltip.setVisibility(View.GONE));
            Typography tooltipText = crackLayoutTooltip.findViewById(R.id.gf_tooltip_text);
            SpannableString spannableString = new SpannableString(tooltipText.getText());
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 4, 15,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tooltipText.setText(spannableString);
        } else {
            if (tooltipHandler != null)
                tooltipHandler.removeCallbacksAndMessages(null);
            crackLayoutTooltip.setVisibility(View.GONE);
        }
    }

    private void setUpToolBar() {
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.black));
    }

    private void setToolbarColor(int titleColor, int toolbarBackgroundColor) {
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(titleColor, PorterDuff.Mode.SRC_ATOP);
        }
        toolbar.setBackgroundColor(toolbarBackgroundColor);
        toolbarTitle.setTextColor(titleColor);
    }

    private void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
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
    public void onDestroyView() {
        widgetCrackResult.clearCrackResult();
        widgetTokenView.releaseResourcesOnDestroy();
        crackTokenPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        // restart the timer (only if the timer was paused in onPaused)
        if (tokenData != null) {
            if (prevTimeStamp > 0) {
                long currentTimeStamp = System.currentTimeMillis();
                int diffSeconds = (int) ((currentTimeStamp - prevTimeStamp) / 1000L);
                TokenUserEntity tokenUser = tokenData.getHome().getTokensUser();
                int prevTimeRemainingSecond = tokenUser.getTimeRemainingSeconds();
                tokenUser.setTimeRemainingSeconds(prevTimeRemainingSecond - diffSeconds);

                showRewards(tokenData);

                prevTimeStamp = 0;
            }
            showToolTip();
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
        if (tooltipHandler != null) {
            tooltipHandler.removeCallbacksAndMessages(null);
        }
        if (crackTokenErrorhandler != null) {
            crackTokenErrorhandler.removeCallbacksAndMessages(null);
        }
    }

    private void renderViewCrackEgg() {
        TokenUserEntity tokenUser = tokenData.getHome().getTokensUser();
        List<HomeActionButton> homeActionButtons = tokenData.getHome().getHomeActionButton();
        HomeSmallButton homeSmallButton = tokenData.getHome().getHomeSmallButton();

        infoTitlePage.setText(tokenData.getHome().getTokensUser().getTitle());

        String backgroundUrl = tokenUser.getBackgroundAsset().getBackgroundImgUrl();
        ObjectKey signature = new ObjectKey(tokenUser.getBackgroundAsset().getVersion());
        ImageHandler.loadImageWithSignature(ivContainer, backgroundUrl, signature);

        if (TextUtils.isEmpty(homeSmallButton.getImageURL())) {
            flPrize.setVisibility(View.GONE);
        } else {
            flPrize.setVisibility(View.VISIBLE);
            ImageHandler.loadImageAndCache(ivPrize, homeSmallButton.getImageURL());
            ivPrize.setOnClickListener(v -> {
                crackLayoutTooltip.setVisibility(View.GONE);
                ApplinkUtil.navigateToAssociatedPage(getActivity(), homeSmallButton.getAppLink(), homeSmallButton.getUrl(), CrackTokenActivity.class);
                trackingDailyPrizeBtnClick();
            });
        }
        widgetTokenView.setToken(tokenUser.getTokenAsset());
        widgetTokenView.setListener(new WidgetTokenView.WidgetTokenListener() {
            @Override
            public void onClick() {
                if (getContext() == null)
                    return;
                fpmCrack = PerformanceMonitoring.start(FPM_CRACKING);
                widgetTokenOnBoarding.hideHandOnBoarding(true);
                TokenUserEntity tokenUser = tokenData.getHome().getTokensUser();
                crackTokenPresenter.crackToken(tokenUser.getTokenUserID(), tokenUser.getCampaignID());

                trackingLuckyEggClick();
            }

            @Override
            public void showCrackResult(CrackResultEntity crackResult) {
                if (getActivity() == null || getActivity().isFinishing() || getContext() == null || !isAdded() || isRemoving()) {
                    return;
                }
                setToolbarColor(getResources().getColor(R.color.white), getResources().getColor(R.color.transparent));
                widgetCrackResult.showCrackResult(crackResult);

            }
        });
        showRemainingToken(tokenUser.getTokenAsset().getSmallImgv2Url(), tokenData.getSumTokenStr());
        if (homeActionButtons.size() > 0) {
            HomeActionButton actionButton = homeActionButtons.get(0);
            widgetEggSource.showEggSource(actionButton.getText());
            widgetEggSource.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(actionButton.getAppLink()) || !TextUtils.isEmpty(actionButton.getUrl())) {
                    ApplinkUtil.navigateToAssociatedPage(getActivity(), actionButton.getAppLink(), actionButton.getUrl(), CrackTokenActivity.class);
                    trackingMainGameLainnyaClick(actionButton.getText());
                }
            });
        } else {
            widgetEggSource.hide();
        }
        showRewards(tokenData);
        showInfoTitle();
    }

    private void showRemainingToken(String smallImageUrl, String remainingTokenString) {
        ImageHandler.loadImageAndCache(imageRemainingToken, smallImageUrl);
        if (TextUtils.isEmpty(remainingTokenString)) {
            tvCounter.setVisibility(View.GONE);
            flRemainingToken.setVisibility(View.GONE);

        } else {
            tvCounter.setText(remainingTokenString);
            tvCounter.setVisibility(View.VISIBLE);
            flRemainingToken.setVisibility(View.VISIBLE);

        }
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
        int rootHeight = rootView.getHeight();
        int marginTop = TokenMarginUtil.getTimerMarginBottom(rootHeight);
        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) textCountdownTimer.getLayoutParams();
        ivFullLp.gravity = Gravity.CENTER_HORIZONTAL;
        ivFullLp.topMargin = marginTop;
        textCountdownTimer.requestLayout();
    }

    private void showRewards(@NonNull TokenDataEntity tokenData) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                infoTitlePage.setVisibility(View.VISIBLE);
                widgetRewards.setVisibility(View.VISIBLE);
                initTimerBound();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        TokenUserEntity tokenUser = tokenData.getHome().getTokensUser();
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
    public void updateRewards(int points, int coupons, int loyalty) {
        widgetRewards.setRewards(points, coupons, loyalty);
    }

    @Override
    public void onSuccessGetToken(TokenDataEntity tokenData) {
        setToolbarColor(getResources().getColor(R.color.black), getResources().getColor(R.color.toolbar_color));
        if (tokenData.getSumToken() == 0) {
            listener.directPageToCrackEmpty(tokenData);
        } else {
            this.tokenData = tokenData;
            crackTokenPresenter.downloadAllAsset(getContext(), this.tokenData);
        }
    }

    @Override
    public void onSuccessDownloadAllAsset() {
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            hideLoading();
            renderViewCrackEgg();
            showToolTip();
            widgetTokenOnBoarding.showHandOnboarding();
            trackingLuckyEggView();
            if (fpmRender != null)
                fpmRender.stopTrace();
        });

    }

    @Override
    public void onErrorGetToken(CrackResultEntity crackResult) {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            loadNetworkConnectionErrorBottomSheet();
        } else {
            setToolbarColor(getResources().getColor(R.color.white), getResources().getColor(R.color.transparent));
            widgetCrackResult.showCrackResult(crackResult);
        }
    }

    @Override
    public void onSuccessCrackToken(final CrackResultEntity crackResult) {
        crackLayoutTooltip.setVisibility(View.GONE);
        stopTimer();
        hideInfoTitle();
        vibrate();
        fpmCrack = PerformanceMonitoring.start(FPM_CRACKING);
        if ((crackResult.getImageBitmap() == null || crackResult.getImageBitmap().isRecycled()) &&
                !TextUtils.isEmpty(crackResult.getImageUrl())) {
            Glide.with(getContext())
                    .asBitmap()
                    .load(crackResult.getImageUrl())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            crackResult.setImageBitmap(resource);
                            showCrackWidgetSuccess(crackResult);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            crackResult.setImageBitmap(null);
                            // image load is failed, but we need to show the text instead.
                            showCrackWidgetSuccess(crackResult);
                        }
                    });
        } else {
            showCrackWidgetSuccess(crackResult);
        }
    }

    private void showCrackWidgetSuccess(final CrackResultEntity crackResult) {
        initCrackTokenSuccessHandler();
        crackTokenSuccessHandler.post(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                if (getContext() != null) {
                    if (widgetTokenView.isCrackPercentageFull()) {
                        widgetTokenView.clearTokenAnimation();
                        widgetTokenView.split(crackResult);
                        trackingRewardLuckyEggView(crackResult.getBenefitType());
                    } else {
                        crackTokenSuccessHandler.postDelayed(this, 50);
                    }
                }
            }
        });     //1250ms as egg crack animation takes 1230ms
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

    private void loadNetworkConnectionErrorBottomSheet() {
        CloseableBottomSheetDialog bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        View view = getLayoutInflater().inflate(R.layout.gf_network_connection_bottomsheet, null, true);
        ImageView closeBtn = view.findViewById(R.id.gf_close_button);
        Typography tryAgainButton = view.findViewById(R.id.gf_no_internet_try_again);
        tryAgainButton.setOnClickListener(v -> {
            widgetCrackResult.clearCrackResult();

            crackTokenPresenter.getGetTokenTokopoints();
            bottomSheet.dismiss();
        });
        closeBtn.setOnClickListener((v) -> {
            bottomSheet.dismiss();
            this.getActivity().finish();
        });
        bottomSheet.setCustomContentView(view, "", false);
        bottomSheet.show();
    }

    @Override
    public void onErrorCrackToken(final CrackResultEntity crackResult) {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (widgetTokenView.getTimesFullEggClicked() % 3 == 0) {
            if (getContext() != null) {
                if (crackResult.getResultStatus() != null && crackResult.getResultStatus().getMessage() != null
                        && !TextUtils.isEmpty(crackResult.getResultStatus().getMessage().get(0))) {
                    NetworkErrorHelper.showErrorSnackBar(crackResult.getResultStatus().getMessage().get(0), getContext(), rootView, true);

                } else {
                    NetworkErrorHelper.showErrorSnackBar(getString(R.string.gf_crack_token_response_error), getContext(), rootView, true);
                }
                trackingSnackbarError(getString(R.string.gf_crack_token_response_error));
            }
        }
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
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                GamificationEventTracking.Action.IMPRESSION,
                String.valueOf(tokenData.getFloating().getTokenId())
        ));
    }

    private void trackingLuckyEggClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                GamificationEventTracking.Action.CRACK_LUCKY_EGG,
                String.valueOf(tokenData.getFloating().getTokenId())
        ));
    }

    private void trackingRewardLuckyEggView(String benefitType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                GamificationEventTracking.Category.VIEW_REWARD,
                GamificationEventTracking.Action.IMPRESSION,
                benefitType
        ));
    }

    private void trackingButtonClick(String benefitType, String buttonTitle) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                GamificationEventTracking.Category.REWARD_CLICK + benefitType,
                GamificationEventTracking.Action.CLICK,
                buttonTitle
        ));
    }

    private void trackingTryAgainBtnClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                GamificationEventTracking.Category.ERROR_PAGE,
                GamificationEventTracking.Action.CLICK_TRY_AGAIN,
                ""
        ));
    }

    private void trackingExpiredBtnClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                GamificationEventTracking.Category.EXPIRED_TOKEN,
                GamificationEventTracking.Action.CLICK_OK,
                ""
        ));
    }

    private void trackingDailyPrizeBtnClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                GamificationEventTracking.Action.CLICK_DAILY_PRIZE,
                ""
        ));
    }

    private void trackingMainGameLainnyaClick(String buttonText) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                GamificationEventTracking.Action.CLICK,
                buttonText
        ));
    }

    private void trackingSnackbarError(String errorText) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                GamificationEventTracking.Event.VIEW_LUCKY_EGG,
                GamificationEventTracking.Category.CRACK_LUCKY_EGG,
                GamificationEventTracking.Action.VIEW_ERROR,
                errorText
        ));
    }

    private void trackingCloseRewardButtonClick(CrackResultEntity crackResult) {
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
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                    category,
                    GamificationEventTracking.Action.CLICK_CLOSE_BUTTON,
                    ""
            ));
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
        void directPageToCrackEmpty(TokenDataEntity tokenData);
    }
}