package com.tokopedia.train.seat.presentation.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpdtrain.R;

import java.util.ArrayList;
import java.util.List;

public class TrainSeatPagerIndicator extends BaseCustomView {
    private LinearLayout container;
    private int currentPosition;
    private List<ImageView> pointViews;

    private ActionListener listener;

    public interface ActionListener {
        void onIndicatorClicked(int position);
    }

    public TrainSeatPagerIndicator(@NonNull Context context) {
        super(context);
        init();
    }

    public TrainSeatPagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrainSeatPagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_seat_pager_indicator, this);
        container = view.findViewById(R.id.indicator_container);
    }

    public void renderView(int count) {
        container.removeAllViews();
        pointViews = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(5, 0, 5, 0);
            if (i == currentPosition) {
                pointView.setImageResource(com.tokopedia.design.R.drawable.indicator_focus);
            } else {
                pointView.setImageResource(com.tokopedia.design.R.drawable.indicator);
            }
            int positionImage = i;
            pointView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onIndicatorClicked(positionImage);
                        setCurrentIndicator(positionImage);
                    }
                }
            });
            pointViews.add(pointView);
            container.addView(pointView);
        }
    }

    public void setCurrentIndicator(int currentPosition) {
        this.currentPosition = currentPosition;
        for (int i = 0; i < pointViews.size(); i++) {
            if (currentPosition != i) {
                pointViews.get(i).setImageResource(com.tokopedia.design.R.drawable.indicator);
            } else {
                pointViews.get(i).setImageResource(com.tokopedia.design.R.drawable.indicator_focus);
            }
        }
    }

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }
}
