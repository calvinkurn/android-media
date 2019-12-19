package com.tokopedia.contactus.inboxticket2.view.customview;

import android.content.Context;
import android.os.Handler;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by pranaymohapatra on 27/06/18.
 */

public class CustomEditText extends AppCompatEditText {

    private static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.SECONDS.toMillis(1);
    private String searchText;
    private String searchHint;
    private long delayTextChanged = DEFAULT_DELAY_TEXT_CHANGED;
    private Listener listener;
    private ResetListener reset;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setResetListener(ResetListener listener) {
        this.reset = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && listener != null) {
                listener.onSearchSubmitted(textView.getText().toString());
                return true;
            }
            return false;
        });
        this.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();

            public void afterTextChanged(Editable s) {
                runTimer(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            private void runTimer(final String text) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        updateListener(text);
                    }
                }, delayTextChanged);
            }

            private void updateListener(final String text) {
                if (listener == null) {
                    return;
                }
                Handler mainHandler = new Handler(getContext().getMainLooper());
                Runnable myRunnable = () -> listener.onSearchTextChanged(text);
                mainHandler.post(myRunnable);
            }
        });
        invalidate();
        requestLayout();
    }

    public void setDelayTextChanged(long delayTextChanged) {
        this.delayTextChanged = delayTextChanged;
    }

    public String getSearchText() {
        return getText().toString();
    }

    public interface Listener {

        void onSearchSubmitted(String text);

        void onSearchTextChanged(String text);

    }

    public interface ResetListener {
        void onSearchReset();
    }
}