package com.tokopedia.train.seat.presentation.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpdtrain.R;

public class TrainSeatPassengerAndWagonView extends BaseCustomView {
    public TrainSeatPassengerAndWagonView(@NonNull Context context) {
        super(context);
        init();
    }

    public TrainSeatPassengerAndWagonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TrainSeatPassengerAndWagonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_train_seat_passenger_wagon, this);

    }

    private void init(AttributeSet attributeSet) {
        init();
    }
}
