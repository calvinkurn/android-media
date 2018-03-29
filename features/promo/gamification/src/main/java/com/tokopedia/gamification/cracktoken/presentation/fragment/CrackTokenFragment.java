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
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.example.gamification.R;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.gamification.cracktoken.presentation.compoundview.WidgetCrackResult;
import com.tokopedia.gamification.cracktoken.presentation.compoundview.WidgetRemainingToken;
import com.tokopedia.gamification.cracktoken.presentation.compoundview.WidgetTokenView;
import com.tokopedia.gamification.cracktoken.presentation.model.RewardTextStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rizky on 28/03/18.
 */

public class CrackTokenFragment extends BaseDaggerFragment {

    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;

    private CountDownTimer countDownTimer;

    private TextView textCountdownTimer;

    private WidgetTokenView widgetTokenView;
    private WidgetCrackResult widgetCrackResult;
    private WidgetRemainingToken widgetRemainingToken;

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
        widgetCrackResult = v.findViewById(R.id.widget_reward);
        widgetRemainingToken = v.findViewById(R.id.widget_remaining_token_view);

        decAccInterpolator = new AccelerateDecelerateInterpolator();

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.special_sprite);
        Bitmap fullEggBitmap = getFullEgg(bitmap);
        Bitmap crackedEgg = getCrackedEgg(bitmap);
        Bitmap leftCrackedEgg = getCrackedLeftEgg(bitmap);
        Bitmap rightCrackedEgg = getCrackedRightEgg(bitmap);
        bitmap.recycle();

        showCountdownTimer();

        widgetTokenView.setToken(fullEggBitmap, crackedEgg, rightCrackedEgg, leftCrackedEgg);

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
                        widgetTokenView.stopShaking();
                        showErrorCrackResult();
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

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

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

    private void showSuccessCrackResult() {
        List<RewardTextStyle> rewardTexts = new ArrayList<>();
        rewardTexts.add(new RewardTextStyle("#ffdc00", "+50 Points", 34));

        Bitmap imageCrackResult = BitmapFactory.decodeResource(getResources(), R.drawable.coupon);
        widgetCrackResult.showCrackResult(imageCrackResult, rewardTexts, "Cek dan Gunakan Hadiah Anda", "");
    }

    private void showErrorCrackResult() {
        List<RewardTextStyle> rewardTexts = new ArrayList<>();
        rewardTexts.add(new RewardTextStyle("#ffffff", "Terjadi Kesalahan Teknis", 40));

        Bitmap imageCrackResult = BitmapFactory.decodeResource(getResources(), R.drawable.group);
        widgetCrackResult.showCrackResult(imageCrackResult, rewardTexts, "Coba Lagi", "");
    }

    private void resetEgg() {
        isClicked = false;
        showCountdownTimer();
        widgetTokenView.reset();
        textCountdownTimer.setVisibility(View.VISIBLE);
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

}
