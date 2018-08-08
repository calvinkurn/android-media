package com.tokopedia.flight.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tokopedia.flight.R;

public class ArrowFlightView extends FrameLayout {
    public ArrowFlightView(Context context) {
        super(context);
        init();
    }

    public ArrowFlightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArrowFlightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ArrowFlightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.include_arrow_flight, this);
    }

}
