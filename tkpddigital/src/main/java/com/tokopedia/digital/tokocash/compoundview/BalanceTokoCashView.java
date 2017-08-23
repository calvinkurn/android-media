package com.tokopedia.digital.tokocash.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tokopedia.digital.R;

import butterknife.ButterKnife;

/**
 * Created by nabillasabbaha on 8/18/17.
 */

public class BalanceTokoCashView extends LinearLayout {

    public BalanceTokoCashView(Context context) {
        super(context);
        init();
    }

    public BalanceTokoCashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalanceTokoCashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tokocash_balance, this, true);
        ButterKnife.bind(this);
    }
 }
