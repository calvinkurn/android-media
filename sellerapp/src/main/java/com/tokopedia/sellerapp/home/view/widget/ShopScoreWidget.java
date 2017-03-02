package com.tokopedia.sellerapp.home.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModelData;

/**
 * Created by sebastianuskh on 2/23/17.
 */

public class ShopScoreWidget extends FrameLayout {
    private ShopScoreMainDetailView shopScoreMainDetailView;
    private TextView shopScoreTitleWidget;
    private ShopScoreWidgetCallback callback;

    public ShopScoreWidget(Context context) {
        super(context);
        initView(context);
    }

    public ShopScoreWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShopScoreWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("NewApi")
    public ShopScoreWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.view_shop_score_home, this);
        shopScoreMainDetailView =
                (ShopScoreMainDetailView) view.findViewById(R.id.shop_score_progress_bar_group);
        shopScoreTitleWidget = (TextView) view.findViewById(R.id.title_shop_score_widget);
        view
                .findViewById(R.id.text_view_go_to_detail)
                .setOnClickListener(goToDetailListener());
    }

    private OnClickListener goToDetailListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.goToShopScoreDetail();
            }
        };
    }

    public void renderView(ShopScoreViewModel shopScoreViewModel) {
        setLimit(shopScoreViewModel.getBadgeScore());
        ShopScoreViewModelData data = shopScoreViewModel.getData();
        setProgress(data.getValue());
        setDescription(data.getDescription());
        setProgressBarColor(data.getProgressBarColor());
        shopScoreTitleWidget.setText(data.getTitle());

    }

    private void setProgressBarColor(int progressBarColor) {
        shopScoreMainDetailView.setProgressBarColor(progressBarColor);
    }

    public void setProgress(float progress) {
        shopScoreMainDetailView.setProgress(progress);
    }

    public void setLimit(float limit) {
        shopScoreMainDetailView.setLimit(limit);
    }

    public void setDescription(String description) {
        shopScoreMainDetailView.setDescription(description);
    }


    public void setCallback(ShopScoreWidgetCallback callback) {
        this.callback = callback;
    }
}
