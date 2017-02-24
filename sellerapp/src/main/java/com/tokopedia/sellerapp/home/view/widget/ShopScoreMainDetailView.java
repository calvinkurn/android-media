package com.tokopedia.sellerapp.home.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tokopedia.sellerapp.R;

/**
 * Created by sebastianuskh on 2/24/17.
 */

public class ShopScoreMainDetailView extends RelativeLayout {
    public ShopScoreMainDetailView(Context context) {
        super(context);
        initView(context);
    }

    public ShopScoreMainDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShopScoreMainDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public ShopScoreMainDetailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_shop_score_main_detail, this);
    }


    public void setProgress(float progress) {

    }

    public void setLimit(float limit) {

    }
}
