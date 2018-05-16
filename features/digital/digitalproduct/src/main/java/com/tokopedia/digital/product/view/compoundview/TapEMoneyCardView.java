package com.tokopedia.digital.product.view.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tokopedia.digital.R;

/**
 * Created by Rizky on 15/05/18.
 */
public class TapEMoneyCardView extends RelativeLayout {

    private ProgressBar progressBar;
    private TextView textLabel;

    public TapEMoneyCardView(Context context) {
        super(context);
        init();
    }

    public TapEMoneyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TapEMoneyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TapEMoneyCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_tap_emoney_card, this);

        progressBar = view.findViewById(R.id.progress_bar);
        textLabel = view.findViewById(R.id.text_label);
    }

    public void showLoading(String textLoading) {
        setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        textLabel.setText(textLoading);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == GONE) {
            progressBar.setVisibility(GONE);
        }
    }
}
