package com.tokopedia.groupchat.channel.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.groupchat.R;

/**
 * @author by StevenFredian on 14/02/18.
 */

public class VoteOptionBar extends FrameLayout{
    private TextView text;
    private ProgressBar progressBar;

    private String textValue;
    private int progress;

    public VoteOptionBar(@NonNull Context context) {
        super(context);
        init();
    }

    public VoteOptionBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VoteOptionBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBarWithTimer);
        try {
            textValue = styledAttributes.getString(R.styleable.ProgressBarWithTimer_pbwt_text);
            progress = styledAttributes.getInt(R.styleable.ProgressBarWithTimer_pbwt_progress, 0);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), R.layout.progress_bar_with_timer, this);
        text = view.findViewById(R.id.text_view);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    public void setText(String textValue) {
        text.setText(textValue);
        invalidate();
        requestLayout();
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
        invalidate();
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(textValue)) {
            text.setText(textValue);
        }
        setProgress(progress);
        invalidate();
        requestLayout();
    }
}
