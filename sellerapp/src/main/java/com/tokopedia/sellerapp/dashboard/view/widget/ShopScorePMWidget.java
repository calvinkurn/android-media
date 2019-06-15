package com.tokopedia.sellerapp.dashboard.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.sellerapp.R;

/**
 * Created by hendry on 2019-06-15.
 *  _________________
 * (_________________)==========#
 *
 */
public class ShopScorePMWidget extends FrameLayout {

    public static final int MAX_PROGRESS = 100;
    private RoundGradientProgressBar roundCornerProgressBar;
    private TextView tvProgressValue;

    public ShopScorePMWidget(Context context) {
        super(context);
        initView(context, null);
    }

    public ShopScorePMWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShopScorePMWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public ShopScorePMWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.widget_shop_score_pm, this);
        roundCornerProgressBar = view.findViewById(R.id.progress_value);
        tvProgressValue = view.findViewById(R.id.tv_progress_value);
    }

    public void setProgress(float progress) {
        roundCornerProgressBar.setProgress(progress);
        tvProgressValue.setText(MethodChecker.fromHtml(
                String.format(getContext().getString(R.string.score_of_total_score), (int)progress, MAX_PROGRESS)));
    }

    public void setProgressColor(int[] progressColors) {
        roundCornerProgressBar.setProgressColor(progressColors);
    }


}
