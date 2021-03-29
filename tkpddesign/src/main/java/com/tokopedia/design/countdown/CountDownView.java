package com.tokopedia.design.countdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tokopedia.design.R;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by henrypriyono on 31/01/18.
 */

public class CountDownView extends FrameLayout {
    public static final int REFRESH_DELAY_MS = 1000;

    private TextView hourView;
    private TextView minuteView;
    private TextView secondView;
    private TextView col1, col2;
    private View rootView;

    private int hour;
    private int minute;
    private int second;
    private boolean isUnify;

    private Handler refreshCounterHandler;
    private TimerRunnable runnableRefreshCounter;

    public CountDownView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CountDownView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
        if (isUnify) {
            rootView = inflate(context, R.layout.widget_count_down_view_unify, this);
        } else {
            rootView = inflate(context, R.layout.widget_count_down_view, this);
        }

        hourView = (TextView) rootView.findViewById(R.id.hourView);
        minuteView = (TextView) rootView.findViewById(R.id.minuteView);
        secondView = (TextView) rootView.findViewById(R.id.secondView);
        col1 = rootView.findViewById(R.id.col1);
        col2 = rootView.findViewById(R.id.col2);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownView, 0, 0);
        try {
            hourView.setTextColor(a.getColor(R.styleable.CountDownView_countDownTxtColor, androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)));
            minuteView.setTextColor(a.getColor(R.styleable.CountDownView_countDownTxtColor, androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)));
            secondView.setTextColor(a.getColor(R.styleable.CountDownView_countDownTxtColor, androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)));
            col1.setTextColor(a.getColor(R.styleable.CountDownView_countDownSparatorColor, ContextCompat.getColor(context, R.color.tkpd_main_orange)));
            col2.setTextColor(a.getColor(R.styleable.CountDownView_countDownSparatorColor, ContextCompat.getColor(context, R.color.tkpd_main_orange)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                hourView.setBackground(a.getDrawable(R.styleable.CountDownView_coundDownBackgroud));
                minuteView.setBackground(a.getDrawable(R.styleable.CountDownView_coundDownBackgroud));
                secondView.setBackground(a.getDrawable(R.styleable.CountDownView_coundDownBackgroud));
            } else {
                final int drawableBackgroud = a.getResourceId(R.styleable.CountDownView_coundDownBackgroud, -1);
                hourView.setBackgroundResource(drawableBackgroud);
                minuteView.setBackgroundResource(drawableBackgroud);
                secondView.setBackgroundResource(drawableBackgroud);
            }
        } finally {
            a.recycle();
        }
        displayTime();
    }

    public void setup(final long serverTimeOffset, final Date expiredTime,
                      final CountDownListener listener) {
        setup(serverTimeOffset, expiredTime, null, listener);
    }

    public void setup(final long serverTimeOffset, final Date expiredTime,
                      final DayChangedListener dayChangedListener,
                      final CountDownListener listener) {
        Date serverTime = new Date(System.currentTimeMillis());
        serverTime.setTime(
                serverTime.getTime() + serverTimeOffset
        );
        if (isExpired(serverTime, expiredTime)) {
            handleExpiredTime(listener);
            return;
        }
        clearCountDownView();

        refreshCounterHandler = new Handler();
        runnableRefreshCounter = new TimerRunnable(
                serverTime, expiredTime, serverTimeOffset, listener, dayChangedListener
        );
        startAutoRefreshCounter();
    }

    public void clearCountDownView() {
        if (refreshCounterHandler != null) {
            refreshCounterHandler.removeCallbacks(runnableRefreshCounter);
        }
        refreshCounterHandler = null;
        runnableRefreshCounter = null;
    }

    public void setupTimerFromRemianingMillis(final long expiredTime,
                                              final CountDownListener listener) {
        new CountDownTimer(expiredTime, REFRESH_DELAY_MS) {
            @Override
            public void onTick(long l) {
                int seconds = (int) (l / 1000) % 60;
                int minutes = (int) ((l / (1000 * 60)) % 60);
                int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                setTime(hours, minutes, seconds);
            }

            @Override
            public void onFinish() {
                setTime(0, 0, 0);
                handleExpiredTime(listener);
            }
        }.start();
    }

    private void handleExpiredTime(CountDownListener listener) {
        clearCountDownView();
        setTime(0, 0, 0);
        if (listener != null) {
            listener.onCountDownFinished();
        }
    }

    private boolean isExpired(Date serverTime, Date expiredTime) {
        return serverTime.after(expiredTime);
    }

    private TimeDiffModel getTimeDiff(Date startTime, Date endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        if (diff < 0) diff = 0;
        TimeDiffModel model = new TimeDiffModel();
        model.setDay((int) (diff / (1000 * 3600 * 24)));
        model.setSecond((int) (diff / 1000 % 60));
        model.setMinute((int) (diff / (60 * 1000) % 60));
        model.setHour((int) (diff / (60 * 60 * 1000) % 24));
        return model;
    }

    private void startAutoRefreshCounter() {
        if (refreshCounterHandler != null &&
                runnableRefreshCounter != null) {
            this.runnableRefreshCounter.start();
            this.refreshCounterHandler.post(runnableRefreshCounter);
        }
    }

    private void stopAutoRefreshCounter() {
        if (refreshCounterHandler != null &&
                runnableRefreshCounter != null) {
            this.runnableRefreshCounter.stop();
            this.refreshCounterHandler.removeCallbacks(runnableRefreshCounter);
        }
    }

    private void setTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        displayTime();
    }

    private void displayTime() {
        hourView.setText(String.format(Locale.US, "%02d", hour));
        minuteView.setText(String.format(Locale.US, "%02d", minute));
        secondView.setText(String.format(Locale.US, "%02d", second));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoRefreshCounter();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoRefreshCounter();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        startAutoRefreshCounter();
    }

    public String getCurrentCountDown() {
        return String.format("%s:%s:%s", hourView.getText(), minuteView.getText(), secondView.getText());
    }

    public boolean isUnify() {
        return isUnify;
    }

    public void setUnify(boolean unify) {
        isUnify = unify;
    }

    private static class TimeDiffModel {
        private int day;
        private int second;
        private int minute;
        private int hour;

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getDay() { return day; }

        public void setDay(int day) { this.day = day; }
    }

    private class TimerRunnable implements Runnable {
        private final Date serverTime;
        private final Date expiredTime;
        private final long serverTimeOffset;
        private final CountDownListener listener;
        private final DayChangedListener dayChangedListener;
        private boolean stop = false;

        TimerRunnable(Date serverTime, Date expiredTime, long serverTimeOffset, CountDownListener listener) {
            this(serverTime, expiredTime, serverTimeOffset, listener, null);
        }

        TimerRunnable(Date serverTime, Date expiredTime, long serverTimeOffset, CountDownListener listener,
                      DayChangedListener dayChangedListener) {
            this.serverTime = serverTime;
            this.expiredTime = expiredTime;
            this.serverTimeOffset = serverTimeOffset;
            this.listener = listener;
            this.dayChangedListener = dayChangedListener;
        }

        public void stop() {
            stop = true;
        }

        public void start() {
            stop = false;
        }

        @Override
        public void run() {
            if (!isExpired(serverTime, expiredTime) && !stop) {
                Date currentDate = new Date();
                long currentMillisecond = currentDate.getTime() + serverTimeOffset;
                serverTime.setTime(currentMillisecond);

                TimeDiffModel timeDiff = getTimeDiff(serverTime, expiredTime);
                setTime(timeDiff.getHour(), timeDiff.getMinute(), timeDiff.getSecond());
                refreshCounterHandler.postDelayed(this, REFRESH_DELAY_MS);

                if (dayChangedListener == null) return;

                if (isMoreThanADay(timeDiff)) {
                    dayChangedListener.onMoreThan24h();
                } else {
                    dayChangedListener.onLessThan24h();
                }
            } else {
                handleExpiredTime(listener);
            }
        }
    }

    private boolean isMoreThanADay(TimeDiffModel timeDiff) {
        return timeDiff.getDay() >= 1;
    }

    public interface CountDownListener {
        void onCountDownFinished();
    }

    public interface DayChangedListener {
        void onMoreThan24h();

        void onLessThan24h();
    }
}
