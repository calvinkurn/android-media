package com.tokopedia.gamification.cracktoken.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
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

    public static final double RATIO_MARGIN_TOP_TIMER = 0.15;
    public static final String SAVED_TIMESTAMP = "timestamp";
    public static final String SAVED_TOKEN_DATA = "extra_token_data";

    private CountDownTimer countDownTimer;

    private TextView textCountdownTimer;
    private WidgetTokenView widgetTokenView;
    private WidgetCrackResult widgetCrackResult;
    private WidgetRemainingToken widgetRemainingToken;
    private LinearLayout layoutTimer;
    private ProgressBar progressBar;

    private String backgroundImageUrl;
    private String smallImageUrl;
    private String fullEggImg;
    private String crackedEggImg;
    private String rightCrackedEggImg;
    private String leftCrackedEggImg;
    private int timeRemainingSeconds;

    private TokenData tokenData;
    private View rootView;
    private ActionListener listener;

    @Inject
    CrackTokenPresenter crackTokenPresenter;
    private ImageView ivContainer;

    public static Fragment newInstance() {
        return new CrackTokenFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            tokenData = savedInstanceState.getParcelable(SAVED_TOKEN_DATA);
            if (savedInstanceState.containsKey(SAVED_TIMESTAMP)) {
                long savedTimeStamp = savedInstanceState.getLong(SAVED_TIMESTAMP);
                long currentTimeStamp = System.currentTimeMillis();
                int diffSeconds = (int) ((currentTimeStamp - savedTimeStamp) / 1000L);
                TokenUser tokenUser = tokenData.getHome().getTokensUser();
                int prevTimeRemainingSecond = tokenUser.getTimeRemainingSeconds();
                tokenUser.setTimeRemainingSeconds(prevTimeRemainingSecond - diffSeconds);
            }
            initDataCrackEgg(tokenData);
        }
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
        if (tokenData == null ||
                (tokenData.isShowCountDown() && tokenData.getHome().getTokensUser().getTimeRemainingSeconds() <= 0)) {
            crackTokenPresenter.getGetTokenTokopoints();
        } else {
            renderViewCrackEgg();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * copy the token data retrieved to field variable
     */
    private void initDataCrackEgg(TokenData tokenData) {
        this.tokenData = tokenData;
        TokenUser tokenUser = tokenData.getHome().getTokensUser();
        backgroundImageUrl = tokenUser.getTokenAsset().getBackgroundImgUrl();
        smallImageUrl = tokenUser.getTokenAsset().getSmallImgUrl();
        fullEggImg = tokenUser.getTokenAsset().getImageUrls().get(0);
        crackedEggImg = tokenUser.getTokenAsset().getImageUrls().get(4);
        rightCrackedEggImg = tokenUser.getTokenAsset().getImageUrls().get(6);
        leftCrackedEggImg = tokenUser.getTokenAsset().getImageUrls().get(5);
        timeRemainingSeconds = tokenUser.getTimeRemainingSeconds();
    }

    private void renderViewCrackEgg() {
        widgetTokenView.reset();

        TokenUser tokenUser = tokenData.getHome().getTokensUser();

        ImageHandler.loadImageAndCache(ivContainer, backgroundImageUrl);

        if (tokenUser.getShowTime()) {
            textCountdownTimer.setVisibility(View.VISIBLE);
            showCountdownTimer(timeRemainingSeconds);
        } else {
            textCountdownTimer.setVisibility(View.GONE);
        }

        widgetTokenView.setToken(fullEggImg, crackedEggImg, rightCrackedEggImg, leftCrackedEggImg);
        widgetTokenView.setListener(new WidgetTokenView.WidgetTokenListener() {
            @Override
            public void onClick() {
                stopTimer();
                TokenUser tokenUser = tokenData.getHome().getTokensUser();
                crackTokenPresenter.crackToken(tokenUser.getTokenUserID(), tokenUser.getCampaignID());
            }
        });

        widgetCrackResult.setListener(new WidgetCrackResult.WidgetRewardListener() {
            @Override
            public void onClickCtaButton(String applink) {
                // TODO: direct to the associated applink page
                widgetCrackResult.clearReward();

                crackTokenPresenter.getGetTokenTokopoints();
            }
        });
        widgetRemainingToken.show();
        widgetRemainingToken.showRemainingToken(smallImageUrl, tokenData.getSumTokenStr(), tokenData.getTokenUnit());

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

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
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

    private void showCountdownTimer(final int timeRemainingSeconds) {
        if (timeRemainingSeconds > 0) {
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
        this.timeRemainingSeconds = (int) (millisUntilFinished / COUNTDOWN_INTERVAL_SECOND);
        if (this.timeRemainingSeconds <= 0) {
            stopTimer();
            widgetTokenView.hide();
            crackTokenPresenter.getGetTokenTokopoints();
        } else {
            setUIFloatingTimer(CrackTokenFragment.this.timeRemainingSeconds);
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
            initDataCrackEgg(tokenData);
            renderViewCrackEgg();
        }
    }

    @Override
    public void onErrorGetToken(Throwable throwable) {

    }

    @Override
    public void onSuccessCrackToken(CrackResult crackResult) {
        widgetTokenView.split();
        List<CrackBenefit> crackBenefits = crackResult.getBenefits();

        widgetCrackResult.showCrackResult(crackResult.getImageUrl(), "Selamat anda mendapatkan",
                crackBenefits, crackResult.getCtaButton().getTitle(), crackResult.getCtaButton().getApplink());
    }

    @Override
    public void onErrorCrackToken(Throwable throwable) {
        widgetTokenView.stopShaking();
        List<CrackBenefit> rewardTexts = new ArrayList<>();
        rewardTexts.add(new CrackBenefit("Terjadi Kesalahan Teknis", "#ffffff", "medium"));

        Bitmap errorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_error_crack_result);
        widgetCrackResult.showErrorCrackResult(errorBitmap, "Maaf, sayang sekali sepertinya", rewardTexts, "Coba Lagi", "");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        tokenData.getHome().getTokensUser().setTimeRemainingSeconds(timeRemainingSeconds);
        outState.putParcelable(SAVED_TOKEN_DATA, tokenData);
        if (tokenData.isShowCountDown()) {
            outState.putLong(SAVED_TIMESTAMP, System.currentTimeMillis());
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActionListener) {
            listener = (ActionListener) context;
        }

    }

    public interface ActionListener {
        void directPageToCrackEmpty();
    }
}