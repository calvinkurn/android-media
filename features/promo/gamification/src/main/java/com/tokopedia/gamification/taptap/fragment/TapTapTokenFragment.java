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
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.HexValidator;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.data.entity.CrackResultEntity;
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
import com.tokopedia.gamification.taptap.utils.TapTapAnalyticsTrackerUtil;
import com.tokopedia.gamification.taptap.utils.TapTapConstants;
import com.tokopedia.gamification.taptap.utils.TokenMarginUtilTapTap;
import com.tokopedia.promogamification.common.applink.ApplinkUtil;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class TapTapTokenFragment extends BaseDaggerFragment implements TapTapTokenContract.View, GamificationDbCallback, TapTapSummaryDialogFragment.InteractionListener {

    private static final String FPM_RENDER = "ft_gamification";
    private static final String FPM_CRACKING = "ft_gamification_cracking_egg";
    public static final int VIBRATE_DURATION = 500;
    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;
    private static final int REQUEST_CODE_LOGIN = 112;
    private static final long SERVER_EXTRA_DELAY = 500;

    @Inject
    TapTapTokenPresenter crackTokenPresenter;

    private View rootView;

    private Subscription countDownTimer;

    private TextView textCountdownTimer;
    private WidgetTokenViewTapTap widgetTokenView;
    private WidgetCrackResultTapTap widgetCrackResult;
    private ProgressBar progressBar;
    private TextView infoTitlePage;

    private ImageView imageShareVia;
    private GamiTapEggHome tokenData;
    private ImageView ivContainer;
    private Handler crackTokenSuccessHandler;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Button buttonUp, buttonDown;
    private PerformanceMonitoring fpmRender;
    private PerformanceMonitoring fpmCrack;
    private View rootContainer;

    GamificationDatabaseWrapper gamificationDatabaseWrapper;
    private TapTapSummaryDialogFragment summaryPageDialogFragment;
    private boolean isExitButtonClickedOnDialog = false;
    private UserSessionInterface userSession;
    private Handler crackTokenErrorhandler;

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
        rootView = inflater.inflate(com.tokopedia.gamification.R.layout.fragment_tap_tap_token, container, false);

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
        toolbarTitle.setText(getString(com.tokopedia.gamification.R.string.tap_tap_title));
        imageShareVia = toolbar.findViewById(R.id.image_share);
        setUpToolBar();
        userSession = new UserSession(getContext());
        imageShareVia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShareActivity();
            }
        });
        widgetCrackResult.setListener(new WidgetCrackResultTapTap.WidgetCrackResultListener() {
            @Override
            public void onCrackResultCleared() {
                renderViewOnCrackResultCleared();
            }
        });

        return rootView;
    }

    private void renderViewOnCrackResultCleared() {
        if (tokenData != null
                && tokenData.getTokensUser() != null
                && TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
            widgetTokenView.startRotateBackAnimation();
        } else {
            crackTokenPresenter.getGetTokenTokopoints(false, true);
        }
    }

    private void startShareActivity() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = String.format(getString(com.tokopedia.gamification.R.string.share_branch_link_body), userSession.getName());
        sharingIntent.putExtra(Intent.EXTRA_TITLE, getString(com.tokopedia.gamification.R.string.share_branch_link_msg_title));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(com.tokopedia.gamification.R.string.share_branch_link_msg_title));
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(com.tokopedia.gamification.R.string.share_via_label)));
        TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                TapTapAnalyticsTrackerUtil.ActionKeys.TAP_EGG_CLICK,
                TapTapAnalyticsTrackerUtil.LabelKeys.PRESS_SHARE_BUTTON);
    }

    private void setUpToolBar() {
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), com.tokopedia.abstraction.R.drawable.ic_action_back));
        setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), com.tokopedia.unifyprinciples.R.color.Unify_N0));
        toolbarTitle.setTextColor(getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
    }

    private void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null) {
            GamificationComponent gamificationComponent =
                    GamificationComponentInstance.getComponent(getActivity());
            gamificationComponent.inject(this);
            gamificationDatabaseWrapper = new GamificationDatabaseWrapper(getActivity());
        }
        crackTokenPresenter.attachView(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        crackTokenPresenter.initializePage();
    }

    @Override
    public void onDestroyView() {
        stopTimer();
        clearViewAndAnimations();
        crackTokenPresenter.detachView();
        super.onDestroyView();
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
        if (timeRemaining.isShow()) {
            Drawable counterBackground = textCountdownTimer.getBackground();
            if (counterBackground instanceof GradientDrawable) {
                GradientDrawable drawable = ((GradientDrawable) counterBackground);

                if (HexValidator.validate(timeRemaining.getBorderColor())) {
                    drawable.setStroke(getResources().getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_4), Color.parseColor(timeRemaining.getBorderColor()));
                }

                if (HexValidator.validate(timeRemaining.getBackgroundColor())) {
                    drawable.setColor(Color.parseColor(timeRemaining.getBackgroundColor()));
                }
            }
            if (HexValidator.validate(timeRemaining.getFontColor())) {
                textCountdownTimer.setTextColor(Color.parseColor(timeRemaining.getFontColor()));
            }
        }
        setActionButtons();
        loadBackgroundImage(tokenAsset.getBackgroundImgURL());
        if (tokenUser.isEmptyState()) {
            widgetTokenView.setEmptyToken(tokenAsset, tokenUser);
        } else {
            widgetTokenView.setToken(tokenAsset, tokenUser);
            widgetTokenView.setListener(new WidgetTokenViewTapTap.WidgetTokenListener() {
                @Override
                public void onClick() {
                    if (getContext() == null)
                        return;
                    fpmCrack = PerformanceMonitoring.start(FPM_CRACKING);
                    hideInfoTitle();
                    vibrate();
                    TokensUser tokenUser = tokenData.getTokensUser();
                    crackTokenPresenter.crackToken(tokenUser.getTokenUserID(), tokenUser.getCampaignID());

                }

                @Override
                public void showCrackResult(CrackResultEntity crackResult) {
                    widgetCrackResult.showCrackResult(crackResult);

                }

                @Override
                public void reShowEgg() {
                    if (tokenData != null && tokenData.getTokensUser() != null)
                        if (TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState()))
                            widgetTokenView.resetForUnlimitedCrack(tokenData.getTokensUser());
                        else
                            downloadAssets();
                }

                @Override
                public void reShowFromLobby() {
                    downloadAssets();
                }
            });
        }
        showInfoAndTimerView(tokenData);
    }

    private void loadBackgroundImage(String backgroundImgURL) {
        CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                ivContainer.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        };
        ImageHandler.loadImageBitmap2(getContext(), backgroundImgURL, target);
    }

    private void setActionButtons() {
        buttonUp.setVisibility(View.GONE);
        buttonDown.setVisibility(View.GONE);
        if (tokenData.getActionButton() != null && tokenData.getActionButton().size() != 0) {
            for (int i = 0; i < tokenData.getActionButton().size(); i++) {
                ActionButton actionButton = tokenData.getActionButton().get(i);
                if (!actionButton.isDisable() && buttonUp.getVisibility() == View.GONE) {
                    setActionButton(buttonUp, actionButton);
                } else if (!actionButton.isDisable() && buttonDown.getVisibility() == View.GONE) {
                    setActionButton(buttonDown, actionButton);
                }
            }
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
                handleActionButtonClick(actionButton);

            }
        });
    }

    private void handleActionButtonClick(ActionButton actionButton) {
        if (TapTapConstants.ButtonType.PLAY_WITH_POINTS.equalsIgnoreCase(actionButton.getType())) {
            crackTokenPresenter.playWithPoints(true);
        } else {
            ApplinkUtil.navigateToAssociatedPage(getActivity(), actionButton.getApplink(), actionButton.getUrl(), TapTapTokenActivity.class);
        }
        if (TapTapConstants.TokenState.STATE_LOBBY.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
            sendActionButtonEvent(TapTapAnalyticsTrackerUtil.ActionKeys.TAP_EGG_CLICK, actionButton.getText());
        }
        if (TapTapConstants.TokenState.STATE_EMPTY.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
            sendActionButtonEvent(TapTapAnalyticsTrackerUtil.ActionKeys.EMPTY_STATE_CLICK, actionButton.getText());
        }
    }


    private int getButtonBackgroundId(String backgroundColor) {
        if (TapTapConstants.ButtonColor.GREEN.equalsIgnoreCase(backgroundColor)) {
            return com.tokopedia.gamification.R.drawable.gf_rounded_btn_green_taptap;
        } else if (TapTapConstants.ButtonColor.WHITE.equalsIgnoreCase(backgroundColor)) {
            return com.tokopedia.gamification.R.drawable.gf_rounded_btn_white_taptap;
        } else {
            return com.tokopedia.gamification.R.drawable.gf_rounded_btn_white_corners_taptap;
        }
    }

    private int getButtonTextColor(String backgroundColor) {
        if (TapTapConstants.ButtonColor.GREEN.equalsIgnoreCase(backgroundColor)) {
            return com.tokopedia.unifyprinciples.R.color.Unify_N0;
        } else if (TapTapConstants.ButtonColor.WHITE.equalsIgnoreCase(backgroundColor)) {
            return com.tokopedia.design.R.color.black_70;
        } else {
            return com.tokopedia.unifyprinciples.R.color.Unify_N0;
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
            countDownTimer.unsubscribe();
            countDownTimer = null;
        }
        textCountdownTimer.setVisibility(View.GONE);
    }

    private void initTimerBound() {
        int rootHeight = rootView.getHeight();
        int marginTop = TokenMarginUtilTapTap.getTimerMarginBottom(rootHeight);
        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) textCountdownTimer.getLayoutParams();
        ivFullLp.gravity = Gravity.CENTER_HORIZONTAL;
        ivFullLp.topMargin = marginTop;
        textCountdownTimer.requestLayout();
    }

    private void initInfoViewLayout() {

        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageHeight = TokenMarginUtilTapTap.getEggWidth(rootWidth, rootHeight);
        int marginTop = TokenMarginUtilTapTap.getEggMarginBottom(rootHeight) - imageHeight
                - getContext().getResources().getDimensionPixelOffset(R.dimen.dp_112);

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) infoTitlePage.getLayoutParams();
        ivFullLp.gravity = Gravity.CENTER_HORIZONTAL;
        ivFullLp.topMargin = marginTop;
        infoTitlePage.requestLayout();

    }

    private void showInfoAndTimerView(@NonNull GamiTapEggHome tokenData) {
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
        if (tokenRemaining.isShow()) {
            textCountdownTimer.setVisibility(View.VISIBLE);
            showCountdownTimer(tokenRemaining.getSeconds());
        } else {
            stopTimer();
        }
    }

    private long currentTime;

    private void showCountdownTimer(final long timeRemainingSeconds) {
        if (timeRemainingSeconds > 0) {
            stopTimer();
            currentTime = timeRemainingSeconds;
            countDownTimer = Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onBackpressureBuffer()
                    .takeUntil(new Func1<Object, Boolean>() {
                        @Override
                        public Boolean call(Object aLong) {
                            return currentTime == -1;
                        }
                    })
                    .subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Object o) {
                            TapTapTokenFragment.this.onTick(currentTime);
                            currentTime--;

                        }
                    });

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

    private void onTick(long secondsUntilFinished) {
        if (!isAdded()) {
            return;
        }
        if (secondsUntilFinished <= 0) {
            setUIFloatingTimer(secondsUntilFinished);
            stopTimer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getContext() != null && crackTokenPresenter != null) {
                        crackTokenPresenter.getGetTokenTokopoints(false, true);
                    }
                }
            }, SERVER_EXTRA_DELAY);
        } else {
            setUIFloatingTimer(secondsUntilFinished);
        }
    }

    private void setUIFloatingTimer(long timeRemainingSeconds) {
        if (textCountdownTimer != null) {
            int seconds = (int) timeRemainingSeconds;
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes % 60;
            seconds = seconds % 60;
            textCountdownTimer.setText(String.format(getString(com.tokopedia.gamification.R.string.countdown_format), hours, minutes, seconds));
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
        if (getActivity() != null) {
            Intent loginIntent = RouteManager.getIntent(getActivity(), ApplinkConst.LOGIN);
            startActivityForResult(loginIntent, REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void closePage() {
        if (getActivity() != null && !getActivity().isFinishing())
            getActivity().finish();
    }

    @Override
    public String getSuccessRewardLabel() {
        return getString(com.tokopedia.gamification.R.string.success_reward_label);
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
            NetworkErrorHelper.showErrorSnackBar(getContext().getResources().getString(com.tokopedia.gamification.R.string.points_not_available), getContext(), rootView, true);
        }
    }


    @Override
    public void showErrorSnackBarOnSummaryPage() {
        if (getContext() != null && summaryPageDialogFragment != null && summaryPageDialogFragment.isAdded() && summaryPageDialogFragment.isVisible()) {
            summaryPageDialogFragment.showErrorSnackBar(getContext().getResources().getString(com.tokopedia.gamification.R.string.points_not_available));
        }
    }

    @Override
    public void showErrorSnackBarOnSummaryPage(String errorMessage) {
        if (getContext() != null && summaryPageDialogFragment != null && summaryPageDialogFragment.isAdded() && summaryPageDialogFragment.isVisible()) {
            summaryPageDialogFragment.showErrorSnackBar(errorMessage);
        }
    }

    @Override
    public void dismissSummaryPage() {
        if (summaryPageDialogFragment != null && summaryPageDialogFragment.isAdded() && summaryPageDialogFragment.isVisible()) {
            summaryPageDialogFragment.dismiss();
        }
    }

    @Override
    public void showErrorSnackBarOnCrackError(String errorMessage, boolean resetEggForUnknownErrorCodes) {

        initCrackTokenErrorHandler();
        crackTokenErrorhandler.post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null) {
                    if (widgetTokenView.isCrackPercentageFull()) {
                        NetworkErrorHelper.showErrorSnackBar(errorMessage, getContext(), rootView, true);
                        if (resetEggForUnknownErrorCodes) {
                            widgetTokenView.clearTokenAnimation();
                            widgetTokenView.resetForUnlimitedCrack(tokenData.getTokensUser());
                            widgetTokenView.stopMediaPlayer();
                            return;
                        }
                        if (tokenData != null
                                && TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
                            widgetTokenView.resetForUnlimitedCrack(tokenData.getTokensUser());
                            widgetTokenView.stopMediaPlayer();
                            return;
                        }
                        stopTimer();
                        crackTokenPresenter.getGetTokenTokopoints(true, false);
                    } else {
                        crackTokenErrorhandler.postDelayed(this, 50);
                    }
                }
            }
        });

    }

    @Override
    public View getRootView() {
        return rootContainer;
    }

    @Override
    public void navigateToHomePage() {
        if (getActivity() != null && !getActivity().isFinishing())
            getActivity().finish();
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

            if (!tokenData.getTokensUser().getState().equalsIgnoreCase(gamiTapEggHome.getTokensUser().getState())
                    || (isCurrentStateLobby && isPreviousStateLobby)) {
                stopTimer();
            }


            if (isPreviousStateLobby
                    && (isCurrentStateLimited
                    || isCurrentStateUnLimited)) {
                this.tokenData = gamiTapEggHome;
                widgetTokenView.fadeOutEggs();
                return;
            } else if (isPreviousStateLimited && isCurrentStateLimited) {
                this.tokenData = gamiTapEggHome;
                widgetTokenView.startRotateBackAnimation();
                return;
            } else if (isPreviousStateUnLimited && isCurrentStateUnLimited) {
                this.tokenData = gamiTapEggHome;
                showSummaryPopup();
            } else if (!isCurrentStateLimited && isPreviousStateLimited) {
                this.tokenData = gamiTapEggHome;
                showSummaryPopup();
            } else if (!isCurrentStateUnLimited && isPreviousStateUnLimited) {
                showSummaryPopup();
            }
        }

        this.tokenData = gamiTapEggHome;
        if (gamiTapEggHome.getTokensUser() != null) {
            if (gamiTapEggHome.getTokensUser().isEmptyState()) {
                sendScreenEvent(TapTapAnalyticsTrackerUtil.ActionKeys.EMPTY_STATE_IMPRESSION, "");
            } else {
                sendScreenEvent(TapTapAnalyticsTrackerUtil.ActionKeys.TAP_TAP_IMPRESSION, String.valueOf(gamiTapEggHome.getTokensUser().getTokenUserID()));
            }
        }
        checkPendingCampaignAndShowSummary();
        downloadAssets();
    }

    private void downloadAssets() {
        if (tokenData != null && tokenData.getTokensUser() != null) {
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
        if (getActivity() == null) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            hideLoading();
            renderViewCrackEgg();
            if (fpmRender != null)
                fpmRender.stopTrace();
        });
    }

    @Override
    public void onSuccessCrackToken(final CrackResultEntity crackResult) {
        if (crackResult != null) {
            gamificationDatabaseWrapper.insert(tokenData.getTokensUser().getCampaignID(), crackResult);
        }
        if ((crackResult.getImageBitmap() == null || crackResult.getImageBitmap().isRecycled()) &&
                !TextUtils.isEmpty(crackResult.getImageUrl())) {
            CustomTarget<Bitmap> target = new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    crackResult.setImageBitmap(resource);
                    showCrackWidgetSuccess(crackResult);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    crackResult.setImageBitmap(null);
                    // image load is failed, but we need to show the text instead.
                    showCrackWidgetSuccess(crackResult);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            };
            Glide.with(getContext())
                    .asBitmap()
                    .load(crackResult.getImageUrl())
                    .into(target);
        } else {
            showCrackWidgetSuccess(crackResult);
        }
    }

    private void showCrackWidgetSuccess(final CrackResultEntity crackResult) {
        initCrackTokenSuccessHandler();
        crackTokenSuccessHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getContext() != null) {
                    if (widgetTokenView.isCrackPercentageFull()) {
                        widgetTokenView.clearTokenAnimation();
                        widgetTokenView.split(crackResult);
                    } else {
                        crackTokenSuccessHandler.postDelayed(this, 50);
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

    private void initCrackTokenErrorHandler() {
        if (crackTokenErrorhandler == null) {
            crackTokenErrorhandler = new Handler();
        } else {
            crackTokenErrorhandler.removeCallbacksAndMessages(null);
        }
    }


    public boolean isShowBackPopup() {
        return tokenData != null && tokenData.getBackButton().isShow();
    }


    @Override
    public void onFinishCrackToken() {
        if (fpmCrack != null)
            fpmCrack.stopTrace();
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

    @Override
    public void onSuccessGetFromDb(List<CrackResultEntity> crackResultEntities) {
        if (crackResultEntities != null && crackResultEntities.size() != 0) {
            if (isExitButtonClickedOnDialog)                   //turn to false if true
                isExitButtonClickedOnDialog = false;
            showSummaryDialogOnSuccess();
        }
    }

    boolean needToShowDialog;
    boolean isFragmentInPauseState;


    @Override
    public void onResume() {
        super.onResume();
        isFragmentInPauseState = false;
        if (needToShowDialog) {
            needToShowDialog = false;
            if (summaryPageDialogFragment != null && getActivity() != null && !getActivity().isFinishing())
                summaryPageDialogFragment.show(getChildFragmentManager(), "summaryPageDialogFragment");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (crackTokenSuccessHandler != null) {
            crackTokenSuccessHandler.removeCallbacksAndMessages(null);
        }
        if (crackTokenErrorhandler != null) {
            crackTokenErrorhandler.removeCallbacksAndMessages(null);
        }
        isFragmentInPauseState = true;
    }

    private void showSummaryDialogOnSuccess() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (getContext() != null) {
            summaryPageDialogFragment = TapTapSummaryDialogFragment.createDialog();
            summaryPageDialogFragment.setListener(this);
        }
        if (tokenData != null) {
            summaryPageDialogFragment.setRewardButtons(tokenData.getRewardButton());
        }
        if (summaryPageDialogFragment != null && summaryPageDialogFragment.getDialog() != null && summaryPageDialogFragment.getDialog().isShowing()) {
            summaryPageDialogFragment.dismiss();
        } else if (summaryPageDialogFragment != null) {
            if (!isFragmentInPauseState)
                summaryPageDialogFragment.show(getChildFragmentManager(), "summaryPageDialogFragment");
            else
                needToShowDialog = true;
            TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                    TapTapAnalyticsTrackerUtil.EventKeys.VIEW_GAME,
                    TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                    TapTapAnalyticsTrackerUtil.ActionKeys.REWARD_SUMMARY_IMPRESSION,
                    "");
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

    @Override
    public void clearViewAndAnimations() {
        widgetTokenView.releaseResourcesOnDestroy();
        widgetCrackResult.clearCrackResult();
        if (crackTokenSuccessHandler != null)
            crackTokenSuccessHandler.removeCallbacksAndMessages(null);
        if (crackTokenErrorhandler != null)
            crackTokenErrorhandler.removeCallbacksAndMessages(null);
    }

    public void showBackPopup() {

        if (getContext() != null && tokenData != null && tokenData.getBackButton() != null) {
            BackButton backButton = tokenData.getBackButton();
            final Dialog dialog = new Dialog(getContext());
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(com.tokopedia.gamification.R.layout.gf_popup_exit_game);

            TextView textHeader = dialog.findViewById(R.id.tv_header);
            TextView textSubHeader = dialog.findViewById(R.id.tv_subheader);
            Button btnExitGame = dialog.findViewById(R.id.btn_cancel);
            Button btnCancelExit = dialog.findViewById(R.id.btn_ok);
            ImageView imagePopupHeader = dialog.findViewById(R.id.image_popup_header);
            textHeader.setText(backButton.getTitle());
            textSubHeader.setText(backButton.getText());
            btnCancelExit.setText(backButton.getCancelText());
            btnExitGame.setText(backButton.getYesText());
            ImageHandler.loadImage(getContext(), imagePopupHeader, backButton.getImageURL(), com.tokopedia.design.R.color.grey_1100, com.tokopedia.design.R.color.grey_1100);
            btnExitGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onExitFromDialog();
                    TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                            TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                            TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                            TapTapAnalyticsTrackerUtil.ActionKeys.POPUP_AND_ERROR_CLICK,
                            backButton.getYesText());
                }
            });

            btnCancelExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                            TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                            TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                            TapTapAnalyticsTrackerUtil.ActionKeys.POPUP_AND_ERROR_CLICK,
                            backButton.getCancelText());
                }
            });

            dialog.show();

        }
    }

    private void onExitFromDialog() {
        if (tokenData != null
                && tokenData.getTokensUser() != null) {
            if (TapTapConstants.TokenState.STATE_CRACK_UNLIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
                if (crackTokenPresenter != null) {
                    if (countDownTimer == null) {
                        crackTokenPresenter.getGetTokenTokopoints(true, false);
                    } else {
                        showSummaryPopup();
                    }
                }
            } else if (TapTapConstants.TokenState.STATE_CRACK_LIMITED.equalsIgnoreCase(tokenData.getTokensUser().getState())) {
                showSummaryPopup();
            } else {
                navigateToHomePage();
            }
        } else {
            navigateToHomePage();
        }
        isExitButtonClickedOnDialog = true;
    }


    @Override
    public void onPlayWithPointsClickedOnSummaryPage() {
        crackTokenPresenter.playWithPoints(true);
    }

    private void sendActionButtonEvent(String action, String label) {
        TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.CLICK_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                action, label);
    }

    private void sendScreenEvent(String action, String label) {
        TapTapAnalyticsTrackerUtil.sendEvent(getContext(),
                TapTapAnalyticsTrackerUtil.EventKeys.VIEW_GAME,
                TapTapAnalyticsTrackerUtil.CategoryKeys.CATEGORY_TAP_TAP,
                action,
                label);
    }

}
