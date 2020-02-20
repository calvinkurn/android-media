package com.tokopedia.flight.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Created by User on 11/20/2017.
 */

public class HorizontalProgressBar extends FrameLayout {

    private ProgressBar progressBar;

    public HorizontalProgressBar(@NonNull Context context) {
        super(context);
        init();
    }

    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), com.tokopedia.flight.R.layout.view_horizontal_progress_bar, this);
        progressBar = (ProgressBar) view.findViewById(com.tokopedia.flight.R.id.progressBar);
    }

    public void setProgress(int progress) {
        if (Build.VERSION.SDK_INT >= 24) {
            progressBar.setProgress(progress, true);
        } else {
            progressBar.setProgress(progress);
        }
    }
}
