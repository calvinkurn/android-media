package com.tokopedia.gamification.taptap.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.util.TokenMarginUtil;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.di.GamificationComponentInstance;
import com.tokopedia.gamification.taptap.activity.TapTapTokenActivity;
import com.tokopedia.gamification.taptap.compoundview.NetworkErrorHelper;
import com.tokopedia.gamification.taptap.compoundview.WidgetCrackResultTapTap;
import com.tokopedia.gamification.taptap.compoundview.WidgetTokenViewTapTap;
import com.tokopedia.gamification.taptap.contract.TapTapTokenContract;
import com.tokopedia.gamification.taptap.data.entiity.ActionButton;
import com.tokopedia.gamification.taptap.data.entiity.BackButton;
import com.tokopedia.gamification.taptap.data.entiity.GamiTapEggHome;
import com.tokopedia.gamification.taptap.data.entiity.TimeRemaining;
import com.tokopedia.gamification.taptap.data.entiity.TokenAsset;
import com.tokopedia.gamification.taptap.data.entiity.TokensUser;
import com.tokopedia.gamification.taptap.database.GamificationDatabaseWrapper;
import com.tokopedia.gamification.taptap.database.GamificationDbCallback;
import com.tokopedia.gamification.taptap.presenter.TapTapTokenPresenter;
import com.tokopedia.gamification.taptap.utils.TapTapConstants;
import com.tokopedia.gamification.util.HexValidator;

import java.util.List;

import javax.inject.Inject;


public class TapTapTokenFragment extends BaseDaggerFragment implements TapTapTokenContract.View, GamificationDbCallback {

    private static final String FPM_RENDER = "ft_gamification";
    private static final String FPM_CRACKING = "ft_gamification_cracking_egg";
    public static final int VIBRATE_DURATION = 500;
    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;
    private static final int REQUEST_CODE_LOGIN = 112;

    @Inject
    TapTapTokenPresenter crackTokenPresenter;

    private View rootView;

    private CountDownTimer countDownTimer;

    private TextView textCountdownTimer;
    private WidgetTokenViewTapTap widgetTokenView;
    private WidgetCrackResultTapTap widgetCrackResult;
    private ProgressBar progressBar;
    private AbstractionRouter abstractionRouter;
    private TextView infoTitlePage;

    private ImageView imageRemainingToken;
    private TextView tvCounter;
    private FrameLayout flRemainingToken;
    private GamiTapEggHome tokenData;
    private ImageView ivContainer;
    private long prevTimeStamp;
    private ActionListener listener;
    private Handler crackTokenErrorhandler;
    private Handler crackTokenSuccessHandler;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Button buttonUp, buttonDown;
    private PerformanceMonitoring fpmRender;
    private PerformanceMonitoring fpmCrack;
    private View rootContainer;
    private BackPopupDialogFragment backPopupDialogFragment;

    @Inject
    GamificationDatabaseWrapper gamificationDatabaseWrapper;
    private TapTapSummaryDialogFragment summaryPageDialogFragment;
    private boolean isExitButtonClickedOnDialog = false;

    public static Fragment newInstance() {
        return new TapTapTokenFragment();
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
        rootView = inflater.inflate(R.layout.fragment_tap_tap_token, container, false);

        ivContainer = rootView.findViewById(R.id.iv_container);
        rootContainer = rootView.findViewById(R.id.root_container);
        textCountdownTimer = rootView.findViewById(R.id.text_countdown_timer);
        widgetTokenView = rootView.findViewById(R.id.widget_token_view);
        widgetCrackResult = rootView.findViewById(R.id.widget_reward);
        progressBar = rootView.findViewById(R.id.progress_bar);
        infoTitlePage = rootView.findViewById(R.id.text_info_page);
        toolbar = rootView.findViewById(R.id.toolbar);
        buttonUp = rootView.findViewById(R.id.button_cta);
        buttonDown = rootView.findViewById(R.id.button_return);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.tap_tap_title));
        imageRemainingToken = toolbar.findViewById(R.id.image_remaining_token);
        tvCounter = toolbar.findViewById(R.id.tv_floating_counter);
        flRemainingToken = toolbar.findViewById(R.id.fl_remaining_token);
        setUpToolBar();
        abstractionRouter = (AbstractionRouter) getActivity().getApplication();
//        showSummaryPopup();


        widgetCrackResult.setListener(new WidgetCrackResultTapTap.WidgetCrackResultListener() {
            @Override
            public void onCrackResultCleared() {
//                crackTokenPresenter.getGetTokenTokopoints();
                if (tokenData != null
                        && tokenData.getTokensUser() != null
                        && TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
                    widgetTokenView.startRotateBackAnimation();
                } else {
                    crackTokenPresenter.getGetTokenTokopoints(false, true);
                }

            }
        });

        return rootView;
    }

    private void setUpToolBar() {
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.white));
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
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
                long diffSeconds = (long) ((currentTimeStamp - prevTimeStamp) / 1000L);

                long prevTimeRemainingSecond = tokenData.getTimeRemaining().getSeconds();
                tokenData.getTimeRemaining().setSeconds(prevTimeRemainingSecond - diffSeconds);

                showRewards(tokenData);

                prevTimeStamp = 0;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the previous time to enable the timer in onResume.
        if (tokenData != null) {
            if (tokenData.getTimeRemaining().getIsShow() && countDownTimer != null) {
                prevTimeStamp = System.currentTimeMillis();
            } else {
                prevTimeStamp = 0;
            }
            stopTimer();
        }
        if (crackTokenSuccessHandler != null) {
            crackTokenSuccessHandler.removeCallbacksAndMessages(null);
        }
        if (crackTokenErrorhandler != null) {
            crackTokenErrorhandler.removeCallbacksAndMessages(null);
        }
    }

    private void renderViewCrackEgg() {
        TokenAsset tokenAsset = tokenData.getTokenAsset();
        TokensUser tokenUser = tokenData.getTokensUser();
        TimeRemaining timeRemaining = tokenData.getTimeRemaining();


        if (!TextUtils.isEmpty(tokenUser.getTitle())) {
            infoTitlePage.setText(tokenUser.getTitle());
            showInfoTitle();
        } else {
            hideInfoTitle();
        }
        if (timeRemaining.getIsShow()) {
            Drawable counterBackground = textCountdownTimer.getBackground();
            if (counterBackground instanceof GradientDrawable) {
                GradientDrawable drawable = ((GradientDrawable) counterBackground);


                drawable.setStroke(getResources().getDimensionPixelOffset(R.dimen.dp_4), Color.parseColor(timeRemaining.getFontColor()));

                if (HexValidator.validate(timeRemaining.getBackgroundColor())) {
                    drawable.setColor(Color.parseColor(timeRemaining.getBackgroundColor()));
                }
            }
            if (HexValidator.validate(timeRemaining.getFontColor())) {
                textCountdownTimer.setTextColor(Color.parseColor(timeRemaining.getFontColor()));
            }
        }
        setActionButtons();
        ImageHandler.loadImageWithSignature(ivContainer, tokenAsset.getBackgroundImgURL(),
                new StringSignature(tokenAsset.getVersion()));

        if (tokenUser.isEmptyState()) {
            widgetTokenView.setEmptyToken(tokenAsset, tokenUser);
        } else {
            widgetTokenView.setToken(tokenAsset, tokenUser);
            widgetTokenView.setListener(new WidgetTokenViewTapTap.WidgetTokenListener() {
                @Override
                public void onClick() {
                    fpmCrack = PerformanceMonitoring.start(FPM_CRACKING);
//                    stopTimer();
                    hideInfoTitle();
                    vibrate();
                    TokensUser tokenUser = tokenData.getTokensUser();
                    crackTokenPresenter.crackToken(tokenUser.getTokenUserID(), tokenUser.getCampaignID());

//                trackingLuckyEggClick();
                }

                @Override
                public void showCrackResult(CrackResultEntity crackResult) {
                    widgetCrackResult.showCrackResult(crackResult);

                }

                @Override
                public void reShowEgg() {
                    downloadAssets();
                }
            });
        }
//        showRemainingToken(tokenAsset.getTokenAsset().getSmallImgv2Url(), tokenData.getSumTokenStr());
        showRewards(tokenData);
    }

    private void setActionButtons() {
        if (tokenData.getActionButton() != null && tokenData.getActionButton().size() != 0) {
            for (int i = 0; i < tokenData.getActionButton().size(); i++) {
                ActionButton actionButton = tokenData.getActionButton().get(i);
                if (!actionButton.getIsDisable() && buttonUp.getVisibility() == View.GONE) {
                    setActionButton(buttonUp, actionButton);
                } else if (!actionButton.getIsDisable() && buttonDown.getVisibility() == View.GONE) {
                    setActionButton(buttonDown, actionButton);
                }
            }
        } else {
            buttonUp.setVisibility(View.GONE);
            buttonDown.setVisibility(View.GONE);
        }
    }

    private void setActionButton(Button actionBtn, ActionButton actionButton) {
        actionBtn.setText(actionButton.getText());
        int resId = getButtonBackgroundId(actionButton.getBackgroundColor());
        actionBtn.setBackgroundResource(resId);
        actionBtn.setVisibility(View.VISIBLE);
        actionBtn.setTextColor(getResources().getColor(getButtonTextColor(actionButton.getBackgroundColor())));
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TapTapConstants.ButtonType.PLAY_WITH_POINTS.equalsIgnoreCase(actionButton.getType())) {
                    crackTokenPresenter.playWithPoints();
                } else {
                    ApplinkUtil.navigateToAssociatedPage(getActivity(), actionButton.getApplink(),
                            actionButton.getUrl(),
                            TapTapTokenActivity.class);
                }
            }
        });
    }

    private int getButtonBackgroundId(String backgroundColor) {
        if (TapTapConstants.ButtonColor.GREEN.equalsIgnoreCase(backgroundColor)) {
            return R.drawable.gf_rounded_btn_green_taptap;
        } else if (TapTapConstants.ButtonColor.WHITE.equalsIgnoreCase(backgroundColor)) {
            return R.drawable.gf_rounded_btn_white_taptap;
        } else {
            return R.drawable.gf_rounded_btn_white_corners_taptap;
        }
    }

    private int getButtonTextColor(String backgroundColor) {
        if (TapTapConstants.ButtonColor.GREEN.equalsIgnoreCase(backgroundColor)) {
            return R.color.white;
        } else if (TapTapConstants.ButtonColor.WHITE.equalsIgnoreCase(backgroundColor)) {
            return R.color.black_70;
        } else {
            return R.color.white;
        }
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

    private void initInfoViewLayout() {

        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageHeight = TokenMarginUtil.getEggWidth(rootWidth, rootHeight);
        int marginTop = TokenMarginUtil.getEggMarginBottom(rootHeight) - imageHeight
                - getContext().getResources().getDimensionPixelOffset(R.dimen.dp_112);

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) infoTitlePage.getLayoutParams();
        ivFullLp.gravity = Gravity.CENTER_HORIZONTAL;
        ivFullLp.topMargin = marginTop;
        infoTitlePage.requestLayout();

    }

    private void showRewards(@NonNull GamiTapEggHome tokenData) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initTimerBound();
                initInfoViewLayout();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        TimeRemaining tokenRemaining = tokenData.getTimeRemaining();
        if (tokenRemaining.getIsShow()) {
            textCountdownTimer.setVisibility(View.VISIBLE);
            showCountdownTimer(tokenRemaining.getSeconds());
        } else {
            textCountdownTimer.setVisibility(View.GONE);
        }
    }


    private void showCountdownTimer(final long timeRemainingSeconds) {
        if (timeRemainingSeconds > 0) {
            stopTimer();
            countDownTimer = new CountDownTimer(timeRemainingSeconds * COUNTDOWN_INTERVAL_SECOND,
                    COUNTDOWN_INTERVAL_SECOND) {
                @Override
                public void onTick(long millisUntilFinished) {
                    TapTapTokenFragment.this.onTick(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    stopTimer();
//                    if (TapTapConstants.TokenState.STATE_LOBBY.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
                    if (crackTokenPresenter != null) {
                        crackTokenPresenter.getGetTokenTokopoints(false, true);
                    }
//                    } else if (TapTapConstants.TokenState.STATE_CRACK_LIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
//                        crackTokenPresenter.getGetTokenTokopoints(false, i);
//                    } else if (TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
//                        showSummaryPopup();
//                    }
                }
            }.start();
            textCountdownTimer.setVisibility(View.VISIBLE);
        } else {
            textCountdownTimer.setVisibility(View.GONE);
        }
    }

    private void showSummaryPopup() {
        gamificationDatabaseWrapper.getAllEntries(this);
    }

    private void checkPendingCampaignAndShowSummary() {

        gamificationDatabaseWrapper.getAllEntriesForCampaignId(this, tokenData.getTokensUser().getCampaignID());

    }

    private void onTick(long millisUntilFinished) {
        if (!isAdded()) {
            return;
        }
        int timeRemainingSeconds = (int) (millisUntilFinished / COUNTDOWN_INTERVAL_SECOND);
        timeRemainingSeconds--;
        tokenData.getTimeRemaining().setSeconds(timeRemainingSeconds);
//        if (timeRemainingSeconds <= 0) {
//            stopTimer();
//            widgetTokenView.hide();
//            crackTokenPresenter.getGetTokenTokopoints();
//        } else {
        setUIFloatingTimer(timeRemainingSeconds);
//        }
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
    public void showErrorSnackBar(String errormessage) {
        if (getContext() != null) {
            NetworkErrorHelper.showErrorSnackBar(errormessage, getContext(), rootView, true);
        }
    }

    @Override
    public void showErrorSnackBar() {
        if (getContext() != null) {
            NetworkErrorHelper.showErrorSnackBar(getContext().getResources().getString(R.string.points_not_available), getContext(), rootView, true);
        }
    }

    @Override
    public View getRootView() {
        return rootContainer;
    }

    @Override
    public void navigateToHomePage() {
        if (getContext() != null)
            ((GamificationRouter) getContext().getApplicationContext()).goToHome(getContext());
    }


    @Override
    public void onSuccessGetToken(TokenDataEntity tokenData) {
//        if (tokenData.getSumToken() == 0) {
//            listener.directPageToCrackEmpty(tokenData);
//        } else {
//            this.tokenData = tokenData;
//            crackTokenPresenter.downloadAllAsset(getContext(), this.tokenData);
//        }
    }

    @Override
    public void onSuccessGetToken(GamiTapEggHome gamiTapEggHome, boolean isRefetchEgg) {
        if (tokenData != null
                && tokenData.getTokensUser() != null
                && gamiTapEggHome.getTokensUser() != null
                && tokenData.getTokensUser().getCampaignID() == gamiTapEggHome.getTokensUser().getCampaignID()) {
            boolean isPreviousStateLobby = TapTapConstants.TokenState.STATE_LOBBY.equalsIgnoreCase(tokenData.getTokensUser().getState());
            boolean isCurrentStateLobby = TapTapConstants.TokenState.STATE_LOBBY.equalsIgnoreCase(gamiTapEggHome.getTokensUser().getState());
            boolean isPreviousStateLimited = TapTapConstants.TokenState.STATE_CRACK_LIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState());
            boolean isCurrentStateLimited = TapTapConstants.TokenState.STATE_CRACK_LIMITED.equalsIgnoreCase(gamiTapEggHome.getTokensUser().getState());
            boolean isPreviousStateUnLimited = TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState());
            boolean isCurrentStateUnLimited = TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(gamiTapEggHome.getTokensUser().getState());


            if (isPreviousStateLobby
                    && (isCurrentStateLimited
                    || isCurrentStateUnLimited)) {
                widgetTokenView.fadeOutEggs();
            } else if (isPreviousStateLimited && isCurrentStateLimited) {
                this.tokenData = gamiTapEggHome;
                widgetTokenView.startRotateBackAnimation();
                return;
            } else if (isPreviousStateUnLimited && isCurrentStateUnLimited) {
                this.tokenData = gamiTapEggHome;
                gamificationDatabaseWrapper.getAllEntries(this);
            } else if (!isCurrentStateLimited && isPreviousStateLimited) {
                this.tokenData = gamiTapEggHome;
                showSummaryPopup();
            } else if (!isCurrentStateUnLimited && isPreviousStateUnLimited) {
                showSummaryPopup();
            }
        }
        this.tokenData = gamiTapEggHome;
        checkPendingCampaignAndShowSummary();
        downloadAssets();


    }

    private void downloadAssets() {
        if (tokenData.getTokensUser() != null) {
            if (tokenData.getTokensUser().isEmptyState()) {
                //            listener.directPageToCrackEmpty(tokenData);
                crackTokenPresenter.downloadEmptyAssets(getContext(), this.tokenData);
            } else {

                crackTokenPresenter.downloadAllAsset(getContext(), this.tokenData);
            }
        }
    }

    @Override
    public void onSuccessDownloadAllAsset() {
        renderViewCrackEgg();
//        trackingLuckyEggView();
        if (fpmRender != null)
            fpmRender.stopTrace();
    }

    @Override
    public void onErrorGetToken(CrackResultEntity crackResult) {
        widgetCrackResult.showCrackResult(crackResult);
    }

    @Override
    public void onSuccessCrackToken(final CrackResultEntity crackResult) {
        if (crackResult != null) {
            gamificationDatabaseWrapper.insert(tokenData.getTokensUser().getCampaignID(), crackResult);
        }
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
//                        trackingRewardLuckyEggView(crackResult.getBenefitType());
                    } else {
                        crackTokenSuccessHandler.postDelayed(this, 100);
                    }
                }
            }
        });
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

    public boolean isShowBackPopup() {
        return tokenData != null && tokenData.getBackButton().getIsShow();
    }


    @Override
    public void onErrorCrackToken(final CrackResultEntity crackResult) {
        initCrackTokenErrorHandler();
        crackTokenErrorhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                if (getContext() != null) {
                    widgetTokenView.stopShaking();
                    widgetCrackResult.showCrackResult(crackResult);
                }
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

//    private void trackingLuckyEggView() {
//        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
//            abstractionRouter
//                    .getAnalyticTracker()
//                    .sendEventTracking(
//                            GamificationEventTracking.Event.VIEW_LUCKY_EGG,
//                            GamificationEventTracking.Category.CRACK_LUCKY_EGG,
//                            GamificationEventTracking.Action.IMPRESSION,
//                            String.valueOf(tokenData.getFloating().getTokenId())
//                    );
//        }
//    }
//
//    private void trackingLuckyEggClick() {
//        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
//            abstractionRouter
//                    .getAnalyticTracker()
//                    .sendEventTracking(
//                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
//                            GamificationEventTracking.Category.CRACK_LUCKY_EGG,
//                            GamificationEventTracking.Action.CRACK_LUCKY_EGG,
//                            String.valueOf(tokenData.getFloating().getTokenId())
//                    );
//        }
//    }
//
//    private void trackingRewardLuckyEggView(String benefitType) {
//        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
//            abstractionRouter
//                    .getAnalyticTracker()
//                    .sendEventTracking(
//                            GamificationEventTracking.Event.VIEW_LUCKY_EGG,
//                            GamificationEventTracking.Category.VIEW_REWARD,
//                            GamificationEventTracking.Action.IMPRESSION,
//                            benefitType
//                    );
//        }
//    }
//
//    private void trackingButtonClick(String benefitType, String buttonTitle) {
//        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
//            abstractionRouter
//                    .getAnalyticTracker()
//                    .sendEventTracking(
//                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
//                            GamificationEventTracking.Category.REWARD_CLICK + benefitType,
//                            GamificationEventTracking.Action.CLICK,
//                            buttonTitle
//                    );
//        }
//    }
//
//    private void trackingTryAgainBtnClick() {
//        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
//            abstractionRouter
//                    .getAnalyticTracker()
//                    .sendEventTracking(
//                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
//                            GamificationEventTracking.Category.ERROR_PAGE,
//                            GamificationEventTracking.Action.CLICK_TRY_AGAIN,
//                            ""
//                    );
//        }
//    }
//
//    private void trackingExpiredBtnClick() {
//        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
//            abstractionRouter
//                    .getAnalyticTracker()
//                    .sendEventTracking(
//                            GamificationEventTracking.Event.CLICK_LUCKY_EGG,
//                            GamificationEventTracking.Category.EXPIRED_TOKEN,
//                            GamificationEventTracking.Action.CLICK_OK,
//                            ""
//                    );
//        }
//    }
//
//    private void trackingCloseRewardButtonClick(CrackResultEntity crackResult) {
//        if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
//            String category = "";
//            if (crackResult.isCrackTokenSuccess()) {
//                if (crackResult.isTokenUserInvalid()) {
//                    category = GamificationEventTracking.Category.ERROR_PAGE;
//                } else if (crackResult.isCrackTokenExpired()) {
//                    category = GamificationEventTracking.Category.EXPIRED_TOKEN;
//                }
//            } else {
//                category = GamificationEventTracking.Category.ERROR_PAGE;
//            }
//
//            if (!category.equals("")) {
//                abstractionRouter
//                        .getAnalyticTracker()
//                        .sendEventTracking(
//                                GamificationEventTracking.Event.CLICK_LUCKY_EGG,
//                                category,
//                                GamificationEventTracking.Action.CLICK_CLOSE_BUTTON,
//                                ""
//                        );
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                crackTokenPresenter.onLoginDataReceived();
                break;
        }
    }

    @Override
    public void onSuccessGetFromDb(List<CrackResultEntity> crackResultEntities) {
        if (crackResultEntities != null && crackResultEntities.size() != 0) {
            if (isExitButtonClickedOnDialog)                   //turn to false if true
                isExitButtonClickedOnDialog = false;
            showSummaryDialogOnSuccess();
        }
    }

    private void showSummaryDialogOnSuccess() {
        if (getContext() != null) {
            summaryPageDialogFragment = TapTapSummaryDialogFragment.createDialog();
        }
        if (tokenData != null) {
            summaryPageDialogFragment.setRewardButtons(tokenData.getRewardButton());
        }
        if (summaryPageDialogFragment != null && summaryPageDialogFragment.getDialog() != null && summaryPageDialogFragment.getDialog().isShowing()) {
            summaryPageDialogFragment.dismiss();
        } else if (summaryPageDialogFragment != null) {
            summaryPageDialogFragment.show(getChildFragmentManager(), "summaryPageDialogFragment");
        }

    }

    @Override
    public void onErrorGetFromDb() {

        if (isExitButtonClickedOnDialog) {           //if no record found and exit from dialog then go to home page
            isExitButtonClickedOnDialog = false;
            navigateToHomePage();
        }
    }

    @Override
    public void onSuccessGetFromDbForCampaign(List<CrackResultEntity> crackResultEntities) {
        if (crackResultEntities != null && crackResultEntities.size() != 0) {
            showSummaryDialogOnSuccess();
        }
    }

    public void clearViewAndAnimations() {
        widgetTokenView.clearTokenAnimation();
        widgetCrackResult.clearCrackResult();
    }

    public interface ActionListener {
        void directPageToCrackEmpty(TokenDataEntity tokenData);
    }

    public void showBackPopup() {
        // custom dialog

//        if (getContext() != null && tokenData != null && tokenData.getBackButton() != null && backPopupDialogFragment == null) {
//            backPopupDialogFragment = BackPopupDialogFragment.createDialog();
//        }
//        if (getContext() != null && tokenData != null && tokenData.getBackButton() != null && backPopupDialogFragment != null) {
//            backPopupDialogFragment.setBackButton(tokenData.getBackButton());
//            backPopupDialogFragment.show(getChildFragmentManager(), "tokenDialog");
//        }
        if (getContext() != null && tokenData != null && tokenData.getBackButton() != null) {
            BackButton backButton = tokenData.getBackButton();
            final Dialog dialog = new Dialog(getContext());
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.gf_popup_exit_game);

            TextView textHeader = dialog.findViewById(R.id.tv_header);
            TextView textSubHeader = dialog.findViewById(R.id.tv_subheader);
            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
            Button btnOkay = dialog.findViewById(R.id.btn_ok);
            ImageView imagePopupHeader = dialog.findViewById(R.id.image_popup_header);
            textHeader.setText(backButton.getTitle());
            textSubHeader.setText(backButton.getText());
            btnOkay.setText(backButton.getYesText());
            btnCancel.setText(backButton.getCancelText());
            ImageHandler.loadImage(getContext(), imagePopupHeader, backButton.getImageURL(), R.color.grey_1100, R.color.grey_1100);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (tokenData != null
                            && tokenData.getTokensUser() != null) {
                        if (TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
                            if (crackTokenPresenter != null) {
                                crackTokenPresenter.getGetTokenTokopoints(true, false);
                            }
                        } else if (TapTapConstants.TokenState.STATE_CRACK_LIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
                            gamificationDatabaseWrapper.getAllEntries(TapTapTokenFragment.this);
                        } else {
                            navigateToHomePage();
                        }
                    } else {
                        navigateToHomePage();
                    }
                    isExitButtonClickedOnDialog = true;
                }
            });

            dialog.show();

        }
    }


}