package com.tokopedia.challenges.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.tokopedia.challenges.R;

import java.util.concurrent.TimeUnit;


public class CountDownView extends FrameLayout {
    @Nullable
    private CountDownTimer timer;
    private long startDuration;
    private long currentDuration;
    private boolean timerRunning;
    @Nullable
    private CountDownListener listener;
    TextView tvHours;
    TextView tvMins;
    TextView tvSecs;
    private ProgressBar progressBar;

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        init();
        int startDuration;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownView);
        startDuration = ta.getInt(R.styleable.CountDownView_startDuration, 0);
        ta.recycle();
        setStartDuration(startDuration);
    }

    private void init() {
        View view = inflate(getContext(), getLayout(), this);
        tvHours = view.findViewById(R.id.tv_hours_value);
        tvMins = view.findViewById(R.id.tv_minutes_value);
        tvSecs = view.findViewById(R.id.tv_seconds_value);
    }

    public void setStartDuration(long duration) {
        if (timerRunning) {
            return;
        }
        startDuration = currentDuration = (duration-System.currentTimeMillis());
        updateText(startDuration);
    }

    public void start(final ProgressBar progressBar) {
        this.progressBar=progressBar;
        if (timerRunning) {
            return;
        }

        timerRunning = true;
        timer = new CountDownTimer(currentDuration, 100) {
            @Override
            public void onTick(long millis) {
                currentDuration = millis;
                progressBar.setProgress(100-(int)((millis/(float)startDuration)*100.0f));
                updateText(millis);
            }

            @Override
            public void onFinish() {
                stop();
                if (listener != null) {
                    listener.onFinishCountDown();
                }
            }
        };
        timer.start();
    }

    public void reset() {
        stop();
        setStartDuration(startDuration);
        invalidate();
    }

    public void stop() {
        if (!timerRunning) {
            return;
        }

        timerRunning = false;
        timer.cancel();
    }

    public void setListener(CountDownListener listener) {
        this.listener = listener;
    }



    void updateText(long duration) {
        generateCountdownText(duration);
    }


    void generateCountdownText(long duration) {

        long seconds=duration/1000;
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        if(hours<10)
            tvHours.setText(String.format(getResources().getString(R.string.append_0), hours));
        else
            tvHours.setText(String.valueOf(hours));
        if(minute<10)
            tvMins.setText(String.format(getResources().getString(R.string.append_0), minute));
        else
            tvMins.setText(String.valueOf(minute));
        if(second<10)
            tvSecs.setText(String.format(getResources().getString(R.string.append_0), second));
        else
            tvSecs.setText(String.valueOf(second));
    }

    protected int getLayout() {
        return R.layout.custom_timer_view;
    }
}