package com.tokopedia.sellerapp.dashboard.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.design.component.ticker.TickerView;

/**
 * @author by nisie on 14/11/18.
 */
public class VerificationWarningTicker extends TickerView {

    public VerificationWarningTicker(@NonNull Context context) {
        super(context);
        setupLayout();
    }

    public VerificationWarningTicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupLayout();


    }

    public VerificationWarningTicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupLayout();

    }

    private void setupLayout() {

    }
}
